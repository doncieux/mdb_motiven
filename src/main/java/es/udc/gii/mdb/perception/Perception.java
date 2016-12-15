/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.perception;

import es.udc.gii.mdb.knowledge.representation.Predictable;
import es.udc.gii.mdb.robot.ComponentValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class that represents a Perception of the robot.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class Perception implements Predictable {
    
    private List<ComponentValue> values;
    
    public Perception() {

        values = new ArrayList<>();
    }

    /**
     * Constructor of a Perception from an array or comma-separated list
     * of {@link ComponentValue}
     * @param inputValues
     */
    public Perception(ComponentValue... inputValues) {
        this();
        values.addAll(Arrays.asList(inputValues));

    }

    /**
     * Constructor from a {@link java.util.List} of {@link ComponentValue}
     * @param values
     */
    public Perception(List<ComponentValue> values) {
        this.values = values;
    }

    /**
     * Getter of the list of {@link ComponentValue}s
     * @return
     */
    @Override
    public List<ComponentValue> getValues() {
        return values;
    }

    @Override
    public void addValue(ComponentValue value) {
        values.add(value);
    }

    @Override
    public void addValues(Collection<ComponentValue> values) {
        this.values.addAll(values);
    }

    /**
     * Get a concrete field of the list of {@link ComponentValue}
     * @param index Index of the element we want to retrieve
     * @return {@link ComponentValue} corresponding to the index provided
     */
    @Override
    public ComponentValue get(int index) {
        return values.get(index);
    }

    /**
     * This method provides a way to filter a {@link Perception}, keeping only
     * those elements stored at positions specified by the list of integers passed
     * as parameter
     * @param indexes Indexes of elements that we want to keep
     * @return new {@link Perception} only with those elements we wanted.
     */
    public Perception getSubPerception(List<Integer> indexes) {
        List<ComponentValue> sub = new ArrayList<ComponentValue>(indexes.size());

        for(Integer i: indexes) {
            sub.add(values.get(i));
        }

        return new Perception(sub);

    }

    @Override
    public String toString() {
        String s = "";
        for(ComponentValue c: values) {
            s += c.getValue()+"\t";
        }
        return s.trim();
    }

    /**
     * Empties the perception
     */
    public void clear() {
        values.clear();
    }
    
}
