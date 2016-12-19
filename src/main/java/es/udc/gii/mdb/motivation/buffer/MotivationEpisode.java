package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.perception.ActionPerceptionPair;

/**
 *
 * @author GII
 */


public class MotivationEpisode implements Storageable {
    private ActionPerceptionPair actionPerceptionPair;
    private double reward;
    private double certainty;
    public enum Type {REAL, VIRTUAL};
    private Type type;

    public MotivationEpisode(ActionPerceptionPair actionPerceptionPair, double reward, double certainty, Type type) {
        this.actionPerceptionPair = actionPerceptionPair;
        this.reward = reward;
        this.certainty = certainty;
        this.type = type;
    }

    public ActionPerceptionPair getActionPerceptionPair() {
        return actionPerceptionPair;
    }

    public void setActionPerceptionPair(ActionPerceptionPair actionPerceptionPair) {
        this.actionPerceptionPair = actionPerceptionPair;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
    
    public void updateReward(double reward) {
        //TODO
        this.reward = reward;
    }

    public double getCertainty() {
        return certainty;
    }

    public void setCertainty(double certainty) {
        this.certainty = certainty;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        String s;
        
        s = this.actionPerceptionPair.toString() + "\t" + Double.toString(this.reward);
        
        return s;
    }
    
    
}
