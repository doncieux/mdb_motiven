package es.udc.gii.mdb.action.chooser.exploration.decay;

import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */


public class ExponentialDecayFunction implements DecayFunctionInterface {

   
    private double decayFactor;
    
    
    @Override
    public double getDecayValue(int index) {
        return Math.pow(decayFactor, index);
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        decayFactor = configuration.getDouble("decayFactor");
    }

    
    
    
}
