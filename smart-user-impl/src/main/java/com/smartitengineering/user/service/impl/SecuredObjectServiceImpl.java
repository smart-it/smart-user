/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.SecuredObjectService;
import java.util.Collection;
import java.util.HashSet;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author russel
 */
public class SecuredObjectServiceImpl extends AbstractCommonDaoImpl<SecuredObject> implements SecuredObjectService {

    public SecuredObjectServiceImpl() {
        setEntityClass(SecuredObject.class);
    }

    @Override
    public void save(SecuredObject securedObject) {
        validateSecuredObject(securedObject);
        try {
            super.save(securedObject);
        } catch (ConstraintViolationException ex) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, ex);
        } catch (StaleStateException ex) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-"
                    + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public void update(SecuredObject securedObject) {
        validateSecuredObject(securedObject);
        try {
            super.update(securedObject);
        } catch (ConstraintViolationException ex) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, ex);
        } catch (StaleStateException ex) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-"
                    + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public void delete(SecuredObject securedObject) {
        try {
            super.delete(securedObject);
        } catch (RuntimeException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ORGANIZATION;
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public Collection<SecuredObject> getByOrganization(String organizationName) {
        Collection<SecuredObject> securedObjects = new HashSet<SecuredObject>();
        QueryParameter qp = QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT, QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationName));
        return super.getList(qp);
    }

    @Override
    public SecuredObject getByOrganizationAndObjectID(String organizationName, String objectID){
        return super.getSingle(QueryParameterFactory.getStringLikePropertyParam("objectID", objectID),
               QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,
               QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationName)));

    }

    public void validateSecuredObject(SecuredObject securedObject) {
        if (securedObject.getId() == null) {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("objectID"), QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getEqualPropertyParam("organization_id",
                    securedObject.getOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "objectID", securedObject.getObjectID())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.name());
            }
        } else {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("objectID"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    securedObject.getId()), QueryParameterFactory.getEqualPropertyParam("organization_id",
                    securedObject.getOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "objectID", securedObject.getObjectID())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.name());
            }

        }
    }

    @Override
    public void populateSecuredObject(Privilege privilege) throws Exception {
        Integer securedObjectID = privilege.getSecuredObjectID();
        if (securedObjectID != null) {
            SecuredObject securedObject = getById(securedObjectID);
            if (securedObject == null) {
                throw new Exception("Secured Object not found");
            }
            privilege.setSecuredObject(securedObject);
        }
    }


}
