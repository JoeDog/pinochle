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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Setup {
  public final static int MEMORY = 0;

  private static Object mutex = new Object();
   
  public final static void install (int what) {
    String memzip = "/org/joedog/pinochle/images/memory.zip";
    String memtxt = System.getProperty("pinochle.memory");

    switch (what) {
      case MEMORY:
        int a = Setup.countLines(memzip);
        int b = FileUtils.countLines(memtxt);
        if (! FileUtils.exists(memtxt) || (a > b)) {
          install_memory();
        }
        break;
      default: 
        return;
    }
  }

  public final static void install_memory() {
    String memzip = "/org/joedog/pinochle/images/memory.zip";
    String memtxt = System.getProperty("pinochle.memory");
    String cfgdir = System.getProperty("pinochle.dir");

    byte[] buf = new byte[1024];
    try {
      // assume nothing....
      File dir = new File(cfgdir);
      if (! dir.exists()) {
        dir.mkdirs();
      }
  
      InputStream     is  = Setup.class.getResourceAsStream(memzip); 
      ZipInputStream zis  = new ZipInputStream(is);
      ZipEntry ze         = zis.getNextEntry();
      while (ze != null) {
        File file = new File(memtxt);
        FileOutputStream fos = new FileOutputStream(file);             

        int len;
        while ((len = zis.read(buf)) > 0) {
          fos.write(buf, 0, len);
        }
        fos.close();   
        ze = zis.getNextEntry();
      }

      zis.closeEntry();
      zis.close();
 
    } catch (IOException ex) {
      ex.printStackTrace(); 
    }
  }

  /**
   * Specialty method for counting lines inside
   * our jar file. I can't make getResource() work
   * on all my environments (cygwin, win and linux)
   */
  public final static int countLines(String name) {
    int    num  = 0;
    try {
      InputStream       is  = null;
      BufferedReader    br  = null;
      InputStreamReader isr = null;
      ZipInputStream    zis = null;
      try {
        is  =  Setup.class.getResourceAsStream(name); 
        if (is != null) {
          zis = new ZipInputStream(is);
          zis.getNextEntry();
          isr = new InputStreamReader(zis);
          br  = new BufferedReader(isr);
        } else {
          return num; // should be zero
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
        if (br != null) br.close();
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("File " + name + " is unreadable : " + e.toString());
    }
    return num;
  }
}
