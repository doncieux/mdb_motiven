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
package es.udc.gii.mdb.ann;

import es.udc.gii.common.ann.ANN;
import es.udc.gii.common.ann.ANNFactory;
import es.udc.gii.common.ann.exceptions.ANNException;
import es.udc.gii.common.ann.exceptions.IncompatibleNeuronWithLayerException;
import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import es.udc.gii.mdb.util.exception.ModelException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class MLPModelApproach extends RepresentationApproach {

    private ANN ann;
    private MLPMDBConfiguration mLPMDBConfiguration;

    public MLPModelApproach() {
    }

    public MLPModelApproach(ANN ann) {
        this.ann = ann;
    }

    @Override
    public void configure(Configuration configuration) {
        try {

            if (configuration.containsKey(ConfigUtilXML.MODEL_APPROACH_CONFIG_FILE)) {
                String path2file = configuration.getString(ConfigUtilXML.MODEL_APPROACH_CONFIG_FILE);

                if (!path2file.startsWith(File.separator)) {
                    path2file = System.getProperty("user.dir") + File.separator + path2file;
                }
                ann = new ANN(path2file);
                //TODO MLPMDBConfiguration

            } else {
                int layersNum = configuration.getInt(ConfigUtilXML.MODEL_APPROACH_LAYERS + "." + ConfigUtilXML.MODEL_APPROACH_LAYERS_NUM);
                int[] topology = new int[layersNum];

                List<String> layersString = configuration.getList(ConfigUtilXML.MODEL_APPROACH_LAYERS + "." + ConfigUtilXML.MODEL_APPROACH_LAYERS_LAYER);
                for (int i = 0; i < layersNum; i++) {
                    topology[i] = Integer.valueOf(layersString.get(i));
                }
                String inputNeuronType = configuration.getString(ConfigUtilXML.MODEL_APPROACH_INPUTNEURONTYPE);
                String hiddenNeuronType = configuration.getString(ConfigUtilXML.MODEL_APPROACH_HIDDENNEURONTYPE);
                String outputNeuronType = configuration.getString(ConfigUtilXML.MODEL_APPROACH_OUTPUTNEURONTYPE);
                String synapsisHiddenType = configuration.getString(ConfigUtilXML.MODEL_APPROACH_SYNAPSISHIDDENTYPE);
                String synapsisOutputType = configuration.getString(ConfigUtilXML.MODEL_APPROACH_SYNAPSISOUTPUTTYPE);
                boolean delayed = configuration.getBoolean(ConfigUtilXML.MODEL_APPROACH_DELAYED);

                ann = ANNFactory.createANN(topology, inputNeuronType, hiddenNeuronType, outputNeuronType, synapsisHiddenType, synapsisOutputType, delayed);
                mLPMDBConfiguration = new MLPMDBConfiguration(topology, inputNeuronType, hiddenNeuronType, outputNeuronType, synapsisHiddenType, synapsisOutputType, delayed);
            }
        } catch (ANNException ex) {
            Logger.getLogger(MLPModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncompatibleNeuronWithLayerException ex) {
            Logger.getLogger(MLPModelApproach.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        try {
            ann.decode(parameters);
        } catch (ANNException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {
        try {
            return ann.calculateOutputs(inputs);
        } catch (ANNException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public void save(String fileName) throws ModelException {
        try {
            ann.save(fileName);
        } catch (ANNException ex) {
            throw new ModelException(ex);
        }
    }

    @Override
    public int calculateNumberOfParameters() {
        return ann.calculateNumberOfParameters();
    }

    @Override
    public RepresentationConfiguration getRepresentationConfiguration() {
        return mLPMDBConfiguration;
    }

}
