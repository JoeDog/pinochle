package org.joedog.pinochle.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class Logger {
  private static Object mutex   = new Object();
   
  public final static void remember(String name, String data) {
    try {
      synchronized(mutex) { // cos file locking is a PITA
        File file = new File(name);
        if (!file.exists()) {
          file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(data+System.getProperty("line.separator"));
        bw.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean log() {
    String log = (String)System.getProperty("pinochle.log");
    if (log != null && log.equals("true")) {
      return true;
    }
    return false;
  }
}
