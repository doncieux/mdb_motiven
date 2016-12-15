package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.common.ann.ANN;
import es.udc.gii.common.ann.ANNFactory;
import es.udc.gii.common.ann.exceptions.IncompatibleNeuronWithLayerException;
import es.udc.gii.mdb.anji.ANJINEATMDBConfiguration;
import es.udc.gii.mdb.anji.ANJINEATModelApproach;
import es.udc.gii.mdb.ann.MLPMDBConfiguration;
import es.udc.gii.mdb.ann.MLPModelApproach;
import es.udc.gii.mdb.knowledge.representation.MockRepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import es.udc.gii.mdb.learning.LearningAlgorithm;
import es.udc.gii.mdb.learning.LearningAlgorithmMap;
import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mastropiero
 */
public class ModelFactory {

    Map<String, Integer> indexCounter;

    public enum ModelApproach {

        MLP, NEAT, MOCK
    }

    private static ModelFactory instance = null;

    private ModelFactory() {
        indexCounter = new HashMap<>();
    }

    public static ModelFactory getInstance() {
        if (instance == null) {
            instance = new ModelFactory();
        }
        return instance;
    }

    public Model createModelFromModel(Model model, String subId) {
        ModelApproach ap = model.getRepresentationApproach() instanceof MLPModelApproach ? ModelApproach.MLP : ModelApproach.NEAT;
        Configuration configuration = model instanceof ValueFunction ? ((ValueFunction) model).getCertainty().getConfiguration() : null;
        Model m = createModel(model.getID() + subId, model.getType(), ap, model.getInputsIDs(), model.getOutputsIDs(), model.getRepresentationApproach().getRepresentationConfiguration(), configuration);
        //TODO Create LearningAlgorithm        
        return m;
    }

