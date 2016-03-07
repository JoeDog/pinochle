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
