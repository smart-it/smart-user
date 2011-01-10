/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.DomainIdInstanceProvider;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;

/**
 *
 * @author imyousuf
 */
public class UserDomainIdInstanceProvider implements DomainIdInstanceProvider {

  @Override
  public <IdType> IdType getInstance(Class<? extends IdType> clazz) {
    final Object id;
    if (UniqueKey.class.isAssignableFrom(clazz)) {
      id = new UniqueKey();
    }
    else {
      id = new Object();
    }
    return (IdType) id;
  }
}
