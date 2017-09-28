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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.joedog.pinochle.control.Game;
import org.joedog.pinochle.view.HandSelector;

public class HandSelectAction implements ActionListener {
  private Game controller;

  public HandSelectAction (Game controller) {
    this.controller = controller;
  }
      
  public void actionPerformed (ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new HandSelector(controller); 
      }
    });
  } 
} 
