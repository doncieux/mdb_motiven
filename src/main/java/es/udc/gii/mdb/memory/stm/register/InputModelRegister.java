/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.memory.stm.register;

import es.udc.gii.mdb.action.strategy.Strategy;
import es.udc.gii.mdb.perception.Perception;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Relization of the interface {@link Register}, in this case representing the registers
 * used by the two types of "input models": world models and internal models.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class InputModelRegister implements Register {

    private Perception inputs, outputs;
    private Strategy strategy;

    /**
     *
     * Constructor of a register of this type. We must provide the perception and the
     * strategy on a instant t and 
     *
     * @param inputs
     * @param strategy
     * @param outputs
     */

    public InputModelRegister(Perception inputs, Strategy strategy, Perception outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.strategy = strategy;
    }

    /**
     * Getter of the input perception of this register
     * @return The {@link Perception}
     */

    public Perception getInputPerception() {
        return inputs;
    }

    /**
     * Getter of the output perception of this register
     * @return The {@link Perception}
     */

    @Override
    public Perception getOutputPerception() {
        return outputs;
    }

    /**
     * Getter of the strategy of this register
     * @return The {@link Strategy}
     */

    public Strategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return inputs.toString()+"\t"+strategy.toString()+"\t"+outputs.toString();
    }
    
    @Override
    public List<Perception> getAllPerceptions() {
        List<Perception> perceptions = new ArrayList<Perception>();
        perceptions.add(getInputPerception());
        perceptions.add(getOutputPerception());
        return perceptions;
    }

    @Override
    public Register getRegister() {
        return this;
    }
}
