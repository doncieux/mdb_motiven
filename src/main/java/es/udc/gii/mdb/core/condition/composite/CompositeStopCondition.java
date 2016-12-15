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
package es.udc.gii.mdb.core.condition.composite;

import es.udc.gii.mdb.core.condition.SimpleStopCondition;
import es.udc.gii.mdb.core.condition.StopCondition;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public abstract class CompositeStopCondition implements StopCondition {

    private static final String CONDITION_LIST_TAG = 
            CompositeStopCondition.CONDITION_TAG + "." + 
            CompositeStopCondition.CLASS_TAG;

    private static final  String CONDITION_TAG = "condition";

    private static final  String CLASS_TAG = "class";

    private List<SimpleStopCondition> stopConditions;

    public CompositeStopCondition() {
    }

    public CompositeStopCondition(List<SimpleStopCondition> stopConditions) {
        this.stopConditions = stopConditions;
    }

    public List<SimpleStopCondition> getStopConditions() {
        return stopConditions;
    }

    public void setStopConditions(List<SimpleStopCondition> stopConditions) {
        this.stopConditions = stopConditions;
    }

    public void addStopCondition(SimpleStopCondition stopCondition) {
        if (this.stopConditions == null) {
            this.stopConditions = new ArrayList<>();
        }
        this.stopConditions.add(stopCondition);
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {

        List<String> condClasses;
        this.stopConditions = new ArrayList<>();
        SimpleStopCondition simpleCondition;

        try {
            if (configuration.containsKey(CompositeStopCondition.CONDITION_LIST_TAG)) {
                condClasses = configuration.getList(CompositeStopCondition.CONDITION_LIST_TAG);

                String s, sClass;
                for (int i = 0; i < condClasses.size(); i++) {
                    s = CompositeStopCondition.CONDITION_TAG + "(" + i + ")";
                    sClass = s + "." + CompositeStopCondition.CLASS_TAG;
                    simpleCondition = (SimpleStopCondition) 
                            Class.forName(configuration.getString(sClass)).newInstance();
                    simpleCondition.configure(configuration.subset(s));
                    stopConditions.add(simpleCondition);

                }

            } else {
            //TODO - Aquí hay que lanzar una excepción porque no puede tomar valor 
                //por defecto.
            }

        } catch (ClassNotFoundException ex) {
            //TODO - Configuration Exception
            Logger.getLogger(CompositeStopCondition.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            //TODO - Configuration Exception
            Logger.getLogger(CompositeStopCondition.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            //TODO - Configuration Exception
            Logger.getLogger(CompositeStopCondition.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.stopConditions != null ? this.stopConditions.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositeStopCondition other = (CompositeStopCondition) obj;
        if (this.stopConditions != other.stopConditions && (this.stopConditions == null
                || !this.stopConditions.equals(other.stopConditions))) {
            return false;
        }
        return true;
    }

}
