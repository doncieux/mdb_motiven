/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.mdb.action.chooser.exploration.evo.DistancesCertainty;
import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.data.ModelData;
import es.udc.gii.mdb.knowledge.representation.Predictable;
import es.udc.gii.mdb.learning.LearningAlgorithm;
import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.memory.stm.register.SatisfactionModelRegister;
import es.udc.gii.mdb.motivation.CertatintyAreaSubGoal;
import es.udc.gii.mdb.motivation.MotivationGoal;
import es.udc.gii.mdb.motivation.SensorGoal;
import es.udc.gii.mdb.motivation.buffer.MotivationEpisode;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.perception.Perception;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 * This class extends Model and represents the satisfaction model.
 *
 * @author GII
 */
public class ValueFunction extends Model {

    private List<ComponentValue> inputs;
    private List<ComponentValue> outputs;
    private List<Integer> inputExternalIndex;
    private List<Integer> inputInternalIndex;
    private List<Integer> outputIndex;
    private MotivationGoal<ActionPerceptionPair> goal;
    private Certainty certainty;
    private int firstIteration;
    private ValueFunction parent = null;
    private ValueFunction child = null;
    private double lastReward;
    private boolean subgoalCreated = false;
    private ActionPerceptionPair euAPP;
    private boolean goalAlreadyReached = false;
    private boolean antiTraceStored = false;

    public ValueFunction() {
        this.firstIteration = MDBCore.getInstance().getIterations();
    }

