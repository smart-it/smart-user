/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.Order;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author modhu7
 */
public class UserServiceImpl extends AbstractCommonDaoImpl<User> implements UserService {    

    @Override
    public void save(User user) {
        validateUser(user);
        try {
            super.save(user);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void update(User user) {
        validateUser(user);
        try {
            super.update(user);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-"
                    + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void delete(User user) {
        try {
            super.delete(user);
        } catch (Exception e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON;
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public Collection<User> search(UserFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getUsername())) {
            qp = QueryParameterFactory.getEqualPropertyParam("username",
                    filter.getUsername());
            queryParameters.add(qp);
        }
        Collection<User> users = new HashSet<User>();
        if (queryParameters.isEmpty()) {
            try {
                users = super.getAll();
            } catch (Exception e) {
            }
        } else {
            users = super.getList(queryParameters);
        }
        return users;
    }

    public Collection<User> getUsers(String userNameLike, String userName, boolean isSmallerThan, int count){

      List<QueryParameter> params = new ArrayList<QueryParameter>();

      if(StringUtils.isNotBlank(userNameLike)){
        final QueryParameter orgNameLikeParam =   QueryParameterFactory.getNestedParametersParam("username",
                                                                                               FetchMode.EAGER,
                                                                                               QueryParameterFactory.getStringLikePropertyParam("username", userNameLike));
        params.add(orgNameLikeParam);
      }
      else{
        params.add(QueryParameterFactory.getNestedParametersParam("username", FetchMode.EAGER));
      }

      if(StringUtils.isNotBlank(userName)){
        if(isSmallerThan){
          params.add(QueryParameterFactory.getLesserThanPropertyParam("username", userName));
        }else{
          params.add(QueryParameterFactory.getGreaterThanPropertyParam("username", userName));
        }
      }

      params.add(QueryParameterFactory.getMaxResultsParam(count));
      params.add(QueryParameterFactory.getOrderByParam("id", Order.DESC));
      params.add(QueryParameterFactory.getDistinctPropProjectionParam("id"));

      List<Integer> userIDs = getOtherList(params);

      if (userIDs != null && !userIDs.isEmpty()) {
      List<User> users = new ArrayList<User>(super.getByIds(userIDs));
      Collections.sort(users, new Comparator<User>(){

        @Override
        public int compare(User o1, User o2) {
          return o1.getId().compareTo(o2.getId()) * -1;
        }
      });
      if (isSmallerThan) {
                Collections.reverse(users);
            }

      return users;
    }
    else {
      return Collections.emptySet();
    }
    }

    @Override
    public Collection<User> getAllUser() {
        Collection<User> users = new HashSet<User>();
        try {
            users = super.getAll();
        } catch (Exception e) {
        }
        return users;
    }

    public Collection<User> getUserByOrganization(String organizationShortName) {
        Collection<User> users = new HashSet<User>();

        QueryParameter qp = QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT, QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationShortName));
        return super.getList(qp);
    }

    public Collection<User> getUserByOrganization(String organizationName, String userName, boolean isSmallerThan, int count){
      List<QueryParameter> params = new ArrayList<QueryParameter>();


      if(StringUtils.isNotBlank(organizationName)){
        final QueryParameter orgNameParam = QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,
                                                                                           QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationName));
        params.add(orgNameParam);
      }
      else{
        return Collections.emptyList();
      }

      if(StringUtils.isNotBlank(userName)){
        if(isSmallerThan){
          params.add(QueryParameterFactory.getLesserThanPropertyParam("username", userName));
        }else{
          params.add(QueryParameterFactory.getGreaterThanPropertyParam("username", userName));
        }
      }

      params.add(QueryParameterFactory.getMaxResultsParam(count));      
      params.add(QueryParameterFactory.getOrderByParam("username", isSmallerThan? Order.DESC : Order.ASC));
      params.add(QueryParameterFactory.getDistinctPropProjectionParam("username"));

      List<String> userNames = getOtherList(params);

      if (userNames != null && !userNames.isEmpty()) {
      List<User> users = new ArrayList<User>(getByUserNames(userNames));
      Collections.sort(users, new Comparator<User>(){

        @Override
        public int compare(User o1, User o2) {
          //return o1.getId().compareTo(o2.getId()) * -1;
          return o1.getUsername().compareTo(o2.getUsername());
        }
      });      
      return users;
    }
    else {
      return Collections.emptySet();
    }
    }

    public List<User> getByUserNames(List<String> userNames) {

    QueryParameter<String> param = QueryParameterFactory.<String>getIsInPropertyParam("username", userNames.toArray(new String[0]));

    Collection<User> result;
    try {
      result = getList(param);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      result = Collections.<User>emptyList();
    }
    return new ArrayList<User>(result);

  }

    @Override
    public User getUserByUsername(String usernameWithOrganizationName) {
        String username;
        String organizationName;
        StringTokenizer tokenizer = new StringTokenizer(usernameWithOrganizationName, "@");
        if (tokenizer.hasMoreTokens()) {
            username = tokenizer.nextToken();
        } else {
            username = "";
        }
        if (tokenizer.hasMoreTokens()) {
            organizationName = tokenizer.nextToken();
        } else {
            organizationName = "";
        }
        User user = getUserByOrganizationAndUserName(organizationName, username);
        return user;
    }

    @Override
    public void validateUser(User user) {
        if (user.getId() == null) {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("username"), QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getEqualPropertyParam("organization.id",
                    user.getOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "username", user.getUsername())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.USER_USERNAME.name());
            }
        } else {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("username"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    user.getId()), QueryParameterFactory.getEqualPropertyParam("organization.id",
                    user.getOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "username", user.getUsername())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.USER_USERNAME.name());
            }

        }
    }

    @Override
    public User getUserByOrganizationAndUserName(String organizationShortName, String userName) {
        return super.getSingle(QueryParameterFactory.getStringLikePropertyParam("username", userName),
                QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,
                QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationShortName)));
    }
}
