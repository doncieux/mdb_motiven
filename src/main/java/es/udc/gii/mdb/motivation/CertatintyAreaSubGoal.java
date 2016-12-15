package es.udc.gii.mdb.motivation;

import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.perception.ActionPerceptionPair;

/**
 *
 * @author Rodri
 */
public class CertatintyAreaSubGoal implements MotivationGoal<ActionPerceptionPair> {

    private ValueFunction rewardModel;

    public CertatintyAreaSubGoal(ValueFunction rm) {
        this.rewardModel = rm;
    }

    @Override
    public boolean goalReached(ActionPerceptionPair context) {

        return rewardModel.getCertainty().getDensityCertainty(context) > 0.95;
    }

    @Override
    public Type getType() {
        return Type.SUBGOAL;
    }

    @Override
    public void setDistance(double distance) {

    }

    @Override
    public double getDistance() {
        return 0.0;
    }



    @Override
    public ActionPerceptionPair getData() {
        return null;

    }

    @Override
    public String toString() {
        String s = "";
        
        return s;
    }
    
    


}
