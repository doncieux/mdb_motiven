package es.udc.gii.mdb.action.chooser.exploration;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.robot.Actuator;
import es.udc.gii.mdb.robot.Component;
import es.udc.gii.mdb.robot.ComponentMap;
import es.udc.gii.mdb.util.MDBRandom;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mastropiero
 */
public class RandomActionSelector implements CandidateActionSelector {

    private int numberOfActions;
    private Actuator actuator;

    @Override
    public List<Action> getActionsToTry(ActionPerceptionPair app) {
        double minValue = actuator.getMinValue();
        double maxValue = actuator.getMaxValue();

        List<Action> actionsToTry = new ArrayList<>();
        
        for (int i = 0; i < numberOfActions; i++) {
            double actionValue = MDBRandom.nextDouble() * (maxValue - minValue) + minValue;
            actionsToTry.add(new Action(actuator.createValue(actionValue, Component.RAW_VALUE)));
        }
        return actionsToTry;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        numberOfActions = configuration.getInt("numberOfActions");
        actuator = (Actuator) ComponentMap.getInstance().getComponent(configuration.getString("actuator"));

    }

}
