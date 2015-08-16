package org.joedog.pinochle.game;

import java.net.URL;
import java.awt.Graphics;
import java.util.Comparator;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import org.joedog.pinochle.player.Actor;
import org.joedog.util.RandomUtils;

public class Card extends Actor {
  int      id       = 0;
  int      y        = 0;
  int      rank     = 0;
  int      suit     = 0;
  int      position = -1;
  int      status   = 0;
  JPanel   card     = null;
  URL      face     = null;
  URL      back     = null;
  boolean  faceup   = false;
  boolean  selected = false;
  boolean  melded   = false;
  boolean  played   = false;
  boolean  counter  = false;

  /** 
   * Constructor - create a generic card of 
   * rank and suit
   * <p>
   * @param int   rank of the card
   * @param int   suit of the card
   * @return      Card
   */  
  public Card(int rank, int suit) {
    this.id   = -1;
    this.rank = rank;
    this.suit = suit;
    createCard();
  }

  /** 
   * Constructor - create a generic card of
   * rank and suit with a specific ID; since
   * a pinochle deck contains duplicates the
   * ID helps us track individual cards
   * <p>
   * @param int  rank of the card
   * @param int  suit of the card
   * @param int  ID of the card (for tracking)
   * @return     Card
   */
  public Card(int rank, int suit, int id) {
    this.id   = id;
    this.rank = rank;
    this.suit = suit;
    createCard();
  }

  /**
   * Contructor - create a Card like card
   * <p>
   * @param Card  create a new card just like this card
   * @return      Card
   */
  public Card(Card card) {
    this.id   = card.id;
    this.rank = card.rank;
    this.suit = card.suit;
    this.createCard();
    this.setLocation(card.getLocation());
  }

  public Card(int id, String s) {
    this.id = id;

    if (s.startsWith("A")) {
      this.rank = Pinochle.ACE;
    }
    if (s.startsWith("10")) {
      this.rank = Pinochle.TEN;
    }
    if (s.startsWith("K")) {
      this.rank = Pinochle.KING;
    }
    if (s.startsWith("Q")) {
      this.rank = Pinochle.QUEEN;
    }
    if (s.startsWith("J")) {
      this.rank = Pinochle.JACK;
    }
    if (s.startsWith("9")) {
      this.rank = Pinochle.NINE;
    }

    if (s.endsWith("H")) {
      this.suit = Pinochle.HEARTS;
    }
    if (s.endsWith("C")) {
      this.suit = Pinochle.CLUBS;
    }
    if (s.endsWith("D")) {
      this.suit = Pinochle.DIAMONDS;
    }
    if (s.endsWith("S")) {
      this.suit = Pinochle.SPADES;
    }
    createCard();
  }

  /**
   * Return the ID of the card as an int;
   * untracked generic cards contain ID -1
   * <p>
   * @return    int 
   */
  public int getId() {
    return this.id;
  }

  /**
   * Given a set of x,y mouse coordinates, this method 
   * determines whether or not this card has been selected.
   * <p>
   * @param  int      The x coordinate
   * @param  int      The y coordinate
   * @return boolean  true if selected, false if not 
   */
  public boolean isSelected(int x, int y) {
    if ((x >= this.getX() && x <= this.getX()+this.getWidth())  &&
        (y >= this.getY() && y <= this.getY()+this.getHeight())) {
      return true;
    }
    return false;
  }

  /**
   * Return the rank value of the card as an int
   * <p>
   * @return    int 
   * @see       org.joedog.pinochle.game.Pinochle
   */
  public int getRank() {
    return this.rank;
  }
  
  /**
   * Return the suit value of the card as an int
   * <p>
   * @return    int 
   * @see       org.joedog.pinochle.game.Pinochle
   */
  public int getSuit() {
    return this.suit;
  }

  /** 
   * Return boolean true if Card is a member of suit
   * otherwise return false.
   * <p>
   * @param int   suit that we wish to check
   * @see       org.joedog.pinochle.game.Pinochle
   */
  public boolean isa(int suit) {
    return (this.suit == suit);
  }

  /**
   * Return boolean true if a Card's rank and suit 
   * matches the parameters
   * <p>
   * @param  int     The rank we're testing
   * @param  int     The suit we're testing
   * return  boolean true if Card matches suit and rank
   */ 
  public boolean isa(int rank, int suit) {
    return (this.rank == rank && this.suit == suit);
  }

  /**
   * Return an int interpretation of this card's value
   * @param none
   * @return      (1+this.suit)*this.rank;
   */
  public int getValue() {
    return (1+this.suit)*this.rank;
  }

  /**
   * Return an ImageIcon to represent this card; 
   * a card face is automatically determined based
   * on whether the card is face down or face up
   * <p>
   * @param none
   * @return      ImageIcon
   * @see         ImageIcon
   */
  public ImageIcon getIcon() {
    return new ImageIcon(this.getImageUrl(), this.getImageUrlString());
  }

