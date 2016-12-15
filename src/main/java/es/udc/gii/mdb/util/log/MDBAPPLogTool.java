/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.core.MDBCore;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * This {@link LogTool} can be used to follow the learning process of the architecture. It stores
 * the iterations when the task is accomplished. This way we can appreciate how as time goes by, the
 * iterations written in the log file get closer.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class MDBAPPLogTool extends MDBLogTool {


    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
    }

    /**
     *
     * When writing a new line, it writes the iteration of the main loop and the perception obtained in that iteration
     *
     * @param o
     * @param arg
     */
    @Override
    public void doUpdate(Observable o, Object arg) {
        MDBCore core = (MDBCore) o;
        String s = MDBCore.getInstance().getIterations() + "\t" + core.getAp().toString() + "\t" + MDBCore.getInstance().getPapEvaluation();
        printLine(s);
    }
}
