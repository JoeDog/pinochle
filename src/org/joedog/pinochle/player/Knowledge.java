package org.joedog.pinochle.player;

import java.util.Map;
import java.util.HashMap;
import org.joedog.pinochle.game.*;

public class Knowledge {
  private final Map<Integer, Card> melded = new HashMap<Integer, Card>();

  public Knowledge() {

  }

  public void remember(int position, Card card) {
    melded.put(new Integer(position), card);
  }
}
