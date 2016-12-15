/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import java.util.Observable;

/**
 *
 * This {@link LogTool} provides the possibility of monitoring the content of
 * the {@link es.udc.gii.gsa.mdb.memory.Memory} of a {@link Model} at each moment.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class MemoryLogTool extends EvolutionLogTool {

    /**
     *
     * When writing a new line, it writes the iteration and the String representation
     * of the stored registers. It will write a line for each register, this way we may
     * later filter this log through the first column (iteration).
     *
     * @param o
     * @param arg
     */

    @Override
    public void doUpdate(Observable o, Object arg) {
        Model m = (Model) o;
        Register reg;

        for(Storageable s: m.getMemory().getContent()) {
            reg = (Register) s;
            printLine(m.getIteration() + "\t" + reg.getRegister().toString());
        }
        
        printLine("");
        printLine("");
    }

}
