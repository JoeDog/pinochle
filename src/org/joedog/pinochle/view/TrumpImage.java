package org.joedog.pinochle.view;

import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TrumpImage {
  private String path         = "org/joedog/pinochle/images/cards";
  private BufferedImage image = null;

  public TrumpImage(int suit) {
    this.setImage(this.getImageUrl(suit));
  }

  public BufferedImage getImage() {
    return this.image;
  }

  public URL getImageUrl(int suit) {
    URL[] url = new URL[]{
      getClass().getClassLoader().getResource(this.path+"/hearts.png"),
      getClass().getClassLoader().getResource(this.path+"/clubs.png"),
      getClass().getClassLoader().getResource(this.path+"/diamonds.png"),
      getClass().getClassLoader().getResource(this.path+"/spades.png"),
      getClass().getClassLoader().getResource(this.path+"/spacer.png")
    };
    return url[suit];
  }

  protected void setImage(URL url) {
    try {
      this.image = ImageIO.read(url);
    } catch (Exception e) {
      System.out.println("ERROR: unable to read: "+url.toString());
    }
  }
}
