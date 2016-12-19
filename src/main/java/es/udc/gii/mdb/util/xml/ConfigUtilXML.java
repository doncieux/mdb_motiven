/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.xml;

/**
 * This class stores the names of the tags used in configuration files, in order
 * to provide an unique access point to them to easily modify.
 *
 * @author GII
 */
public class ConfigUtilXML {

    public static final String LEARNING_ALGORITHM_TAG = "learning";

    public static final String MDBCORE_TAG = "core";
    public static final String MDBCORE_ITERATIONS_TAG = "iterations";
    public static final String MDBCORE_STOP_CONDITION_TAG = "stopCondition";
    public static final String MDBCORE_STOP_CONDITION_CLASS_TAG = "stopCondition.class";
    public static final String MDBCORE_STRATEGY_CHOOSER_TAG = "strategyChooser";
    public static final String MDBCORE_STRATEGY_RANDOM_ACTUATOR = "actuator";
    public static final String BEHAVIOUR_CHOOSER_DELAY_TAG = "delay";
    public static final String BEHAVIOUR_CHOOSER_DELAY_ITERATION_TAG = "iterations";
    public static final String BEHAVIOUR_CHOOSER_DELAY_CLASS_TAG = "class";
    public static final String MDBCORE_TASK_ACCOMPLISHED_SENSOR_TAG = "accomplished";
    public static final String MDBCORE_ABORT_SENSOR_TAG = "abortSensor";
    public static final String MDBCORE_FILE_PATH_TAG = "path";
    public static final String MDBCORE_SAVE_PATH_TAG = "savePath";
    public static final String MDBCORE_IP_TAG = "ip";
    public static final String ACTION_PERCEPTION_PAIR_TAG = "actionPerceptionPair";
    public static final String ACTION_PERCEPTION_PAIR_PERCEPTION_T_TAG = "perception_t";
    public static final String ACTION_PERCEPTION_PAIR_PERCEPTION_T_N_TAG = "perception_t_n";
    public static final String ACTION_PERCEPTION_PAIR_INTERNAL_TAG = "internal";
    public static final String ACTION_PERCEPTION_PAIR_EXTERNAL_TAG = "external";
    public static final String ACTION_PERCEPTION_PAIR_STRATEGY_TAG = "strategy";
    public static final String ACTION_PERCEPTION_PAIR_STRATEGY_ACTION_TAG = "action";
    public static final String ACTION_PERCEPTION_PAIR_STRATEGY_LENGTH_TAG = "length";
    public static final String ACTION_PERCEPTION_PAIR_FIELD_TAG = "field";
    public static final String ACTION_PERCEPTION_PAIR_ID_TAG = "id";
    public static final String ACTION_PERCEPTION_PAIR_TYPE_TAG = "type";
    public static final String ACTION_PERCEPTION_PAIR_SATISFACTION_T_N_TAG = "satisfaction_t_n";
    public static final String WORLD_MODELS_GROUP_TAG = "worldModels";
    public static final String INTERNAL_MODELS_GROUP_TAG = "internalModels";
    public static final String SATISFACTION_MODELS_GROUP_TAG = "satisfactionModels";

    public static final String BEHAVIOUR_MODELS_GROUP_TAG = "behaviourModels";
    public static final String VALUE_FUNCTIONS_GROUP_TAG = "valueFunctions";
    public static final String MODEL_TAG = "model";
    public static final String MODEL_APPROACH = "approach";
    public static final String MODEL_APPROACH_CONFIG_FILE = "configFile";
    public static final String MODEL_APPROACH_LAYERS = "layers";
    public static final String MODEL_APPROACH_LAYERS_NUM = "number";
    public static final String MODEL_APPROACH_LAYERS_LAYER = "layer";

    public static final String MODEL_APPROACH_INPUTNEURONTYPE = "inputNeuronType";
    public static final String MODEL_APPROACH_HIDDENNEURONTYPE = "hiddenNeuronType";
    public static final String MODEL_APPROACH_OUTPUTNEURONTYPE = "outputNeuronType";
    public static final String MODEL_APPROACH_SYNAPSISHIDDENTYPE = "synapsisHiddenType";
    public static final String MODEL_APPROACH_SYNAPSISOUTPUTTYPE = "synapsisOutputType";
    public static final String MODEL_APPROACH_DELAYED = "delayed";

