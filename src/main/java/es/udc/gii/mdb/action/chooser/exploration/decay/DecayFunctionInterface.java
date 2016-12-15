package es.udc.gii.mdb.action.chooser.exploration.decay;

import es.udc.gii.mdb.util.config.Configurable;

/**
 *
 * @author Mastropiero
 */


public interface DecayFunctionInterface extends Configurable {
    
    
    
    public double getDecayValue(int index);
}
