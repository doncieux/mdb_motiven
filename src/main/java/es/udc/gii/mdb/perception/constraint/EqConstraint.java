/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.perception.constraint;

/**
 *
 * This class implements an equality constraint.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class EqConstraint extends DoubleConstraint {

    public EqConstraint(String field, double refValue) {
        super(field, refValue);
    }

    
    @Override
    protected boolean doEvaluate(double value) {
        return Math.abs(value - getRefValue()) <= DoubleConstraint.THRESHOLD;
    }

    @Override
    protected String getSimbol() {
        return "=";
    }

    
    
    
}
