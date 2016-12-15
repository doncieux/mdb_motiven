/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.mdb.robot;


import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 * A Component of a robot, that may be a sensor or an actuator. Both of them will have
 * two ranges of values: the normal or 'raw' one (the values directly obtained or passed
 * to the component) and the normalized one (useful when working with ANN, for example)
 *
 * @author Borja Santos-Diez Vazquez
 */
public abstract class Component implements Configurable {

    private double minValue;
    private double maxValue;
    private double normMinValue;
    private double normMaxValue;
    private String id;
    private int port;
    private Socket socket;
    public static final int RAW_VALUE = 0, NORMALIZED_VALUE = 1;

    /**
     * Setter of the maximum value
     * @param maxValue
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Setter of the minimum value
     * @param minValue
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * Setter of the maximum normalized value
     * @param normMaxValue
     */
    public void setNormMaxValue(double normMaxValue) {
        this.normMaxValue = normMaxValue;
    }

    /**
     * Setter of the minimum normalized value
     * @param normMinValue
     */
    public void setNormMinValue(double normMinValue) {
        this.normMinValue = normMinValue;
    }

    /**
     * Setter of the id of the component. It must be unique throughout the
     * architecture
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter of the port associated with this component
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Getter of the maximum value
     * @return
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Getter of the minimum value
     * @return
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Getter of the maximum normalized value
     * @return
     */
    public double getNormMaxValue() {
        return normMaxValue;
    }

    /**
     * Getter of the minimum normalized value
     * @return
     */
    public double getNormMinValue() {
        return normMinValue;
    }

    /**
     * Getter of the id of the component
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Getter of the port associated with this component
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Getter of the socket created for this component
     * @return
     */
    protected Socket getSocket() {
        return socket;
    }

    private double normalizeValue(double value) {
        
        double m = (getNormMaxValue()-getNormMinValue())/(getMaxValue()-getMinValue());
        
        return m*(value-getMinValue()) + getNormMinValue();
    }
    
    private double denormalizeValue(double value) {
        
        double m = (getMaxValue()-getMinValue())/(getNormMaxValue()-getNormMinValue());
        
        return m*(value-getNormMinValue()) + getMinValue();
    }
    
    /**
     * Helper method, to create a {@link ComponentValue} from an integer value, specifying
     * wether we are providing a raw value or a normalized value
     * @param value Integer value
     * @param mode Mode of the value (raw or normalized)
     * @return
     */
    public ComponentValue createValue(double value, int mode) {
        ComponentValue res = null;
        switch(mode) {
            case RAW_VALUE:
                res = new ComponentValue(this.id,value,normalizeValue(value));
                break;
            case NORMALIZED_VALUE:
                res = new ComponentValue(this.id,denormalizeValue(value),value);
                break;
            default: 
                //EXCEPTION
                throw new MDBRuntimeException();
        }
        
        return res;
    }

    @Override
    public void configure(Configuration conf) {
        
        try {
            doConfigure(conf);
            socket = new Socket(MDBCore.getInstance().getURL(), getPort());
            createStream();
            System.out.println("#SOCKET ACCEPTED - " + getId() + " - " + getPort());
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
           Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);

            //EXCEPTION 
            //LOGGER -> "#CONNECTION REFUSED - " + getId() + " - " + getPort()
        }
    }

    /**
     * Template method pattern to configure concrete subclasses
     * @param conf
     */
    protected abstract void doConfigure(Configuration conf);

    /**
     * Creates an output stream for this component
     */
    protected abstract void createStream();
}
;