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

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.fitness.FitnessUtil;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.learning.memory.LearningMemory;
import es.udc.gii.mdb.learning.PopulationLearningAlgorithm;
import es.udc.gii.mdb.learning.memory.LearningMemoryElement;
import es.udc.gii.mdb.util.ArrayIterator;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pilar
 */
public class JEAFLearningAlgorithm extends PopulationLearningAlgorithm {

    private EvolutionaryAlgorithm algorithm;

    private StopTest stopTest;

    private EAFFacade facade;

    private static final Logger LOGGER = LoggerFactory.getLogger(JEAFLearningAlgorithm.class);

    public JEAFLearningAlgorithm() {
        super();
    }

    public JEAFLearningAlgorithm(String id, String modelId, int samples,
            int runsBeforeLearning, String path2config,
            double populationFactor, LearningMemory learningMemory) {
        super(id, modelId, samples, runsBeforeLearning, path2config, populationFactor, learningMemory);
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        super.configure(configuration); //To change body of generated methods, choose Tools | Templates.

        //Se crea el algoritmo evolutivo a partir de la configuración indicada:
        facade = new EAFFacade();
        algorithm = facade.createAlgorithm(this.getPath2Config());
        stopTest = facade.createStopTest(this.getPath2Config());
        EAFRandom.init();
        //TODO -> Esto no debería ir en el configure
        if (this.getModel() != null) {
            //Establecemos el tamaño del cromosoma:
            int[] dimension = new int[]{this.getModel().getRepresentationApproach().calculateNumberOfParameters()};
            algorithm.getPopulation().setDimension(dimension);
            //Establecemos el tamaño de la población:
            algorithm.setPopulationSize((int) (this.getPopulationFactor() * dimension[0]));

            //Si tenemos una memoria de aprendizaje con contenido cargamos la población del evolutivo:
            loadPopulationFromMemory();
        }
    }

    public void loadPopulationFromMemory() {

        int populationSize, memoryCapacity, elementsToLoad;
        List<LearningMemoryElement> memoryContent;
        List<Individual> individuals;
        Individual individual;

        if (this.getLearningMemory() != null
                && this.getLearningMemory().getContent() != null
                && this.getLearningMemory().getContent().size() > 0) {
            memoryContent = this.getLearningMemory().getContent();

            populationSize = algorithm.getPopulation().getSize();
            memoryCapacity = this.getLearningMemory().getCapacity();
            //Si el tamaño de la memoria es mayor que la población del evolutivo
            //sólo cargamos hasta que se llene la población del evolutivo.
            elementsToLoad = Math.min(populationSize, memoryCapacity);
            elementsToLoad = (elementsToLoad < 0 ? populationSize : elementsToLoad);

            individuals = new ArrayList<>();
            for (int i = 0; i < elementsToLoad; i++) {
                individual = (Individual) algorithm.getPopulation().getIndividual(0).clone();
                individual.setChromosomeAt(0, memoryContent.get(i).getParameters());
                individual.setFitness(memoryContent.get(i).getUtility());

                individuals.add(individual);
            }

            //Se reemplazan en la población del evolutivo desde el primer elemento:
            algorithm.getPopulation().replaceIndividuals(individuals, 0);

        }

    }

    @Override
    public void doLearn() {

        //Se coge el último mejor:
        Individual lastBest = algorithm.getBestIndividual();
        facade.resolve(stopTest, algorithm);

        this.increaseIteration();

        //Actualizamos el modelo:
        Individual bestIndividual = algorithm.getBestIndividual();
        //Actualiza la población:
        updateLearningMemory();
        
        //Actualiza el modelo y las calidades -> antiguo updateModel() de JEAFEvolution
        updateModel(new ArrayIterator(
                ArrayUtils.toObject(bestIndividual.getChromosomeAt(0))), 
                bestIndividual.getFitness(), 
                FitnessUtil.meanFitnessValue(algorithm.getPopulation().getIndividuals()));

        //TODO TODELETE - this code is needed for R2LogTool:
        if (lastBest != null && lastBest.equals(bestIndividual)) {
            LOGGER.info("Individual not changed in iteration " + MDBCore.getInstance().getIterations());
            this.increaseModelEqual();
        } else {
            this.setModelEqual(0);
        }

    }

    @Override
    public List<LearningMemoryElement> createMemoryElements() {
        List<LearningMemoryElement> elements = new ArrayList<>();
        LearningMemoryElement element;

        for (Individual ind : algorithm.getPopulation().getIndividuals()) {
            element = new LearningMemoryElement();
            element.setParameters(ind.getChromosomeAt(0));
            element.setUtility(ind.getFitness());

            elements.add(element);
        }

        return elements;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash * 79 + super.hashCode();
        return hash;
    }

}
