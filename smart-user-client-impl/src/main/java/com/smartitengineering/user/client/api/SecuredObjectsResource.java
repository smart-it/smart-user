/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.resource.api.LinkedResource;
import com.smartitengineering.user.resource.api.Resource;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface SecuredObjectsResource extends Resource {

  public Collection<LinkedResource<SecuredObjectResource>> getSecuredObjectResources();

  public SecuredObjectResource create(SecuredObjcet securedObjcet);

  public SecuredObjectsResource search(SecuredObjectFilter filter);
}
