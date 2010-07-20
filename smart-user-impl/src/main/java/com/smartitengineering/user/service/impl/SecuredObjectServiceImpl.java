/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.SecuredObjectService;
import java.util.Collection;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;


/**
 *
 * @author russel
 */
public class SecuredObjectServiceImpl extends AbstractCommonDaoImpl<SecuredObject> implements SecuredObjectService{

    public SecuredObjectServiceImpl(){
        setEntityClass(SecuredObject.class);
    }

    public void save(SecuredObject securedObject){
        validateSecuredObject(securedObject);
        try{
            super.save(securedObject);
        }catch(ConstraintViolationException ex){
            ex.printPartialStackTrace(null);
        }catch(StaleStateException ex){
            ex.printStackTrace();
        }
    }

    public void update(SecuredObject securedObject){
        throw new UnsupportedOperationException();
    }

    public void delete(SecuredObject securedObject){
        validateSecuredObject(securedObject);
        try{
            super.update(securedObject);
        }catch(ConstraintViolationException ex){
            ex.printStackTrace();
        }catch(StaleStateException ex){
            ex.printStackTrace();
        }
    }

    public SecuredObject getByObjectID(String objectID){
        throw new UnsupportedOperationException();
    }

    public Collection<SecuredObject> getByOrganization(String organizationName){
        throw new UnsupportedOperationException();
    }

    public void validateSecuredObject(SecuredObject securedObject){
        if (securedObject.getId() == null) {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("objectID"), QueryParameterFactory.getStringLikePropertyParam(
                    "objectID",
                    securedObject.getObjectID()));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.OBJECT_ID.name());
            }
        } else {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("uniqueShortName"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    securedObject.getId()), QueryParameterFactory.
                    getStringLikePropertyParam(
                    "uniqueShortName", securedObject.getObjectID())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.OBJECT_ID.name());
            }

        }
    }

    public void populateSecuredObject(Privilege privilege) throws Exception{
        Integer securedObjectID = privilege.getSecuredObjectID();
        if( securedObjectID != null){
            SecuredObject securedObject = getById(securedObjectID);
            if(securedObject == null){
                throw new Exception("Secured Object not found");
            }
            privilege.setSecuredObject(securedObject);
        }
    }
}
