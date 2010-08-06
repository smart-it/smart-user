/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

/**
 *
 * @author modhu7
 */
public interface AuthorizationService {

    public Integer authorize(String username, String organizationName, String oid, Integer permission);

}
