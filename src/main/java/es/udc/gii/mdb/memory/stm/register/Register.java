/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.memory.stm.register;

import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.perception.Perception;
import java.util.List;

/**
 *
 * Intermediate interface, that represents the registers that the models use
 * in the learning processes.
 *
 * @author GII
 */
public interface Register extends Storageable {

    /**
     * 
     * Retrieve the output perception of a register.
     * 
     * @return The {@link Perception} representing the output values of the register.
     */
    
    public abstract List<Perception> getAllPerceptions();

    public abstract Perception getOutputPerception();

    public abstract Register getRegister();
}
