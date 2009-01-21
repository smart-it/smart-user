/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.util.spring.BeanFactoryRegistrar;
import com.smartitengineering.util.spring.annotations.Aggregator;
import com.smartitengineering.util.spring.annotations.InjectableField;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName="userRestClientContext")
public final class ConfigFactory {
    @InjectableField
    private ConnectionConfig connectionConfig;
    
    private static ConfigFactory configFactory;
    
    public static ConfigFactory getInstance() {
        if(configFactory == null) {
            configFactory = new ConfigFactory();
        }
        return configFactory;
    }
    
    private ConfigFactory() {
        BeanFactoryRegistrar.aggregate(this);
    }

    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }
}
