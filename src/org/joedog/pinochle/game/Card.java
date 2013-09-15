package org.joedog.pinochle.game;

import java.net.URL;
import java.util.Comparator;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Card {
  int     id       = 0;
  int     rank     = 0;
  int     suit     = 0;
  JPanel  card     = null;
  JLabel  label    = null;
  URL     face     = null;
  URL     back     = null;
  boolean faceup   = false;
  boolean selected = false;
  boolean melded   = false;
  
  public Card(int rank, int suit) {
    this.id   = -1;
    this.rank = rank;
    this.suit = suit;
    createCard();
  }

  public Card(int rank, int suit, int id) {
    this.id   = id;
    this.rank = rank;
    this.suit = suit;
    createCard();
  }

  public Card(Card card) {
    this.id   = card.id;
    this.rank = card.rank;
    this.suit = card.suit;
    System.out.println("this.id: "+this.id);
    createCard();
  }

  public int getId() {
    return this.id;
  }

  public int getRank() {
    return this.rank;
  }
  
  public int getSuit() {
    return this.suit;
  }

  public int getValue() {
    return (1+this.suit)*this.rank;
  }

  public ImageIcon getIcon() {
    return new ImageIcon(this.getImageUrl(), this.getImageUrlString());
  }

  public boolean isFaceDown() {
    return (this.faceup) ? false : true;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setFaceUp() {
    this.faceup = true;
  }

  public void setFaceDown() {
    this.faceup = false;
  }

  public boolean isFaceUp() {
    return this.faceup;
  }

  public void select(boolean select) {
    this.selected = select;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public boolean melded () {
    return this.melded;
  }

  public void meld () {
    this.melded = true;
  }

  public void turnOver() {
    this.faceup = !this.faceup;
  }

  public boolean matches (Card card) {
    if ((this.suit - card.suit == 0) && (this.rank - card.rank == 0)) {
      return true;
    } else {
      return false; 
    } 
  }

  public URL getImageUrl() {
    return (this.isFaceUp()) ? this.face : this.back;        
  }

  public String getImageUrlString() {
    return (this.isFaceUp()) ? this.face.toString() : this.back.toString();
  }


  public String toString() {
    String suit = "";
    String rank = "";

    switch (this.rank) {
      case Pinochle.ACE:
        rank = "A";
        break;
      case Pinochle.TEN:
        rank = "10";
        break;
      case Pinochle.KING:
        rank = "K";
        break;
      case Pinochle.QUEEN:
        rank = "Q";
        break;
      case Pinochle.JACK:
        rank = "J";
        break;
      case Pinochle.NINE:
        rank = "9";
        break;
    }  
    switch (this.suit) {
      case Pinochle.HEARTS:
        suit = "H";
        break;
      case Pinochle.CLUBS:
        suit = "C";
        break;
      case Pinochle.DIAMONDS:
        suit = "D";
        break;
      case Pinochle.SPADES:
        suit = "S";
        break;
    }
    return rank+suit;
  }
  
  private void createCard() {
    if (this.face == null) {
      face = getClass().getClassLoader().getResource(
        "org/joedog/pinochle/images/cards/"+
        Pinochle.suit(this.suit)+
        Pinochle.rank(this.rank)+
        ".png"
      );
    }
    if (this.back == null) {
      back = getClass().getClassLoader().getResource(
        "org/joedog/pinochle/images/cards/back.png"
      );
    }
  }

  public static Comparator<Card> getComparator(SortParameter... sortParameters) {
    return new CardComparator(sortParameters);
  }

  public enum SortParameter {
    SUIT_ASCENDING,
    SUIT_DESCENDING,
    RANK_ASCENDING,
    RANK_DESCENDING,
  }

  private static class CardComparator implements Comparator<Card> {
    private SortParameter[] parameters;

    private CardComparator(SortParameter[] parameters) {
      this.parameters = parameters;
    }

    public int compare(Card c1, Card c2) {
      int comparison;
      for (SortParameter parameter : parameters) {
        switch (parameter) {
          case RANK_ASCENDING:
            comparison = c1.rank - c2.rank;
            if (comparison != 0) return comparison;
            break;
          case RANK_DESCENDING:
            comparison = c2.rank - c1.rank;
            if (comparison != 0) return comparison;
            break;
          case SUIT_ASCENDING:
            comparison = c1.suit - c2.suit;
            if (comparison != 0) return comparison;
            break;
          case SUIT_DESCENDING:
            comparison = c2.suit - c1.suit;
            if (comparison != 0) return comparison;
            break;
        }
      }
      return 0;
    }
  } 
}
