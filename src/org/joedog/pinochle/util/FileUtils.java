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
import java.util.zip.ZipInputStream;

public final class FileUtils {

  public final static boolean exists(String name) {
    File   file = new File(name);
    return file.exists();
  }

  public final static void mkdirs(String name) {
    File file = new File(name);
    file.mkdirs();
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
      try {
        file = new File(name);
        if (! file.exists()) {
          return false;
        }
        raf  = new RandomAccessFile(file, "r");  
        n = raf.readInt();  
      } finally {
        if (raf != null) {
          raf.close();
        } 
      }
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
      try {
        file = new File(name);
        if (! file.exists()) {
          return false;
        }
        raf  = new RandomAccessFile(file, "r");
        n    = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
      } finally {
        if (raf != null) {
          raf.close();
        } 
      }
    } catch (Throwable e) {
      e.printStackTrace(System.err);
    }
    return n == GZIPInputStream.GZIP_MAGIC;
  }

  /**
   * Returns a String array of all the lines
   * in an ASCII, gzip'd or zipped file whose
   * full path is 'name' NOTE: a zip file may
   * contain multiple files. This method will
   * only read the first file in an zip archive
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
      ZipInputStream    zis = null;
      try {
        if (FileUtils.isGZipped(name)) {
          fis = new FileInputStream(name);
          gis = new GZIPInputStream(fis);
          isr = new InputStreamReader(gis);
          br  = new BufferedReader(isr);
        } else if (FileUtils.isZipped(name)) {
          fis = new FileInputStream(name);
          zis = new ZipInputStream(fis); 
          zis.getNextEntry();
          isr = new InputStreamReader(zis);
          br  = new BufferedReader(isr);
        } else {
          fr = new FileReader(file);
          br = new BufferedReader(fr);
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
      } finally {
        br.close();
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + file.getName() + " is unreadable : " + e.toString());
    }

    String[] lines = new String[linesVector.size()];
    for (int i = 0; i < lines.length; i++) {
      lines[i] = (String) (linesVector.get(i));
    }
    return lines;
  }
 
  /**
   * Returns an int which represents the number of
   * lines in a file whose path is 'name' NOTE: This 
   * method will automagically detect ASCII, gzip'd 
   * or zipped files. 
   * <p>
   * @param  String  the path to the file
   * @return int     the number of lines in the file
   */
  public final static int countLines(String name) {
    int    num  = 0;
    File   file = new File(name);
    if (! FileUtils.exists(name)) {
      return 0;
    }

    try {
      FileReader        fr  = null;
      BufferedReader    br  = null;
      FileInputStream   fis = null;
      GZIPInputStream   gis = null;
      InputStreamReader isr = null;
      ZipInputStream    zis = null;
      try {
        if (FileUtils.isGZipped(name)) {
          fis = new FileInputStream(name);
          gis = new GZIPInputStream(fis);
          isr = new InputStreamReader(gis);
          br  = new BufferedReader(isr);
        } else if (FileUtils.isZipped(name)) {
          fis = new FileInputStream(name);
          zis = new ZipInputStream(fis);
          zis.getNextEntry();
          isr = new InputStreamReader(zis);
          br  = new BufferedReader(isr);
        } else {
          fr = new FileReader(file);
          br = new BufferedReader(fr);
        }

  
        boolean eof = false;
        while (!eof) {
          String line = br.readLine();
          if (line == null) {
            eof = true;
          } else {
            num++;
          }
        }
      } finally {
        br.close();
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + file.getName() + " is unreadable : " + e.toString());
    }
    return num;
  } 
}
