package es.udc.gii.mdb.perception.constraint;

import es.udc.gii.mdb.perception.ActionPerceptionPair;

/**
 *
 * @author Mastropiero
 */
public abstract class Constraint {

    public abstract boolean isSatisfied(ActionPerceptionPair ap);

    public void addConstraint(Constraint c) {
        throw new UnsupportedOperationException("Imposible to add to this class");
    }

    public void removeConstraint(Constraint c) {
        throw new UnsupportedOperationException("Imposible to add to this class");

    }
}
