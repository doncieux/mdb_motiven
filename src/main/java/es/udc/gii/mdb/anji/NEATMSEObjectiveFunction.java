/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.anji;

import com.anji.util.Configurable;
import com.anji.util.Properties;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import es.udc.gii.mdb.knowledge.declarative.model.ValueFunction;
import es.udc.gii.mdb.objective.MSEModelCalculation;
import es.udc.gii.mdb.objective.MSERetardedSatisfaction;
import java.util.List;
import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;

/**
 *
 * @author Grupo Integrado de Ingeniería
 * (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class NEATMSEObjectiveFunction implements BulkFitnessFunction, Configurable {

    private Model model;

    private String modelID;

    private int precission = 10;

    private int delays = 0;

    @Override
    public void evaluate(List population) {

        model = ModelMap.getInstance().getModel(modelID);
        Chromosome c;
        double cFitness;

        for (int i = 0; i < population.size(); i++) {

            c = (Chromosome) population.get(i);
            model.decode(c.getAlleles().iterator());

            if (model instanceof ValueFunction) {
                cFitness = MSERetardedSatisfaction.calculateMSE(model);
            } else {
                cFitness = MSEModelCalculation.calculateMSE(model, delays);
            }

            //Como el NEAT maximiza hay que poner el negativo de lo que devuelve, además de transformarlo
            //a un entero según la precisión (número de decimales considerados):
            cFitness = 1.0 - cFitness;

            cFitness = cFitness * Math.pow(10, precission);

            c.setFitnessValue((int) Math.round(cFitness));

        }

    }

    @Override
    public int getMaxFitnessValue() {
        return 0;
    }

    @Override
    public void init(Properties prprts) throws Exception {

        this.modelID = prprts.getProperty("modelID");
        this.precission = prprts.getIntProperty("precission", 10);
        this.delays = prprts.getIntProperty("temporal.connection.fifo.size");

    }

}
