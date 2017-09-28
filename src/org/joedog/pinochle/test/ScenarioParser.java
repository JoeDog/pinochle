package org.joedog.pinochle.test;
/**
 * Copyright (C) 2013-2017
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.joedog.util.*;
import org.joedog.pinochle.game.Pinochle;

public class ScenarioParser {
  private boolean loaded = false;
  private String  name   = System.getProperty("pinochle.cards");
  private Map<Integer,ScenarioList<String>> map = new HashMap<Integer,ScenarioList<String>>(); 
  private List<Map<Integer, ScenarioList<String>>> array = new ArrayList<Map<Integer,ScenarioList<String>>>();
  public  enum Seats { NORTH, SOUTH, EAST, WEST, BLANK };

  public ScenarioParser() {
    this.loaded = parseLists();
  }

  public boolean loaded() {
    return this.loaded;
  }

  public List getScenarios() {
    return this.array;
  }

  public ScenarioList<String> get(int i) {
    return map.get(i);
  }

  private boolean parseLists() {
    boolean okay = false;

    try {
      int num = 0;
      int cnt = 0;
      Map<Integer,ScenarioList<String>> map = null;
      for (String line : FileUtils.fileLineIterator(this.name)) {
        if (map == null) 
          map = new HashMap<Integer,ScenarioList<String>>(); 
        Seats    seat  = Seats.BLANK;
        if (line == null) continue;
        if (line.trim().startsWith("#")) continue;
        if (line.trim().isEmpty()) continue;
        String[] parts = line.trim().split(":",-1);
        parts[0].trim();
        parts[1].trim();
        if (parts[0].length() > 3) {
          seat = Seats.valueOf(parts[0].toUpperCase());
        }
        String cards[];
        switch (seat) {
          case NORTH:
            cards = parts[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.NORTH, new ScenarioList<String>(cards));
            cnt++;
            break;
          case SOUTH:
            cards = parts[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.SOUTH, new ScenarioList<String>(cards));
            cnt++;
            break;
          case EAST:
            cards = parts[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.EAST, new ScenarioList<String>(cards));
            cnt++;
            break;
          case WEST:
            cards = parts[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.WEST, new ScenarioList<String>(cards));
            cnt++;
            break;
        }
        if (map.size() == 4) {
          array.add(num, map);
          map = null;
          num++;
          okay = true; // we have to add at least one set of four
        }
      }
    } catch (Exception e){ /*e.printStackTrace();*/ }
    return okay;
  }
}
