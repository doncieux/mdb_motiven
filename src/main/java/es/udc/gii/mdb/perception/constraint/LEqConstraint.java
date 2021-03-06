/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.perception.constraint;

/**
 *
 * This class implements a "less equal than" constraint.
 *
 * @author GII
 */
public class LEqConstraint extends DoubleConstraint {

    public LEqConstraint(String field, double refValue) {
        super(field, refValue);
    }

    @Override
    protected boolean doEvaluate(double value) {
        return (getRefValue() - value) >= DoubleConstraint.THRESHOLD;
    }

    @Override
    protected String getSimbol() {
        return "<=";
    }



}
