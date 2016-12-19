/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.action;

import es.udc.gii.mdb.robot.Actuator;
import es.udc.gii.mdb.robot.ActuatorMap;
import es.udc.gii.mdb.robot.ComponentValue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class represents an action, and it contains a set of {@link ComponentValue}s,
 * each one associated to a concrete {@link Actuator}. An action can be made up of
 * as many {@link ComponentValue}s as desired.
 *
 * @author GII
 */
public class Action {
    
    private List<ComponentValue> values;

    /**
     *
     * Constructor of the class. It takes a set of {@link ComponentValue}s as parameter
     * (as array or as comma-separated values).
     *
     * @param inputValues {@link ComponentValue}s to create an action.
     */

    public Action(ComponentValue... inputValues) {
        values = new ArrayList<ComponentValue>(inputValues.length);
        for(int i=0; i<inputValues.length; i++) {
            values.add(inputValues[i]);
        }
        
    }

    /**
     *
     * This constructor takes a {@link List} of {@link ComponentValue} as parameter.
     * Useful if we already have a list of {@link ComponentValue} to create an action.
     *
     * @param values {@link List} of {@link ComponentValue}s.
     */

    public Action(List<ComponentValue> values) {
        this.values = values;
    }

    /**
     * 
     * Returns a list containing the set of {@link ComponentValue}s that define
     * this action.
     *
     * @return A list containing the {@link ComponentValue}s of the action.
     */


    public List<ComponentValue> getValues() {
        return values;
    }

    /**
     *
     * This method returns an action containing the fields specified by the parameter.
     * Useful when simplifying the {@link es.udc.gii.gsa.mdb.perception.ActionPerceptionPair} to be stored in
     * memory.
     *
     * @param indexes {@link List} of {@link Integer} specifying which fields of
     * the action have to be filtered.
     * @return A "simplified" action, containing just the fields specified by
     * the parameter indexes.
     */

    public Action getSubAction(List<Integer> indexes) {
        List<ComponentValue> res = new ArrayList<ComponentValue>();

        for(Integer i: indexes) {
            res.add(values.get(i));
        }

        return new Action(res);
    }


    /**
     *
     * Executes the action, as each action is self-sufficient. It retrieves
     * the {@link Actuator}s that have to be considered and sends the orders to
     * them.
     *
     */

    public void execute() {
        Actuator a;
        for(ComponentValue c: values) {
            //TEST
            a = ActuatorMap.getInstance().getComponent(c.getComponent());
            a.setValue(c);
        }
    }


    /**
     *
     * Returns a string representation of the action, showing the different
     * {@link ComponentValue} as strings.
     *
     * @return A string with the representation of the action.
     */

    @Override
    public String toString() {
        String s="";
        for(ComponentValue c: values) {
            s+=c.getValue()+"\t";
        }

        return s.trim();
    }


    
}