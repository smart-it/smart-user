/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.util.spring.BeanFactoryRegistrar;
import com.smartitengineering.util.spring.annotations.Aggregator;
import com.smartitengineering.util.spring.annotations.InjectableField;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName = "userRestClientContextTest")
public final class TestConfigFactory {

    @InjectableField
    private TestConnectionConfig testConnectionConfig;    
    
    private static TestConfigFactory configFactory;

    public static TestConfigFactory getInstance() {
        if (configFactory == null) {
            configFactory = new TestConfigFactory();
        }
        return configFactory;
    }

    private TestConfigFactory() {
        BeanFactoryRegistrar.aggregate(this);
    }

    public TestConnectionConfig getTestConnectionConfig() {
        return testConnectionConfig;
    }
    
}
