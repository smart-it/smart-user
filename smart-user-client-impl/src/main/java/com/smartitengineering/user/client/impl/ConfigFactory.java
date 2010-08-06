/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;

/**
 *
 * @author russel
 */
@Aggregator(contextName = "userRestClientContext")
public final class ConfigFactory {

    @InjectableField
    private ConnectionConfig connectionConfig;
    private static ConfigFactory configFactory;

    public static ConfigFactory getInstance() {
        if (configFactory == null) {
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
