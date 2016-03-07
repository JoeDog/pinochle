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

  /** The full path to the user's home directory  */
  public final static String HOME = System.getProperty("user.home");

  /** The full path to the system temp directory  */
  public final static String TEMP = System.getProperty("java.io.tmpdir");

  /** The platform specific directory separator   */
  public final static String SEPARATOR = System.getProperty("file.separator");

  /**
   * Test whether or not a file or directory denoted by 
   * the parameter exists. Returns false if the input is 
   * null or if the resource cannot be located.
   * <p>
   * @ param   String  The name of the file or directory
   * @ return  boolean
   */
  public final static boolean exists(String name) {
    File file;
    if (name == null) {
      return false;
    }
    file = new File(name);
    return file.exists();
  }

  public final static boolean delete(String name) {
    File file;
    if (name == null) {
      return true; // consider it deleted!
    }
    file = new File(name);
    if (file.exists()) {
      return file.delete();
    } else {
      return true;
    }
  }

  /**
   * Creates the directory named by this abstract pathname, 
   * including any necessary but nonexistent parent directories.
   * <p>
   * @param  String  The direct pathname
   * @return boolean Returns true if successful
   */ 
  public final static boolean mkdirs(String name) {
    File file;
    if (name == null) {
      return false;
    }
    file = new File(name);
    return file.mkdirs();
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
   * Returns an Iterable iterator 
   * for (String line : FileUtils.fileLineIterator("/home/jeff/cards.txt")) {
   *   // You can handle comments and empty lines like this:
   *   if (line.trim().startsWith("#")) continue;
   *   if (line.trim().isEmpty()) continue;
   *   // Do something with it:
   *   System.out.println(line);
   * } 
   * <p>
   * @param  String            the path to the file, i.e., /home/jeff/cards.txt
   * @return FileLineIterator  implements Iterable, can be used in a foreach loop
   */
  public final static FileLineIterator fileLineIterator(String name) {
    FileLineIterator fli = null;
    try {
      fli = new FileLineIterator(name);
    } catch (IOException ioe) {}
    return fli;
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
    if (name == null || ! FileUtils.exists(name)) {
      return null;
    }
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
