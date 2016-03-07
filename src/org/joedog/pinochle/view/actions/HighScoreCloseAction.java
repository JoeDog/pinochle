package org.joedog.pinochle.view.actions;
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

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.model.HighScoreTableModel;

public class HighScoreCloseAction implements ActionListener {
  private JFrame frame;
  private HighScoreTableModel model;
  private Game control;
  public final static String NAME = "Okay";

  public HighScoreCloseAction (JFrame frame, Game control, HighScoreTableModel model) {
    this.frame   = frame;
    this.control = control;
    this.model   = model;
  }

  public void actionPerformed (ActionEvent e) {
    this.model.save();
    this.control.setModelProperty("ConfigX",  Integer.toString(this.frame.getX()));
    this.control.setModelProperty("ConfigY",  Integer.toString(this.frame.getY()));
    this.frame.setVisible(false); 
    this.frame.dispose();
  }
}

