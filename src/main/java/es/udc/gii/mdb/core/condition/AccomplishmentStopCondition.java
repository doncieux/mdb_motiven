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
package es.udc.gii.mdb.core.condition;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.util.config.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * This class represents a stop criteria which tests if the execution cycle
 * reach a specific number of accomplishments established in the configuration file.
 *
 * @author Grupo Integrado de Ingeniería
 * (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class AccomplishmentStopCondition implements SimpleStopCondition {

    private static final String MAX_ACCOMPLISHMENTS = "maxAccomplishments";

    /**
     * Number of Accomplishements needed to stop.
     */
    private int maxAccomplishments = 1;
    
    /**
     * Counter of the accomplishments of the current execution.
     */
    private int accomplishmentCounter = 0;

    public AccomplishmentStopCondition() {
    }

    public AccomplishmentStopCondition(int numberOfAccomplishement) {
        this.maxAccomplishments = numberOfAccomplishement;
    }

    @Override
    public void configure(Configuration configuration) {
        if (configuration.containsKey(AccomplishmentStopCondition.MAX_ACCOMPLISHMENTS)) {
            maxAccomplishments = configuration.getInt(AccomplishmentStopCondition.MAX_ACCOMPLISHMENTS);
        } else {
            ConfWarning w = new ConfWarning(
                    AccomplishmentStopCondition.MAX_ACCOMPLISHMENTS, maxAccomplishments);
            w.warn();
        }
    }

    @Override
    public boolean evaluateCondition(MDBCore core) {
        if (core.getTaskAccomplished()) {
            accomplishmentCounter++;
        }
        return accomplishmentCounter >= maxAccomplishments;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.maxAccomplishments;
        hash = 23 * hash + this.accomplishmentCounter;

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
        final AccomplishmentStopCondition other = (AccomplishmentStopCondition) obj;
        return this.maxAccomplishments == other.maxAccomplishments && this.accomplishmentCounter == other.accomplishmentCounter;
    }

}
