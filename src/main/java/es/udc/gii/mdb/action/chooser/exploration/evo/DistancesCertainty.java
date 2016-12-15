package es.udc.gii.mdb.action.chooser.exploration.evo;

import es.udc.gii.mdb.knowledge.declarative.model.Certainty;
import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.motivation.buffer.CorrelationRecord;
import es.udc.gii.mdb.motivation.buffer.CorrelationChange;
import es.udc.gii.mdb.motivation.buffer.ValueFunctionEpisodicBuffer;
import es.udc.gii.mdb.motivation.buffer.MotivationEpisode;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;

/**
 * This is an ad hoc class for the experiment that must be updated as soon as possible in order to take into account all the possible correlations.
 * In this class, the correlations of the experiment are set directly 
 * 
 * @author GII
 */
public class DistancesCertainty implements Certainty, Configurable {

    private ValueFunction valueFunction;
    private double successCertainty;
    private double ns;
    private double nMax;
    private double minTraceSize;
    private double gamma;
    private Configuration configuration;
    private List<Integer> indexes;

    private List<List<MotivationEpisode>> antiTracesList;
    private List<List<MotivationEpisode>> tracesList;
    private List<List<MotivationEpisode>> weakTracesList;

    private double nt = 0;
    private double nat = 0;
    private int numberOfGoalWithoutAntiTraces = 0;

    private double CP;
    private double CF;
    private double ntFactor;
    private double M;
    private int NTRACESTODISTANCE;
    private List<String> perceptions;
    //FIXME Get sensors limit from SensorMap
    private static final double lInf = 0;
    private static final double lSup = 1000;
    private Map<Integer, List<Double>> tracesMinDistancesMap;
    private Map<Integer, List<Double>> weakTracesMinDistancesMap;
    private Map<Integer, List<Double>> antitracesMinDistancesMap;
    private CorrelationRecord corCandidate;

    private double correlationChangesCounter = 0;
    private Map<String, CorrelationRecord> candidates;
    private Map<String, CorrelationChange.TYPE> correlationTypes;
    private List<Map<String, CorrelationChange>> correlationChanges;

