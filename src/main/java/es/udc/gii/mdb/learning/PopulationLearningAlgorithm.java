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
package es.udc.gii.mdb.learning;

import es.udc.gii.mdb.learning.memory.LearningMemory;
import es.udc.gii.mdb.util.config.ConfWarning;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public abstract class PopulationLearningAlgorithm extends LearningAlgorithm {
    
    private static final String POPULATION_FACTOR_TAG = "populationfactor";
    
    private double populationFactor = 1.0;

    public PopulationLearningAlgorithm() {
        super();
    }

    public PopulationLearningAlgorithm(String id, String modelId, int samples, 
            int runsBeforeLearning, String path2config, 
            double populationFactor, LearningMemory learningMemory) {
        super(id, modelId, samples, runsBeforeLearning, path2config, learningMemory);
        this.populationFactor = populationFactor;
    }
    
    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        super.configure(configuration); 
        
        if (configuration.containsKey(POPULATION_FACTOR_TAG)) {
            populationFactor = configuration.getDouble(POPULATION_FACTOR_TAG);
        } else {
            ConfWarning w = new ConfWarning(POPULATION_FACTOR_TAG, populationFactor);
            w.warn();
        }
    }
    
    public double getPopulationFactor() {
        return populationFactor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + super.hashCode();
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.populationFactor) ^ (Double.doubleToLongBits(this.populationFactor) >>> 32));
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
        final PopulationLearningAlgorithm other = (PopulationLearningAlgorithm) obj;
        if (Double.doubleToLongBits(this.populationFactor) != Double.doubleToLongBits(other.populationFactor)) {
            return false;
        }
        return super.equals(obj);
    }
    
    
    
}
