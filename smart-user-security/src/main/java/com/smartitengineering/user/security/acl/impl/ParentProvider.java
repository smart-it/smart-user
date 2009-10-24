/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import java.util.List;

/**
 *
 * @author modhu7
 */
public interface ParentProvider {

    public List<String> getParent(String className);
}
