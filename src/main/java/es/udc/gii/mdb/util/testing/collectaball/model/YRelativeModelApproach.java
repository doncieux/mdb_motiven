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
public class YRelativeModelApproach extends RepresentationApproach {
    private double avance;
    private double maxDistance;
    @Override
    public void doDecode(Iterator parameters) throws ModelException {
        //DO nothing
    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {
        double xRelative = inputs[0]*800 - 400.0;
        double yRelative = inputs[1]*600 - 300.0;      
        double motor = inputs[2] * Math.PI - Math.PI / 2.0;

        ModelsCalculations.calculateFromXYRelative(xRelative, yRelative, motor, avance);

        double distance1 = ModelsCalculations.getDistance();

        if (distance1 > maxDistance) {
            return new double[]{1.0};
        } else if (distance1 <= avance) {
            return new double[]{0.5};
        }

        double yRelative1 = ModelsCalculations.getyRelative();

        yRelative1 = (yRelative1 >= 300.0 ? 300.0 :
                (yRelative1 <= -300.0 ? -300.0 : yRelative1));
        
        yRelative1 = (yRelative1 + 300.0) / 600.0;        
        
        return new double[]{yRelative1};
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
        this.avance = configuration.getDouble("avance",25);
        this.maxDistance = configuration.getDouble("maxDistance",500);    }

}
