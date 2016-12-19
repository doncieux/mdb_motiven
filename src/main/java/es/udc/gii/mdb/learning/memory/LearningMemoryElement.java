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
package es.udc.gii.mdb.learning.memory;

import java.util.Arrays;

/**
 * Clase que representa un elemento de la memoria de aprendizaje.
 * Estos elementos están formados por una lista de parámetros y un valor double
 * que representa su utilidad, fitness o como queramos llamarle.
 * 
 * @author GII
 */
public class LearningMemoryElement {
   
    private double[] parameters;
    
    private double utility;

    public LearningMemoryElement() {
    }

    public LearningMemoryElement(double[] parameters, double utility) {
        this.parameters = parameters;
        this.utility = utility;
    }

    public double[] getParameters() {
        return parameters;
    }

    public void setParameters(double[] parameters) {
        this.parameters = parameters;
    }

    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    public void loadFromStringArray(String[] strElement) {
        
        this.parameters = new double[strElement.length - 1];
        
        int i;
        for (i = 0; i < this.parameters.length; i++) {
            this.parameters[i] = Double.parseDouble(strElement[i].trim());
        }
        this.utility = Double.parseDouble(strElement[i].trim());
        
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(this.parameters);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.utility) ^ (Double.doubleToLongBits(this.utility) >>> 32));
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
        final LearningMemoryElement other = (LearningMemoryElement) obj;
        if (!Arrays.equals(this.parameters, other.parameters)) {
            return false;
        }
        if (Double.doubleToLongBits(this.utility) != Double.doubleToLongBits(other.utility)) {
            return false;
        }
        return true;
    }
    
    
    
}
