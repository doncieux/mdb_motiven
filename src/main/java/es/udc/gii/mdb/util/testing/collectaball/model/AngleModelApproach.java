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
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.Iterator;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class AngleModelApproach extends RepresentationApproach {

    private double avance;
    private double maxDistance;

    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        //Do nothing
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {

        double angle = inputs[0] * Math.PI - Math.PI / 2.0;
        double distance = inputs[1] * maxDistance;
        double motor = inputs[2] * Math.PI - Math.PI / 2.0;

        if (distance >= maxDistance) {
            return new double[]{1.0};
        }

        ModelsCalculations.calculateFromDistanceAndAngle(distance, angle, motor ,avance);

        double angle1 = ModelsCalculations.getAngle();
        double distance1 = ModelsCalculations.getDistance();
        
        if (distance1 <= avance) {
            angle1 = 0.0;
        } else {
            if (angle1 > Math.PI / 2.0) {
                angle1 = Math.PI / 2.0;
            } else if (angle1 < -Math.PI / 2.0) {
                angle1 = Math.PI / 2.0;
            }
        }

        angle1 = (angle1 + Math.PI / 2.0) / Math.PI;

        return new double[]{angle1};
    }

    @Override
    public void save(String fileName) throws ModelException {
        //Do nothing
    }

    @Override
    public int calculateNumberOfParameters() {
        //Do nothing
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
        this.avance = configuration.getDouble("avance",25);
        this.maxDistance = configuration.getDouble("maxDistance",500);    }

}
