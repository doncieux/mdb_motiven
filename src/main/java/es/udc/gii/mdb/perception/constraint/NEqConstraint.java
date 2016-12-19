/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.mdb.perception.constraint;

/**
 *
 * This class implements a "not equal" constraint.
 *
 * @author GII
 */
public class NEqConstraint extends DoubleConstraint {

    public NEqConstraint(String field, double refValue) {
        super(field, refValue);
    }

    @Override
    protected boolean doEvaluate(double value) {
        double comparison = Math.abs(value - getRefValue());
        return comparison > DoubleConstraint.THRESHOLD;
    }

    @Override
    protected String getSimbol() {
        return "!=";
    }

}
