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
import es.udc.gii.mdb.util.config.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * This class represents a stop criteria which tests if the execution cycle
 * reach a specific number of iterations established in the configuration file.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MaxIterationsStopCondition implements SimpleStopCondition {
    
    private static final String MAX_ITERATIONS_TAG = "maxIterations";
    
    /**
     * Maximum number of iterations.
     */
    private int maxIterations = 1000;
    
    public MaxIterationsStopCondition() {
    }
    
    public MaxIterationsStopCondition(int maxIterations) {
        this.maxIterations = maxIterations;
    }
    
    @Override
    public void configure(Configuration configuration) {
        if (configuration.containsKey(MaxIterationsStopCondition.MAX_ITERATIONS_TAG)) {
            maxIterations = configuration.getInt(MaxIterationsStopCondition.MAX_ITERATIONS_TAG);
        } else {
            ConfWarning w = new ConfWarning(
                    MaxIterationsStopCondition.MAX_ITERATIONS_TAG, maxIterations);
            w.warn();
        }
    }

    @Override
    public boolean evaluateCondition(MDBCore core) {
        
        return core.getIterations() >= maxIterations;
        
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.maxIterations;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MaxIterationsStopCondition other = (MaxIterationsStopCondition) obj;
        return this.maxIterations == other.maxIterations;
    }
    
    

}