    public DistancesCertainty() {
        this.successCertainty = 0.0;
        this.antiTracesList = new ArrayList<>();
        this.tracesList = new ArrayList<>();
        this.weakTracesList = new ArrayList<>();
        nt = 0;
        tracesMinDistancesMap = new HashMap<>();
        weakTracesMinDistancesMap = new HashMap<>();
        antitracesMinDistancesMap = new HashMap<>();
        candidates = new HashMap<>();
        correlationTypes = new HashMap<>();
        correlationChanges = new ArrayList<>();
    }

    
    private Map<Integer, List<Double>> getMaxDistancesMap(List<List<MotivationEpisode>> episodesList) {
        Map<Integer, List<Double>> maxDistancesMap = new HashMap<>();
        for (int index : indexes) {
            maxDistancesMap.put(index, new ArrayList<Double>());
        }
        if (episodesList.size() > 1) {
            for (int i = 0; i < episodesList.size() - 1; i++) {
                List<MotivationEpisode> episodes = episodesList.get(i);
                for (int ii = 0; ii < episodes.size(); ii++) {
                    MotivationEpisode currentEpisode = episodes.get(ii);
                    Map<Integer, Double> maxDists = new HashMap<>();
                    for (int index : indexes) {
                        maxDists.put(index, -Double.MAX_VALUE);
                    }
                    for (int j = 0; j < episodesList.size(); j++) {
                        if (i != j) {
                            List<MotivationEpisode> otherEpisodes = episodesList.get(j);
                            for (int jj = 0; jj < otherEpisodes.size(); jj++) {
                                MotivationEpisode compared = otherEpisodes.get(jj);
                                for (int index : indexes) {
                                    double diff = Math.abs(compared.getActionPerceptionPair().getExternalTn().get(index).getValue()
                                            - currentEpisode.getActionPerceptionPair().getExternalTn().get(index).getValue());
                                    if (diff > maxDists.get(index)) {
                                        maxDists.put(index, diff);
                                    }
                                }
                            }
                        }
                    }
                    for (int index : indexes) {
                        List<Double> orderedDistances = maxDistancesMap.get(index);
                        int k;
                        for (k = 0; k < orderedDistances.size(); k++) {
                            if (maxDists.get(index) > orderedDistances.get(k)) {
                                break;
                            }
                        }
                        orderedDistances.add(k, maxDists.get(index));
                    }
                }
            }
        } else {
            for (int index : indexes) {
                maxDistancesMap.put(index, new ArrayList<Double>());
            }
            List<MotivationEpisode> episodes = episodesList.get(0);
            for (int i = 0; i < episodes.size(); i++) {
                MotivationEpisode currentEpisode = episodes.get(i);
                Map<Integer, Double> maxDists = new HashMap<>();
                if (episodes.size() > 1) {
                    for (int index : indexes) {
                        maxDists.put(index, -Double.MAX_VALUE);
                    }

                    for (int j = 0; j < episodes.size(); j++) {
                        if (j != i) {
                            MotivationEpisode compared = episodes.get(j);
                            for (int index : indexes) {
                                double diff = Math.abs(compared.getActionPerceptionPair().getExternalTn().get(index).getValue() - currentEpisode.getActionPerceptionPair().getExternalTn().get(index).getValue());
                                if (diff > maxDists.get(index)) {
                                    maxDists.put(index, diff);
                                }
                            }
                        }
                    }

                    for (int index : indexes) {
                        List<Double> orderedDistances = maxDistancesMap.get(index);
                        int k;
                        for (k = 0; k < orderedDistances.size(); k++) {
                            if (maxDists.get(index) > orderedDistances.get(k)) {
                                break;
                            }
                        }
                        orderedDistances.add(k, maxDists.get(index));
                    }
                } else {
                    for (int index : indexes) {
                        maxDistancesMap.put(index, Arrays.asList(new Double[]{0.0}));
                    }
                }
            }
        }

        return maxDistancesMap;
    }

    public void addTraces(List<MotivationEpisode> newTrace) {
        if (newTrace.size() > 5) {
            tracesList.add(newTrace);
            nt += 1;
            numberOfGoalWithoutAntiTraces++;
            tracesMinDistancesMap = getMaxDistancesMap(tracesList);
        }
    }

    public void addWeakTraces(List<MotivationEpisode> newTrace) {
        weakTracesList.add(newTrace);
        nt += CF * 1;
        weakTracesMinDistancesMap = getMaxDistancesMap(weakTracesList);
    }

    public void addAntiTraces(List<MotivationEpisode> newTrace) {
        antiTracesList.add(newTrace);
        nat += 1;
        antitracesMinDistancesMap = getMaxDistancesMap(antiTracesList);
        numberOfGoalWithoutAntiTraces = 0;
    }

    public double getDrM(int index, MotivationEpisode current) {
        return Math.min(Math.abs(current.getActionPerceptionPair().getExternalTn().get(index).getValue() - lInf), Math.abs(current.getActionPerceptionPair().getExternalTn().get(index).getValue() - lSup));
    }

    public double getK(int index, double percentil100, double drm) {
        return Math.pow(0.0005, 1.0 / (ntFactor - 1));
    }

    public double norm(int t, List<Double> values) {
        double sum = 0;
        for (Double v : values) {
            sum += Math.pow(v, t);
        }
        return Math.pow(sum, (1.0 / (double) t));
    }

    private double getPercentil(int index, double percentil, Map<Integer, List<Double>> tracesMap) {
        return getPercentil(percentil, tracesMap.get(index));
    }

    private double getPercentil(double percentil, List<Double> minDistances) {
        int index = (int) (percentil * (minDistances.size() - 1));
        return minDistances.get(index);
    }

