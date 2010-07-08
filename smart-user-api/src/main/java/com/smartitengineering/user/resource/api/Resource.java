/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.resource.api;

import java.net.URI;
import java.util.Date;

/**
 *
 * @author modhu7
 */
public interface Resource<T> {

  public boolean isCacheEnabled();

  public Date getLastModifiedDate();

  public Date getExpirationDate();

  public String getUUID();

  public URI getUri();

  public T refresh();
}
