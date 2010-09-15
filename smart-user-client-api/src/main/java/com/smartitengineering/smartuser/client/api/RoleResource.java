/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface RoleResource extends WritableResource<Feed> {

  public Role getRole();

  public void update();
}
