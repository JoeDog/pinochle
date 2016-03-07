package org.joedog.pinochle.view;
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
