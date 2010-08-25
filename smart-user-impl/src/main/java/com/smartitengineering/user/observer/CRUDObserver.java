/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.observer;

import com.smartitengineering.domain.PersistentDTO;

/**
 *
 * @author modhu7
 */
public interface CRUDObserver {

  public void update(ObserverNotification notification, PersistentDTO obejct);
}
