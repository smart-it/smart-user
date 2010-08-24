/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.service.BasicIdentityService;

/**
 *
 * @author russel
 */
public class BasicIdentityServiceImpl extends AbstractCommonDaoImpl<BasicIdentity> implements BasicIdentityService{

  public BasicIdentityServiceImpl() {
    setEntityClass(BasicIdentity.class);
  }


  @Override
  public Integer count(String nationalID) {
    return (Integer) super.getOther(QueryParameterFactory.getElementCountParam(
        "nationalID"), QueryParameterFactory.getStringLikePropertyParam(
        "nationalID", nationalID,
        MatchMode.EXACT));
  }

  @Override
  public Integer count(Integer id, String nationalID) {
    return (Integer) super.getOther(QueryParameterFactory.getElementCountParam("nationalID"), QueryParameterFactory.
        getConjunctionParam(QueryParameterFactory.getNotEqualPropertyParam("id", id), QueryParameterFactory.
        getStringLikePropertyParam("nationalID", nationalID, MatchMode.EXACT)));
  }
}
