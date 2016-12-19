/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.action.strategy;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.robot.ComponentValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * Represents a simple strategy. It stores actions into a {@link List}.
 *
 * @author GII
 */
public class PlainStrategy extends Strategy {
    private List<Action> actions;

    /**
     *
     * Constructor that takes a set of {@link Action}s (as array or comma-separated
     * values).
     *
     * @param inputActions Set of {@link Action}s that form a {@link Strategy}.
     */

    public PlainStrategy(Action... inputActions) {
        this.actions = new ArrayList();
        this.actions.addAll(Arrays.asList(inputActions));
    }

    /**
     *
     * Constructor that creates a {@link Strategy} from a {@link List} of {@link Action}s
     * provided
     *
     * @param actions {@link List} of {@link Action}s.
     */

    public PlainStrategy(List<Action> actions) {
        this.actions = actions;
    }

    /**
     *
     * Returns a {@link List} containing the set of {@link Action}s that make up
     * the {@link Strategy}.
     *
     * @return A {@link List} containing the {@link Action}s.
     */

    @Override
    public List<Action> getActions() {
        return actions;
    }

    /**
     *
     * Using the method {@link Action#getSubStrategy}, this method returns a substrategy
     * filtering each {@link Action} using the indexes specified by the parameter.
     *
     * @param indexes {@link List} of {@link Integer} that specifies the fields of the {@link Action}
     * to be considered.
     * @return A "simplified" {@link Strategy}, where the {@link Action}s only contain the fields
     * specified by the parameter.
     */

    @Override
    public Strategy getSubStrategy(List<Integer> indexes) {
        List<Action> sub = new ArrayList<Action>();

        for(Action a: getActions()) {
            sub.add(a.getSubAction(indexes));
        }

        return new PlainStrategy(sub);

    }

    @Override
    public List<ComponentValue> getValues() {
        List<ComponentValue> values = new ArrayList<ComponentValue>();
        for (Action a : getActions()) {
            values.addAll(a.getValues());
        }
        return values;
    }

    @Override
    public void addValue(ComponentValue value) {
        actions.add(new Action(value));
    }

    @Override
    public void addValues(Collection<ComponentValue> values) {
        
        for (ComponentValue v : values) {
            Action a = new Action(v);
            actions.add(a);
        }
    }

    @Override
    public ComponentValue get(int index) {
        return actions.get(0).getValues().get(index);
    }

    
}
