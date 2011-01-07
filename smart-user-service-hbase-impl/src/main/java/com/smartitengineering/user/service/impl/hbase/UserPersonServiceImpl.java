/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase;

import com.google.inject.Inject;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.service.UserPersonService;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class UserPersonServiceImpl implements UserPersonService{

  @Inject
  private CommonWriteDao<UserPerson> userPersonWriteDao;
  @Inject
  private CommonReadDao<UserPerson, Long> userPersonReadDao;
  @Inject
  private CommonWriteDao<User> userWriteDao;
  @Inject
  private CommonReadDao<User, Long> userReadDao;
  @Inject
  private CommonWriteDao<Person> personWriteDao;
  @Inject
  private CommonReadDao<Person, Long> personReadDao;
  @Inject
  private CRUDObservable observable;

  @Override
  public void create(UserPerson userPerson) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete(UserPerson userPerson) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteByPerson(Person person) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteByUser(User user) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<UserPerson> getAllUserPerson() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<UserPerson> getByOrganization(String organizationUniqueShortName, String userName,
                                                  boolean isSmallerThan, int count) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserPerson getUserPersonByUsernameAndOrgName(String username, String orgName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<UserPerson> search(UserPersonFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void update(UserPerson userPerson) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<UserPerson> getAllByOrganization(String organizationUniqueShortName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean validate(UserPerson userPerson){    
    return false;
  }

}
