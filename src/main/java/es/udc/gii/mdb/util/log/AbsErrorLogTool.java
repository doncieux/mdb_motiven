/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.learning.LearningAlgorithm;
import es.udc.gii.mdb.learning.LearningAlgorithmMap;
import java.util.Observable;

/**
 *
 * From this log we can obtain the information of the mean absolute error in
 * each of the outputs predicted by the model, calculating it against the
 * samples in memory at the moment of evolving the model.
 *
 * @author GII
 */
public class AbsErrorLogTool extends EvolutionLogTool {

    /**
     *
     * Each time this {@link LogTool} writes a line, it writes the iteration and
     * a set of columns, one per output, specifying the mean absolute error in
     * the prediction of the model for that output and separated by tabs.
     *
     * @param o
     * @param arg
     */
    @Override
    public void doUpdate(Observable o, Object arg) {

        Model m = (Model) o;
        LearningAlgorithm la;
        la = LearningAlgorithmMap.getInstance().getByModelId(m.getID());

        String line = "";
        if (la != null) {
            int iteration = la.getIteration();
            line = iteration + "\t";

            for (double d : m.getAbsMeanError()) {
                line += d + "\t";
            }
        }

        printLine(line.trim());

    }
}
