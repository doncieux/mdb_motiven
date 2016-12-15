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
package es.udc.gii.mdb.core;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.action.chooser.StrategyChooser;
import es.udc.gii.mdb.action.strategy.PlainStrategy;
import es.udc.gii.mdb.action.strategy.Strategy;
import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.core.condition.MaxIterationsStopCondition;
import es.udc.gii.mdb.core.condition.StopCondition;
import es.udc.gii.mdb.util.log.LogTool;
import es.udc.gii.mdb.knowledge.declarative.model.InputModel;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.knowledge.declarative.model.SatisfactionModel;
import es.udc.gii.mdb.learning.LearningAlgorithm;
import es.udc.gii.mdb.learning.LearningAlgorithmMap;
import es.udc.gii.mdb.memory.stm.EpisodicBuffer;
import es.udc.gii.mdb.motivation.MotivationGoal;
import es.udc.gii.mdb.motivation.SensorGoal;
import es.udc.gii.mdb.motivation.buffer.ValueFunctionEpisodicBuffer;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.Perception;
import es.udc.gii.mdb.robot.ActuatorMap;
import es.udc.gii.mdb.robot.Component;
import es.udc.gii.mdb.robot.ComponentMap;
import es.udc.gii.mdb.robot.Sensor;
import es.udc.gii.mdb.robot.SensorMap;
import es.udc.gii.mdb.util.MDBRandom;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import es.udc.gii.mdb.util.log.LogToolSingleton;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class represents the core of the architecture. It holds the main
 * execution, the strategy selection mechanism, sensory data gathering and
 * information storage.
 *
 * @author GII
 */
public class MDBCore extends Observable implements Configurable, Runnable {

    protected static final double THRESHOLD = 1.0e-6;

    private static final Logger LOGGER = LoggerFactory.getLogger(MDBCore.class);

    private Collection<InputModel> worldModels;
    private Collection<InputModel> internalModels;
    private Collection<SatisfactionModel> satisfactionModels;
    private Collection<ValueFunction> valueFunctions;
    private Collection<Sensor> externalSensorsT;
    private Collection<Sensor> internalSensorsT;
    private Collection<Sensor> externalSensorsTn;
    private Collection<Sensor> internalSensorsTn;
    private Collection<Sensor> satisfactionSensors;

    private Collection<LearningAlgorithm> learningAlgorithm;

    private Sensor crashSensor;
    private Strategy finalStrategy;
    private StopCondition stopCondition;
    private static MDBCore instance = null;
    private int iterations;
    private boolean finished = false, taskAccomplished = false;
    private String url, savePath;
    private int saveIterations;
    private StrategyChooser strategyChooser;
    private Collection<LogTool> logs;
    private ActionPerceptionPair ap;
    private EpisodicBuffer<ActionPerceptionPair> globalEpisodicBuffer;

    private MotivationGoal<ActionPerceptionPair> subgoal;
    private boolean subgoalReached;

    //F log
    private double papEvaluation = -1;

    private List<MotivationGoal<ActionPerceptionPair>> subgoalCandidates = new ArrayList<>();
    private List<Double> rewards = new ArrayList<>();

    private boolean crashed = false;

    private int maxSubgoals;

    private MDBCore() {
    }

    public static void setConfigurationFilePath(String path) {
        MDBConfiguration.setConfigurationFilePath(path);
    }

