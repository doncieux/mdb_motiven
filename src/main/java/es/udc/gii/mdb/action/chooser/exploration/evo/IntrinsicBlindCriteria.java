package es.udc.gii.mdb.action.chooser.exploration.evo;

import es.udc.gii.mdb.action.chooser.exploration.BlindCriteria;
import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class IntrinsicBlindCriteria implements BlindCriteria {

    private boolean useAction;
    private double n;
    private List<String> perceptions;
    private List<Integer> indexes;

    public IntrinsicBlindCriteria() {
    }
    
    public IntrinsicBlindCriteria(double n, boolean useAction, int[] indexes) {
        this.n = n;
        this.useAction = useAction;
        this.indexes = new ArrayList<>();
        for (int i : indexes) {
            this.indexes.add(i);
        }
    }

    public double getValue(ActionPerceptionPair app) {

        List<Double> candidateValues = getValues(app);

        double distance = 0;
        double auxDistance;
        double size = MDBCore.getInstance().getGlobalEpisodicBuffer().getContent().size();
        for (ActionPerceptionPair otherApp : MDBCore.getInstance().getGlobalEpisodicBuffer().getContent()) {
            auxDistance = calculateDistance(getValues(otherApp), candidateValues);
            distance += auxDistance/size;
        }

        return distance;
    }

    private List<Double> getValues(ActionPerceptionPair app) {
        List<Double> values = new ArrayList<>();
        for (ComponentValue cv : app.getExternalTn().getValues()) {
            values.add(cv.getNormalizedValue());
        }
        if (useAction) {
            for (ComponentValue cv : app.getStrategy().getValues()) {
                values.add(cv.getNormalizedValue());
            }
        }
        
        return values;
    }

   private double calculateDistance(List<Double> v1, List<Double> v2) {
        double dist = 0, val;
        double length;
        if (indexes != null && !indexes.isEmpty()) {
            for (int i : indexes) {
                val = (v1.get(i) - v2.get(i));
                dist += val * val;
            }
            length = indexes.size();
        } else {
            for (int i =0; i < v1.size(); i++) {
                val = (v1.get(i) - v2.get(i));
                dist += val * val;
            }
            length = v1.size();
        }
        dist = dist > 0 ? Math.pow(dist / length, n / 2.0) : 0.0;
        return dist;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
       n =  configuration.getDouble("n");
       useAction = configuration.getBoolean("useAction");
       perceptions = (List<String>) configuration.getList("perceptions.id");
       indexes = new ArrayList<>();
        Integer index;
       for (String s : perceptions) {
            index = ActionPerceptionPairMap.getInstance().getRelativeIndex(s);
            if (index == null) {
                break;
            }
            indexes.add(index);

        }
    }

}
