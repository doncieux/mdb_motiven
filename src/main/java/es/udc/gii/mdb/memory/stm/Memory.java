/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.memory.stm;



import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * This class supports the memory system of the architecture. It implements most of
 * the properties of a memory, but the replacement method is designed as a template
 * method pattern, thus we can add several types of memories, extending this class
 * and setting different strategies.
 *
 * @author GII
 */
public abstract class Memory extends Observable implements Configurable, Cloneable{

    private List<Storageable> content;
    private int size;
    /* Model */
    private Model model;
    //Para la atención
    private Storageable lastAdded;

    public Memory() {
        this.content = new ArrayList<>();
    }

    public Memory(int size) {
        this.size = size;
        this.content = new ArrayList<>();
    }
   
    
    /**
     *
     * Stores a new element in the memory, calling the method of replacement
     * if needed. It notifies its observers when a new sample is stored.
     *
     * @param reg Element to be stored.
     * @return The element replaced, in case replacement took place, or null otherwise.
     */
    public Storageable store(Storageable reg) {
        Storageable ret;
        if (this.getContentSize() == this.size) {
            ret = replaceWith(reg);
        } else 
        {
            ret = spaceAvailable(reg);
        }
        
        //TODELETE -> Está avisando a "Evolution" para que lance el proceso de aprendizaje
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
        
        return ret;
    }

    /**
     *
     * Template method used to implement different types of memories with
     * different replacement strategies.
     *
     * @param s Element to be stored, replacing one existing in the memory
     * @return The element replaced.
     */
    protected abstract Storageable replaceWith(Storageable s);
    
    public int getContentSize() {
        return this.getContent().size();
    }

    protected Storageable spaceAvailable(Storageable s) {
        getContent().add(s);
        return s;
    }
    /**
     *
     * Get the elements contained in this memory.
     *
     * @return A List containing all the elements that are stored in this memory.
     */
    public List<Storageable> getContent() {
        return content;
    }

    public void setContent(List<Storageable> content) {
        this.content = content;
    }

    /**
     *
     * Get the maximum number of elements that can be stored in this memory.
     *
     * @return Maximum capacity of the memory.
     */
    public int getMaxCapacity() {
        return size;
    }

    public void setMaxCapacity(int size) {
        this.size = size;
    }

    /**
     *
     * Method to configure each memory from the parameters of a configuration file.
     *
     * @param conf Object containing the configuration details related to a memory.
     */
    @Override
    public void configure(Configuration conf) {
        this.size = conf.getInt(ConfigUtilXML.MEMORY_SIZE_TAG);
    }

    public void purgue() {
        //TODO -> Qué hacemos con esto?
        /**
         * This method is thought in order to delete all content of the memory.
         */
    }

    public Storageable getLastAdded() {
        return lastAdded;
    }

    @Override
    public Memory clone() {
        Memory obj = null;
        try {
            obj = (Memory) super.clone();
        } catch (CloneNotSupportedException ex) {
            //TODO EXCEPTION
            throw new MDBRuntimeException();
        }

        List<Storageable> contentCopy = new ArrayList<Storageable>(size);
        for (int i = 0; i < content.size(); i++) {
            contentCopy.add(content.get(i));
        }
        obj.setContent(contentCopy);
        obj.setMaxCapacity(size);
        return obj;
    }

    
    //TODO Estos dos métodos deberán desaparecer
    public void setModel(Model m) {
        this.model = m;
    }

    public Model getModel() {
        return model;
    }

    //TO REMOVE
    public abstract int isReplaced();
    
    
}
