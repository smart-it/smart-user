/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;

/**
 *
 * @author russel
 */
public class UserPerson extends AbstractClientDomain implements com.smartitengineering.user.client.api.UserPerson {

  private User user;
  private Person person;
  private Date lastModifiedDate;

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    if (person == null) {
      return;
    }
    this.person = person;
  }

  public void setUser(User user) {
    if (user == null) {
      return;
    }
    this.user = user;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    if (lastModifiedDate == null) {
      return;
    }
    this.lastModifiedDate = lastModifiedDate;
  }
}
