package org.joedog.pinochle.player;
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


