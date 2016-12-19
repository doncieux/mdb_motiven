/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.robot;

import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 * Extension of the class {@link Component} representing a sensor of the robot
 *
 * @author GII
 */
public class Sensor extends Component {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Sensor.class);

    private DataInputStream stream;

    public Sensor() {
        super();
    }

    /**
     * Gets the {@link ComponentValue} from the {@link Sensor}
     *
     * @return
     */
    public ComponentValue getValue() {

        double val = 0;
        try {
            val = stream.readDouble();
//            LOGGER.info("Sensor " + getId() + " read raw = " + val);
        } catch (IOException ex) {
            //EXCEPTION
            throw new MDBRuntimeException();
        }
        return createValue(val, RAW_VALUE);

    }

    @Override
    protected void doConfigure(Configuration conf) {
        setId(conf.getString(ConfigUtilXML.SENSOR_ID_TAG));
        setPort(conf.getInt(ConfigUtilXML.SENSOR_PORT_TAG));
        setPort(conf.getInt(ConfigUtilXML.SENSOR_PORT_TAG));
        setMinValue(conf.getDouble(ConfigUtilXML.SENSOR_MINVAL_TAG));
        setMaxValue(conf.getDouble(ConfigUtilXML.SENSOR_MAXVAL_TAG));
        setNormMinValue(conf.getDouble(ConfigUtilXML.SENSOR_NORMMINVAL_TAG));
        setNormMaxValue(conf.getDouble(ConfigUtilXML.SENSOR_NORMMAXVAL_TAG));
    }

    @Override
    protected void createStream() {
        try {
            this.stream = new DataInputStream(getSocket().getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
