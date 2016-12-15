package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import java.util.List;

/**
 * Registers the correlation changes for a perception 
 * @author GII
 */
public class CorrelationRecord {

    private CorrelationChange.TYPE typeR;
    private int nR;
    private int commonR;
    private List<CorrelationChange> changes;
    private String component;

    public CorrelationRecord(String component, CorrelationChange.TYPE typeR, List<CorrelationChange> changes) {
        this.component = component;
        this.typeR = typeR;
        this.nR = 0;
        this.commonR = 0;
        this.changes = changes;
    }

    public void init() {
        for (CorrelationChange correlationChange : changes) {
            nR += correlationChange.getnR();
            commonR++;
        }

    }

    public void update(CorrelationChange correlationChange) {
        changes.add(correlationChange);
        if (correlationChange.getTypeR() == typeR) {
            nR += correlationChange.getnR();
            if (nR > 5) {
                commonR++;
            }
        } else {
            nR -= 2 * correlationChange.getnR();
            commonR -= 2;
        }
    }

    
    public int getnR() {
        return nR;
    }

    public List<CorrelationChange> getChanges() {
        return changes;
    }


    public int getCommonR() {
        return commonR;
    }

    public CorrelationChange.TYPE getTypeR() {
        return typeR;
    }

    public String getComponent() {
        return component;
    }

    public boolean satisfyCorrelation(ActionPerceptionPair app1, ActionPerceptionPair app2) {
        int index = -1;

        if (component.toLowerCase().startsWith("ball")) {
            index = 2;
        } else if (component.toLowerCase().startsWith("box")) {
            index = 5;
        } else if (component.toLowerCase().startsWith("button")) {
            index = 12;
        }

        double diff = app1.getExternalTn().get(index).getValue() - app2.getExternalTn().get(index).getValue();

        if (typeR == CorrelationChange.TYPE.DECREASE) {
            diff = -diff;
        }

        return diff > 0;
    }

    public boolean satisfyCorrelation(ActionPerceptionPair app) {
        int index = -1;

        if (component.toLowerCase().startsWith("ball")) {
            index = 2;
        } else if (component.toLowerCase().startsWith("box")) {
            index = 5;
        } else if (component.toLowerCase().startsWith("button")) {
            index = 12;
        }

        double diff = app.getExternalTn().get(index).getValue() - app.getExternalT().get(index).getValue();

        if (typeR == CorrelationChange.TYPE.DECREASE) {
            diff = -diff;
        }

        return diff > 0;
    }

}
