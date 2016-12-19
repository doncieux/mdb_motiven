/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.perception.constraint;

/**
 *
 * This class implements a "greater than" constraint.
 *
 * @author GII
 */
public class GTConstraint extends DoubleConstraint {

    public GTConstraint(String field, double refValue) {
        super(field, refValue);
    }

    @Override
    protected boolean doEvaluate(double value) {
        return (value - getRefValue()) > DoubleConstraint.THRESHOLD;
    }

    @Override
    protected String getSimbol() {
        return ">";
    }

}