    private double getTraceHnjm(int index, ActionPerceptionPair current, MotivationEpisode trace, Map<Integer, List<Double>> minDistancesMap, double n) {
        double percentil100 = getPercentil(index, 1, minDistancesMap);
        double drm = getDrM(index, trace);

        double hlim;
        if (drm > percentil100) {
            double k = getK(index, percentil100, drm);
            hlim = (percentil100 + (drm - percentil100) * Math.pow(k, n - 1)) / 2.0;
        } else {
            hlim = percentil100 / 2.0;
        }

        double hjm = getHjm(index, current, trace);
        double hnjm;
        if (hjm < hlim) {
            hnjm = hjm;
        } else {
            hnjm = hlim + (hjm - hlim) * M;
        }
        return hnjm;
    }

    private double getHjm(int index, ActionPerceptionPair me, MotivationEpisode trace) {
        return Math.abs(me.getExternalTn().get(index).getValue() - trace.getActionPerceptionPair().getExternalTn().get(index).getValue());
    }

    private List<Double> getBiggestTraces(List<List<MotivationEpisode>> traceList, ActionPerceptionPair vector, Map<Integer, List<Double>> minTracesDistancesMap, double n) {
        List<Double> biggestTraceW = new ArrayList<>();
        for (List<MotivationEpisode> episodeList : traceList) {
            for (MotivationEpisode trace : episodeList) {
                List<Double> traceHnjm = new ArrayList<>();
                for (int index : indexes) {
                    double hnjm = getTraceHnjm(index, vector, trace, minTracesDistancesMap, n);
                    traceHnjm.add(hnjm);
                }
                double wj = Math.max(0, 1 - norm(2, traceHnjm) / norm(2, Arrays.asList(lSup - lInf, lSup - lInf)));
                if (biggestTraceW.size() < NTRACESTODISTANCE || wj > biggestTraceW.get(biggestTraceW.size() - 2)) {
                    int i;
                    for (i = 0; i < biggestTraceW.size(); i++) {
                        if (wj > biggestTraceW.get(i)) {
                            break;
                        }
                    }
                    biggestTraceW.add(i, wj);
                    if (biggestTraceW.size() > NTRACESTODISTANCE) {
                        biggestTraceW.remove(biggestTraceW.size() - 1);
                    }
                }
            }
        }
        while (biggestTraceW.size() < NTRACESTODISTANCE) {
            biggestTraceW.add(0.0);
        }
        return biggestTraceW;
    }

    public double getDistanceCertainty(ActionPerceptionPair current) {
        List<Double> biggestTraceW = getBiggestTraces(tracesList, current, tracesMinDistancesMap, nt);
        List<Double> biggestWeakTraceW = getBiggestTraces(weakTracesList, current, weakTracesMinDistancesMap, nt);
        List<Double> biggestAntiTraceW = getBiggestTraces(antiTracesList, current, antitracesMinDistancesMap, nat);
        double antiTraceFactor = 1;
        double traceFactor = 1;
        antiTraceFactor = nat;
        double sumW = 0;

        for (int i = 0; i < NTRACESTODISTANCE; i++) {
            if (biggestTraceW.get(0) >= biggestWeakTraceW.get(0)) {
                if (biggestTraceW.get(0) >= biggestAntiTraceW.get(0)) {
                    sumW += traceFactor * biggestTraceW.get(0);
                    biggestTraceW.remove(0);
                } else {
                    sumW -= antiTraceFactor * biggestAntiTraceW.get(0);
                    biggestAntiTraceW.remove(0);
                }
            } else {
                if (biggestWeakTraceW.get(0) >= biggestAntiTraceW.get(0)) {
                    sumW += biggestWeakTraceW.get(0);
                    biggestWeakTraceW.remove(0);
                } else {
                    sumW -= antiTraceFactor * biggestAntiTraceW.get(0);
                    biggestAntiTraceW.remove(0);
                }
            }
        }

        return Math.tanh(CP * sumW);
    }

    @Override
    public double getDensityCertainty(ActionPerceptionPair current) {

        if (correlationChangesCounter < 4) {
            return 0.0;
        }

        if (!corCandidate.satisfyCorrelation(current)) {
            return 0.0;
        }

        return Math.max(0, getDistanceCertainty(current));

    }

