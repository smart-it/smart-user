/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import java.util.List;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface UserGroupUsersResource extends WritableResource<Feed> {

  public List<UserGroupUserResource> getUserGroupUserResources();

  public UserGroupUserResource add(User user);
}
