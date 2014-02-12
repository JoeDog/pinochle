package org.joedog.pinochle.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.util.*;

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
