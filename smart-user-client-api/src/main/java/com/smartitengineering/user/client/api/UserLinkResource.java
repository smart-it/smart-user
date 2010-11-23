/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.api;

import com.smartitengineering.util.rest.client.Resource;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface UserLinkResource extends Resource<Feed> {

  public UserResource getUserResource();

}
