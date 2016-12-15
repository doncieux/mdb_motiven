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

import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ActuatorMap {
    
    private static ActuatorMap instance = null;
    private Map<String, Actuator> map;

    private ActuatorMap() {
        
        map = new HashMap<String, Actuator>();

        Configuration actuators = MDBConfiguration.getInstance().subset(ConfigUtilXML.ACTUATORS_GROUP_TAG);
        List<String> actuatorClasses = actuators.getList(ConfigUtilXML.ACTUATOR_TAG + "." + ConfigUtilXML.ACTUATOR_ID_TAG);

        Actuator a;

            for (int i = 0; i < actuatorClasses.size(); i++) {
                a = new Actuator();
                a.configure(actuators.subset(ConfigUtilXML.ACTUATOR_TAG + "(" + i + ")"));
                map.put(a.getId(), a);
            }
        
    }

    /**
     * Implemented as Singleton, this method provides the instance of the class
     * @return
     */
    public static ActuatorMap getInstance() {
        if (instance == null) {
            instance = new ActuatorMap();
        }
        return instance;
    }

    /**
     * Retrieve a {@link Component} by its identifier
     * @param key Identifier of the component we want to obtain
     * @return
     */
    public Actuator getComponent(String key) {
        return map.get(key);
    }

    public Map<String, Actuator> getMap() {
        return map;
    }
    
    
    
}
