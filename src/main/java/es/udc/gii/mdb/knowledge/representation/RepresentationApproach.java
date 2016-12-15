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
package es.udc.gii.mdb.knowledge.representation;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This interface represents an specific implementation of a model. That is, the approximation used to compute the outputs, an ANN, rules, etc.
 *
 * Each specific approaximation has its own methods to calculate the outputs. The classes that implement this interface should implement the method to calculate
 * them an also methods to create and configure the specific method.
 *
 * @author pilar
 */
public abstract class RepresentationApproach implements Configurable, Cloneable {

    private List modelParameters;

    /**
     * Method that decodes a list of parameters into a specific model.
     *
     * @param parameters Iterator over a collection of genes
     * @throws ModelException
     */
    public void decode(Iterator parameters) throws ModelException {
        if (this.modelParameters == null) {
            this.modelParameters = new ArrayList();
        } else {
            this.modelParameters.clear();
        }
        
        while (parameters.hasNext()) {
            this.modelParameters.add(parameters.next());
        }
        doDecode(this.modelParameters.iterator());

    }

    public abstract void doDecode(Iterator parameters) throws ModelException;

    /**
     * This methods calculates the outputs using the specific model and the inputs.
     *
     * @param inputs double values representing the inputs to the model.
     * @return an array of double values which represents the outputs of the model.
     * @throws ModelException
     */
    public abstract double[] calculateOutputs(double[] inputs) throws ModelException;

    /**
     * Saves the current state of a model into a file
     *
     * @param fileName Path where the file will be stored
     * @throws ModelException
     */
    public abstract void save(String fileName) throws ModelException;


    //TODO -> Se usa en evolución, se puede cambiar a "calculateNumberOfParameters"
    //para que sea más genérico.
    public abstract int calculateNumberOfParameters();

    /**
     * This method returns the paramters of the specific model approach.
     *
     * @return a list with the parameters of the model approach
     */
    public List getParameters() {
        return this.modelParameters;
    }
    
    
    public abstract RepresentationConfiguration getRepresentationConfiguration();
    

}
