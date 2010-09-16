/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.resource.api;

/**
 *
 * @author modhu7
 */
@Deprecated
public interface WritableResource<T> extends Resource<T> {

  public T update();

  public void delete();

  public T refreshAndMerge();

}
