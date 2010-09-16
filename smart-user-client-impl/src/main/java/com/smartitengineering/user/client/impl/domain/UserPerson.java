/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author russel
 */
public class UserPerson extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.UserPerson {

  private User user;
  private Person person;

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    if(person==null)
      return;
    this.person = person;
  }

  public void setUser(User user) {
    if(user==null)
      return;
    this.user = user;
  }

}
