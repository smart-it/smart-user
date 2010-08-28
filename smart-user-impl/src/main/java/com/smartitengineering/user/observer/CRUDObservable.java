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
public interface CRUDObservable {

  public void addObserver(CRUDObserver observer);

  public void removeObserver(CRUDObserver observer);

  public void notifyObserver(ObserverNotification notification, PersistentDTO object);
}
