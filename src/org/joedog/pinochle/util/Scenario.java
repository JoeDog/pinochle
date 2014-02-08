package org.joedog.pinochle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;

import org.joedog.pinochle.game.Pinochle;

public class Scenario {
  private boolean scenario = false;
  private String  cardstxt = System.getProperty("pinochle.cards");
  private Map<Integer,CustomHandsList<String>> map = new HashMap<Integer,CustomHandsList<String>>(); 
  public  enum Seats { NORTH, SOUTH, EAST, WEST, BLANK };

  public CustomHands() {
    this.custom = parseHands();
  }

  public boolean hasScenario() {
    return this.scenario;
  }

  public ScenarioList<String> get(int i) {
    return map.get(i);
  }

  private boolean parseHands() {
    File file = new File(cardstxt);
    if (! file.exists()) {
      return false;
    } 
    try {
      FileInputStream fis = new FileInputStream(cardstxt);
      DataInputStream dis = new DataInputStream(fis);
      BufferedReader br = new BufferedReader(new InputStreamReader(dis));
      String line;
      while ((line = br.readLine()) != null)   {
        Seats    seat  = Seats.BLANK;
        if (line.trim().startsWith("#")) continue;
        if (line.trim().isEmpty()) continue;
        String[] array = line.trim().split(":",-1);
        array[0].trim();
        array[1].trim();
        if (array[0].length() > 3) {
          seat = Seats.valueOf(array[0].toUpperCase());
        }
        String cards[];
        switch (seat) {
          case NORTH:
            cards = array[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.NORTH, new CustomHandsList<String>(cards));
            break;
          case SOUTH:
            cards = array[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.SOUTH, new CustomHandsList<String>(cards));
            break;
          case EAST:
            cards = array[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.EAST, new CustomHandsList<String>(cards));
            break;
          case WEST:
            cards = array[1].replaceFirst("^ ", "").split(" ", -1);
            map.put(Pinochle.WEST, new CustomHandsList<String>(cards));
            break;
        }
      }
      dis.close();
    } catch (Exception e){ //Catch exception if any
      e.printStackTrace();
    }
    return ! map.isEmpty();
  }
}
