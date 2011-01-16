/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 *
 * @author saumitra
 */
public class AbstractFilter {
  protected String id;
  protected String name;
  protected String sortBy;
  protected Order sortOrder;
  protected Integer count;
  protected Integer index;

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public String getName() {
    if(name == null){
      name = "";
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Order getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Order sortOrder) {
    this.sortOrder = sortOrder;
  }


  public static enum Order {

    ASC, DESC;
  }

  public static String formatDateForQuery(Date date) {
    return DateFormatUtils.ISO_DATETIME_FORMAT.format(date);
  }

}
