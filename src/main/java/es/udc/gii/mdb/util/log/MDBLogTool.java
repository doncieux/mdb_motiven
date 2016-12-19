/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import java.util.Observable;

/**
 *
 * This {@link LogTool} is created to observe the core of the architecture, the
 * execution of the main loop. It's an abstract class and will concreted in
 * several types of logs.
 *
 * @author GII
 */
public abstract class MDBLogTool extends LogTool {

    private Observable observable;

    /**
     *
     * In this method we establish each instance of this class as observer of
     * the core.
     *
     * @param m {@link Model} provided. Useless in this case.
     */
    @Override
    public void setObservable(Observable m) {
        observable = MDBCore.getInstance();
        MDBCore.getInstance().addObserver(this);
    }

    @Override
    public Observable getObservable() {
        return observable;
    }

}
