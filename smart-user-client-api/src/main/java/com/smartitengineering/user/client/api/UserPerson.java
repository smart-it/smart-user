/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import java.util.Date;

/**
 *
 * @author modhu7
 */
public interface UserPerson {

  public User getUser();

  public Person getPerson();

  public Date getLastModifiedDate();

  public Date getCreationDate();

  public void setUser(User user);

  public void setPerson(Person person);
}
