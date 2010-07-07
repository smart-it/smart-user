/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.resource.api;

import java.util.Collection;
import java.util.Map;

/**
 * provides searching parameters
 * @author modhu7
 */
public interface Filter {

  /**
   * Method to get all the searching parameters in search filter
   * @return
   */
  public Map<String, Collection<String>> getRequestParams();

}
