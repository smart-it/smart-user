/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.ip.provider;

import com.smartitengineering.dao.impl.hbase.spi.DomainIdInstanceProvider;

/**
 *
 * @author modhu7
 */
public class DomainIdInstanceProviderImpl implements DomainIdInstanceProvider {

  @Override
  public <IdType> IdType getInstance(Class<? extends IdType> clazz) {
    Object object = null;

    return (IdType) object;
  }
}
