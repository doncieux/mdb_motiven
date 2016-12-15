/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.robot;

/**
 * A ComponentValue encapsulates both raw value and normalized value
 *
 * @author Borja Santos-Diez Vazquez
 */
public class ComponentValue {

    private String component;
    private double value;
    private double normalizedValue;

    public ComponentValue(String component, double value, double normalizedValue) {
        this.component = component;
        this.value = value;
        this.normalizedValue = normalizedValue;
    }

    /**
     * Getter of the normalized value
     * @return
     */
    public double getNormalizedValue() {
        return normalizedValue;
    }

    /**
     * Getter of the raw value
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter of the String that identifies the {@link Component} associated to
     * this ComponentValue
     * @return
     */
    public String getComponent() {
        return component;
    }
}
