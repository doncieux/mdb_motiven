/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import java.util.Observable;

/**
 *
 * This {@link LogTool} is created to observe a model, and thus the evolutionary
 * processes.
 *
 * @author Borja Santos-Diez Vazquez
 */
public abstract class EvolutionLogTool extends LogTool {

    private Observable observable;

    /**
     *
     * This method establishes the {@link LogTool} as an observer of the
     * {@link Model} provided.
     *
     * @param m {@link Model} to be observed.
     */
    @Override
    public void setObservable(Observable m) {
        this.observable = m;
        m.addObserver(this);
    }

    @Override
    public Observable getObservable() {
        return observable;
    }

}
