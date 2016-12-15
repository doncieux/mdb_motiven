/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.jeaf.evaluate;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.objective.MSEModelCalculation;
import es.udc.gii.mdb.objective.MSERetardedSatisfaction;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * 
 *
 * @author GII
 */
public class MSEObjectiveFunction extends ObjectiveFunction {

    private String modelID;
    private Model model;

    @Override
    public double evaluate(double[] values) {

        List<Double> valuesList = new ArrayList<Double>();
        for (double d : values) {
            valuesList.add(d);
        }

        //TODO get cloned model 
        model = ModelMap.getInstance().getModel(modelID);
        model.decode(valuesList.iterator());

        if (model instanceof ValueFunction) {
            return MSERetardedSatisfaction.calculateMSE(model);

        } else {
            return MSEModelCalculation.calculateMSE(model, 0);
        }
    }

    @Override
    public void reset() {
        /**
         *
         */
    }

    @Override
    public void configure(Configuration conf) {
        modelID = conf.getString(ConfigUtilXML.MODEL_ID_TAG);
    }

}
