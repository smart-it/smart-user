/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.dao;

import com.smartitengineering.dao.common.CommonDao;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;

/**
 *
 * @author modhu7
 */
public abstract class AbstractDao<T extends PersistentDTO>
        extends AbstractCommonDaoImpl<T>
        implements CommonDao<T> {
}
