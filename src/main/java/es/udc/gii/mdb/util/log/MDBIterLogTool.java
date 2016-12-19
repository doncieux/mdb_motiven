/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.declarative.model.ModelMap;
import java.util.Observable;

/**
 *
 * Main {@link LogTool} and one of the simplest. With this log we can follow
 * the learning process for a model. It shows the fitness of the model and the
 * mean fitness of the population as well.
 *
 * @author GII
 */
public class MDBIterLogTool extends MDBLogTool {

    private int iter;
    private double fitness,  populationFitness;
    private Model model;

    /**
     *
     * When writing a new line, it writes the iteration and, separated by tabs,
     * the fitness of the model at that moment and the mean fitness of the population
     * as well.
     *
     * @param o
     * @param arg
     */

    @Override
    public void doUpdate(Observable o, Object arg) {

        MDBCore core = (MDBCore) o;

        model = ModelMap.getInstance().getModel(getName());
        iter = model.getIteration();
        fitness = model.getFitness();
        populationFitness = model.getPopulationFitness();

        printLine(core.getIterations() + "\t" + fitness + "\t" + populationFitness + "\t" + iter);
    }
}