    public Model createModel(String id, Model.ModelType modelType, ModelApproach modelApproach, List<String> inputs, List<String> outputs, RepresentationConfiguration representationConfiguration, Configuration componentsConfiguration) {

        //Pasar la clase en lugar de el tipo?
        //TODO Debe desaparecer el annPath2File en algún momento y crear las redes en base a su tamaño. Hay que modificar la librearía de redes
        //para añadir un constructor que lo permita.
        //Configuramos la aproximación escogida en función del tipo de modelo que queremos
        RepresentationApproach representationApproach = null;
        Model model = null;
        if (modelApproach == ModelApproach.MLP) {
            try {
                //TODO Cuando este la clase de configuracion en la libreria de redes, pasarle solo el configuration a un nuevo constructor
                MLPMDBConfiguration configuration = (MLPMDBConfiguration) representationConfiguration;
                ANN ann = ANNFactory.createANN(configuration.getTopology(), configuration.getInputNeuronType(), configuration.getHiddenNeuronType(), configuration.getOutputNeuronType(), configuration.getSynapsisHiddenType(), configuration.getSynapsisOutputType(), configuration.isDelayed());
                representationApproach = new MLPModelApproach(ann);
            } catch (IncompatibleNeuronWithLayerException ex) {
                Logger.getLogger(ModelFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (modelApproach == ModelApproach.NEAT) {
            ANJINEATMDBConfiguration configuration = (ANJINEATMDBConfiguration) representationConfiguration;
            representationApproach = new ANJINEATModelApproach(configuration.getProperties());
        } else if (modelApproach == ModelApproach.MOCK) {
            representationApproach = new MockRepresentationApproach();
      
        }

        Memory memory = null;
        memory = createMemory(modelType);

        switch (modelType) {
            case WORLD:
                model = new InputModel(id, inputs, outputs, modelType);
                break;
            case INTERNAL:
                model = new InputModel(id, inputs, outputs, modelType);
                break;
            case SATISFACTION:
                model = new SatisfactionModel(id, inputs, outputs, modelType);
                break;
            case VALUEFUNCTION:
                model = new ValueFunction(id, inputs, outputs, modelType, componentsConfiguration);
                break;
            default:
                break;
        }

        model.setModelApproach(representationApproach);
        model.setMem(memory);
        memory.setModel(model);

        model.createLogs(getLogConfigurationSubset(modelType));
        ModelMap.getInstance().addModel(id, model);
        return model;
    }

    public List<InputModel> createInputModels() {
        //TODO
        //Para cada salida de nuestro par acción percepción creamos un modelo
        List<InputModel> inputModels = new ArrayList<>();
        for (String externalTnId : ActionPerceptionPairMap.getInstance().getExternalTnIds()) {

            //ID
            String id = externalTnId + "Model";
            int index = 0;
            if (indexCounter.containsKey(externalTnId)) {
                index = indexCounter.get(externalTnId) + 1;
                id += (index);
            }
            indexCounter.put(externalTnId, index);

            //Type
            Model.ModelType type = Model.ModelType.WORLD;

            //Approach
            //TODO que sea configurable
            ModelApproach modelApproach = ModelApproach.MLP;
//TODO ANNConfiguration
            //   inputModels.add((InputModel) createModel(id, type, modelApproach, ActionPerceptionPairMap.getInstance().getExternalTIds(), Arrays.asList(externalTnId)));
        }

        return inputModels;
    }

    //TODO -> Pendiente de fachada de la STM
    public Memory createMemory(Model.ModelType modelType) {
        Memory memory = null;
        String modelPrefix = null;
        switch (modelType) {
            case VALUEFUNCTION:
                modelPrefix = "valuefunction";
                break;
            case INTERNAL:
                modelPrefix = "internalmodel";
                break;
            case WORLD:
                modelPrefix = "worldmodel";
                break;
            case SATISFACTION:
                modelPrefix = "satisfactionmodel";
                break;
            default:
                break;
        }
        
        //TODO startsWith == null -> EXCEPTION
        Configuration stmConfiguration = MDBConfiguration.getInstance().subset("shorttermmemory");
        if (stmConfiguration != null && !stmConfiguration.isEmpty()) {
            List<String> stmIds = stmConfiguration.getList("stmemory.id");
            for (int i = 0; i < stmIds.size(); i++) {
                try {
                    String id = stmIds.get(i);
                    if (id.toLowerCase().startsWith(modelPrefix)) {
                        Configuration stmSubset = stmConfiguration.subset("stmemory(" + i + ")");
                        memory = (Memory) Class.forName(stmSubset.getString("class")).newInstance();
                        memory.configure(stmSubset);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ModelFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ModelFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ModelFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return memory;
    }
  
    public Configuration getLogConfigurationSubset(Model.ModelType modelType) {
        Configuration config = null;
        String modelPrefix = "";

        switch (modelType) {
            case VALUEFUNCTION:
                modelPrefix = "valuefunction";
                break;
            case INTERNAL:
                modelPrefix = "internalmodel";
                break;
            case SATISFACTION:
                modelPrefix = "satisfactionmodel";
                break;
            case WORLD:
                modelPrefix = "worldmodel";
                break;
            default:
                //EXCEPTION
                break;
        }
        Configuration logsConfiguration = MDBConfiguration.getInstance().subset("logs");
        if (logsConfiguration != null && !logsConfiguration.isEmpty()) {
            List<String> logsIds = logsConfiguration.getList("logTool.id");
            for (int i = 0; i < logsIds.size(); i++) {
                String id = logsIds.get(i);
                if (id.toLowerCase().startsWith(modelPrefix)) {
                    config = logsConfiguration.subset("logTool(" + i + ")");
                }
            }
        }
        //EXCEPTION if config == null
        return config;
    }

    public LearningAlgorithm createLearningAlgorithmFromModel(String modelId, String subId) {

        //TODO Tags
        Configuration laconf = MDBConfiguration.getInstance().subset(ConfigUtilXML.LEARNING_ALGORITHMS_GROUP_TAG);
        List<String> modelIds = laconf.getList(ConfigUtilXML.LEARNING_ALGORITHM_TAG + "." + "modelId");
        LearningAlgorithm learningAlgorithm = null;
        for (int i = 0; i < modelIds.size(); i++) {
            if (modelIds.get(i).toLowerCase().equals(modelId.toLowerCase())) {

                try {
                    learningAlgorithm = (LearningAlgorithm) Class.forName(laconf.getString(ConfigUtilXML.LEARNING_ALGORITHM_TAG + "(" + i + ")" + "." + "class")).newInstance();
                    learningAlgorithm.configure(laconf.subset(ConfigUtilXML.LEARNING_ALGORITHM_TAG + "(" + i + ")"));

                    LearningAlgorithmMap.getInstance().addLearningAlgorithm(learningAlgorithm.getId() + subId, learningAlgorithm);
                    //FIXME Ñapa algo fea
                    learningAlgorithm.setId(learningAlgorithm.getId() + subId);
                    learningAlgorithm.setModelId(modelId + subId);
                } catch (MissingConfigurationParameterException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    //EXCEPTION
                    Logger.getLogger(LearningAlgorithmMap.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return learningAlgorithm;
    }
}
