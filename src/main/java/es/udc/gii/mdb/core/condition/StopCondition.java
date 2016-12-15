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

package es.udc.gii.mdb.core.condition;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.util.config.Configurable;

/**
 * This interface represents a stop condition. The represented condition by this test 
 * is the one that the core cycle has to reach to finish its execution.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface StopCondition extends Configurable {

    /**
     * This methods test if a specific stop condition is satisfied or not.
     * @param core the method receives the core execution cycle to access to its
     * parameters.
     * @return true or false depending on the fullfilment of the specific stop condition.
     */
    public boolean evaluateCondition(MDBCore core);

}
