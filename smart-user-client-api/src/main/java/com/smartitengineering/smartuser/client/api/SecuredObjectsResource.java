/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import java.util.List;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface SecuredObjectsResource extends WritableResource<Feed> {
  
  public List<SecuredObjectResource> getSecuredObjectResources();

  public SecuredObjectResource create(SecuredObject securedObjcet);

  public SecuredObjectsResource search(SecuredObjectFilter filter);
}
