/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.knowledge.declarative.model.Model.ModelType;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 * This class provides access to each model present on the architecture.
 *
 * @author GII
 */
public class ModelMap {

    private static ModelMap instance = null;
    private Map<String, Model> currentModels;

    private List<ValueFunction> valueFunctions;

    private ModelMap() {
        currentModels = new HashMap<>();
        valueFunctions = new ArrayList<>();
        Configuration worldModels = MDBConfiguration.getInstance().subset(ConfigUtilXML.WORLD_MODELS_GROUP_TAG);
        List<String> wmClasses = worldModels.getList(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);
        InputModel worldModel;
        for (int i = 0; i < wmClasses.size(); i++) {
            try {
                worldModel = new InputModel();
                worldModel.setType(ModelType.WORLD);
                worldModel.configure(worldModels.subset(ConfigUtilXML.MODEL_TAG + "(" + i + ")"));
                currentModels.put(worldModel.getID(), worldModel);

            } catch (MissingConfigurationParameterException ex) {
                //EXCEPTION
                Logger.getLogger(ModelMap.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        Configuration internalModels = MDBConfiguration.getInstance().subset(ConfigUtilXML.INTERNAL_MODELS_GROUP_TAG);
        List<String> imClasses = internalModels.getList(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);
        InputModel internalModel;
        for (int i = 0; i < imClasses.size(); i++) {
            try {
                internalModel = new InputModel();
                internalModel.setType(ModelType.INTERNAL);
                internalModel.configure(internalModels.subset(ConfigUtilXML.MODEL_TAG + "(" + i + ")"));
                currentModels.put(internalModel.getID(), internalModel);

            } catch (MissingConfigurationParameterException ex) {
                //EXCEPTION
                Logger.getLogger(ModelMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Configuration satisfactionModels = MDBConfiguration.getInstance().subset(ConfigUtilXML.SATISFACTION_MODELS_GROUP_TAG);
        List<String> smClasses = satisfactionModels.getList(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);
        SatisfactionModel satisfactionModel;
        for (int i = 0; i < smClasses.size(); i++) {
            try {
                satisfactionModel = new SatisfactionModel();
                satisfactionModel.setType(ModelType.SATISFACTION);
                satisfactionModel.configure(satisfactionModels.subset(ConfigUtilXML.MODEL_TAG + "(" + i + ")"));
                currentModels.put(satisfactionModel.getID(), satisfactionModel);

            } catch (MissingConfigurationParameterException ex) {
                //EXCEPTION
                Logger.getLogger(ModelMap.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        Configuration rwModels = MDBConfiguration.getInstance().subset(ConfigUtilXML.VALUE_FUNCTIONS_GROUP_TAG);
        List<String> rmClasses = rwModels.getList(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);
        ValueFunction valueFunction;
        for (int i = 0; i < rmClasses.size(); i++) {
            try {
                valueFunction = new ValueFunction();
                valueFunction.setType(ModelType.VALUEFUNCTION);
                valueFunction.configure(rwModels.subset(ConfigUtilXML.MODEL_TAG + "(" + i + ")"));
                currentModels.put(valueFunction.getID(), valueFunction);
                valueFunctions.add(valueFunction);
                //WHATIS

            } catch (MissingConfigurationParameterException ex) {
                //EXCEPTION
                Logger.getLogger(ModelMap.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Implemented as Singleton. This method provides the instance of the map.
     *
     * @return Map containing all the models.
     */
    public static ModelMap getInstance() {
        if (instance == null) {
            instance = new ModelMap();
        }
        return instance;
    }

    /**
     * Get a concrete model by its key (identifier)
     *
     * @param key Identifier of the model to be retrieved
     * @return The model found
     */
    public Model getModel(String key) {
        return currentModels.get(key);
    }

    /**
     * Get the type of a model by its identifier
     *
     * @param key Key of the model we are looking for
     * @return Type of the model
     */
    public ModelType getModelType(String key) {
        return getModel(key).getType();
    }

    public Collection<Model> getModels() {
        return currentModels.values();
    }

    public Model getModelByOutput(String ouputId) {
        for (Model m : currentModels.values()) {
            for (String modelOutputId : m.getOutputsIDs()) {
                if (modelOutputId.equals(ouputId)) {
                    return m;
                }
            }
        }
        return null;
    }

    public void addModel(String id, Model model) {
        if (currentModels.containsKey(id)) {
            //EXCEPTION
            throw new RuntimeException();
        } else {
            currentModels.put(id, model);
            if (model instanceof ValueFunction) {
                valueFunctions.add((ValueFunction) model);
            }
        }
    }

    public List<ValueFunction> getValueFunctions() {

        return valueFunctions;
    }
}