    public static final String MODEL_CLASS_TAG = "class";
    public static final String MODEL_INPUTS_TAG = "inputs";
    public static final String MODEL_OUTPUTS_TAG = "outputs";
    public static final String MODEL_ID_TAG = "id";
    public static final String MODEL_CONSTRAINTS_GROUP_TAG = "constraints";
    public static final String MODEL_CONSTRAINT_TAG = "constraint";
    public static final String MODEL_CONSTRAINT_FIELD_TAG = "field";
    public static final String MODEL_CONSTRAINT_CONDITION_TAG = "cond";
    public static final String MODEL_CONSTRAINT_VALUE_TAG = "value";
    public static final String MODEL_EVOLUTION_TAG = "evolution";
    public static final String MODEL_EVOLUTION_CLASS_TAG = "evolution.class";
    public static final String MODEL_EVOLUTION_CONFIG_FILE_TAG = "config";
    public static final String MODEL_EVOLUTION_SAMPLES_TAG = "samples";
    public static final String MODEL_EVOLUTION_POPULATION_FACTOR = "populationfactor";
    public static final String MEMORY_TAG = "memory";
    public static final String MEMORY_CLASS_TAG = "class";
    public static final String MEMORY_SIZE_TAG = "size";
    public static final String SENSORS_GROUP_TAG = "sensors";
    public static final String SENSOR_TAG = "sensor";
    public static final String SENSOR_ID_TAG = "id";
    public static final String SENSOR_PORT_TAG = "port";
    public static final String SENSOR_MINVAL_TAG = "minVal";
    public static final String SENSOR_MAXVAL_TAG = "maxVal";
    public static final String SENSOR_NORMMINVAL_TAG = "normMinVal";
    public static final String SENSOR_NORMMAXVAL_TAG = "normMaxVal";
    public static final String ACTUATORS_GROUP_TAG = "actuators";
    public static final String ACTUATOR_TAG = "actuator";
    public static final String ACTUATOR_PORT_TAG = "port";
    public static final String ACTUATOR_ID_TAG = "id";
    public static final String ACTUATOR_MINVAL_TAG = "minVal";
    public static final String ACTUATOR_MAXVAL_TAG = "maxVal";
    public static final String ACTUATOR_STEP_TAG = "step";
    public static final String ACTUATOR_NORMMINVAL_TAG = "normMinVal";
    public static final String ACTUATOR_NORMMAXVAL_TAG = "normMaxVal";
    public static final String WORLD_MODEL_TAG = "worldModel";
    public static final String LOG_TOOL_TAG = "logTool";
    public static final String LOG_TAG = "log";
    public static final String LOG_CLASS_TAG = "class";
    public static final String LOG_FOLDER_TAG = "folder";
    public static final String LOG_ITERATIONS_TAG = "iterations";

    public static final String ATENTION_TAG = "atention";
    public static final String ATENTION_CLASS_TAG = "class";
    public static final String LABEL_GROUP_TAG = "labels";
    public static final String LABEL_TAG = "label";
    public static final String LABEL_CLASS_TAG = "class";
    public static final String LABEL_COEFFICIENT_TAG = "coefficient";

    public static final String LTMEMORY_TAG = "ltmemory";
    public static final String LTMEMORY_CLASS_TAG = "class";

    public static final String STABILITY_TAG = "stability";
    public static final String STABILITY_CLASS_TAG = "class";
    public static final String STABILITY_PARAMETERS_TAG = "parameters";

    public static final String REPLACEMENT_TAG = "replacement";
    public static final String REPLACEMENT_CLASS_TAG = "class";
    public static final String REPLACEMENT_PARAMETERS_TAG = "parameters";
    public static final String REPLACEMENT_SIMILARITY_THR_TAG = "similarityThr";

    public static final String STABILITY_ERROR_MEMORY_SIZE_TAG = "errorMemorySize";
    public static final String STABILITY_UNSTABLE_CONT_TAG = "unstableCont";
    public static final String STABILITY_UNSTABLE_THR_TAG = "unstableThr";
    public static final String STABILITY_CV_THR_TAG = "coeffOfVariation";
    public static final String DISCONTINUITY_TAG = "discontinuity";
    public static final String LEARNING_ALGORITHMS_GROUP_TAG = "learningAlgorithms";
    public static final String LEARNING_ALGORITHM_CLASS_TAG = "class";

    private ConfigUtilXML() {
    }

}
