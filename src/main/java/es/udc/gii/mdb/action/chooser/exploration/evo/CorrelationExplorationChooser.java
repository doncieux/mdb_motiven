package es.udc.gii.mdb.action.chooser.exploration.evo;

import es.udc.gii.mdb.action.chooser.exploration.*;
import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.action.chooser.StrategyChooser;
import es.udc.gii.mdb.action.strategy.PlainStrategy;
import es.udc.gii.mdb.action.strategy.Strategy;
import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.motivation.buffer.CorrelationChange;
import es.udc.gii.mdb.motivation.buffer.ValueFunctionEpisodicBuffer;
import es.udc.gii.mdb.motivation.buffer.MotivationEpisode;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.util.MDBRandom;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.log.LogTool;
import es.udc.gii.mdb.util.log.LogToolSingleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public abstract class CorrelationExplorationChooser extends Observable implements StrategyChooser {

    private CandidateActionSelector candidateActionSelector;
    private ExplorationLogData logData;
    private BlindCriteria intrinsicBlindCriteria;
    private List<CorrelatedCandidateEvaluation> candidateEvaluations;
    private List<CorrelatedCandidateEvaluation> candidatesChoosen;
    private ValueFunction lastVFFollowed = null;
    private CorrelationChange.TYPE correlationType = null;
    private String correlationElement = null;

    public CorrelationExplorationChooser() {
        correlationType = null;
        candidatesChoosen = new ArrayList<>();
    }

    @Override
    public Strategy selectStrategy(ActionPerceptionPair app) {
        logData = new ExplorationLogData();
        List<Action> actions = candidateActionSelector.getActionsToTry(app);
        int bestIndex = MDBRandom.nextInt(actions.size());
        Action bestAction = actions.get(bestIndex);
        if (MDBCore.getInstance().getIterations() < 2) {
            return new PlainStrategy(bestAction);
        }

        if (MDBCore.getInstance().isTaskAccomplished()) {
            lastVFFollowed = null;
            correlationElement = null;
            correlationType = null;
        }

        double bestEval = -1;
        int hierarchy = Integer.MAX_VALUE;
        boolean needBlindMotivation = Boolean.TRUE;
        ValueFunction selectedRM = null;

        List<ActionPerceptionPair> appList = new ArrayList<>();
        for (Action a : actions) {
            ActionPerceptionPair calculatedAPP = buildActionPerceptionPair(app, a);
            appList.add(calculatedAPP);
            if (lastVFFollowed != null) {
                double eval = getExtrinsicEval(correlationElement, correlationType, calculatedAPP);
                if (eval > bestEval) {
                    selectedRM = lastVFFollowed;
                    bestEval = eval;
                    bestAction = a;
                    needBlindMotivation = Boolean.FALSE;
                    hierarchy = lastVFFollowed.getHierarchy();
                }
            }
        }

        for (ActionPerceptionPair actionPerceptionPair : appList) {
            for (ValueFunction vf : ModelMap.getInstance().getValueFunctions()) {
                if (!vf.getMemory().getContent().isEmpty()) {
                    if (vf.getHierarchy() < hierarchy) {
                        double certainty = vf.getCertainty().getCertainty(actionPerceptionPair);
                        if (certainty > 0.0) {
                            needBlindMotivation = Boolean.FALSE;
                            CorrelationChange.TYPE typeR = ((DistancesCertainty) vf.getCertainty()).getCorCandidate().getTypeR();
                            String element = ((DistancesCertainty) vf.getCertainty()).getCorCandidate().getComponent();
                            double eval = getExtrinsicEval(element, typeR, actionPerceptionPair);
                            if (eval > bestEval) {
                                selectedRM = vf;
                                correlationType = typeR;
                                correlationElement = element;
                                bestEval = eval;
                                bestAction = actionPerceptionPair.getStrategy().getActions().get(0);
                                needBlindMotivation = Boolean.FALSE;
                                hierarchy = vf.getHierarchy();
                            }
                        }
                    }
                }
            }
        }

        //Antitraza
        if (needBlindMotivation) {
            if (lastVFFollowed != null && !MDBCore.getInstance().isTaskAccomplished()) {

                //Antitraza
                ((DistancesCertainty) candidatesChoosen.get(candidatesChoosen.size() - 1).getValueFunction().getCertainty()).addAntiTraces(storeAntiTrace(candidatesChoosen.get(candidatesChoosen.size() - 1)));
                candidatesChoosen.get(candidatesChoosen.size() - 1).getValueFunction().setAntiTraceStored();
                for (ValueFunction rm : ModelMap.getInstance().getValueFunctions()) {
                    rm.resetGoalReachments();
                }
            }
            //Selección acción
            lastVFFollowed = null;
            correlationType = null;
            correlationElement = null;
            for (ActionPerceptionPair actionPerceptionPair : appList) {
                double blindEval = intrinsicBlindCriteria.getValue(actionPerceptionPair);
                if (blindEval > bestEval) {
                    bestEval = blindEval;
                    bestAction = actionPerceptionPair.getStrategy().getActions().get(0);
                }
            }
        } else {
            lastVFFollowed = selectedRM;
        }

        CorrelatedCandidateEvaluation cce = new CorrelatedCandidateEvaluation(app, selectedRM, bestEval, bestEval, correlationType, correlationElement);
        candidatesChoosen.add(cce);
        logData = new ExplorationLogData(getLogMultipleData(), bestIndex);
        Strategy strategy = new PlainStrategy(bestAction);

        String logPrint = MDBCore.getInstance().getIterations() + "\t";
        if (lastVFFollowed != null) {
            if (lastVFFollowed.getID().equalsIgnoreCase("valuefunction")) {
                logPrint += 1;
            } else {
                logPrint += lastVFFollowed.getID().substring(13);
            }
        } else {
            logPrint += 0;
        }
        System.out.println(logPrint);

        notifyLogs();

        return strategy;
    }

    public ExplorationLogData getLogData() {
        return logData;
    }

    public List<List<Double>> getLogMultipleData() {
        List<List<Double>> multipleLogData = new ArrayList<>();
        if (candidateEvaluations != null) {
            for (CorrelatedCandidateEvaluation ce : candidateEvaluations) {
                multipleLogData.add(ce.getLogLine());
            }
        }
        return multipleLogData;
    }

    public List<CorrelatedCandidateEvaluation> getCandidateEvaluations(List<ActionPerceptionPair> appList) {

        candidateEvaluations = new ArrayList<>();
        boolean needBlindMotivation = true;

        //Para cada candidato
        double extrinsicEval = 0;
        for (ActionPerceptionPair app : appList) {
            int hierarchy = Integer.MAX_VALUE;
            String correlationComponent = null;
            double certainty = 0;
            ValueFunction selected = null;
            if (lastVFFollowed != null) {
                needBlindMotivation = Boolean.FALSE;
                extrinsicEval = getExtrinsicEval(correlationElement, correlationType, app);
                hierarchy = lastVFFollowed.getHierarchy();
                selected = lastVFFollowed;
                correlationComponent = correlationElement;
            }
            //Para cada modelo, se evalúa este APP

            for (ValueFunction rm : ModelMap.getInstance().getValueFunctions()) {
                if (!rm.getMemory().getContent().isEmpty()) {
                    if (rm.getHierarchy() < hierarchy) {
                        certainty = rm.getCertainty().getCertainty(app);
                        if (certainty > 0.0) {
                            needBlindMotivation = Boolean.FALSE;
                            selected = rm;
                            hierarchy = rm.getHierarchy();
                            correlationType = ((DistancesCertainty) rm.getCertainty()).getCorCandidate().getTypeR();
                            correlationComponent = ((DistancesCertainty) rm.getCertainty()).getCorCandidate().getComponent();
                        }
                    }
                }
            }

            //Se crea el objeto que almacena los valores calculados para la evaluación
            CorrelatedCandidateEvaluation candidateEvaluation = new CorrelatedCandidateEvaluation(app, selected, extrinsicEval, certainty, correlationType, correlationComponent);
            candidateEvaluations.add(candidateEvaluation);
        }

        //Si todas las evaluaciones son muy bajas es debido a que no hay confianza en esa zona, se debe hacer una exploración blind
        if (needBlindMotivation) {
            for (CorrelatedCandidateEvaluation ce : candidateEvaluations) {
                double blindEval = intrinsicBlindCriteria.getValue(ce.getApp());
                ce.setIntrinsicBlindMotivation(blindEval);
            }
        }

        return candidateEvaluations;
    }

    public void notifyLogs() {

        this.setChanged();
        this.notifyObservers();
        this.clearChanged();

    }

    private List<MotivationEpisode> storeAntiTrace(CorrelatedCandidateEvaluation correlatedCandidateEvaluation) {
        ValueFunction rm = correlatedCandidateEvaluation.getValueFunction();
        ValueFunctionEpisodicBuffer correlativeMotivationCacheEpisodicBuffer = (ValueFunctionEpisodicBuffer) rm.getMemory();
        List<MotivationEpisode> cacheContent = correlativeMotivationCacheEpisodicBuffer.getCache().getContent();
        List<MotivationEpisode> antiTrace = new ArrayList<>();
        if (cacheContent.size() < 2) {
            return new ArrayList<>();
        }
        int endLoop = cacheContent.size() - 1;
        int endCandidateEvaluations = candidatesChoosen.size() - 1;
        CorrelationChange.TYPE correlationType = candidatesChoosen.get(endCandidateEvaluations).getCorrelationType();
        String correlationElement = candidatesChoosen.get(endCandidateEvaluations).getCorrelationElement();

        for (int i = 0; i < endLoop; i++) {
            MotivationEpisode currentEpisode = cacheContent.get(endLoop - i);
            MotivationEpisode prevEpisode = cacheContent.get(endLoop - i - 1);
            double evaluationValue = getEvaluation(correlationElement, correlationType, currentEpisode.getActionPerceptionPair(), prevEpisode.getActionPerceptionPair());
            if (evaluationValue > 0) {
                antiTrace.add(currentEpisode);
            } else {
                break;
            }
            CorrelatedCandidateEvaluation evaluation = candidatesChoosen.get(endCandidateEvaluations - i);
            if (evaluation.getCorrelationType() != correlationType) {
                break;
            }
        }
        return antiTrace;
    }

    @Override
    public String toString() {
        return "ExplorationChooser";
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        try {

            this.candidateActionSelector = (CandidateActionSelector) Class.forName(configuration.getString("candidateActionSelector.class")).newInstance();
            candidateActionSelector.configure(configuration.subset("candidateActionSelector"));
            this.intrinsicBlindCriteria = (BlindCriteria) Class.forName(configuration.getString("blindCriteria.class")).newInstance();
            intrinsicBlindCriteria.configure(configuration.subset("blindCriteria"));

            Configuration logsSubset = configuration.subset("logs");

            if (logsSubset != null) {
                LogTool log;
                List<String> classes = logsSubset.getList("log.class");

                try {
                    for (int i = 0; i < classes.size(); i++) {
                        log = (LogTool) Class.forName(classes.get(i)).newInstance();
                        log.setName("ExplorationChooser");
                        log.configure(logsSubset.subset("log(" + i + ")"));
                        log.setObservable(this);
                        LogToolSingleton.getInstance().addLog(log);
                    }

                } catch (InstantiationException ex) {
                    //EXCEPTION
                } catch (IllegalAccessException ex) {
                    //EXCEPTION
                } catch (ClassNotFoundException ex) {
                    //EXCEPTION
                }

            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CorrelationExplorationChooser.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected abstract ActionPerceptionPair buildActionPerceptionPair(ActionPerceptionPair app, Action a);

    protected abstract double getEvaluation(String correlationElement, CorrelationChange.TYPE correlationType, ActionPerceptionPair app1, ActionPerceptionPair app2);

    public abstract double getExtrinsicEval(String correlationElement, CorrelationChange.TYPE correlationType, ActionPerceptionPair app);

}