  /**
   * Return a BufferedImage which reprsets the card;
   * the face is determined automatically depending  
   * upon whether it is up or down.
   * <p>
   * @param  none
   * @return BufferedImage
   * @see    BufferedImage
   */
  public BufferedImage getImage() {
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(this.getImageUrl());
    } catch (Exception e) {

    }
    return bi;
  }

  /**
   * Returns the width of the image in pixels
   * <p>
   * @param  none
   * @return int    The width in pixels
   */
  public int getImageWidth() {
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(this.getImageUrl());
    } catch (Exception e) {

    }
    return bi.getWidth();
  }

  /**
   * Returns the height of the image in pixels
   * <p>
   * @param  none
   * @return int    The height in pixels
   */
  public int getImageHeight() {
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(this.getImageUrl());
    } catch (Exception e) {

    }
    return bi.getHeight();
  }

  /**
   * Return boolean true if the card is a counter (A,10,K) 
   * or boolean false if it is not (Q,J,9)
   * <p>
   * @param none
   * @return      boolean
   */
  public boolean isCounter() {
    return this.counter;
  }

  /**
   * Return boolean true if the card is face down 
   * or boolean false if it is face up
   * <p>
   * @param none
   * @return      boolean
   */
  public boolean isFaceDown() {
    return (this.faceup) ? false : true;
  }

  /**
   * Allows you to set a specific ID for a card
   * <p>
   * @param int   ID for the card
   * @return      void
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the clickable width of the card

  /**
   * Sets a int position value for the Card's 
   * place in a org.joedog.pinochle.game.Hand
   * <p>
   * @param int 
   * @return     void
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns an int interpretation of a Card's
   * position in a org.joedog.pinochle.game.Hand
   * <p>
   * @param none
   * @return      int
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * Places a card face up
   * <p>
   * @param none
   * @return      void
   */
  public void setFaceUp() {
    this.faceup = true;
  }

  /**
   * Places a card face down
   * <p>
   * @param none
   * @return      void
   */
  public void setFaceDown() {
    this.faceup = false;
  }

  /**
   * Returns boolean true if a card is face up
   * <p>
   * @param none
   * @return      boolean
   */
  public boolean isFaceUp() {
    return this.faceup;
  }

  /**
   * Designates a card as selected; used by methods
   * that perform operations based on Human actions 
   * <p>
   * @param  boolean
   * @return void  
   */
  public void select(boolean select) {
    this.selected = select;
  }

  /** 
   * Return boolean true if a card is selected or 
   * boolean false if it is not
   * <p>
   * @param  none
   * @return boolean
   */
  public boolean isSelected() {
    return this.selected;
  }

  /**
   * Return boolean true if a card is melded or
   * boolean false if it is not
   * <p>
   * @param  none
   * @return boolean
   */ 
  public boolean melded () {
    return this.melded;
  }

  /**
   * Set the card into state melded 
   * <p>
   * @param  none
   * @return void
   */
  public void meld () {
    this.melded = true;
  }

  public void play() {
    this.played = true;
  }

  public boolean played() {
    return this.played;
  }

  /**
   * Unset the card from state melded
   * <p>
   * @param  none
   * @return void
   */
  public void unmeld() {
    this.melded = false;
  }

  /** 
   * Turn the card over; if it was down, place it 
   * up and vice versa.
   * @param  none
   * @return void
   */
  public void turnOver() {
    this.faceup = !this.faceup;
  }

  /** 
   * Return boolean true if this matches card or
   * false if it does not; the match is determined
   * by rank and suit.
   */
  public boolean matches (Card card) {
    if ((this.suit == card.suit) && (this.rank == card.rank)) {
      return true;
    } else {
      return false; 
    } 
  }

  public boolean isPinochleMate() {
    if (this.suit == Pinochle.DIAMONDS && this.rank == Pinochle.JACK) 
      return true;
    if (this.suit == Pinochle.SPADES && this.rank == Pinochle.QUEEN) 
      return true;
    return false;
  }

  /**
   * Return a URL {@Link URL} which locates the image
   * of the card; automatically determines if it
   * should present the back or the face depending
   * on how the card is facing
   * <p>
   * @param  none
   * @return URL
   * @see    URL
   */
  public URL getImageUrl() {
    return (this.isFaceUp()) ? this.face : this.back;        
  }

  /** 
   * Return a String interpretation of the image
   * URL which represents the card; this method
   * automatically presents a face or back String
   * depending on how the card is facing
   * <p>
   * @param  none
   * @return String
   */
  public String getImageUrlString() {
    return (this.isFaceUp()) ? this.face.toString() : this.back.toString();
  }

  /** 
   * Return a String interpretation for the Card
   * <p>
   * @param  none
   * @return String
   */
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
    if (this.rank > Pinochle.QUEEN) {
      this.counter = true;
    }
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
    POSITION_ASCENDING,
    POSITION_DESCENDING
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
          case POSITION_ASCENDING:
            comparison = c1.position - c2.position;
            if (comparison != 0) return comparison;
            break;
          case POSITION_DESCENDING:
            comparison = c2.position - c1.position;
            if (comparison != 0) return comparison;
            break;
        }
      }
      return 0;
    }
  } 
}
