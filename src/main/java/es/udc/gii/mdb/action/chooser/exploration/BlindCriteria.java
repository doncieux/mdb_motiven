/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.action.chooser.exploration;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.util.config.Configurable;

/**
 *
 * @author GII
 */
public interface BlindCriteria extends Configurable {
    
    
     public double getValue(ActionPerceptionPair app);
    
}
