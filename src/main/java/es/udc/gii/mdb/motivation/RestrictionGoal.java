package es.udc.gii.mdb.motivation;

import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.constraint.Constraint;
import java.util.List;

/**
 *
 * @author Rodri
 */
public class RestrictionGoal implements MotivationGoal<ActionPerceptionPair> {

    private double step = 25;
    private List<Constraint> constraints;
    private ValueFunction valueFunction;

    public RestrictionGoal(ValueFunction vf, List<Constraint> constraints) {
        this.valueFunction = vf;
        this.constraints = constraints;
    }

    @Override
    public boolean goalReached(ActionPerceptionPair context) {
        boolean satisfied = Boolean.TRUE;
        for (Constraint c : constraints) {
            satisfied &= c.isSatisfied(context);
        }
        return satisfied;
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
        for (Constraint c : constraints) {
            s+=c.toString()+ "\n";
        }
        return s;
    }
    
    


}