    /**
     *
     * As this class is implemented following the Singleton design pattern,
     * through this method we provide access to the unique instance of the
     * class.
     *
     * @return Instance of the class.
     */
    public static MDBCore getInstance() {
        if (instance == null) {
            instance = new MDBCore();
            try {
                instance.configure(MDBConfiguration.getInstance());
            } catch (MissingConfigurationParameterException ex) {
                java.util.logging.Logger.getLogger(MDBCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    /**
     *
     * Execution of the main loop.
     *
     *
     */
    @Override
    public void run() {

        mainLoop();

        closeLogs();

    }

    private void mainLoop() {
        Perception externalT = null;
        Perception internalT = null;
        Perception externalTn = null;
        Perception internalTn = null;

        Perception satisfactionTn = null;
        finalStrategy = null;

        while (!finished) {
            ap = new ActionPerceptionPair();

            ///
            //
            // Sensorization in t
            //
            ///
            externalT = new Perception();
            for (Sensor s : externalSensorsT) {
                externalT.addValue(s.getValue());

            }
            ap.setExternalT(externalT);

            internalT = new Perception();
            for (Sensor s : internalSensorsT) {
                internalT.addValue(s.getValue());
            }

            ap.setInternalT(internalT);

            ///
            //
            // Actuation in t
            //
            ///
            if (strategyChooser != null) {
                if (crashed) { //FIXME Ad hoc solution to execute a random action when robot crashed againts a wall or a border in the previous iteration
                    //so we can avoid the robot get stuck
                    Action randomAct = new Action(ActuatorMap.getInstance().getComponent("motor").createValue(MDBRandom.nextDouble(), Component.NORMALIZED_VALUE));
                    finalStrategy = new PlainStrategy(randomAct);
                } else {
                    finalStrategy = strategyChooser.selectStrategy(ap);
                }
                finalStrategy.execute();
                ap.setStrategy(finalStrategy);
            }

            ///
            //
            // Sensorization in t+1
            //
            ///
            externalTn = new Perception();
            for (Sensor s : externalSensorsTn) {
                externalTn.addValue(s.getValue());
            }
            ap.setExternalTn(externalTn);

            internalTn = new Perception();
            for (Sensor s : internalSensorsTn) {
                internalTn.addValue(s.getValue());
            }
            ap.setInternalTn(internalTn);

            ///
            //
            // Satisfaction in t+1 (unused with motivations, keeped for compatibility with previous MDB versions
            //
            ///
            satisfactionTn = new Perception();
            for (Sensor s : satisfactionSensors) {
                satisfactionTn.addValue(s.getValue());
            }
            ap.setSatisfactionTn(satisfactionTn);

            ///
            // FIXME
            // Check if the robot crashed after executing last action
            // Ad hoc for this experiment
            //
            ///
            if (crashSensor != null) {
                double crash = crashSensor.getValue().getValue();
                if (crash > THRESHOLD) {
                    crashed = true;
                } else {
                    crashed = false;
                }
            }

            // The ActionPerception Pair is stored in a global memory
            addToEpisodicBuffer(ap);

            //The new episode is stored in the models and the learning algorithms of the models are triggered 
            LearningAlgorithm la;
            for (Model m : worldModels) {
                m.store(ap);

                //Learning process call and model update
                la = LearningAlgorithmMap.getInstance().getByModelId(m.getID());
                if (la != null) {
                    la.learn();
                    m.decode(la.getCurrentParameters());
                    m.updateModel(m.getRepresentationApproach(),
                            la.getCurrentModelFitness(),
                            la.getCurrentPopulationFitness(),
                            la.getIteration());
                }
                m.notifyLogs();

            }

            for (Model m : internalModels) {
                m.store(ap);

                //The new episode is stored in the models and the learning algorithms of the models are triggered 
                //Learning process call and model update
                la = LearningAlgorithmMap.getInstance().getByModelId(m.getID());
                if (la != null) {
                    la.learn();
                    m.decode(la.getCurrentParameters());
                    m.updateModel(m.getRepresentationApproach(),
                            la.getCurrentModelFitness(),
                            la.getCurrentPopulationFitness(),
                            la.getIteration());
                }
                m.notifyLogs();
            }

            for (Model m : satisfactionModels) {
                m.store(ap);
                //The new episode is stored in the models and the learning algorithms of the models are triggered 
                //Learning process call and model update
                la = LearningAlgorithmMap.getInstance().getByModelId(m.getID());
                if (la != null) {
                    la.learn();
                    m.decode(la.getCurrentParameters());
                    m.updateModel(m.getRepresentationApproach(),
                            la.getCurrentModelFitness(),
                            la.getCurrentPopulationFitness(),
                            la.getIteration());
                }
                m.notifyLogs();
            }

            rewards.clear();
            List<ValueFunction> currentValueFunctions = new ArrayList<>();
            currentValueFunctions.addAll(ModelMap.getInstance().getValueFunctions());
            boolean resetMemories = Boolean.FALSE;
            for (ValueFunction m : currentValueFunctions) {
                m.store(ap);
                rewards.add(m.getLastReward());
                if (m.getGoal() instanceof SensorGoal) {
                    //Check if the VF reward is sensed by a SensorGoal, and senses in that case 
                    taskAccomplished = m.getLastReward() > 1.0e-6;
                    if (taskAccomplished) {
                        resetMemories = Boolean.TRUE;

                    }
                }
                la = LearningAlgorithmMap.getInstance().getByModelId(m.getID());
                if (la != null) {
                    if (la.learn()) {
                        m.decode(la.getCurrentParameters());
                        m.updateModel(m.getRepresentationApproach(),
                                la.getCurrentModelFitness(),
                                la.getCurrentPopulationFitness(),
                                la.getIteration());
                    }

                }
                m.notifyLogs();

            }

            if (resetMemories) {
                globalEpisodicBuffer.getContent().clear();

                for (ValueFunction rm : currentValueFunctions) {
                    if (rm.getMemory() instanceof ValueFunctionEpisodicBuffer) {
                        ValueFunctionEpisodicBuffer memory = (ValueFunctionEpisodicBuffer) rm.getMemory();
                        memory.getCache().getContent().clear();
                        rm.resetGoalReachments();
                    }

                }
            }

            this.setChanged();
            this.notifyObservers();
            this.clearChanged();

            saveModels();

            //LOGGER -> Hay que ir guardándolo en el logger, cuando lo tengamos. De momento lo dejamos porque es útil para ver la velocidad a la que 
            //va la aplicación y que se sigue ejecutando etc. 
            //LOGGER.info(String.valueOf(iterations));
            System.out.println(iterations);
            finished = stopCondition.evaluateCondition(this);
            iterations++;

        }
    }

    public boolean getFinished() {
        return finished;
    }

    /**
     *
     * In the body of this method we should specify the models that we want to
     * be saved into XML files.
     *
     */
    private void saveModels() {
        try {
            if (getIterations() % saveIterations == 0) {
                for (Model m : worldModels) {
                    m.save(savePath);
                }

                for (Model m : internalModels) {
                    m.save(savePath);
                }

                for (Model m : satisfactionModels) {
                    m.save(savePath);
                }

                for (Model m : valueFunctions) {
                    m.save(savePath);
                }
            }
        } catch (ModelException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     *
     * Generic method that contains all the calls of the closeLogs method of
     * each model used.
     *
     */
    private void closeLogs() {

        LogToolSingleton.getInstance().closeLogs();

    }

    /**
     *
     * Get the number of iteration where the main loop is in.
     *
     * @return Integer representing the iteration.
     */
    public int getIterations() {
        return iterations;
    }

    /**
     *
     * Returns the URL where the MDB connects to. It may be an IP, specified
     * through the configuration file.
     *
     * @return The URL as String.
     */
    public String getURL() {
        return url;
    }

    /**
     *
     * Get the parameter that defines if the task has been accomplished or not.
     *
     * @return Whether the task has been accomplished or not.
     */
    public boolean getTaskAccomplished() {
        return taskAccomplished;
    }

    /**
     *
     * This method is used to configure the class from the details specified in
     * the configuration file.
     *
     * @param conf Object containing the part of the configuration file relating
     * to this class.
     * @throws
     * es.udc.gii.mdb.util.exception.MissingConfigurationParameterException
     */
    @Override
    public void configure(Configuration conf) throws MissingConfigurationParameterException {

        // Init things and configure MDB core
        iterations = 1;

        Configuration core = conf.subset(ConfigUtilXML.MDBCORE_TAG);

        url = core.getString(ConfigUtilXML.MDBCORE_IP_TAG);
        savePath = core.getString(ConfigUtilXML.MDBCORE_SAVE_PATH_TAG);
        maxSubgoals = core.getInt("maxSubgoals");
        int globalEBSize = core.getInt("episodicBufferSize", 200);
        saveIterations = core.getInteger("saveIterations", 100);
        if (!savePath.startsWith(File.separator)) {
            savePath = System.getProperty("user.dir") + File.separator + savePath;
        }

        globalEpisodicBuffer = new EpisodicBuffer<>(globalEBSize);

        // Configure the stop condition
        try {
            String stopCond = core.getString(ConfigUtilXML.MDBCORE_STOP_CONDITION_CLASS_TAG);
            if (stopCond != null) {
                stopCondition = (StopCondition) Class.forName(stopCond).newInstance();
                stopCondition.configure(core.subset(ConfigUtilXML.MDBCORE_STOP_CONDITION_TAG));
            } else {
                stopCondition = new MaxIterationsStopCondition();
                LOGGER.warn("MDB WARNING: The missing configuration parameter "
                        + "stopCondition is set to MaxIterationsStopCondition("
                        + ((MaxIterationsStopCondition) stopCondition).getMaxIterations() + ")");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.error("MDB ERROR: Stop condition instantiation exception.");
            LOGGER.info(ex.getMessage());
            System.exit(-1);
        }

        // Configure the strategy chooser
        try {
            Configuration chooser = core.subset(ConfigUtilXML.MDBCORE_STRATEGY_CHOOSER_TAG);
            if (chooser != null) {
                String className = chooser.getString(ConfigUtilXML.MODEL_CLASS_TAG);
                if (className != null) {
                    strategyChooser = (StrategyChooser) Class.forName(className).newInstance();
                    strategyChooser.configure(chooser);
                } else {
                    LOGGER.warn("MDB WARNING: Missing configuration parameter strategyChooser. No actions will be executed");
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.error("MDB ERROR: Strategy chooser instantiation exception.");
            LOGGER.info(ex.getMessage());
            System.exit(-1);
        }

        // Configure crashSensor. If this sensor exists and the robot sensorize a bigger than zero value, it will execute a random action.
        if (SensorMap.getInstance().getMap().containsKey("crash")) {
            crashSensor = SensorMap.getInstance().getComponent("crash");
        } else {
            crashSensor = null;
        }
        // Configure sensors based on the action-perception pair structure
        Configuration actPerceptPair = conf.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_TAG);

        externalSensorsT = new ArrayList<>();
        internalSensorsT = new ArrayList<>();

        Configuration confSensorsT = actPerceptPair.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_PERCEPTION_T_TAG);

        Configuration externalConf = confSensorsT.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_EXTERNAL_TAG);
        for (String s : externalConf.getStringArray(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG)) {
            Sensor sensor = SensorMap.getInstance().getComponent(s);
            externalSensorsT.add(sensor);
        }

        Configuration internalConf = confSensorsT.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_INTERNAL_TAG);
        for (String s : internalConf.getStringArray(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG)) {
            Sensor sensor = SensorMap.getInstance().getComponent(s);
            internalSensorsT.add(sensor);
        }

        ActuatorMap.getInstance();
        Configuration confSensorsTn = actPerceptPair.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_PERCEPTION_T_N_TAG);

        externalSensorsTn = new ArrayList<>();
        internalSensorsTn = new ArrayList<>();

        externalConf = confSensorsTn.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_EXTERNAL_TAG);
        for (String s : externalConf.getStringArray(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG)) {
            Sensor sensor = SensorMap.getInstance().getComponent(s);
            externalSensorsTn.add(sensor);
        }

        internalConf = confSensorsTn.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_INTERNAL_TAG);
        for (String s : internalConf.getStringArray(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG)) {
            Sensor sensor = SensorMap.getInstance().getComponent(s);
            internalSensorsTn.add(sensor);
        }

        // Configure satisfaction sensors
        satisfactionSensors = new ArrayList<>();
        Configuration confSatisfaction = actPerceptPair.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_SATISFACTION_T_N_TAG);
        for (String s : confSatisfaction.getStringArray(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG)) {
            Sensor sensor = SensorMap.getInstance().getComponent(s);
            satisfactionSensors.add(sensor);
        }

        worldModels = new ArrayList<>();

        Configuration world = conf.subset(ConfigUtilXML.WORLD_MODELS_GROUP_TAG);

        if (world != null) {
            for (String s : world.getStringArray(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG)) {
                worldModels.add((InputModel) ModelMap.getInstance().getModel(s));
            }
        }

        internalModels = new ArrayList<>();

        Configuration internal = conf.subset(ConfigUtilXML.INTERNAL_MODELS_GROUP_TAG);

        if (internal != null) {
            for (String s : internal.getStringArray(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG)) {
                internalModels.add((InputModel) ModelMap.getInstance().getModel(s));
            }
        }

        satisfactionModels = new ArrayList<>();

        Configuration satisfaction = conf.subset(ConfigUtilXML.SATISFACTION_MODELS_GROUP_TAG);

        if (satisfaction != null) {
            for (String s : satisfaction.getStringArray(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG)) {
                satisfactionModels.add((SatisfactionModel) ModelMap.getInstance().getModel(s));
            }
        }

        valueFunctions = new ArrayList<>();

        Configuration vf = conf.subset(ConfigUtilXML.VALUE_FUNCTIONS_GROUP_TAG);

        if (vf != null) {
            for (String s : vf.getStringArray(ConfigUtilXML.MODEL_TAG + "." + ConfigUtilXML.MODEL_ID_TAG)) {
                valueFunctions.add((ValueFunction) ModelMap.getInstance().getModel(s));
            }
        }

        createLogs(conf.subset(ConfigUtilXML.MDBCORE_TAG + "." + ConfigUtilXML.LOG_TOOL_TAG));

    }

    public Collection<Sensor> getExternalSensorsT() {
        return this.externalSensorsT;
    }

    public Collection<Sensor> getExternalSensorsTn() {
        return this.externalSensorsTn;
    }

    public ActionPerceptionPair getAp() {
        return ap;
    }

    private void createLogs(Configuration conf) {
        LogTool log;
        logs = new ArrayList<>();
        List<String> classes = conf.getList(ConfigUtilXML.LOG_TAG + "." + ConfigUtilXML.LOG_CLASS_TAG);

        try {
            for (int i = 0; i < classes.size(); i++) {
                log = (LogTool) Class.forName(classes.get(i)).newInstance();
                log.setName("MDBCore");
                log.configure(conf.subset(ConfigUtilXML.LOG_TAG + "(" + i + ")"));
                log.setObservable(this);
                logs.add(log);
                LogToolSingleton.getInstance().addLog(log);
            }

        } catch (InstantiationException ex) {
            //EXCEPTION
            LOGGER.error(ex.getMessage());
        } catch (IllegalAccessException ex) {
            //EXCEPTION
            LOGGER.error(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            //EXCEPTION
            LOGGER.error(ex.getMessage());
        }
    }

    private void addToEpisodicBuffer(ActionPerceptionPair app) {
        if (globalEpisodicBuffer.isFull()) {
            globalEpisodicBuffer.removeEpisode(0);
        }
        globalEpisodicBuffer.addEpisode(app);
    }

    public EpisodicBuffer<ActionPerceptionPair> getGlobalEpisodicBuffer() {
        return globalEpisodicBuffer;
    }

    public boolean isSubgoalReached() {
        return subgoalReached;
    }

    public boolean isTaskAccomplished() {
        return taskAccomplished;
    }

    public double getPapEvaluation() {
        return papEvaluation;
    }

    public List<Double> getRewards() {
        return rewards;
    }

    public int getMaxSubgoals() {
        return maxSubgoals;
    }

    @Override
    public String toString() {
        return "MDBCore";
    }

}
