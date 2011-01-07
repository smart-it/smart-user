/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author modhu7
 */
public class UserPerson extends AbstractGenericPersistentDTO<UserPerson, Long, Integer> {

  User user;
  Person person;
  private Date lastModifiedDate;
  private Date creationDate;

  @JsonIgnore
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Person getPerson() {
    if (person == null) {
      person = new Person();
    }
    return person;
  }

  public void setPerson(Person person) {
    if (person == null) {
      return;
    }
    this.person = person;
  }

  public User getUser() {
    if (user == null) {
      user = new User();
    }
    return user;
  }

  public void setUser(User user) {
    if (user == null) {
      return;
    }
    this.user = user;
  }

  @Override
  @JsonIgnore
  public boolean isValid() {
    return user.isValid();
  }

  @JsonIgnore
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}
