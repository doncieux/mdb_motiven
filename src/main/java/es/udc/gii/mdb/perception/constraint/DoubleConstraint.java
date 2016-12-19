/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.perception.constraint;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap.APPairModule;

/**
 *
 * Abstract class representing a generic constraint based in double values. Used
 * when filtering an {@link ActionPerceptionPair} based on a value of a concrete field.
 *
 * @author GII
 */
public abstract class DoubleConstraint extends Constraint {

    protected static final double THRESHOLD = 0.0;
    
    private final double refValue;
    private final APPairModule module;
    private final int index;
    private final String field;

    /**
     *
     * Constructor of the constraint. Here we specify the field to take into consideration
     * and a reference value, which we will use to compare.
     *
     * @param field Name of the field of the {@link ActionPerceptionPair}.
     * @param refValue Reference value to perform the comparison.
     */

    public DoubleConstraint(String field, double refValue) {
        this.refValue = refValue;
        this.field = field;
        this.module = ActionPerceptionPairMap.getInstance().getModule(field);
        this.index = ActionPerceptionPairMap.getInstance().getRelativeIndex(field);

    }

    /**
     *
     * Get the reference value.
     *
     * @return Reference value to perform the comparison.
     */

    protected double getRefValue() {
        return refValue;
    }

    /**
     *
     * Get the index (integer) into the {@link ActionPerceptionPair} of the field
     * considered by this constraint.
     *
     * @return Index of the field.
     */

    protected int getIndex() {
        return index;
    }

    /**
     *
     * Evaluates the {@link ActionPerceptionPair} provided and establishes whether it
     * satisfies the constraint or not. It implements a template method pattern, that
     * each extending class will concrete.
     *
     * @param ap {@link ActionPerceptionPair} to be evaluated.
     * @return Whether the {@link ActionPerceptionPair} satisfies the constraint or not.
     */

    public boolean isSatisfied(ActionPerceptionPair ap) {
        double value = 0;

        switch(module) {
            case EXTERNAL_T:
                value = ap.getExternalT().getValues().get(index).getValue();
                break;
            case INTERNAL_T:
                value = ap.getInternalT().getValues().get(index).getValue();
                break;
            case EXTERNAL_T_N:
                value = ap.getExternalTn().getValues().get(index).getValue();
                break;
            case INTERNAL_T_N:
                value = ap.getInternalTn().getValues().get(index).getValue();
                break;
            case SATISFACTION_T_N:
                value = ap.getSatisfactionTn().getValues().get(index).getValue();
                break;
            default:
                //TODO
                break;
        }

        return doEvaluate(value);
    }
    
    
    /**
     *
     * Abstract method to be implemented by each extending class. It represents
     * the comparison itself.
     *
     * @param value Current value of the field taken into consideration.
     * @return Whether the field satisfies the constraint or not.
     */
    protected abstract boolean doEvaluate(double value);

    
    protected abstract String getSimbol();
    
    
    @Override
    public String toString() {
        return field + " " + getSimbol() + " " + refValue;
    }


    
    
}
