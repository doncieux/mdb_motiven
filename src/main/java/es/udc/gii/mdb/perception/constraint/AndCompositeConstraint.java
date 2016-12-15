package es.udc.gii.mdb.perception.constraint;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import java.util.List;

/**
 *
 * @author Mastropiero
 */
public class AndCompositeConstraint extends CompositeConstraint {    
    
    @Override
    protected boolean doEvaluate(List<Constraint> constraints, ActionPerceptionPair ap) {
        for (Constraint c : constraints) {
            if (!c.isSatisfied(ap)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    protected String getSimbol() {
        return "&&";
    }

}
