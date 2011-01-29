/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;

/**
 *
 * @author imyousuf
 */
public class AutoId extends AbstractGenericPersistentDTO<AutoId, String, Long> {

  private Long value;

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  @Override
  public boolean isValid() {
    return value != null;
  }
}
