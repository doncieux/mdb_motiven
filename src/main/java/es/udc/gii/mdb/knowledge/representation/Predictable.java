/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.knowledge.representation;

import es.udc.gii.mdb.robot.ComponentValue;
import java.util.Collection;
import java.util.List;

/**
 *
 * Esta clase se corresponde con cualquier tipo de prediccion o seleccion 
 * que pueda realizar un modelo. 
 * 
 * Es necesario para crear los modelos de comportamientos a imagen de los 
 * modelos de mundo y de satisfaccion, ya que estos predecian solo Perception
 * y ahora necesitamos calcular Strategy's
 * @author GII
 */
public interface Predictable {

    public abstract List<ComponentValue> getValues();
    
    public abstract void addValue(ComponentValue value);

    public abstract void addValues(Collection<ComponentValue> values);
    
    public abstract ComponentValue get(int index);
}
