/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.mdb.util.config;

import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public interface Configurable {
 
    public void configure(Configuration configuration) throws MissingConfigurationParameterException;
    
}
