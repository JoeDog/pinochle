package org.joedog.pinochle.model;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

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
