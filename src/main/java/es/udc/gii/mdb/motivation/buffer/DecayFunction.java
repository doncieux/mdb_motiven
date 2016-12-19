package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */


public class DecayFunction implements DecayFunctionInterface {

   
    private double decayFactor;
    private double initValue;
    
    
    @Override
    public double getDecayValue(MotivationEpisode motivationEpisody, double initValue, double endValue, double currentValue) {
        return currentValue - decayFactor > endValue ? currentValue - decayFactor : endValue;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        decayFactor = configuration.getDouble("decayFactor");
        initValue = configuration.getDouble("initValue");    
    }

    public double getInitValue() {
        return initValue;
    }

    public double getDecayFactor() {
        return decayFactor;
    }
    
    
    
    
}
