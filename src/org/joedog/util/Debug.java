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

public final class Debug {
   
  public final static void print(String s) {
    if (Debug.debug() == true) {
      System.out.println(s);
    }
  }

  /**
   * Returns a stack trace without an Exception
   * <p>
   * @return String
   */
  public static String trace() {
    StringBuffer ret = new StringBuffer("Stack Trace\n");
    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    for (int i = 1; i < elements.length; i++) {
      StackTraceElement s = elements[i];
      ret.append("\tat " + s.getClassName() + "." + s.getMethodName()
        + "(" + s.getFileName() + ":" + s.getLineNumber() + ")\n");
    }
    return ret.toString();
  }

  /**
   * Returns a stack trace without an Exception
   * <p>
   * @param  String - A message for the first line of the trace
   * @return String
   */
  public static String trace(String str) {
    StringBuffer ret = new StringBuffer(str+"\n");
    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    for (int i = 1; i < elements.length; i++) {
      StackTraceElement s = elements[i];
      ret.append("\tat " + s.getClassName() + "." + s.getMethodName()
        + "(" + s.getFileName() + ":" + s.getLineNumber() + ")\n");
    }
    return ret.toString();
  }

  public static boolean debug() {
    String debug = (String)System.getProperty("joedog.debug");
    if (debug != null && debug.equals("true")) {
      return true;
    }
    return false;
  }
}