    /**
     * Actualiza la densidad de éxito, teniendo en cuenta el número de goals
     * conseguidos
     */
    @Override
    public void updateSuccessCertainty() {
        List<MotivationEpisode> currentChain = null;
        int traceSize = 0;
        if (valueFunction.getMemory() instanceof ValueFunctionEpisodicBuffer) {
            ValueFunctionEpisodicBuffer memory = (ValueFunctionEpisodicBuffer) valueFunction.getMemory();
            currentChain = memory.getCurrentChain();
            traceSize = memory.getTraceSize();
        }
        ns += currentChain.size() < minTraceSize ? 0 : (currentChain.size() / traceSize);
        this.successCertainty = Math.min(1.0, Math.pow(ns / nMax, gamma));
    }

    /**
     * Actualiza la densidad de certeza. Para ello, lo que hace es actualizar el
     * valor dt_sat de la función.
     */
    @Override
    public void updateDensityCertainty() {
        List<MotivationEpisode> traceEpisodes = null;
        if (valueFunction.getMemory() instanceof ValueFunctionEpisodicBuffer) {
            traceEpisodes = ((ValueFunctionEpisodicBuffer) valueFunction.getMemory()).getLastTrace();
        }
        Map<String, CorrelationChange> changes = getChanges(traceEpisodes);
        if (changes != null) {
            correlationChangesCounter++;
            if (correlationChangesCounter == 1) {
                correlationChanges.add(changes);
                initCandidateCorrelationChanges(correlationChanges);
            } else if (correlationChangesCounter > 1) {
                updateCandidateCorrelationChanges(changes);
            }
        }
        addTraces(storeTrace(((ValueFunctionEpisodicBuffer) valueFunction.getMemory()).getLastTrace()));
    }

    private List<MotivationEpisode> storeTrace(List<MotivationEpisode> episodes) {

        ValueFunctionEpisodicBuffer correlativeMotivationCacheEpisodicBuffer = (ValueFunctionEpisodicBuffer) valueFunction.getMemory();
        List<MotivationEpisode> cacheContent = correlativeMotivationCacheEpisodicBuffer.getLastTrace();
        List<MotivationEpisode> antiTrace = new ArrayList<>();
        if (cacheContent.size() < 2) {
            return new ArrayList<>();
        }
        int endLoop = cacheContent.size() - 1;

        for (int i = 0; i < endLoop; i++) {
            MotivationEpisode currentEpisode = cacheContent.get(endLoop - i);
            MotivationEpisode prevEpisode = cacheContent.get(endLoop - i - 1);
            double evaluationValue = getEvaluation(corCandidate.getComponent(), corCandidate.getTypeR(), currentEpisode.getActionPerceptionPair(), prevEpisode.getActionPerceptionPair());
            if (evaluationValue > 0) {
                antiTrace.add(currentEpisode);
            } else {
                break;
            }
        }
        return antiTrace;
    }

    public double getEvaluation(String correlationElement, CorrelationChange.TYPE correlationType, ActionPerceptionPair app1, ActionPerceptionPair app2) {
        int index = -1;

        if (correlationElement.toLowerCase().startsWith("ball")) {
            index = 2;
        } else if (correlationElement.toLowerCase().startsWith("box")) {
            index = 5;
        } else if (correlationElement.toLowerCase().startsWith("button")) {
            index = 12;
        }

        double diff = app1.getExternalTn().get(index).getValue() - app2.getExternalTn().get(index).getValue();

        if (correlationType == CorrelationChange.TYPE.DECREASE) {
            diff = -diff;
        }
        return diff;
    }

    @Override
    public double getSuccessCertainty() {
        return successCertainty;
    }

    @Override
    public double getCertainty(ActionPerceptionPair app) {
        return getSuccessCertainty() * getDensityCertainty(app);
    }


    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public double getNs() {
        return ns;
    }

