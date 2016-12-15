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
 * This class provides a common access point to all of the Components present in this architecture
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 *
 * @deprecated debe desaparecer la clase cuando ya no tengamos dependencias a ella
 */
@Deprecated
public class ComponentMap {

    private static ComponentMap instance = null;
    private Map<String, Component> map;

    private ComponentMap() {
        map = new HashMap<String, Component>();

//        
//        
//        
//        Configuration sensors = MDBConfiguration.getInstance().subset(ConfigUtilXML.SENSORS_GROUP_TAG);
//        List<String> sensorClasses = sensors.getList(ConfigUtilXML.SENSOR_TAG + "." + ConfigUtilXML.SENSOR_ID_TAG);
//
//        Configuration actuators = MDBConfiguration.getInstance().subset(ConfigUtilXML.ACTUATORS_GROUP_TAG);
//        List<String> actuatorClasses = actuators.getList(ConfigUtilXML.ACTUATOR_TAG + "." + ConfigUtilXML.ACTUATOR_ID_TAG);
//
//        Sensor s;
//        Actuator a;
//
//        for (int i = 0; i < sensorClasses.size(); i++) {
//
//            s = new Sensor();
//            s.configure(sensors.subset(ConfigUtilXML.SENSOR_TAG + "(" + i + ")"));
//            map.put(s.getId(), s);
//
//        }
//
//        for (int i = 0; i < actuatorClasses.size(); i++) {
//            a = new Actuator();
//            a.configure(actuators.subset(ConfigUtilXML.ACTUATOR_TAG + "(" + i + ")"));
//            map.put(a.getId(), a);
//        }

    }

    /**
     * Implemented as Singleton, this method provides the instance of the class
     *
     * @return
     */
    public static ComponentMap getInstance() {
        if (instance == null) {
            instance = new ComponentMap();
        }
        return instance;
    }

    /**
     * Retrieve a {@link Component} by its identifier
     *
     * @param key Identifier of the component we want to obtain
     * @return
     */
    public Component getComponent(String key) {
        if (SensorMap.getInstance().getMap().containsKey(key)) {
            return SensorMap.getInstance().getMap().get(key);
        } else if (ActuatorMap.getInstance().getMap().containsKey(key)) {
            return ActuatorMap.getInstance().getMap().get(key);

        }
        return map.get(key);
    }

}
