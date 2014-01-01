package org.joedog.pinochle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public final class FileUtils {

  public final static boolean exists(String name) {
    File   file = new File(name);
    return file.exists();
  }

  /**
   * Returns true if file is in Zip format and 
   * false if it is not. 
   * <p>
   * @param  String   the path to the file
   * @return boolean  true for Zip format; false if not
   */
  public final static boolean isZipped(String name) {
    long n    = 0x0;
    File file = null;
    RandomAccessFile raf = null; 
    try {
      file = new File(name);
      raf  = new RandomAccessFile(file, "r");  
      n = raf.readInt();  
      raf.close();  
    } catch (java.io.IOException ioe) { 
      System.err.println("Unable to read: "+name);
    }

    boolean b = (n == 0x504B0304) ? true : false;
    return b;
  }

  /**
   * Returns true if file is in GZip format and 
   * false if it is not. 
   * <p>
   * @param  String   the path to the file
   * @return boolean  true for GZip format; false if not
   */
  public final static boolean isGZipped(String name) {
    int  n    = 0;
    File file = null;
    RandomAccessFile raf = null; 
    try {
      file = new File(name);
      raf  = new RandomAccessFile(file, "r");
      n    = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
      raf.close();
    } catch (Throwable e) {
      e.printStackTrace(System.err);
    }
    return n == GZIPInputStream.GZIP_MAGIC;
  }

  /**
   * Returns a String array of all the lines
   * in file 'name'. 
   * <p>
   * @param  String   path to the the file
   * @return String[] an array containing lines in the file
   */
  public final static String[] readLines(String name) {
    File   file = new File(name);
    Vector linesVector = new Vector(); ;

    try {
      FileReader        fr  = null; 
      BufferedReader    br  = null;
      FileInputStream   fis = null; 
      GZIPInputStream   gis = null;
      InputStreamReader isr = null;

      if (FileUtils.isGZipped(name)) {
        fis = new FileInputStream(name);
        gis = new GZIPInputStream(fis);
        isr = new InputStreamReader(gis);
        br  = new BufferedReader(isr);
      } else {
        fr = new FileReader(file);
        br  = new BufferedReader(fr);
      }

      boolean eof = false;
      while (!eof) {
        String line = br.readLine();
        if (line == null) {
          eof = true;
        } else {
          linesVector.add(line);
        }
      }
      br.close();
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
