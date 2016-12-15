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

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;
import com.anji.neat.Evolver;
import com.anji.neat.NeatConfiguration;
import com.anji.util.Properties;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.util.exception.ModelException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.jgap.Allele;
import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;
import org.jgap.ChromosomeMaterial;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author pilar
 */
public class ANJINEATModelApproach extends RepresentationApproach {

    private Activator neatActivator;
    private ActivatorTranscriber activatorFactory;
    private ChromosomeMaterial chromosomeMaterial;
    private Properties properties;

    public ANJINEATModelApproach() {
    }

    public ANJINEATModelApproach(Properties properties) {
        this.properties = properties;
        configure(properties);
    }

    
    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        try {
            //El iterador de genes viene el evolutivo entonces podemos suponer que es correcto.
            //Hay que crear un chrom material y crear el activator a partir de ese chrom materia:
            Collection material = new ArrayList();
            while (parameters.hasNext()) {
                material.add(parameters.next());
            }

            chromosomeMaterial = new ChromosomeMaterial(material);
            neatActivator = activatorFactory.newActivator(new Chromosome(chromosomeMaterial, Long.MIN_VALUE));
        } catch (TranscriberException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {
        return neatActivator.next(inputs);
    }

    @Override
    public void save(String fileName) throws ModelException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            PrintStream printStream = new PrintStream(out);
            printStream.println(this.neatActivator.toXml());
        } catch (IOException ex) {
            //EXCEPTION
            Logger.getLogger(ANJINEATModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                //EXCEPTION - ConfigurationException
                Logger.getLogger(ANJINEATModelApproach.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void configure(Properties properties) {
        try {
                        //Creo el fichero de configuración de NEAT:

            NeatConfiguration config = new NeatConfiguration(properties);
            //Creo el ActivatorFactory para crear después el Activator:
            activatorFactory = (ActivatorTranscriber) properties.singletonObjectProperty(ActivatorTranscriber.class);

            //WHATIS - Es realmente necesario establecer la función objetivo?
            BulkFitnessFunction fitnessFunc = (BulkFitnessFunction) properties.singletonObjectProperty(Evolver.FITNESS_FUNCTION_CLASS_KEY);
            config.setBulkFitnessFunction(fitnessFunc);

            //Creo un ChomosomeMaterial inizial aleatorio:
            ChromosomeMaterial chromMat = ChromosomeMaterial.randomInitialChromosomeMaterial(config);

            Iterator iter = chromMat.getAlleles().iterator();
            while (iter.hasNext()) {
                Allele newAllele = (Allele) iter.next();

                // Set the gene's value (allele) to a random value.
                // ------------------------------------------------
                newAllele.setToRandomValue(config.getRandomGenerator());
            }
            Chromosome chrom = new Chromosome(chromMat, Long.MIN_VALUE);

            //Creo el neat_activator que representa la red:
            neatActivator = activatorFactory.newActivator(chrom);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(ANJINEATModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TranscriberException ex) {
            Logger.getLogger(ANJINEATModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @Override
    public void configure(Configuration configuration) {
        try {
            //TODO Revisar 
           String path2file = configuration.getString(ConfigUtilXML.MODEL_APPROACH_CONFIG_FILE);

            if (!path2file.startsWith(File.separator)) {
                path2file = System.getProperty("user.dir") + File.separator + path2file;
            }

            properties = new Properties(path2file);
            
            configure(properties);

        } catch (IOException ex) {
            //EXCEPTION - ConfigurationException
            Logger.getLogger(ANJINEATModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int calculateNumberOfParameters() {
        return -Integer.MAX_VALUE;
    }

    @Override
    public RepresentationConfiguration getRepresentationConfiguration() {
        return new ANJINEATMDBConfiguration(properties);
    }


}
