package org.joedog.pinochle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    switch (what) {
      case MEMORY:
        install_memory();
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
      //ZipInputStream zis  = new ZipInputStream(new FileInputStream(memzip.getPath()));
  
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
}
