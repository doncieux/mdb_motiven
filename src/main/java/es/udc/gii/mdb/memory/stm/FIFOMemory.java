/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.memory.stm;

/**
 *
 * Concrete class extending {@link Memory}. It implements a FIFO replacement
 * strategy.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class FIFOMemory extends Memory {

    /**
     *
     * When storing a new element and replacement needed, the "oldest" element
     * is the chosen one to be replaced (FIFO - First In First Out).
     *
     * @param s Element to be stored.
     * @return The element replaced.
     */

  
    @Override
    protected Storageable replaceWith(Storageable s) {
        Storageable ret = super.getContent().remove(0);
        super.getContent().add(s);
        return ret;
    }

    @Override
    public int isReplaced() {
        return 1;
    }

    
    
}
