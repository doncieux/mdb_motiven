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

import es.udc.gii.mdb.knowledge.representation.RepresentationApproach;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import es.udc.gii.mdb.robot.Actuator;
import es.udc.gii.mdb.robot.ActuatorMap;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.Iterator;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class SinModelApproach extends RepresentationApproach {

    private double maxDistance;
    private double avance;

    private Actuator motorActuator = null;
    
    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        //DO nothing
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {

        
                        if (motorActuator == null) {
             //TODO - Configurable
        this.motorActuator = ActuatorMap.getInstance().getComponent("motor");
        }
        double sinAngle = inputs[0] * 2.0 - 1.0;
        double cosAngle = inputs[1] * 2.0 - 1.0;
        double distance = inputs[2] * maxDistance;
        double motor = inputs[3] * Math.toRadians(motorActuator.getMaxValue() - motorActuator.getMinValue()) - 
                Math.toRadians(motorActuator.getMaxValue());

        ModelsCalculations.calculateFromDistanceSinCos(distance, sinAngle, cosAngle, motor, avance);

        double sinAngle1 = ModelsCalculations.getSinAngle();
        double distance1 = ModelsCalculations.getDistance();

        if (distance1 >= maxDistance) {
            sinAngle1 = Math.sin(Math.PI / 2.0);
        } 
//        else if (distance1 <= avance) {
//            sinAngle1 = Math.cos(Math.PI / 2.0);
//        }

        sinAngle1 = (sinAngle1 + 1.0) / 2.0;

        return new double[]{sinAngle1};

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
