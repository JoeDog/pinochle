package org.joedog.pinochle.player;

import java.util.Map;
import java.util.HashMap;
import org.joedog.util.*;
import org.joedog.pinochle.game.*;

public class Knowledge {
  private String [] memory = null;
  private String    memtxt = System.getProperty("pinochle.memory");
  private static    Knowledge _instance = null;
  private static    Object mutex  = new Object();
  
  public Knowledge() {
    if (this.memory == null) {
      this.memory = FileUtils.readLines(memtxt);  
    }
  }

  public synchronized static Knowledge getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Knowledge();
        }
      }
    }
    return _instance;
  }

  public String [] getMemory() {
    if (this.memory == null) {
      this.memory = FileUtils.readLines(memtxt);  
    }
    return this.memory;
  }
}


