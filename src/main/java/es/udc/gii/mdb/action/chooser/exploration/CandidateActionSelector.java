package es.udc.gii.mdb.action.chooser.exploration;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.util.config.Configurable;
import java.util.List;

/**
 *
 * @author Mastropiero
 */


public interface CandidateActionSelector extends Configurable{
    
    public List<Action> getActionsToTry(ActionPerceptionPair app);
}
