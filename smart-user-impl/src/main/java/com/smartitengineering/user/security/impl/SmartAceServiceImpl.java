/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.SmartAceFilter;
import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.service.SmartAceService;
import com.smartitengineering.user.security.service.SmartAclService;
import com.smartitengineering.user.service.ExceptionMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;


/**
 *
 * @author modhu7
 */
public class SmartAceServiceImpl implements SmartAceService {

    private CommonReadDao<SmartAce> smartAceReadDao;
    private CommonWriteDao<SmartAce> smartAceWriteDao;
    private SmartAclService smartAclService;

    public SmartAclService getSmartAclService() {
        return smartAclService;
    }

    public void setSmartAclService(SmartAclService smartAclService) {
        this.smartAclService = smartAclService;
    }


    public CommonReadDao<SmartAce> getSmartAceReadDao() {
        return smartAceReadDao;
    }

    public void setSmartAceReadDao(CommonReadDao<SmartAce> smartAceReadDao) {
        this.smartAceReadDao = smartAceReadDao;
    }

    public CommonWriteDao<SmartAce> getSmartAceWriteDao() {
        return smartAceWriteDao;
    }

    public void setSmartAceWriteDao(CommonWriteDao<SmartAce> smartAceWriteDao) {
        this.smartAceWriteDao = smartAceWriteDao;
    }



    public void create(SmartAce ace) {
        getSmartAclService().validate(ace.getAcl());
        try {
            getSmartAceWriteDao().save(ace);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
                    UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }
    public void update(SmartAce ace) {
        getSmartAclService().validate(ace.getAcl());
        try {
            getSmartAceWriteDao().update(ace);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
                    UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }

    public void delete(SmartAce ace) {
        try {
            getSmartAceWriteDao().delete(ace);
        } catch (RuntimeException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ACE;
            throw new RuntimeException(message, e);
        }
    }

    public Collection<SmartAce> search(SmartAceFilter filter) {
        QueryParameter qp = null;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getObjectIdentity().getOid())) {
            qp = QueryParameterFactory.getEqualPropertyParam("oid",
                    filter.getObjectIdentity().getOid());
            queryParameters.add(qp);
        }
        if (!StringUtils.isEmpty(filter.getSidUsername())) {
            qp = QueryParameterFactory.getEqualPropertyParam("oid",
                    filter.getSidUsername());
            queryParameters.add(qp);
        }

        Collection<SmartAce> aces = new HashSet<SmartAce>();
        if (queryParameters.size() == 0) {
            try {
                aces = getSmartAceReadDao().getAll();
            } catch (Exception e) {
            }
        } else {
            try {
                aces = getSmartAceReadDao().getList(queryParameters);
            } catch (Exception e) {
            }
        }
        return aces;
    }
}
