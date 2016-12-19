package es.udc.gii.mdb.motivation.buffer;

import java.util.List;

/**
 *
 * @author GII
 */
public class VariableCorrelation {

    private int correlation;
    private int correlationIters;

    private MotivationEpisode initCorrelation;
    private MotivationEpisode endCorrelation;
    private MotivationEpisode breakEpisode;

    private int index;
    private double minValue;
    private double maxValue;
    private List<Integer> correlations;

    public VariableCorrelation(int index, int correlation, int correlationIters, MotivationEpisode initCorrelation, MotivationEpisode endCorrelation, MotivationEpisode breakEpisode) {
        this.index = index;
        this.correlation = correlation;
        this.correlationIters = correlationIters;
        this.initCorrelation = initCorrelation;
        this.endCorrelation = endCorrelation;
        this.breakEpisode = breakEpisode;
        if (this.correlation == 0) {
            minValue = initCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() - 10e-6;
            maxValue = initCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() + 10e-6;
        } else if (correlation == 1) {
            minValue = endCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() - 10e-6;
            maxValue = initCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() + 10e-6;
        } else if (correlation == -1) {
            minValue = initCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() - 10e-6;
            maxValue = endCorrelation.getActionPerceptionPair().getExternalTn().get(index).getValue() + 10e-6;
        }
    }

    public void setCorrelations(List<Integer> correlations) {
        this.correlations = correlations;
    }

    public int getCorrelation() {
        return correlation;
    }

    public void setCorrelation(int correlation) {
        this.correlation = correlation;
    }

    public int getCorrelationIters() {
        return correlationIters;
    }

    public void setCorrelationIters(int correlationIters) {
        this.correlationIters = correlationIters;
    }

    public MotivationEpisode getInitCorrelation() {
        return initCorrelation;
    }

    public void setInitCorrelation(MotivationEpisode initCorrelation) {
        this.initCorrelation = initCorrelation;
    }

    public MotivationEpisode getEndCorrelation() {
        return endCorrelation;
    }

    public void setEndCorrelation(MotivationEpisode endCorrelation) {
        this.endCorrelation = endCorrelation;
    }

    public MotivationEpisode getBreakEpisode() {
        return breakEpisode;
    }

    public void setBreakEpisode(MotivationEpisode breakEpisode) {
        this.breakEpisode = breakEpisode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

}
