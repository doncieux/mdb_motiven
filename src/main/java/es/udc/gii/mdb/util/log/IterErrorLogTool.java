/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import java.util.Observable;

/**
 *
 * Using this {@link LogTool} we can retrieve information about the absolute errors
 * in the prediction of each sample stored in memory at the moment of obtaining the model.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class IterErrorLogTool extends EvolutionLogTool {

    private double[][] sampleErrors;

    /**
     *
     * When updating and writing a new line in the log file, it writes the iteration
     * and, in several columns separated by tabs, the errors for each sample.
     *
     * @param o
     * @param arg
     */

    @Override
    public void doUpdate(Observable o, Object arg) {

        Model m = (Model) o;
        String line;
        sampleErrors = m.getSampleError();
        
        for(int i=0; i<sampleErrors.length; i++) {
            line = m.getIteration()+"\t";
            for(int j=0; j<sampleErrors[0].length; j++) {
                line += sampleErrors[i][j]+"\t";
            }
            printLine(line.trim());
        }

    }
}

