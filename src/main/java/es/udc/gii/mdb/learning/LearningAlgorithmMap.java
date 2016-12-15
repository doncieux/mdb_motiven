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

import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class LearningAlgorithmMap {

    private static LearningAlgorithmMap instance = null;

    private Map<String, LearningAlgorithm> learningAlgorithms;

    private LearningAlgorithmMap() {
        learningAlgorithms = new HashMap<>();

        Configuration las = MDBConfiguration.getInstance().subset(ConfigUtilXML.LEARNING_ALGORITHMS_GROUP_TAG);
        List<String> laClasses = las.getList(ConfigUtilXML.LEARNING_ALGORITHM_TAG + "." + ConfigUtilXML.LEARNING_ALGORITHM_CLASS_TAG);
        LearningAlgorithm learningAlgorithm;
        for (int i = 0; i < laClasses.size(); i++) {
            try {
                learningAlgorithm = (LearningAlgorithm) Class.forName(laClasses.get(i)).newInstance();                
                learningAlgorithm.configure(las.subset(ConfigUtilXML.LEARNING_ALGORITHM_TAG + "(" + i + ")"));
                learningAlgorithms.put(learningAlgorithm.getId(), learningAlgorithm);
                

            } catch (MissingConfigurationParameterException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                //EXCEPTION
                Logger.getLogger(LearningAlgorithmMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public static LearningAlgorithmMap getInstance() {
        if (instance == null) {
            instance = new LearningAlgorithmMap();
        }
        return instance;
    }
    
    public LearningAlgorithm getByModelId(String modelId) {
    
        for (LearningAlgorithm la : learningAlgorithms.values()) {
            if (la.getModelId().equalsIgnoreCase(modelId)) {
                return la;
            }
        }
        
        return null;
    }
    
    public void addLearningAlgorithm(String id, LearningAlgorithm learningAlgorithm) {
        if (learningAlgorithms.containsKey(id)) {
            //EXCEPTION
            throw new RuntimeException();
        } else {
            learningAlgorithms.put(id, learningAlgorithm);
        }
    }

}
