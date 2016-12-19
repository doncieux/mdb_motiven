/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.memory.stm.register;

import es.udc.gii.mdb.perception.Perception;
import java.util.ArrayList;
import java.util.List;

/**
 * Realization of the interface {@link Register} to represent those registers
 * managed by the satisfaction model.
 *
 * @author GII
 */
public class SatisfactionModelRegister implements Register {

    private Perception externalPerception, internalPerception, satisfaction;

    /**
     * Constructor of a register of this type. We need to provide both internal
     * and external perceptions and the associated satisfaction.
     * @param externalPerception
     * @param internalPerception
     * @param satisfaction
     */

    public SatisfactionModelRegister(Perception externalPerception, Perception internalPerception, Perception satisfaction) {
        this.externalPerception = externalPerception;
        this.internalPerception = internalPerception;
        this.satisfaction = satisfaction;
    }

    /**
     * Getter of the external perception
     * @return
     */
    public Perception getExternalPerception() {
        return externalPerception;
    }

    /**
     * Getter of the internal perception
     * @return
     */
    public Perception getInternalPerception() {
        return internalPerception;
    }

    /**
     * Getter of the output perception. Inherited.
     * @return
     */

    @Override
    public Perception getOutputPerception() {
        return satisfaction;
    }

    @Override
    public String toString() {
        return externalPerception.toString() + "\t" + internalPerception.toString() + "\t" + satisfaction.toString();
    }

    @Override
    public Register getRegister() {
        return this;
    }

    @Override
    public List<Perception> getAllPerceptions() {
        List<Perception> perceptions = new ArrayList<Perception>();
        perceptions.add(getOutputPerception());
        perceptions.add(getExternalPerception());
        perceptions.add(getInternalPerception());
        return perceptions;
    }

}
