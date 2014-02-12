package org.joedog.pinochle.util;

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

