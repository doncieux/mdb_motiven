package es.udc.gii.mdb.action.chooser.exploration.evo;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.action.chooser.StrategyChooser;
import es.udc.gii.mdb.action.strategy.PlainStrategy;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.motivation.buffer.CorrelationChange;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.Perception;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * Specific class for the Collect A Ball experiment. This class should be
 * removed in the future in order to generalize its methods for any case
 *
 * @author GII
 */
public class CollectABallCorrelationExplorationChooser extends CorrelationExplorationChooser implements StrategyChooser {

  
    private Model ballSinAngleModel, ballCosAngleModel, ballDistanceModel,
            boxSinAngleModel, boxCosAngleModel, boxDistanceModel, doorSinAngleModel,
            doorCosAngleModel, doorDistanceModel, doorStateModel, buttonSinAngleModel,
            buttonCosAngleModel, buttonDistanceModel, buttonStateModel;

    public CollectABallCorrelationExplorationChooser() {
    }

    public double getExtrinsicEval(String correlationElement, CorrelationChange.TYPE correlationType, ActionPerceptionPair app) {
        int index = -1;
        if (correlationElement == null) {
            return 0.0;
        }
        if (correlationElement.toLowerCase().startsWith("ball")) {
            index = 2;
        } else if (correlationElement.toLowerCase().startsWith("box")) {
            index = 5;
        } else if (correlationElement.toLowerCase().startsWith("button")) {
            index = 12;
        }
        double diff = app.getExternalTn().get(index).getValue() - app.getExternalT().get(index).getValue();

        if (correlationType == CorrelationChange.TYPE.DECREASE) {
            diff = -diff;
        }
        return diff;
    }

    public double getEvaluation(String correlationElement, CorrelationChange.TYPE correlationType, ActionPerceptionPair app1, ActionPerceptionPair app2) {
        int index = -1;

        if (correlationElement.toLowerCase().startsWith("ball")) {
            index = 2;
        } else if (correlationElement.toLowerCase().startsWith("box")) {
            index = 5;
        } else if (correlationElement.toLowerCase().startsWith("button")) {
            index = 12;
        }

        double diff = app1.getExternalTn().get(index).getValue() - app2.getExternalTn().get(index).getValue();

        if (correlationType == CorrelationChange.TYPE.DECREASE) {
            diff = -diff;
        }
        return diff;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        super.configure(configuration);
        this.ballSinAngleModel = ModelMap.getInstance().getModel("ballSinAngleModel");
        this.ballCosAngleModel = ModelMap.getInstance().getModel("ballCosAngleModel");
        this.ballDistanceModel = ModelMap.getInstance().getModel("ballDistanceModel");

        this.boxSinAngleModel = ModelMap.getInstance().getModel("boxSinAngleModel");
        this.boxCosAngleModel = ModelMap.getInstance().getModel("boxCosAngleModel");
        this.boxDistanceModel = ModelMap.getInstance().getModel("boxDistanceModel");

        this.doorSinAngleModel = ModelMap.getInstance().getModel("doorSinAngleModel");
        this.doorCosAngleModel = ModelMap.getInstance().getModel("doorCosAngleModel");
        this.doorDistanceModel = ModelMap.getInstance().getModel("doorDistanceModel");
        this.doorStateModel = ModelMap.getInstance().getModel("doorStateModel");

        this.buttonSinAngleModel = ModelMap.getInstance().getModel("buttonSinAngleModel");
        this.buttonCosAngleModel = ModelMap.getInstance().getModel("buttonCosAngleModel");
        this.buttonDistanceModel = ModelMap.getInstance().getModel("buttonDistanceModel");
        this.buttonStateModel = ModelMap.getInstance().getModel("buttonStateModel");

    }

    protected ActionPerceptionPair buildActionPerceptionPair(ActionPerceptionPair app, Action a) {
        ActionPerceptionPair returnApp = new ActionPerceptionPair();
        returnApp.setExternalT(app.getExternalT());
        returnApp.setInternalT(app.getInternalT());
        returnApp.setStrategy(new PlainStrategy(a));

        Perception ballSinAngleTn = (Perception) ballSinAngleModel.calculateOutput(returnApp);
        Perception ballCosAngleTn = (Perception) ballCosAngleModel.calculateOutput(returnApp);
        Perception ballDistanceTn = (Perception) ballDistanceModel.calculateOutput(returnApp);
        Perception boxSinAngleTn = (Perception) boxSinAngleModel.calculateOutput(returnApp);
        Perception boxCosAngleTn = (Perception) boxCosAngleModel.calculateOutput(returnApp);
        Perception boxDistanceTn = (Perception) boxDistanceModel.calculateOutput(returnApp);
        Perception doorSinAngleTn = (Perception) doorSinAngleModel.calculateOutput(returnApp);
        Perception doorCosAngleTn = (Perception) doorCosAngleModel.calculateOutput(returnApp);
        Perception doorDistanceTn = (Perception) doorDistanceModel.calculateOutput(returnApp);
        Perception doorStateTn = (Perception) doorStateModel.calculateOutput(returnApp);
        Perception buttonSinAngleTn = (Perception) buttonSinAngleModel.calculateOutput(returnApp);
        Perception buttonCosAngleTn = (Perception) buttonCosAngleModel.calculateOutput(returnApp);
        Perception buttonDistanceTn = (Perception) buttonDistanceModel.calculateOutput(returnApp);
        Perception buttonStateTn = (Perception) buttonStateModel.calculateOutput(returnApp);

        List<ComponentValue> perceptionTn = new ArrayList<>();
        perceptionTn.addAll(ballSinAngleTn.getValues());
        perceptionTn.addAll(ballCosAngleTn.getValues());
        perceptionTn.addAll(ballDistanceTn.getValues());
        perceptionTn.addAll(boxSinAngleTn.getValues());
        perceptionTn.addAll(boxCosAngleTn.getValues());
        perceptionTn.addAll(boxDistanceTn.getValues());
        perceptionTn.addAll(doorSinAngleTn.getValues());
        perceptionTn.addAll(doorCosAngleTn.getValues());
        perceptionTn.addAll(doorDistanceTn.getValues());
        perceptionTn.addAll(doorStateTn.getValues());
        perceptionTn.addAll(buttonSinAngleTn.getValues());
        perceptionTn.addAll(buttonCosAngleTn.getValues());
        perceptionTn.addAll(buttonDistanceTn.getValues());
        perceptionTn.addAll(buttonStateTn.getValues());
        //    perceptionTn.addAll(loadTn.getValues());

        Perception externalTn = new Perception(perceptionTn);
        returnApp.setExternalTn(externalTn);

        return returnApp;

    }

}
