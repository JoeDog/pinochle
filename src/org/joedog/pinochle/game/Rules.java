package org.joedog.pinochle.game;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.util.*;

public class Rules {
  // We want a controller to set status
  private GameController controller;

  public Rules(GameController controller) { 
    this.controller = controller;
  }

  public boolean isLegitPlay(Trick trick, Hand hand, Card card) {
    int  suit  = trick.getLeadingSuit();
    int  trump = trick.getTrump();
    Card tops  = trick.getWinningCard(); 
    Card tmp   = null;

    /** 
     * If the trick is empty, we can 
     * play anything we please...
     */
    if (trick.isEmpty()) { 
      Debug.print("Trick is empty; play what you want...");
      return true;
    }

    if (suit == trump) {
      // then we must 1.) follow and 2.) beat it
      if (card.getSuit() == trump && card.getRank() > tops.getRank()) {
        // we're too legit to quit
        return true;
      }
      if (card.getSuit() == trump && card.getRank() <= tops.getRank()) {
        // we're OK as long as we don't have a higher card...
        tmp = hand.getHighest(card.getSuit());
        if (tmp != null && tmp.getRank() > tops.getRank()) {
          controller.setStatus("CHEATER!! You must beat the "+tops.toString());
          return false; // CHEATER!! 
        } else {
          return true;
        }
      }
    }
    if (card.getSuit() == suit) {
      if (controller.getIntProperty("TopVariation") == 0) {
        if (tops != null && (card.getRank() > tops.getRank() || tops.getRank() == Pinochle.ACE)) {
          return true;
        } else if (hand.canTop(tops))  {
          controller.setStatus("CHEATER!! You must beat the "+tops.toString());
          return false;
        } 
      } 
      // it doesn't matter which rank we played....
      return true;
    } else {
      if (card.getSuit() == trump) {
        if (hand.contains(suit) > 0) {
          controller.setStatus("CHEATER!! You must follow suit by playing "+Pinochle.suitname(suit));
          return false;
        }
        if (tops != null && (card.getRank() > tops.getRank() || tops.getRank() == Pinochle.ACE)) {
          return true;
        } else if (hand.canTop(tops))  {
          controller.setStatus("CHEATER!! You must beat the "+tops.toString());
          return false;
        } 
      } else if (card.getSuit() != suit) {
        if (hand.contains(suit) > 0) {
          controller.setStatus("CHEATER!! You must follow suit ("+Pinochle.suitname(suit)+")");
          return false;
        }
        if (hand.contains(trump) > 0) {
          controller.setStatus("CHEATER!! In this case, you must play trump ("+Pinochle.suitname(trump)+")");
          return false;
        } 
      }
    }
    return true;
  }
}
