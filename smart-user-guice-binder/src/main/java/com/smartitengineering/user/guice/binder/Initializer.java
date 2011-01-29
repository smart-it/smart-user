/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.guice.binder;

import com.smartitengineering.util.bean.guice.GuiceUtil;

/**
 *
 * @author russel
 */
public class Initializer {

  private Initializer() {
  }

  public static void init() {
    GuiceUtil.getInstance("com/smartitengineering/user/binder/guice/api-modules.properties").register();
  }
}
