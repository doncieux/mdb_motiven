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
package es.udc.gii.mdb.util.testing.collectaball.model;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.robot.Actuator;
import es.udc.gii.mdb.robot.ActuatorMap;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;


public class StateDistanceSinCosModelApproach extends RepresentationApproach {

    private double avance;
    private double maxDistance;

    private Actuator motorActuator = null;

    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        //Do nothing
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {
        //Recibe:
        //inputs[0] -> ángulo, entre 0 y 1, hay que pasarlo a -90, 90
        //inputs[1] -> distancia, entre 0 y 1, hay que pasarlo a 0, 500
        //inputs[2] -> acción, incremento del ángulo

        if (motorActuator == null) {
            //TODO - Configurable
            this.motorActuator = ActuatorMap.getInstance().getComponent("motor");
        }

        double sinAngle = inputs[0] * 2.0 - 1.0;
        double cosAngle = inputs[1] * 2.0 - 1.0;
        double distance = inputs[2] * maxDistance;
        double state = inputs[3];
        double motor = inputs[4] * Math.toRadians(motorActuator.getMaxValue() - motorActuator.getMinValue())
                - Math.toRadians(motorActuator.getMaxValue());

        ModelsCalculations.calculateFromDistanceSinCos(distance, sinAngle, cosAngle, motor, avance);

        double distance1 = ModelsCalculations.getDistance();

        if (state == 0.0 && distance1 < avance) {
            state = 1.0;
        }

        double angle1 = ModelsCalculations.getAngle();

//        if (distance1 < avance) {
//            distance1 = 0.0;
//        } else
        if (distance1 > maxDistance) {
            distance1 = maxDistance;
        }

        return new double[]{state};
    }

    @Override
    public void save(String fileName) throws ModelException {
        //Do nothing
    }

    @Override
    public int calculateNumberOfParameters() {
        return 0;
    }

    @Override
    public RepresentationConfiguration getRepresentationConfiguration() {
        //Do nothing
        return null;
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        //Do nothing
        this.avance = configuration.getDouble("avance", 25);
        this.maxDistance = configuration.getDouble("maxDistance", 500);

    }

}
