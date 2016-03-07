package org.joedog.util;
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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TextUtils {

  /**
   * Tests two strings to see if they're the same.
   * <p>
   * @param  String   The first string argument
   * @param  String   The second string argument
   * @return boolean  True if they're the same, false if they're not
   */
  public static boolean equals(String a, String b) {
    return (a == null) ? b == null : a.equals(b);
  }

  /**
   * Tests a string object to determin if it's empty.
   * <p>
   * @param  String  The string to test
   * @return boolean True if empty, false if not
   */
  public boolean empty(String str) {
    return (str == null || str.trim().length() < 1);
  }

  /**
   * Parses a formated string and returns an array. The string must
   * be formatted in the following manner: {n,n} or (n,n) or [n,n,n]
   * The array's size will be determined automatically based on the 
   * number of comma separated tokens inside the brackets.
   * <p>
   * @param  String  A formated string, i.e., (23,12,12,3)
   * @return ArrayList<Integer>
   */
  public static ArrayList<Integer> toArrayList(String str) {
    int [] tmp = toArray(str);
    ArrayList<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < tmp.length; i++) {
      list.add(tmp[i]);
    }
    return list;
  }

  /**
   * Parses a formated string and returns an array. The string must
   * be formatted in the following manner: {n,n} or (n,n) or [n,n,n]
   * The array's size will be determined automatically based on the 
   * number of comma separated tokens inside the brackets.
   * <p>
   * @param  String  A formated string, i.e., (23,12,12,3)
   * @return int[]
   */
  public static int[] toArray(String str) {
    if (str == null && ! str.matches("\\(.*|\\{.*|\\[.*")) {
      System.err.println("ERROR: Format should match: {n,n} (n,n) or [n,n]");
      return null;
    }

    String[] tmp = str.split(",");
    int size     =  tmp.length;
    
    if ((str = str.replaceAll("\\s", "")).length()  < 2 * size + 1) {
      System.err.println("ERROR: Parsed length is too short: "+str.length());
      return null;
    }

    String[] tokens = (str = str.substring(1, str.length() - 1)).split(","); 
 
    int[] ret = new int[tokens.length];
    try {
      for (int i = 0; i < size; ++i) {
        ret[i] = Integer.parseInt(tokens[i]);
      }
    } catch (NumberFormatException ex) {
      return null;
    }
    return ret;
  }
}
