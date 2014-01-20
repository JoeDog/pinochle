package org.joedog.pinochle.view;

import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;

public class TrumpIcon extends ImageIcon {
  private String path  = "org/joedog/pinochle/images/cards";

  public TrumpIcon(int suit) {
    this.setImage(this.getImageUrl(suit));
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
    Image image = Toolkit.getDefaultToolkit().getImage(url);
    if (image != null) {
      this.setImage(image);
    }
  } 
}
 
