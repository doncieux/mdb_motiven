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

import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 * Extension of the class {@link Component} representing an actuator of the robot
 *
 * @author GII
 */
public class Actuator extends Component {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Actuator.class);

    private DataOutputStream stream;
    private Double step;

    /**
     * Sets the value through the socket to the real actuator
     * @param val {@link ComponentValue} containing the value to be set
     */
    public void setValue(ComponentValue val) {
        try {
//            LOGGER.info("Actuator " + getId() + " write raw = " + val.getValue() + ", normalized = " + val.getNormalizedValue());
            stream.writeDouble(val.getValue());
            //            LOGGER.info("Actuation write in " + (timeFin - timeIni) + "ms");

        } catch (IOException ex) {
            //EXCEPTION
            Logger.getLogger(Actuator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    @Override
    protected void doConfigure(Configuration conf) {
        setId(conf.getString(ConfigUtilXML.ACTUATOR_ID_TAG));
        setPort(conf.getInt(ConfigUtilXML.ACTUATOR_PORT_TAG));
        setMinValue(conf.getDouble(ConfigUtilXML.ACTUATOR_MINVAL_TAG));
        setMaxValue(conf.getDouble(ConfigUtilXML.ACTUATOR_MAXVAL_TAG));
        //WHATIS ?
      //  setStep(conf.getDouble(ConfigUtilXML.ACTUATOR_STEP_TAG));
        setNormMinValue(conf.getDouble(ConfigUtilXML.ACTUATOR_NORMMINVAL_TAG));
        setNormMaxValue(conf.getDouble(ConfigUtilXML.ACTUATOR_NORMMAXVAL_TAG));
    }

    @Override
    protected void createStream() {
        try {
            stream = new DataOutputStream(getSocket().getOutputStream());
        } catch (IOException ex) {
            //EXCEPTION
            Logger.getLogger(Actuator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}