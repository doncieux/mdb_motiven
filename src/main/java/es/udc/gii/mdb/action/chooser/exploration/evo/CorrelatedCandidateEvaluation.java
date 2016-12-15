package es.udc.gii.mdb.action.chooser.exploration.evo;

import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.motivation.buffer.CorrelationChange;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.robot.ComponentValue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GII
 */
public class CorrelatedCandidateEvaluation {

    private ActionPerceptionPair app;
    private int hierarchy;
    private double intrinsicBlindMotivation;
    private CorrelationChange.TYPE correlationType;
    private String correlationElement;
    private double certainty;
    private ValueFunction valueFunction;
    private double followingCorrelationEval;

    public CorrelatedCandidateEvaluation(ActionPerceptionPair app, ValueFunction valueFunction, double followingCorrelationEval, double certainty, CorrelationChange.TYPE correlationType, String correlationElement) {
        this.app = app;
        this.followingCorrelationEval = followingCorrelationEval;
        this.valueFunction = valueFunction;
        this.correlationType = correlationType;
        this.certainty = certainty;
        this.correlationElement = correlationElement;
    }

    public double getFollowingCorrelationEval() {
        return followingCorrelationEval;
    }
    
    public void setIntrinsicBlindMotivation(double intrinsicBlindMotivation) {
        this.intrinsicBlindMotivation = intrinsicBlindMotivation;
    }

    public ActionPerceptionPair getApp() {
        return app;
    }

    public double getIntrinsicBlindMotivation() {
        return intrinsicBlindMotivation;
    }

    public int getHierarchy() {
        return hierarchy;
    }

    public double getExtrinsicEvaluation() {
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

    public ValueFunction getValueFunction() {
        return valueFunction;
    }

    public double getEvaluation() {
        int index = -1;
        if (correlationElement == null) {
            return intrinsicBlindMotivation;

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

    public List<Double> getLogLine() {
        List<Double> logLine = new ArrayList<>();

        //APP
        for (ComponentValue cv : app.getExternalT().getValues()) {
            logLine.add(cv.getValue());
        }
        for (ComponentValue cv : app.getStrategy().getValues()) {
            logLine.add(cv.getValue());
        }

        for (ComponentValue cv : app.getExternalTn().getValues()) {
            logLine.add(cv.getValue());
        }

        logLine.add(certainty);

        return logLine;
    }

    public CorrelationChange.TYPE getCorrelationType() {
        return correlationType;
    }

    public String getCorrelationElement() {
        return correlationElement;
    }

    
    
}
