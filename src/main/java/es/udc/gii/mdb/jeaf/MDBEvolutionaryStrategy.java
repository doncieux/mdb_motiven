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
package es.udc.gii.mdb.jeaf;

import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;

/**
 *
 * This class overrides the {@link EvolutionaryStrategy#init} method, to avoid
 * the reset of the population each time the class is called to resolve the problem.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MDBEvolutionaryStrategy extends EvolutionaryStrategy {



    @Override
    protected void init() {
     /**
     *
     * Empty method that overrides the original one to avoid the initialization of
     * the population each time the strategy is called.
     *
     */ 
    }

}
