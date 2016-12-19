/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.perception.constraint;

/**
 *
 * This class implements a "greater equal than" constraint.
 *
 * @author GII
 */
public class GEqConstraint extends DoubleConstraint {

    public GEqConstraint(String field, double refValue) {
        super(field, refValue);
    }

    @Override
    protected boolean doEvaluate(double value) {
        return (value - getRefValue()) >= DoubleConstraint.THRESHOLD;
    }

    @Override
    protected String getSimbol() {
        return ">=";
    }

}
