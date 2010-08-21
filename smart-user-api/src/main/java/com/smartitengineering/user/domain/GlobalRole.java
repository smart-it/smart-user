/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

/**
 *
 * @author modhu7
 */
public enum GlobalRole {

  ROLE_ADMIN("role_admin"),
  ROLE_READ("role_read"),
  ROLE_CREATE("role_create"),
  ROLE_UPDATE("role_update"),
  ROLE_DELETE("role_delete");

  private String name;

  GlobalRole(String name){
    this.name=name;
  }
}
