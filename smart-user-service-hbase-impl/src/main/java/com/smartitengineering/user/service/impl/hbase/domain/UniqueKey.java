/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.domain;

import com.smartitengineering.dao.impl.hbase.spi.Externalizable;
import com.smartitengineering.user.service.impl.hbase.dao.Utils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class UniqueKey implements Externalizable, Comparable<UniqueKey> {

  private KeyableObject object;
  private String key;
  private String orgId;
  protected transient Logger logger = LoggerFactory.getLogger(getClass());

  public KeyableObject getObject() {
    return object;
  }

  public void setObject(KeyableObject object) {
    this.object = object;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getOrgId() {
    return StringUtils.isNotBlank(orgId) ? orgId : "";
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  @Override
  public String toString() {
    return new StringBuilder(getOrgId()).append(':').append(object.name()).append(':').append(key).toString();
  }

  @Override
  public void writeExternal(DataOutput output) throws IOException {
    output.write(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(toString()));
  }

  @Override
  public void readExternal(DataInput input) throws IOException, ClassNotFoundException {
    String idString = Utils.readStringInUTF8(input);
    readIdFromString(idString);
  }

  protected void readIdFromString(String idString) throws IOException {
    if (logger.isInfoEnabled()) {
      logger.info("Trying to parse unique key id: " + idString);
    }
    if (StringUtils.isBlank(idString)) {
      throw new IOException("No content!");
    }
    String[] params = idString.split(":");
    if (params == null || params.length != 3) {
      throw new IOException("Object should have been in the format orgId?:object:key");
    }
    orgId = params[0];
    object = KeyableObject.valueOf(params[1]);
    key = params[2];
  }

  @Override
  public int compareTo(UniqueKey o) {
    if (o == null) {
      return 1;
    }
    if (equals(o)) {
      return 0;
    }
    return toString().compareTo(o.toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UniqueKey other = (UniqueKey) obj;
    if ((this.object == null) ? (other.object != null) : !this.object.equals(other.object)) {
      return false;
    }
    if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
      return false;
    }
    if ((this.getOrgId() == null) ? (other.getOrgId() != null) : !this.getOrgId().equals(other.getOrgId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + (this.object != null ? this.object.hashCode() : 0);
    hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
    hash = 97 * hash + (this.orgId != null ? this.orgId.hashCode() : 0);
    return hash;
  }

}
