package org.joedog.pinochle.game;
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

import org.joedog.util.*;
import org.joedog.pinochle.control.*;

public class Rules {
  private Game control;

  public Rules(Game control) { 
    this.control = control;
  }

  public boolean isLegitPlay(Trick trick, Hand hand, Card card) {
    if (card == null) return false;

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
          control.setMessage("CHEATER!! You must beat the "+tops.toString());
          return false; // CHEATER!! 
        } else {
          return true;
        }
      }
    }
    if (card.getSuit() == suit) {
      /**
       * This first block is for the overstick variation;
       * we must top the highest card in the trick if we can.
       */
      if (control.getModelBooleanProperty("TopVariation") == true) {
        if (tops != null && (card.getRank() > tops.getRank() || tops.getRank() == Pinochle.ACE)) {
          return true;
        } 
        if (card.getSuit() == trick.getLeadingSuit()) {
          if (hand.canTop(tops) && ! trick.containsTrump()) {
            control.setMessage("CHEATER!! You must beat the "+tops.toString());
            return false;
          } else {
            // someone trumped but we can follow suit....
            control.setMessage("You're okay. The lead was trumped but  "+tops.toString());
            return true;
          }
        } else if (hand.canTop(tops))  {
          control.setMessage("CHEATER!! You must beat the "+tops.toString());
          return false;
        } 
      }
      /**
       * This applies to slough variation; since we're not in
       * trump, we're not required to overstick.
       */
      return true;
    } else {
      if (card.getSuit() == trump) {
        if (hand.contains(suit) > 0) {
          control.setMessage("CHEATER!! You must follow suit by playing "+Pinochle.suitname(suit));
          return false;
        }
        if (tops != null && (card.getRank() > tops.getRank() || tops.getRank() == Pinochle.ACE)) {
          return true;
        } else if (hand.canTop(tops))  {
          control.setMessage("CHEATER!! You must beat the "+tops.toString());
          return false;
        } 
      } else if (card.getSuit() != suit) {
        if (hand.contains(suit) > 0) {
          control.setMessage("CHEATER!! You must follow suit ("+Pinochle.suitname(suit)+")");
          return false;
        }
        if (hand.contains(trump) > 0) {
          control.setMessage("CHEATER!! In this case, you must play trump ("+Pinochle.suitname(trump)+")");
          return false;
        } 
      }
    }
    return true;
  }
}
