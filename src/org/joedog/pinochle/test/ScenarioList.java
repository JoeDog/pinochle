package org.joedog.pinochle.test;
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

import java.util.Iterator;

public class ScenarioList<Type> implements Iterable<Type> {
  private Type[] arrayList;
  private int    currentSize;

  public ScenarioList(Type[] newArray) {
    this.arrayList = newArray;
    this.currentSize = arrayList.length;
  }

  @Override
  public Iterator<Type> iterator() {
    Iterator<Type> it = new Iterator<Type>() {
      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < currentSize && arrayList[currentIndex] != null;
      }

      @Override
      public Type next() {
        return arrayList[currentIndex++];
      }

      @Override
      public void remove() {
        // TODO Auto-generated method stub
      }
    };
    return it;
  }
}
