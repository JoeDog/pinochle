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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The purpose of a <code>FileLineIterable</code> is to supply an iterator that
 * will return each line of a text file as a String. This can be used in
 * conjunction with the new (Java1.5) 'foreach' syntax.<BR>
 * Example:
 * <PRE>
 *  for (String s : new FileLineIterable(filename))
 *    System.out.println(s);
 * </PRE>
 *
 * @author Carey Brown
 */
public class FileLineIterator implements Iterable<String> {
  private File file;
 
  public FileLineIterator (String fileName) throws IOException {
    this (new File(fileName));
  }

  public FileLineIterator(File file) throws IOException {
    if (! file.exists()) throw new FileNotFoundException("File does not exist: "+file.getPath());
    if (! file.isFile()) throw new IOException("File is not of type 'file': "+file.getPath());
      this.file = file;
    }

  public FileLineReader iterator() {
    try {
      return new FileLineReader(file);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}


