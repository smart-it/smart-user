/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl;

import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.observer.CRUDObserver;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;

/**
 *
 * @author modhu7
 */
public class DummyObservableImpl implements CRUDObservable{

  @Override
  public void addObserver(CRUDObserver observer) {
  }

  @Override
  public void removeObserver(CRUDObserver observer) {
  }

  @Override
  public void notifyObserver(ObserverNotification notification, PersistentDTO object) {
  }

}
