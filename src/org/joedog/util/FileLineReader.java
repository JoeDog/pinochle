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
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class FileLineReader implements Iterator<String> {
  private FileReader     fr;
  private BufferedReader br   = null;
  private String         line = null;

  public FileLineReader(File file) throws IOException {
    try {
      fr   = new FileReader(file);
      br   = new BufferedReader(fr);
      line = br.readLine();
      if (line == null) {
        br.close();
	fr = null;
      }
    } catch (IOException ex)  {  
      line = null;  
      if (br != null ) try { 
        br.close(); 
      } catch( IOException ex2 ) { }  
      br = null;  
      throw ex;  
    } 
  }

  @Override
  public boolean hasNext() {
    return line != null;
  }

  @Override
  public String next() {
    String  str = line;
    try {
      if (line == null) {
        throw new NoSuchElementException( "Next line is not available" );
      } else {
        line = br.readLine();
	if (line == null && br != null) {
          br.close();
	  br = null;
        }
      }
    } catch (Exception ex) {
      System.err.println("Exception caught in FileLineIterator.next(): "+ex);
    }
    return str;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("FileLineIterator.remove() is not supported");
  }

  @Override
  protected void finalize() {
    try {
      line = null;
      if (br != null) try {
          br.close(); 
        } catch( Exception ex ) { }
          br = null;
    } finally {
      try {
        super.finalize();
      } catch (java.lang.Throwable t) {}
    }
  }
}

