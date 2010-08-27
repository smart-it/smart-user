/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl;

import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.CRUDObserver;
import com.smartitengineering.user.observer.ObserverNotification;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author modhu7
 */
public class ObservableImpl implements CRUDObservable{

   Set<CRUDObserver> observers = new HashSet<CRUDObserver>();

  @Override
  public void addObserver(CRUDObserver observer) {
    if (observer != null){
      observers.add(observer);
    }
  }

  @Override
  public void removeObserver(CRUDObserver observer) {
    if (observer != null){
      observers.remove(observer);
    }
  }

  @Override
  public void notifyObserver(ObserverNotification notification, PersistentDTO object) {
    for(CRUDObserver observer: observers){
      observer.update(notification, object);
    }
  }

}
