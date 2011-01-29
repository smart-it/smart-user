package com.smartitengineering.user.service.impl.hbase;

import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class UniqueKeyTest extends TestCase {

  public void testKeyParse() {
    UniqueKey key = new UniqueKey();
    key.setKey("imran@smartitengineering.com");
    key.setObject(KeyableObject.PERSON);
    System.out.println(key.toString());
    UniqueKey revKey = new UniqueKey();
    try {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream os = new DataOutputStream(byteArrayOutputStream);
      key.writeExternal(os);
      DataInputStream is = new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
      revKey.readExternal(is);
    }
    catch (Exception ex) {
      fail(ex.getMessage());
    }
    System.out.println(revKey.toString());
    assertEquals(key, revKey);
  }
}
