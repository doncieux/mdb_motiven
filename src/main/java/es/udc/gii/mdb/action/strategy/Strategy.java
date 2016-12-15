/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.action.strategy;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.knowledge.representation.Predictable;
import java.util.List;

/**
 *
 * Abstract class representing a generic strategy.
 *
 * @author Borja Santos-Diez Vazquez
 */
public abstract class Strategy implements Predictable {

    /**
     *
     * This method provides access to the {@link Action}s that make up the strategy.
     *
     * @return A {@link List} containing the {@link Action}s of this strategy.
     */

    public abstract List<Action> getActions();

    /**
     *
     * Using the method {@link Action#getSubAction}, a strategy can also be filtered.
     *
     * @param indexes {@link List} of {@link Integer} specifying the fields of the action
     * to be kept.
     * @return A "simplified" strategy, with filtered {@link Action}s.
     */

    public abstract Strategy getSubStrategy(List<Integer> indexes);

    /**
     *
     * This method provides a strategy the ability to self-execute. As {@link Action}s
     * are self-sufficient, a strategy can be executed by executing each action individually.
     *
     */

    public void execute() {
        for(Action a: getActions()) {
            a.execute();
        }
    }

    /**
     *
     * Returns a string representation of the strategy, useful when writing data
     * in logs or similar.
     *
     * @return A string containing the representation of the strategy.
     */

    @Override
    public String toString() {
        String s = "";

        for(Action a: getActions()) {
            s += a.toString()+"\t";
        }

        return s.trim();
    }

}
