package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.util.config.Configurable;

/**
 *
 * @author Mastropiero
 */


public interface DecayFunctionInterface extends Configurable {
    
    
    
    public double getDecayValue(MotivationEpisode motivationEpisody, double initValue, double endValue, double previosValue);
    
    public double getInitValue();
    
    public double getDecayFactor();
    

}
