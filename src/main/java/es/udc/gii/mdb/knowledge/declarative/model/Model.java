/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.knowledge.declarative.model;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.data.ModelData;
import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.util.log.LogTool;
import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.knowledge.representation.Predictable;
import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.robot.Component;
import es.udc.gii.mdb.robot.ComponentValue;
import es.udc.gii.mdb.robot.SensorMap;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import es.udc.gii.mdb.util.log.LogToolSingleton;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * This class abstracts the common properties of models.
 *
 * @author GII
 */
public abstract class Model extends Observable implements Configurable, Cloneable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Model.class);
    private ModelData modelData;
    private String id;
    private RepresentationApproach modelApproach;
    private Memory mem;
    private List<LogTool> logs;
    private List<String> inputsIDs, outputsIDs;
    private ModelType type;

    /**
     * Parámetros de la evolución que se va a almacenar en el modelo para evitar
     * la relación Model -> Evolution TODO - Revisar esto
     */
    private double fitness;

    private double populationFitness;

    private int iteration;

    //TODO Se va al Factory
    public enum ModelType {

        WORLD, INTERNAL, SATISFACTION, VALUEFUNCTION;
    }

    public Model() {
        modelData = new ModelData();
    }

    public Model(String id, List<String> inputsIDs, List<String> outputsIDs, ModelType type) {
        this.id = id;
        this.inputsIDs = inputsIDs;
        this.outputsIDs = outputsIDs;
        this.type = type;
        modelData = new ModelData();

    }

    /**
     * Getter of the type of the model: world, internal, satisfaction
     *
     * @return
     */
    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    public List<String> getInputsIDs() {
        return inputsIDs;
    }

    /**
     * Getter of the IDs of the outputs. This will be useful to identify which
     * fields of the action-perception pair are used by the model (as outputs)
     *
     * @return
     */
    public List<String> getOutputsIDs() {
        return outputsIDs;
    }

    /**
     * Getter of the ID of the model
     *
     * @return
     */
    public String getID() {
        return id;
    }

    /**
     * Getter of the fitness, obtained from the last evolutionary process.
     *
     * @return
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Obtains the fitness obtained for the population in the last execution of
     * the evolutionary process
     *
     * @return
     */
    public double getPopulationFitness() {
        return populationFitness;
    }

    /**
     * Obtains the iteration of the inner evolutionary process
     *
     * @return
     */
    public int getIteration() {
        return iteration;
    }

    /**
     * Each model will be responsible of storing its registers
     *
     * @param ap Action-perception pair to be stored
     * @return Register replaced (just in case replacement needed, null
     * otherwise)
     */
    public Register store(ActionPerceptionPair ap) {
        return doStore(ap);
    }
    

    /**
     * Getter of the inner model, the inner representation
     *
     * @param ap
     * @return
     */
    protected abstract Register doStore(ActionPerceptionPair ap);
    
    public synchronized RepresentationApproach getRepresentationApproach() {
        return modelApproach;
    }

    /**
     * Method to update the inner model. Each time an evolutionary process
     * finishes, this method should be called, in order to change and update the
     * model with the obtained one.
     *
     * @param newModel New model to be established as current
     * @param fitness
     * @param populationFitness
     * @param iteration
     */
    public synchronized void updateModel(RepresentationApproach newModel,
            double fitness, double populationFitness, int iteration) {
        this.modelApproach = newModel;

        this.fitness = fitness;
        this.populationFitness = populationFitness;
        this.iteration = iteration;

    }
    
    //TODO -> Tiene que estar fuera porque tienen que poder llamarlo incluso
    //Aquellos modelos que no tengan aprendizaje, y el updateModel sólo se llama
    //después de aprender
    public void notifyLogs() {
        //TODO -> Lo paso aquí porque también lo necesito siempre:
        modelData = calculateData(mem.getContent());
        
        //No puedo borrarlo por los logs:
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    

        
    }

    public ModelData calculateData(List<Storageable> content) {
        double[] absMeanEr;
        double[][] sampleEr, predictedOut, realOut;

        absMeanEr = new double[getOutputsIDs().size()];
        sampleEr = new double[content.size()][getOutputsIDs().size()];

        predictedOut = new double[content.size()][getOutputsIDs().size()];
        realOut = new double[content.size()][getOutputsIDs().size()];

        Register reg;
        Predictable output, realOutput;

        for (int i = 0; i < content.size(); i++) {
            reg = (Register) content.get(i);
            output = calculateOutput(reg);
            realOutput = reg.getOutputPerception();
            for (int j = 0; j < output.getValues().size(); j++) {
                predictedOut[i][j] = output.get(j).getValue();
                realOut[i][j] = realOutput.get(j).getValue();
                sampleEr[i][j] = Math.abs(predictedOut[i][j] - realOut[i][j]);
                if (i == 0) {
                    absMeanEr[j] = sampleEr[i][j];
                } else {
                    absMeanEr[j] += sampleEr[i][j];
                }
            }
        }
        double absError = 0.0;
        for (int i = 0; i < absMeanEr.length; i++) {
            absMeanEr[i] = absMeanEr[i] / content.size();
            absError += absMeanEr[i];
        }
        absError = absError / content.size();
        return new ModelData(absMeanEr, absError, sampleEr, predictedOut, realOut);
    }

    /**
     * Calculates the prediction (according to each concrete model) for a
     * concrete action-perception pair.
     *
     * @param ap Action-perception pair to be processed
     * @return Predictable containing the prediction of the model
     */
    public Predictable calculateOutput(ActionPerceptionPair ap) {
        return calculateOutput(filterPair(ap));
    }

    /**
     * Calculates the output of the model getting a {@link Register} as input
     *
     * @param reg Register to process
     * @return
     */
    public abstract Predictable calculateOutput(Register reg);

    /**
     * Method to filter an action-perception pair. This method is useful in
     * order to optimize the architecture, as it reduces the data stored by the
     * models and the data managed when predicting outputs.
     *
     * @param ap Action-perception pair to be filtered
     * @return
     */
    protected abstract Register filterPair(ActionPerceptionPair ap);
    
    /**
     * Method to decode an array of double values (chromosome values) into a
     * usable model
     *
     * @param values
     */
    //TODO - Lo usan las funciones objetivo. Hay que desacoplar esto. Ya que
    //llama a modelApproach para decodificarse. A la implementación concreta.
    public void decode(Iterator values) {
        try {
            modelApproach.decode(values);
        } catch (ModelException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Getter of the memory of the model
     *
     * @return
     */
    public Memory getMemory() {
        return mem;
    }

    /**
     * Method to configure the model from a xml file
     *
     * @param conf
     */
    @Override
    public void configure(Configuration conf) throws MissingConfigurationParameterException {
        try {
            id = conf.getString(ConfigUtilXML.MODEL_ID_TAG);

            Configuration approachConf = conf.subset(ConfigUtilXML.MODEL_APPROACH);
            approachConf.getKeys();
            modelApproach = (RepresentationApproach) Class.forName(approachConf.getString(ConfigUtilXML.MODEL_CLASS_TAG)).newInstance();
            modelApproach.configure(approachConf);

            inputsIDs = (List<String>) conf.getList(ConfigUtilXML.MODEL_INPUTS_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);
            outputsIDs = (List<String>) conf.getList(ConfigUtilXML.MODEL_OUTPUTS_TAG + "." + ConfigUtilXML.MODEL_ID_TAG);

            mem = (Memory) Class.forName(conf.getString(ConfigUtilXML.MEMORY_TAG + "." + ConfigUtilXML.MEMORY_CLASS_TAG)).newInstance();
            mem.configure(conf.subset(ConfigUtilXML.MEMORY_TAG));

            //FIXME should be removed this dependency  
            mem.setModel(this);

            createLogs(conf.subset(ConfigUtilXML.LOG_TOOL_TAG));

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            //EXCEPTION
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Private method to create and initialize the logs of a model
     *
     * @param conf
     */
    public void createLogs(Configuration conf) {
        LogTool log;
        logs = new ArrayList<>();
        List<String> classes = conf.getList(ConfigUtilXML.LOG_TAG + "." + ConfigUtilXML.LOG_CLASS_TAG);

        try {
            for (int i = 0; i < classes.size(); i++) {
                log = (LogTool) Class.forName(classes.get(i)).newInstance();
                log.setName(getID());
                log.configure(conf.subset(ConfigUtilXML.LOG_TAG + "(" + i + ")"));
                log.setObservable(this);
                logs.add(log);
                LogToolSingleton.getInstance().addLog(log);
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            //EXCEPTION
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the absolute mean errors. Useful to display this info in the logs
     *
     * @return
     */
    public double[] getAbsMeanError() {
        return modelData.getAbsMeanError();
    }

    public double getAbsError() {
        return modelData.getAbsError();
    }

    /**
     * Get the predicted outputs. Useful to display this info in the logs
     *
     * @return
     */
    public double[][] getPredictedOutputs() {
        return modelData.getPredictedOutputs();
    }

    /**
     * Get the real outputs used when trying to predict them. Useful to display
     * this info in the logs
     *
     * @return
     */
    public double[][] getRealOutputs() {
        return modelData.getRealOutputs();
    }

    /**
     * Get error per sample in memory. Useful to display this info in the logs.
     *
     * @return
     */
    public double[][] getSampleError() {
        return modelData.getSampleError();
    }

    /**
     * Method to save the inner model into a xml file, readable later to re-load
     * an obtained model if needed
     *
     * @param path Path where the file will be saved
     * @throws ModelException
     */
    public void save(String path) throws ModelException {
        File p = new File(path);
        File fullPath = new File(p, this.id + "-" + MDBCore.getInstance().getIterations() + ".xml");
        modelApproach.save(fullPath.getPath());
    }


    /**
     * Method that calculates the outputs of a model depending on the inputs
     * passed as parameter and the output ids.
     *
     * @param inputs List of {@link ComponentValue} that represent the inputs of
     * the model
     * @param outputsIDs Ids of the outputs, provided in order to identify them
     * and denormalize results
     * @return List of {@link ComponentValue} representing the outputs of the
     * model
     * @throws ModelException
     */
    public List<ComponentValue> calculateOutputs(List<ComponentValue> inputs, List<String> outputsIDs) throws ModelException {

        //TODO LOGGER
        double[] doubleInputs = new double[inputs.size()];

        List<ComponentValue> outputValues = new ArrayList<>();
        for (int i = 0; i < doubleInputs.length; i++) {
            doubleInputs[i] = inputs.get(i).getNormalizedValue();
        }

        double[] outputs = this.modelApproach.calculateOutputs(doubleInputs);

        Component component;

        for (int i = 0; i < outputs.length; i++) {
            component = SensorMap.getInstance().getComponent(ActionPerceptionPairMap.getInstance().getType(outputsIDs.get(i)));
            outputValues.add(component.createValue(outputs[i], Component.NORMALIZED_VALUE));
        }

        return outputValues;
    }

    public int getNumberOfInputs() {
        return this.inputsIDs.size();
    }

    public int getNumberOfOutputs() {
        return this.outputsIDs.size();
    }

    public void setModelApproach(RepresentationApproach modelApproach) {
        this.modelApproach = modelApproach;
    }

    public void setMem(Memory mem) {
        this.mem = mem;
    }

    @Override
    public String toString() {
        return getID();
    }

    
}
