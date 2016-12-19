/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.motivation.buffer.MotivationEpisode;
import java.util.List;
import java.util.Observable;

/**
 *
 * This {@link LogTool} can be used to follow the improvement of the models,
 * and the enhancement of their predictions, comparing them with the real samples.
 *
 * @author GII
 */
public class RewardOutputsLogTool extends EvolutionLogTool {

    private String line;
    private double[][] predicted, real;


    /**
     *
     * When writing a new line, it writes the current iteration and, in columns
     * separated by tabs, interpreted two by two as the real output followed by the
     * predicted by the model, doing it for each of the outputs of the model.
     *
     * @param o
     * @param arg
     */

    @Override
    public void doUpdate(Observable o, Object arg) {

        Model m = (Model) o;
        predicted = m.getPredictedOutputs();
        real = m.getRealOutputs();
        List<Storageable> meb = m.getMemory().getContent();
        
        for(int i=0; i<real.length; i++) {
            line = m.getIteration()+"\t";
            line = MDBCore.getInstance().getIterations() +"\t";
            line += ((MotivationEpisode) meb.get(i)).getActionPerceptionPair().getExternalTn().toString() + "\t";
            for(int j=0; j<real[0].length; j++) {
                line += real[i][j]+"\t"+predicted[i][j]+"\t";
            }
            printLine(line.trim());
        }
        printLine("\n");
        printLine("\n");
    }

}
