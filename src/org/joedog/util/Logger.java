package org.joedog.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class Logger {
  private static Object mutex   = new Object();

  public final static void log(String data) {
    String name = (String)System.getProperty("log.file");
    if (name == null) {
      System.err.println("Logger ERROR: log file is undefined.");
      return;
    }
    Logger.log(name, data);
  }
  
  public final static void remember(String name, String data) {
    Logger.log(name, data);
  }
 
  public final static void log(String name, String data) {
    try {
      synchronized(mutex) { // cos file locking is a PITA
        File file = new File(name);
        if (!file.exists()) {
          file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        System.out.println("DATA: "+data);
        bw.write(data+System.getProperty("line.separator"));
        bw.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
