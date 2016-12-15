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

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.learning.memory.LearningMemory;
import es.udc.gii.mdb.learning.memory.LearningMemoryElement;
import es.udc.gii.mdb.util.config.ConfWarning;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public abstract class LearningAlgorithm implements Configurable {

    private static final String LEARNING_ID_TAG = "id";

    private static final String MODEL_ID_TAG = "modelId";

    private static final String SAMPLES_TAG = "samples";

    private static final String CONFIG_TAG = "config";

    private static final String LEARNING_MEMORY_TAG = "learningmemory";

    private static final String RUNS_BEFORE_LEARNING_TAG = "runsBeforeLearning";

    private String id;

    private String modelId;

    private int samples = 1;

    private int runsBeforeLearning = 0;

    private String path2config;

    private LearningMemory learningMemory;

    private int iteration = 0;

    private Iterator currentParameters;

    //TODO -> Tienen que desaparecer
    private double currentModelFitness;

    //TODO -> Tienen que desaparecer
    private double currentPopulationFitness;

    //TODO -> tiene que desaparecer, está para el log de R2:
    private int modelEqual;

    private int contInsert = 0;

    public LearningAlgorithm() {
    }

    public LearningAlgorithm(String id,
            String modelId,
            int samples,
            int runsBeforeLearning,
            String path2config,
            LearningMemory learningMemory) {
        this.id = id;
        this.modelId = modelId;
        this.samples = samples;
        this.runsBeforeLearning = runsBeforeLearning;
        this.path2config = path2config;
        this.learningMemory = learningMemory;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {

        if (configuration.containsKey(LEARNING_ID_TAG)) {
            this.id = configuration.getString(LEARNING_ID_TAG);
        } else {
            throw new MissingConfigurationParameterException(LEARNING_ID_TAG);
        }

        if (configuration.containsKey(MODEL_ID_TAG)) {
            modelId = configuration.getString(MODEL_ID_TAG);
        } else {
            throw new MissingConfigurationParameterException(MODEL_ID_TAG);
        }

        if (configuration.containsKey(SAMPLES_TAG)) {
            samples = configuration.getInt(SAMPLES_TAG);
        } else {
            ConfWarning w = new ConfWarning(SAMPLES_TAG, samples);
            w.warn();
        }

        if (configuration.containsKey(CONFIG_TAG)) {
            path2config = configuration.getString(CONFIG_TAG);
            path2config = (path2config.startsWith(File.separator)
                    ? path2config : System.getProperty("user.dir") + File.separator + path2config);
        } else {
            throw new MissingConfigurationParameterException(CONFIG_TAG);
        }

        if (configuration.subset(LEARNING_MEMORY_TAG) != null &&
                !configuration.subset(LEARNING_MEMORY_TAG).isEmpty()) {
            learningMemory = new LearningMemory();
            learningMemory.configure(configuration.subset(LEARNING_MEMORY_TAG));

        }

        if (configuration.containsKey(RUNS_BEFORE_LEARNING_TAG)) {
            runsBeforeLearning = configuration.getInt(RUNS_BEFORE_LEARNING_TAG);
        } else {
            ConfWarning w = new ConfWarning(RUNS_BEFORE_LEARNING_TAG, runsBeforeLearning);
            w.warn();
        }

    }

    //Se añade que devuelva bool para saber si entrenó o no, ya que en algunas situaciones no lo haría y se actualizaria el modelo
    //De todas formas, esto debería aprender siempre y lo otro controlarse desde fuera
    public boolean learn() {

        if (MDBCore.getInstance().getIterations() % this.samples == 0
                && MDBCore.getInstance().getIterations() > this.runsBeforeLearning) {
            try {

                //Metodo que implementa cada algoritmo de aprendizaje concreto:
                doLearn();
                return true;
            } catch (Exception ex) {
                //EXCEPTION -> LearningException
                return false;
            }
        } else {
            return false;
        }

    }

    public abstract void doLearn();

    public void updateLearningMemory() {
        List<LearningMemoryElement> elements;

        if (this.learningMemory != null) {
            elements = createMemoryElements();
            this.learningMemory.updateContent(elements);
        }
    }

    //TODO -> revisar
    public void updateModel(Iterator parameters, double utility, double meanUtility) {

        this.currentParameters = parameters;
        this.currentModelFitness = utility;
        this.currentPopulationFitness = meanUtility;

    }

    public abstract List<LearningMemoryElement> createMemoryElements();

    protected double[] toDoubleArray(List l) {

        double[] doubleArray = new double[l.size()];

        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = (double) l.get(i);
        }

        return doubleArray;

    }

    //NHAPA -> Cambiar para que de soporte tanto a Model como a Behavior
    public Model getModel() {

        return ModelMap.getInstance().getModel(modelId);
    }

    public String getId() {
        return this.id;
    }

    public String getModelId() {
        return this.modelId;
    }

    public String getPath2Config() {
        return this.path2config;
    }

    public int getSamples() {
        return samples;
    }

    public int getRunsBeforeLearning() {
        return runsBeforeLearning;
    }

    public LearningMemory getLearningMemory() {
        return learningMemory;
    }

    public int getIteration() {
        return iteration;
    }

    public void increaseIteration() {
        iteration++;
    }

    public Iterator getCurrentParameters() {
        return currentParameters;
    }

    public double getCurrentModelFitness() {
        return currentModelFitness;
    }

    public void setCurrentModelFitness(double currentModelFitness) {
        this.currentModelFitness = currentModelFitness;
    }

    public double getCurrentPopulationFitness() {
        return currentPopulationFitness;
    }

    public void setCurrentPopulationFitness(double currentPopulationFitness) {
        this.currentPopulationFitness = currentPopulationFitness;
    }

    public int getModelEqual() {
        return modelEqual;
    }

    public void setModelEqual(int modelEqual) {
        this.modelEqual = modelEqual;
    }

    public void increaseModelEqual() {
        this.modelEqual++;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.modelId);
        hash = 29 * hash + this.samples;
        hash = 29 * hash + this.runsBeforeLearning;
        hash = 29 * hash + Objects.hashCode(this.learningMemory);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        boolean equal = Boolean.TRUE;

        if (obj == null) {
            equal = Boolean.FALSE;
        }
        if (getClass() != obj.getClass()) {
            equal = Boolean.FALSE;
        }
        final LearningAlgorithm other = (LearningAlgorithm) obj;
        if (!Objects.equals(this.modelId, other.modelId)) {
            equal = Boolean.FALSE;
        }
        if (this.samples != other.samples
                || this.runsBeforeLearning != other.runsBeforeLearning) {
            equal = Boolean.FALSE;
        }

        return equal && Objects.equals(this.learningMemory, other.learningMemory);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    
    
    

}
