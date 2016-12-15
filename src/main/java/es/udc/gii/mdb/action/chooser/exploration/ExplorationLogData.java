package es.udc.gii.mdb.action.chooser.exploration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mastropiero
 */
public class ExplorationLogData {

    private List<List<Double>> values;
    private int bestIndex = -1;

    public ExplorationLogData() {
        values = new ArrayList<>();
    }

    public ExplorationLogData(List<List<Double>> values, int bestIndex) {
        this.values = values;
        this.bestIndex = bestIndex;
    }

    public ExplorationLogData(List<List<Double>> values) {
        this.values = values;
    }

    public List<List<Double>> getValues() {
        return values;
    }

    public void setValues(List<List<Double>> values) {
        this.values = values;
    }

    public void addValues(List<Double> val) {
        values.add(val);
    }

    public int getBestIndex() {
        return bestIndex;
    }

    public void setBestIndex(int bestIndex) {
        this.bestIndex = bestIndex;
    }

    public List<Double> getBest() {
        if (bestIndex >= 0 && bestIndex < values.size()) {
            return values.get(bestIndex);
        } else {
            return new ArrayList<>();
        }
    }

}
