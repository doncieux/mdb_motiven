/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.mdb.action.strategy.PlainStrategy;
import es.udc.gii.mdb.action.strategy.Strategy;
import es.udc.gii.mdb.perception.constraint.DoubleConstraint;
import es.udc.gii.mdb.perception.constraint.EqConstraint;
import es.udc.gii.mdb.perception.constraint.GEqConstraint;
import es.udc.gii.mdb.perception.constraint.GTConstraint;
import es.udc.gii.mdb.perception.constraint.LEqConstraint;
import es.udc.gii.mdb.perception.constraint.LTConstraint;
import es.udc.gii.mdb.perception.constraint.NEqConstraint;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.memory.stm.register.InputModelRegister;
import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.Perception;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * This class inherits from Model and represents those 'first layer' models
 * (i.e. world models and internal model)
 *
 * @author GII
 */
public class InputModel extends Model {

    private List<DoubleConstraint> constraints;
    private List<ComponentValue> inputs, outputs;
    private List<Integer> perceptionIndex, actionIndex, outputIndex;

    public InputModel() {
    }

    public InputModel(String id, List<String> inputsIDs, List<String> outputsIDs, ModelType type) {
        super(id, inputsIDs, outputsIDs, type);

        perceptionIndex = new ArrayList<>();
        actionIndex = new ArrayList<>();
        outputIndex = new ArrayList<>();

        int k;
        Integer index;
        String s;
        for (k = 0; k < getInputsIDs().size() - ActionPerceptionPairMap.getInstance().getStrategyLength(); k++) {
            s = getInputsIDs().get(k);
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(s);
            if (index == null) {
                break;
            }
            perceptionIndex.add(index);

        }

        for (int j = k; j < getInputsIDs().size(); j++) {
            s = getInputsIDs().get(j);
            actionIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(s));
        }

        for (String str : getOutputsIDs()) {
            outputIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
        }

        constraints = new ArrayList<>();

    }

    private enum Operations {

        EQ, NEQ, LT, GT, LEQ, GEQ;
    }

    @Override
    public InputModelRegister doStore(ActionPerceptionPair ap) {

        boolean store = true;
        Register ret;
        InputModelRegister iMReg;
        for (DoubleConstraint c : constraints) {
            store = store && c.isSatisfied(ap);
        }

        if (store) {
            ret = (Register) getMemory().store(filterPair(ap));
            iMReg = (InputModelRegister) ret.getRegister();
            return iMReg;
        } else {
            return null;
        }
    }

    @Override
    protected InputModelRegister filterPair(ActionPerceptionPair ap) {

        Perception externalT = new Perception();
        Perception externalTn = new Perception();
        Strategy strategy = new PlainStrategy();

        if (ap.getExternalT() != null && !ap.getExternalT().getValues().isEmpty()) {
            externalT = ap.getExternalT().getSubPerception(perceptionIndex);
        }
        if (ap.getStrategy() != null && !ap.getStrategy().getActions().isEmpty()) {
            strategy = ap.getStrategy();

        }
        if (ap.getExternalTn() != null && !ap.getExternalTn().getValues().isEmpty()) {
            externalTn = ap.getExternalTn().getSubPerception(outputIndex);
        }

        return new InputModelRegister(externalT, strategy, externalTn);
    }

    @Override
    public Perception calculateOutput(Register r) {
        InputModelRegister reg = (InputModelRegister) r.getRegister();
        Perception o = calculateOutput(reg.getInputPerception(), reg.getStrategy());
        return o;
    }

    /**
     * Private helper method to calculate the output of an input model.
     *
     * @param perceptionT Perception obtained at instant t
     * @param strategyT Strategy executed at instant t
     * @return Perception predicted by the model
     */
    private Perception calculateOutput(Perception perceptionT, Strategy strategyT) {
        Perception perceptionTn = new Perception();

        try {
            inputs = new ArrayList<>();

            if (perceptionT != null) {
                inputs.addAll(perceptionT.getValues());
            }
            if (strategyT != null && !strategyT.getActions().isEmpty()) {
                inputs.addAll(strategyT.getActions().get(0).getValues());
            }

            outputs = this.calculateOutputs(inputs, getOutputsIDs());

            if (strategyT != null && !strategyT.getActions().isEmpty()) {
                for (int i = 1; i < strategyT.getActions().size(); i++) {
                    inputs = outputs;
                    inputs.addAll(strategyT.getActions().get(i).getValues());
                    outputs = this.calculateOutputs(inputs, getOutputsIDs());
                }
            }

            perceptionTn = new Perception(outputs);

        } catch (ModelException ex) {
            //EXCEPTION
            throw new MDBRuntimeException();
        }
        return perceptionTn;

    }

    @Override
    public void configure(Configuration conf) throws MissingConfigurationParameterException {
        super.configure(conf);

        perceptionIndex = new ArrayList<>();
        actionIndex = new ArrayList<>();
        outputIndex = new ArrayList<>();

        int k;
        Integer index;
        String s;
        for (k = 0; k < getInputsIDs().size() - ActionPerceptionPairMap.getInstance().getStrategyLength(); k++) {
            s = getInputsIDs().get(k);
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(s);
            if (index == null) {
                break;
            }
            perceptionIndex.add(index);

        }

        for (int j = k; j < getInputsIDs().size(); j++) {
            s = getInputsIDs().get(j);
            actionIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(s));
        }

        for (String str : getOutputsIDs()) {
            outputIndex.add(ActionPerceptionPairMap.getInstance().getRelativeIndex(str));
        }

        constraints = new ArrayList<>();

        Configuration constraintConf = conf.subset(ConfigUtilXML.MODEL_CONSTRAINTS_GROUP_TAG);
        List<String> constStrings = (List<String>) constraintConf.getList(ConfigUtilXML.MODEL_CONSTRAINT_TAG + "." + ConfigUtilXML.MODEL_CONSTRAINT_CONDITION_TAG);

        DoubleConstraint constraint;
        String field;
        double value;

        for (int i = 0; i < constStrings.size(); i++) {
            field = constraintConf.getString(ConfigUtilXML.MODEL_CONSTRAINT_TAG + "(" + i + ")." + ConfigUtilXML.MODEL_CONSTRAINT_FIELD_TAG);
            value = constraintConf.getDouble(ConfigUtilXML.MODEL_CONSTRAINT_TAG + "(" + i + ")." + ConfigUtilXML.MODEL_CONSTRAINT_VALUE_TAG);
            constraint = null;
            switch (Operations.valueOf(constStrings.get(i).toUpperCase())) {
                case EQ:
                    constraint = new EqConstraint(field, value);
                    break;
                case NEQ:
                    constraint = new NEqConstraint(field, value);
                    break;
                case GEQ:
                    constraint = new GEqConstraint(field, value);
                    break;
                case LEQ:
                    constraint = new LEqConstraint(field, value);
                    break;
                case GT:
                    constraint = new GTConstraint(field, value);
                    break;
                case LT:
                    constraint = new LTConstraint(field, value);
                    break;
                default:
                    //TODO
                    break;
            }

            constraints.add(constraint);
        }

    }

}
