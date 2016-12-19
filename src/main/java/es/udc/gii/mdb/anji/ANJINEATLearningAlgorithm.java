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
package es.udc.gii.mdb.anji;

import com.anji.util.Properties;
import es.udc.gii.mdb.learning.PopulationLearningAlgorithm;
import es.udc.gii.mdb.learning.memory.LearningMemoryElement;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.slf4j.LoggerFactory;

/**
 *
 * @author GII
 */
public class ANJINEATLearningAlgorithm extends PopulationLearningAlgorithm {

    private ANJINEATEvolver evolver;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ANJINEATLearningAlgorithm.class);

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        try {
            super.configure(configuration); //To change body of generated methods, choose Tools | Templates.

            evolver = new ANJINEATEvolver();
            Properties props = new Properties(this.getPath2Config());
            evolver.init(props);

            //TODO -> ¿PopulationFactor?
        } catch (IOException ex) {
            //EXCEPTION -> Configuration Exception pero que no sea missing:
            Logger.getLogger(ANJINEATLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            //EXCEPTION -> Configuration Exception pero que no sea missing:
            Logger.getLogger(ANJINEATLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doLearn() {
        try {
            //Cojo último mejor:
            Chromosome lastBest = evolver.getChamp();

            evolver.run();

            this.increaseIteration();
            Chromosome bestIndividual = evolver.getChamp();
            //Actualiza la población:
            updateLearningMemory();
            List bestList = new ArrayList(bestIndividual.getAlleles());
            //TODO -> las calidades en el neat son para maximizar, hay que tener cuidado con esto
            updateModel(bestList.iterator(),
                    1.0 - bestIndividual.getFitnessValue()/1E9,
                    meanFitnessValue());

            if (lastBest != null && lastBest.equals(evolver.getChamp())) {
                this.setModelEqual(this.getModelEqual() + 1);
            } else {
                this.setModelEqual(0);
            }
        } catch (Exception ex) {
            //EXCEPTION -> LearningException
            Logger.getLogger(ANJINEATLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double meanFitnessValue() {

        double meanFitness = 0.0;
        Chromosome c;
        List population = evolver.getGenotype().getChromosomes();

        for (Object population1 : population) {
            c = (Chromosome) population1;
            meanFitness += (1.0 - c.getFitnessValue()/1E9);
        }

        meanFitness /= population.size();

        return meanFitness;

    }

    @Override
    public List<LearningMemoryElement> createMemoryElements() {
        List<LearningMemoryElement> elements = new ArrayList<>();
        LearningMemoryElement element;
        Genotype genotype = evolver.getGenotype();
        
        Chromosome c;
        for (Object o : genotype.getChromosomes()) {
            c = (Chromosome)o;
            element = new LearningMemoryElement();
            element.setParameters(toDoubleArray(new ArrayList(c.getAlleles())));
            //TODO -> de nuevo hay que tener cuidado con el valor de calidad porque este maximiza:
            element.setUtility(1.0 - c.getFitnessValue()/1E9);

            elements.add(element);
        }


        return elements;
    }

}
