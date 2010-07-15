/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.SecuredObject;

/**
 *
 * @author russel
 */
public interface SecuredObjectService {

    public void save(SecuredObject securedObject);

    public void update(SecuredObject securedObject);

    public void delete(SecuredObject securedObject);

    public SecuredObject getByObjectID(String objectID);


}
