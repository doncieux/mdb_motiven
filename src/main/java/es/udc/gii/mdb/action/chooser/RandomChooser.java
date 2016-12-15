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
package es.udc.gii.mdb.action.chooser;

import es.udc.gii.mdb.action.Action;
import es.udc.gii.mdb.action.strategy.PlainStrategy;
import es.udc.gii.mdb.action.strategy.Strategy;
import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.robot.Actuator;
import es.udc.gii.mdb.robot.ActuatorMap;
import es.udc.gii.mdb.robot.Component;
import es.udc.gii.mdb.util.MDBRandom;
import org.apache.commons.configuration.Configuration;

/**
 *
 * Simple {@link StrategyChooser}, that selects randomly a {@link Strategy}
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */


public class RandomChooser implements StrategyChooser {
    
    //Tags for Configuration
    public static final String CONFIG_ACTUATOR = "actuator";

    private Strategy strategy;
    private Actuator actuator;
    private String actuatorId;

    /**
     * 
     * Selects a random {@link Strategy} to be applied.
     * 
     * @param ap {@link ActionPerceptionPair} is not used in this case.
     * @return The random {@link Strategy}.
     */

    @Override
    public Strategy selectStrategy(ActionPerceptionPair ap) {
        actuator = ActuatorMap.getInstance().getComponent(actuatorId);
        double action = MDBRandom.nextDouble()*(actuator.getMaxValue()-actuator.getMinValue()) + actuator.getMinValue();
        
        strategy = new PlainStrategy(new Action(actuator.createValue(action, Component.RAW_VALUE)));

      
        return strategy;
    }


    /**
     *
     * Method to configure this chooser. In this method is where the models that
     * are going to be used are specified.
     *
     * @param conf Object containing the configuration information.
     */

    @Override
    public void configure(Configuration conf) {
        
       //TODO Multiple actuators?
       //TEST Fichero de configuracion
       actuatorId = conf.getString(CONFIG_ACTUATOR);

    }
}
