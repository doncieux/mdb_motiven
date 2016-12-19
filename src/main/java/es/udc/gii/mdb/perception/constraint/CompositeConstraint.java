package es.udc.gii.mdb.perception.constraint;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GII
 */


public abstract class CompositeConstraint extends Constraint {

    private List<Constraint> constraints;

    public CompositeConstraint() {
        this.constraints = new ArrayList<>();
    }
    
    
    @Override
    public boolean isSatisfied(ActionPerceptionPair ap) {
        return doEvaluate(constraints, ap);
    }

    @Override
    public void addConstraint(Constraint c) {
        constraints.add(c);
    }
    
    @Override
    public void removeConstraint(Constraint c) {
        constraints.remove(c);
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
    
    
    
    /**
     *
     * Abstract method to be implemented by each extending class. It represents
     * the comparison itself.
     *
     * @param constraints
     * @param ap
     * @return Whether the field satisfies the constraint or not.
     */
    protected abstract boolean doEvaluate(List<Constraint> constraints, ActionPerceptionPair ap);

    protected abstract String getSimbol();
    
    @Override
    public String toString() {
        String s = "";
        for (Constraint c : constraints) {
            s+= c.toString() + " " + getSimbol() + " ";
        }
        
        return s; //To change body of generated methods, choose Tools | Templates.
    }

    
}
