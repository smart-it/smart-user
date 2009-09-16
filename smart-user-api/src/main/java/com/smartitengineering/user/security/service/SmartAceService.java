/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.service;

import com.smartitengineering.user.filter.SmartAceFilter;
import com.smartitengineering.user.security.domain.SmartAce;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface SmartAceService {

    public void create(SmartAce ace);

    public void update(SmartAce ace);

    public void delete(SmartAce ace);

    public Collection<SmartAce> search(SmartAceFilter filter);
    
}
