/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.service;

import com.smartitengineering.user.filter.SmartAclFilter;
import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.domain.SmartAcl;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface SmartAclService {

    public void create(SmartAcl acl);

    public void update(SmartAcl ack);

    public void delete(SmartAcl acl);

    public Collection<SmartAcl> search(SmartAclFilter filter);

    public Collection<SmartAce> getAceEntries(SmartAcl acl);

    public void validate(SmartAcl acl);

}
