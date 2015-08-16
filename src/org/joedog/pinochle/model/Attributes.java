package org.joedog.pinochle.model;

import java.util.Properties;
import java.util.Comparator;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

public class Attributes extends Properties {
  private static final long serialVersionUID = 1L;

  public Enumeration<Object> keys() {
    Enumeration<Object> keysEnum = super.keys();
    Vector<Object> keyList = new Vector<Object>();

    while (keysEnum.hasMoreElements()) {
      keyList.add(keysEnum.nextElement());
    }

    Collections.sort(keyList, new Comparator<Object>() {
      @Override
      public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
      }
    });
    return keyList.elements();
  }
}