    @Override
    public void setValueFunction(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    public CorrelationRecord getCorCandidate() {
        return corCandidate;
    }


    /**
     * FIXME
     * Ad hoc function for the experiment. Should be generalized as soon as possible.
     * This function calculates the correlation for every sensor and stores them, so most reliable 
     * can be chosen as correlation to be followed (to do in the future)
     * @param traces
     * @return 
     */
    public Map<String, CorrelationChange> getChanges(List<MotivationEpisode> traces) {

        CorrelationChange changeBall = null;
        CorrelationChange changeBox = null;
        CorrelationChange changeButton = null;

        MotivationEpisode perceptionBallI = null;
        MotivationEpisode perceptionBallT_1 = null;
        MotivationEpisode perceptionBallT = null;
        MotivationEpisode perceptionR;
        List<MotivationEpisode> episodesBallR = null;
        List<MotivationEpisode> episodesBallI = null;

        MotivationEpisode perceptionBoxI = null;
        MotivationEpisode perceptionBoxT_1 = null;
        MotivationEpisode perceptionBoxT = null;
        List<MotivationEpisode> episodesBoxR = null;
        List<MotivationEpisode> episodesBoxI = null;

        MotivationEpisode perceptionButtonI = null;
        MotivationEpisode perceptionButtonT_1 = null;
        MotivationEpisode perceptionButtonT = null;
        List<MotivationEpisode> episodesButtonR = null;
        List<MotivationEpisode> episodesButtonI = null;

        int ballCounterR = 1;
        int boxCounterR = 1;
        int buttonCounterR = 1;

        int ballCounterI = 0;
        int boxCounterI = 0;
        int buttonCounterI = 0;

        boolean isBallCorrelatedR = Boolean.TRUE;
        boolean isBoxCorrelatedR = Boolean.TRUE;
        boolean isButtonCorrelatedR = Boolean.TRUE;

        boolean isBallCorrelatedI = Boolean.TRUE;
        boolean isBoxCorrelatedI = Boolean.TRUE;
        boolean isButtonCorrelatedI = Boolean.TRUE;

        CorrelationChange.TYPE ballTypeR;
        CorrelationChange.TYPE ballTypeI = CorrelationChange.TYPE.CONSTANT;

        CorrelationChange.TYPE boxTypeR;
        CorrelationChange.TYPE boxTypeI = CorrelationChange.TYPE.CONSTANT;

        CorrelationChange.TYPE buttonTypeR;
        CorrelationChange.TYPE buttonTypeI = CorrelationChange.TYPE.CONSTANT;

        if (!traces.isEmpty()) {
            perceptionR = traces.get(traces.size() - 1);
            MotivationEpisode last = perceptionR;
            if (traces.size() > 1) {
                MotivationEpisode current = traces.get(traces.size() - 2);
                ComponentValue currentBall = current.getActionPerceptionPair().getExternalTn().getValues().get(2);
                ComponentValue currentBox = current.getActionPerceptionPair().getExternalTn().getValues().get(5);
                ComponentValue currentButton = current.getActionPerceptionPair().getExternalTn().getValues().get(12);

                ComponentValue lastBall = last.getActionPerceptionPair().getExternalTn().getValues().get(2);
                ComponentValue lastBox = last.getActionPerceptionPair().getExternalTn().getValues().get(5);
                ComponentValue lastButton = last.getActionPerceptionPair().getExternalTn().getValues().get(12);

                ballTypeR = ((currentBall.getValue() - lastBall.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentBall.getValue() - lastBall.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);
                boxTypeR = ((currentBox.getValue() - lastBox.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentBox.getValue() - lastBox.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);
                buttonTypeR = ((currentButton.getValue() - lastButton.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentButton.getValue() - lastButton.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);

                for (int i = traces.size() - 3; i >= 0; i--) {
                    last = current;
                    current = traces.get(i);

                    currentBall = current.getActionPerceptionPair().getExternalTn().getValues().get(2);
                    currentBox = current.getActionPerceptionPair().getExternalTn().getValues().get(5);
                    currentButton = current.getActionPerceptionPair().getExternalTn().getValues().get(12);

                    lastBall = last.getActionPerceptionPair().getExternalTn().getValues().get(2);
                    lastBox = last.getActionPerceptionPair().getExternalTn().getValues().get(5);
                    lastButton = last.getActionPerceptionPair().getExternalTn().getValues().get(12);

                    CorrelationChange.TYPE ballTypeCheck = ((currentBall.getValue() - lastBall.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentBall.getValue() - lastBall.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);
                    CorrelationChange.TYPE boxTypeCheck = ((currentBox.getValue() - lastBox.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentBox.getValue() - lastBox.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);
                    CorrelationChange.TYPE buttonTypeCheck = ((currentButton.getValue() - lastButton.getValue()) < -10e-6) ? CorrelationChange.TYPE.INCREASE : (((currentButton.getValue() - lastButton.getValue()) > 10e-6) ? CorrelationChange.TYPE.DECREASE : CorrelationChange.TYPE.CONSTANT);

                    if (isBallCorrelatedR) {
                        if (ballTypeR != ballTypeCheck) {
                            episodesBallR = new ArrayList<>();
                            episodesBallR.addAll(traces.subList(traces.size() - ballCounterR - 1, traces.size()));
                            perceptionBallT = last;
                            perceptionBallT_1 = current;
                            isBallCorrelatedR = Boolean.FALSE;
                        } else {
                            ballCounterR++;
                        }
                    } else if (isBallCorrelatedI) {
                        if (ballCounterI == 0) {
                            ballTypeI = ballTypeCheck;
                            ballCounterI++;
                        } else {
                            if (ballTypeCheck == ballTypeI) {
                                ballCounterI++;
                            } else {
                                perceptionBallI = last;
                                isBallCorrelatedI = Boolean.FALSE;
                            }
                        }
                    }

                    if (isBoxCorrelatedR) {
                        if (boxTypeR != boxTypeCheck) {
                            episodesBoxR = new ArrayList<>();
                            episodesBoxR.addAll(traces.subList(traces.size() - boxCounterR - 1, traces.size()));
                            perceptionBoxT = last;
                            perceptionBoxT_1 = current;
                            isBoxCorrelatedR = Boolean.FALSE;

                        } else {
                            boxCounterR++;
                        }
                    } else if (isBoxCorrelatedI) {
                        if (boxCounterI == 0) {
                            boxTypeI = boxTypeCheck;
                            boxCounterI++;
                        } else {
                            if (boxTypeCheck == boxTypeI) {
                                boxCounterI++;
                            } else {
                                perceptionBoxI = last;
                                isBoxCorrelatedI = Boolean.FALSE;
                            }
                        }
                    }

                    if (isButtonCorrelatedR) {
                        if (buttonTypeR != buttonTypeCheck) {
                            episodesButtonR = new ArrayList<>();
                            episodesButtonR.addAll(traces.subList(traces.size() - buttonCounterR - 1, traces.size()));
                            perceptionButtonT = last;
                            perceptionButtonT_1 = current;
                            isButtonCorrelatedR = Boolean.FALSE;

                        } else {
                            buttonCounterR++;
                        }
                    } else if (isButtonCorrelatedI) {
                        if (buttonCounterI == 0) {
                            buttonTypeI = buttonTypeCheck;
                            buttonCounterI++;
                        } else {
                            if (buttonTypeCheck == buttonTypeI) {
                                buttonCounterI++;
                            } else {
                                perceptionButtonI = last;
                                isButtonCorrelatedI = Boolean.FALSE;
                            }
                        }
                    }

                }

                if (perceptionBallT == null) {
                    episodesBallR = new ArrayList<>();
                    episodesBallR.addAll(traces);
                    perceptionBallT = current;
                }
                if (perceptionBoxT == null) {
                    episodesBoxR = new ArrayList<>();
                    episodesBoxR.addAll(traces);
                    perceptionBoxT = current;
                }
                if (perceptionButtonT == null) {
                    episodesButtonR = new ArrayList<>();
                    episodesButtonR.addAll(traces);
                    perceptionButtonT = current;
                }
                changeBall = new CorrelationChange("ballDistance_t+n", ballTypeR, ballTypeI, perceptionR, perceptionBallT, perceptionBallT_1, perceptionBallI, ballCounterR, ballCounterI, episodesBallR, episodesBallI);
                changeBox = new CorrelationChange("boxDistance_t+n", boxTypeR, boxTypeI, perceptionR, perceptionBoxT, perceptionBoxT_1, perceptionBoxI, boxCounterR, boxCounterI, episodesBoxR, episodesBoxI);
                changeButton = new CorrelationChange("buttonDistance_t+n", buttonTypeR, buttonTypeI, perceptionR, perceptionButtonT, perceptionButtonT_1, perceptionButtonI, buttonCounterR, buttonCounterI, episodesButtonR, episodesButtonI);

                Map<String, CorrelationChange> changs = new HashMap<>();

                changs.put("ballDistance_t+n", changeBall);
                changs.put("boxDistance_t+n", changeBox);
                changs.put("buttonDistance_t+n", changeButton);

                return changs;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    private void initCandidateCorrelationChanges(List<Map<String, CorrelationChange>> correlationChanges) {
        Map<String, List<CorrelationChange>> changes = new HashMap<>();
        for (Map<String, CorrelationChange> corChanges : correlationChanges) {
            for (String key : corChanges.keySet()) {
                if (!correlationTypes.containsKey(key)) {
                    correlationTypes.put(key, corChanges.get(key).getTypeR());
                    changes.put(key, new ArrayList<CorrelationChange>());
                    changes.get(key).add(corChanges.get(key));
                } else {
                    if (correlationTypes.get(key) != null) {
                        if (corChanges.get(key).getTypeR() != correlationTypes.get(key)) {
                            correlationTypes.put(key, null);
                            changes.remove(key);
                        } else {
                            changes.get(key).add(corChanges.get(key));
                        }
                    }
                }
            }
        }

        //Ad hoc code to determine the correlation of each value function. This should be detected (or explored) autonomously.
        if (valueFunction.getID().equals("valueFunction")) {
            corCandidate = new CorrelationRecord("boxDistance_t+n", CorrelationChange.TYPE.DECREASE, changes.get("boxDistance_t+n"));
            corCandidate.init();
        } else if (valueFunction.getID().equals("valuefunction2")) {
            corCandidate = new CorrelationRecord("ballDistance_t+n", CorrelationChange.TYPE.DECREASE, changes.get("ballDistance_t+n"));
            corCandidate.init();
        } else {
            corCandidate = new CorrelationRecord("buttonDistance_t+n", CorrelationChange.TYPE.DECREASE, changes.get("buttonDistance_t+n"));
            corCandidate.init();
        }
    }

    private void updateCandidateCorrelationChanges(Map<String, CorrelationChange> correlationChanges) {
        for (String key : correlationChanges.keySet()) {
            if (candidates.containsKey(key)) {
                candidates.get(key).update(correlationChanges.get(key));
            }
        }
    }

    public List<List<MotivationEpisode>> getTraces() {
        return tracesList;
    }

    public List<List<MotivationEpisode>> getAntiTraces() {
        return antiTracesList;
    }

    /**
     * Getter method that returns the number of consecutive goals achieved without having to store an antitrace in that time.
     * That is, when a goal is achieved, the counter is incremented by 1 and if an antitrace is stored, the counter is reset to 0.
     * @return the number of consecutive goals achieved without having to store an antitrace in that time
     */
    public int getNumberOfGoalsWithoutAntiTraces() {
        return numberOfGoalWithoutAntiTraces;
    }
    
    
    
    
    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        this.configuration = configuration;
        this.nMax = configuration.getDouble("nMax");
        this.minTraceSize = configuration.getInt("minTraceSize");
        this.gamma = configuration.getDouble("gamma");
        this.ntFactor = configuration.getDouble("ntFactor");
        this.CF = configuration.getDouble("CF");
        this.CP = configuration.getDouble("CP");
        this.M = configuration.getDouble("M");
        this.NTRACESTODISTANCE = configuration.getInt("NTRACESTODISTANCE");
        perceptions = (List<String>) configuration.getList("perceptions.id");
        indexes = new ArrayList<>();
        Integer index;
        for (String s : perceptions) {
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(s);
            if (index == null) {
                break;
            }
            indexes.add(index);
        }
    }

}
