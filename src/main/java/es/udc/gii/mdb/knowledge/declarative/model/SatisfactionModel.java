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

import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.memory.stm.register.SatisfactionModelRegister;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.perception.Perception;
import es.udc.gii.mdb.robot.ComponentValue;
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
public class SatisfactionModel extends Model {

    private List<ComponentValue> inputs;
    private List<ComponentValue> outputs;
    private List<Integer> inputExternalIndex;
    private List<Integer> inputInternalIndex;
    private List<Integer> outputIndex;

    public SatisfactionModel() {
    }

    public SatisfactionModel(String id, List<String> inputsIDs, List<String> outputsIDs, ModelType type) {
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
        Register r = (Register) getMemory().store(filterPair(ap));
        SatisfactionModelRegister smr = (SatisfactionModelRegister) r.getRegister();
        return smr;
    }

    
    @Override
    public Perception calculateOutput(Register r) {
        SatisfactionModelRegister reg = (SatisfactionModelRegister) r.getRegister();
        return calculateOutput(reg.getExternalPerception(), reg.getInternalPerception());
    }

}
