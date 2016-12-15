package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.util.config.Configurable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Rodri
 */
public interface Certainty extends Configurable {


    public void updateDensityCertainty();

    public void updateSuccessCertainty();

    public double getDensityCertainty(ActionPerceptionPair app);
     
    public double getSuccessCertainty();    
    
    public double getCertainty(ActionPerceptionPair app);

    public Configuration getConfiguration();
    
    public double getNs();

    public void setValueFunction(ValueFunction rewardModel);

}
