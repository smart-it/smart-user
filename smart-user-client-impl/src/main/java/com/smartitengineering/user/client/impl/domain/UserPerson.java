/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author russel
 */
public class UserPerson extends AbstractClientDomain<Long> implements com.smartitengineering.user.client.api.UserPerson {

  private com.smartitengineering.user.client.api.User user;
  private com.smartitengineering.user.client.api.Person person;
  private Date lastModifiedDate;

  @Override
  public com.smartitengineering.user.client.api.User getUser() {
    return user;
  }

  @Override
  public com.smartitengineering.user.client.api.Person getPerson() {
    return person;
  }

  @Override
  @JsonDeserialize(as = Person.class)
  public void setPerson(com.smartitengineering.user.client.api.Person person) {
    if (person == null) {
      return;
    }
    this.person = person;
  }

  @Override
  @JsonDeserialize(as = User.class)
  public void setUser(com.smartitengineering.user.client.api.User user) {
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
