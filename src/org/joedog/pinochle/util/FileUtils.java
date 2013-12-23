package org.joedog.pinochle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public final class FileUtils {

  public final static boolean exists(String name) {
    File   file = new File(name);
    return file.exists();
  }

  public final static String[] readLines(String name) {
    File   file = new File(name);
    Vector linesVector = new Vector(); ;
    try {
      FileReader fr = new FileReader(file);
      BufferedReader b = new BufferedReader(fr);
      boolean eof = false;
      while (!eof) {
        String line = b.readLine();
        if (line == null) {
          eof = true;
        } else {
          linesVector.add(line);
        }
      }
      b.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + file.getName() + " is unreadable : " + e.toString());
    }
    String[] lines = new String[linesVector.size()];
    for (int i = 0; i < lines.length; i++) {
      lines[i] = (String) (linesVector.get(i));
    }
    return lines;
  }
}