    public ValueFunction(String id, List<String> inputsIDs, List<String> outputsIDs, ModelType type, Configuration certaintyConfiguration) {
        super(id, inputsIDs, outputsIDs, type);
        inputExternalIndex = new ArrayList<>();
        inputInternalIndex = new ArrayList<>();
        outputIndex = new ArrayList<>();

        Integer index;

        for (String str : getInputsIDs()) {
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(str);
            if (index == null) {
                inputInternalIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
            } else {
                inputExternalIndex.add(index);
            }
        }

        for (String str : getOutputsIDs()) {
            outputIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
        }
        this.firstIteration = MDBCore.getInstance().getIterations();

        String className = certaintyConfiguration.getString("class");

        try {
            certainty = (Certainty) Class.forName(className).newInstance();
            certainty.setValueFunction(this);
            certainty.configure(certaintyConfiguration);
        } catch (InstantiationException ex) {
            Logger.getLogger(ValueFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ValueFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ValueFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MissingConfigurationParameterException ex) {
            Logger.getLogger(ValueFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Perception calculateOutput(Perception externalPerceptionTn, Perception internalPerceptionTn) {
        Perception satisfactionTn = null;

        try {
            inputs = new ArrayList<>();
            inputs.addAll(externalPerceptionTn.getValues());
            inputs.addAll(internalPerceptionTn.getValues());

            outputs = this.calculateOutputs(inputs, getOutputsIDs());

            satisfactionTn = new Perception(outputs);

        } catch (ModelException ex) {
            //EXCEPTION
            Logger.getLogger(InputModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return satisfactionTn;
    }

    @Override
    public void configure(Configuration conf) throws MissingConfigurationParameterException {
        super.configure(conf);

        inputExternalIndex = new ArrayList<>();
        inputInternalIndex = new ArrayList<>();
        outputIndex = new ArrayList<>();

        Integer index;

        for (String str : getInputsIDs()) {
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(str);
            if (index == null) {
                inputInternalIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
            } else {
                inputExternalIndex.add(index);
            }
        }

        for (String str : getOutputsIDs()) {
            outputIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
        }

        String sensorId = conf.getString("rewardSensor");
        setGoal(new SensorGoal(sensorId));

        Configuration certaintyConfiguration = conf.subset("certainty");
        String className = certaintyConfiguration.getString("class");

        try {
            certainty = (Certainty) Class.forName(className).newInstance();
            certainty.setValueFunction(this);
            certainty.configure(certaintyConfiguration);

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new MDBRuntimeException("Error instantiating Certainty class");
        }
    }

    @Override
    protected SatisfactionModelRegister filterPair(ActionPerceptionPair ap) {

        Perception externalTn = new Perception();
        Perception internalTn = new Perception();
        Perception satisfactionTn = new Perception();

        if (ap.getExternalTn() != null && !ap.getExternalTn().getValues().isEmpty()) {
            externalTn = ap.getExternalTn().getSubPerception(inputExternalIndex);
        }
        if (ap.getInternalTn() != null && !ap.getInternalTn().getValues().isEmpty()) {
            internalTn = ap.getInternalTn().getSubPerception(inputInternalIndex);
        }
        if (ap.getSatisfactionTn() != null && !ap.getSatisfactionTn().getValues().isEmpty()) {
            satisfactionTn = ap.getSatisfactionTn().getSubPerception(outputIndex);
        }

        return new SatisfactionModelRegister(externalTn, internalTn, satisfactionTn);
    }

    @Override
    public SatisfactionModelRegister doStore(ActionPerceptionPair ap) {
        //Al almacenar, comprobamos si se consiguió el goal y creamos el MotivationEpisode para almacenarlo en la memoria
        antiTraceStored = false;
        boolean goalReached = goal.goalReached(ap);
        lastReward = goalAlreadyReached ? 0 : (goalReached ? 1.0 : 0.0);
        //  globalFrequencyCriterion.update(ap);
        MotivationEpisode me = new MotivationEpisode(ap, lastReward, 1.0, MotivationEpisode.Type.REAL);
        Storageable store = getMemory().store(me);
        if (goalReached && !goalAlreadyReached) {
            certainty.updateSuccessCertainty();
            certainty.updateDensityCertainty();
            if (parent != null) {
                double parentEval = parent.calculateOutput(ap).getValues().get(0).getValue() * parent.getEU();
                double prevEval = getEU();

                if (parentEval < prevEval) {
                    euAPP = ap;
                }
            }
            goalAlreadyReached = true;
        }
        int maxSubogals = MDBCore.getInstance().getMaxSubgoals();

        if (!subgoalCreated && getHierarchy() <= maxSubogals) {

            if ((((DistancesCertainty) certainty).getNumberOfGoalsWithoutAntiTraces() > 4 && ((DistancesCertainty) certainty).getNs() > 6) || (((DistancesCertainty) certainty).getNs() > 10)) {
//                if (getHierarchy() % 2 == 0) {
                createCorrelationSubgoal();
//                } else {
//                    createDistanceSubgoal();
//                }
            }

//            if (((CorrelativeMotivationCacheEpisodicBuffer) getMemory()).canCreateSubgoal()) {
//                createCorrelationSubgoal();
//            } else if (certainty.getNs() >= 10.0) {
//                createDistanceSubgoal();
//            }
        }

        //FIXME
        return null;
    }

    @Override
    public Predictable calculateOutput(ActionPerceptionPair ap) {

//        if (!(goal instanceof SensorGoal) && goal.goalReached(ap)) {
//            ComponentValue cv = ComponentMap.getInstance().getComponent("satisfaction").createValue(1.0, Component.RAW_VALUE);
//            Perception p = new Perception(cv);
//            return p;
//        }
        return calculateOutput(filterPair(ap));
    }

    @Override
    public Perception calculateOutput(Register r) {
        SatisfactionModelRegister reg = (SatisfactionModelRegister) r.getRegister();
        return calculateOutput(reg.getExternalPerception(), reg.getInternalPerception());
    }

    @Override
    public ModelData calculateData(List<Storageable> content) {
        double[] absMeanEr;
        double[][] sampleEr, predictedOut, realOut;

        absMeanEr = new double[getOutputsIDs().size()];
        sampleEr = new double[content.size()][getOutputsIDs().size()];

        predictedOut = new double[content.size()][getOutputsIDs().size()];
        realOut = new double[content.size()][getOutputsIDs().size()];

        MotivationEpisode motivationEpisody;
        Predictable output;
        double realOutput;

        for (int i = 0; i < content.size(); i++) {
            motivationEpisody = (MotivationEpisode) content.get(i);
            output = calculateOutput(motivationEpisody.getActionPerceptionPair());
            realOutput = motivationEpisody.getReward();
            for (int j = 0; j < output.getValues().size(); j++) {
                predictedOut[i][j] = output.get(j).getValue();
                realOut[i][j] = realOutput;
                sampleEr[i][j] = Math.abs(predictedOut[i][j] - realOut[i][j]);
                if (i == 0) {
                    absMeanEr[j] = sampleEr[i][j];
                } else {
                    absMeanEr[j] += sampleEr[i][j];
                }
            }
        }
        double absError = 0.0;
        for (int i = 0; i < absMeanEr.length; i++) {
            absMeanEr[i] = absMeanEr[i] / content.size();
            absError += absMeanEr[i];
        }
        absError = absError / content.size();
        return new ModelData(absMeanEr, absError, sampleEr, predictedOut, realOut);
    }

    public void createCorrelationSubgoal() {
        createNewModelForSubgoal(new CertatintyAreaSubGoal(this));
    }

    private void createNewModelForSubgoal(MotivationGoal<ActionPerceptionPair> subgoal) {
        int hierarchy = getHierarchy() + 1;
        //FIXME
        ValueFunction vf2 = (ValueFunction) ModelFactory.getInstance().createModel("valuefunction" + hierarchy, Model.ModelType.VALUEFUNCTION, ModelFactory.ModelApproach.MOCK, getInputsIDs(), getOutputsIDs(), getRepresentationApproach().getRepresentationConfiguration(), certainty.getConfiguration());
        LearningAlgorithm newLearningAlgorithm = ModelFactory.getInstance().createLearningAlgorithmFromModel("valuefunction" + hierarchy, "" + hierarchy);
        vf2.setGoal(subgoal);
        vf2.setParent(this);
        setChild(vf2);
        subgoalCreated = true;
    }

    public MotivationGoal getGoal() {
        return goal;
    }

    public void setGoal(MotivationGoal goal) {
        this.goal = goal;
    }

    public int getFirstIteration() {
        return firstIteration;
    }

    public Certainty getCertainty() {
        return certainty;
    }

    public void setParent(ValueFunction parent) {
        this.parent = parent;
    }

    public ValueFunction getParent() {
        return parent;
    }

    public double getLastReward() {
        return lastReward;
    }

    public boolean isSubgoalCreated() {
        return subgoalCreated;
    }

    public void setChild(ValueFunction child) {
        this.child = child;
    }

    public ValueFunction getChild() {
        return child;
    }

    public double getEU() {
        if (parent == null || euAPP == null) {
            return 1.0;
        } else {
            return parent.calculateOutput(euAPP).getValues().get(0).getValue() * parent.getEU();
        }
    }

    public int getHierarchy() {
        if (parent == null) {
            return 1;
        } else {
            return 1 + parent.getHierarchy();
        }
    }

    public List<Integer> getInputExternalIndex() {
        return inputExternalIndex;
    }

    public List<Integer> getOutputIndex() {
        return outputIndex;
    }

    public List<Integer> getInputInternalIndex() {
        return inputInternalIndex;
    }

    public void resetGoalReachments() {
        this.goalAlreadyReached = false;
    }

    public void setAntiTraceStored() {
        antiTraceStored = true;
    }

    public boolean isAntiTraceStored() {
        return antiTraceStored;
    }

}
