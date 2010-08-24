/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

/**
 *
 * @author russel
 */
public interface BasicIdentityService {

  public Integer count(String nationalID);

  public Integer count(Integer id, String nationalID);
}
