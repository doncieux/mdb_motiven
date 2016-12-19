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
package es.udc.gii.mdb.perception;

import es.udc.gii.mdb.action.strategy.Strategy;
/**
 * The class representing an ActionPerceptionPair.
 *
 * @author GII
 */
public class ActionPerceptionPair  {

    private Perception externalT, internalT, externalTn, internalTn, satisfactionTn;
    private Strategy strategy;
    
    
    public ActionPerceptionPair() {
        super();
        externalT = new Perception();
        internalT = new Perception();
        externalTn = new Perception();
        internalTn = new Perception();
        satisfactionTn = new Perception();
    }

    /**
     * Getter of the external perception on instant t
     * @return
     */
    public Perception getExternalT() {
        return externalT;
    }

    /**
     * Getter of the external perception on instant t+n
     * @return
     */
    public Perception getExternalTn() {
        return externalTn;
    }

    /**
     * Getter of the satisfaction on instant t+n
     * @return
     */
    public Perception getSatisfactionTn() {
        return satisfactionTn;
    }

    /**
     * Getter of the internal perception on instant t
     * @return
     */
    public Perception getInternalT() {
        return internalT;
    }

    /**
     * Getter of the internal perception on instant t+n
     * @return
     */
    public Perception getInternalTn() {
        return internalTn;
    }

    /**
     * Getter of the strategy associated with this action-perception pair
     * @return
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Setter of the external perception on instant t
     * @param externalT
     */
    public void setExternalT(Perception externalT) {
        this.externalT = externalT;
    }

    /**
     * Setter of external perception on instant t+n
     * @param externalTn
     */
    public void setExternalTn(Perception externalTn) {
        this.externalTn = externalTn;
    }

    /**
     * setter of satisfaction on instant t+n
     * @param satisfactionTn
     */
    public void setSatisfactionTn(Perception satisfactionTn) {
        this.satisfactionTn = satisfactionTn;
    }

    /**
     * Setter of internal perception on instant t
     * @param internalT
     */
    public void setInternalT(Perception internalT) {
        this.internalT = internalT;
    }

    /**
     * Setter of internal perception on instant t+n
     * @param internalTn
     */
    public void setInternalTn(Perception internalTn) {
        this.internalTn = internalTn;
    }

    /**
     * Setter of the strategy associated with this action-perception pair
     * @param strategy
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void clearPerceptions() {
        externalT.clear();
        internalT.clear();
        externalTn.clear();
        internalTn.clear();
        satisfactionTn.clear();
    }

    @Override
    public String toString() {
        return externalT.toString() + "\t" + internalT.toString() + "\t" + strategy.toString() + "\t" + externalTn.toString() + "\t" + internalTn.toString() + "\t" + satisfactionTn.toString();
    }
    
    
    
    


}
