package org.joedog.pinochle.util;

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
        try {
        } catch (Exception e) {}
        int a = Setup.countLines(memzip);
        int b = FileUtils.countLines(memtxt);
        System.out.println("File: "+FileUtils.exists(memtxt)+", a: "+a+", b: "+b);
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
        zis = new ZipInputStream(is);
        zis.getNextEntry();
        isr = new InputStreamReader(zis);
        br  = new BufferedReader(isr);

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
      throw new IllegalArgumentException("File " + name + " is unreadable : " + e.toString());
    }
    return num;
  }
}
