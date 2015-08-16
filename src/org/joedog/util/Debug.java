package org.joedog.util;

public final class Debug {
   
  public final static void print(String s) {
    if (Debug.debug() == true) {
      System.out.println(s);
    }
  }

  public final static void trace(String s) {
    if (Debug.debug() == true) {
      System.out.println(s);
      new IllegalStateException().printStackTrace();
    }
  }

  public static boolean debug() {
    String debug = (String)System.getProperty("joedog.debug");
    if (debug != null && debug.equals("true")) {
      return true;
    }
    return false;
  }
}
