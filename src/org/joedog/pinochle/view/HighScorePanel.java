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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.DefaultListCellRenderer;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.view.actions.*;
import org.joedog.pinochle.model.HighScoreTableModel;

public class HighScorePanel extends JFrame {
  private JLabel header = new JLabel("All-Time Highest Hands"); 
  private JTable table;
  private JPanel south;
  private JButton okay;
  private JScrollPane scroll;
  private Container   main;
  private HighScoreTableModel model;
  private Game      control;
  static final long serialVersionUID = -687991492884005033L;
  
  public HighScorePanel (Game control) {
    super("Hall of Fame");
    this.control = control;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui();
      }
    });
  }

  public void createAndShowGui() {
    main = this.getContentPane();
    main.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(420,380));
    this.model = new HighScoreTableModel();
    this.table = new JTable(model);
    TableColumn scoreColumn = table.getColumnModel().getColumn(1);
    scoreColumn.setCellRenderer(new AlignRightCellRenderer());
    this.add(header, BorderLayout.NORTH);
    Dimension dim   = Toolkit.getDefaultToolkit().getScreenSize();
    int w = this.getSize().width;
    int h = this.getSize().height;
    int x = control.getModelIntProperty("ConfigX");
    int y = control.getModelIntProperty("ConfigY");
    this.setLocation(x, y);
    this.setDefaultCloseOperation(model.save());
    this.scroll = new JScrollPane(table);
    this.south  = new JPanel();
    this.okay   = new JButton("Okay"); 
    this.okay.addActionListener(new HighScoreCloseAction(this, control, model));
    south.add(okay);
    main.add(scroll, BorderLayout.CENTER);
    main.add(south,  BorderLayout.SOUTH);
    JRootPane root = this.getRootPane();
    root.setDefaultButton(okay);
    this.addWindowListener(new WindowAdapter() {
      public void windowGainedFocus(WindowEvent e) {
        okay.requestFocusInWindow();
      }
    });
    this.pack();
    this.setVisible(true);
  }
 
  class AlignRightCellRenderer implements TableCellRenderer {
    DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
    DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();

    private void configureRenderer(JLabel renderer, Object value) {
      renderer.setHorizontalAlignment(JLabel.RIGHT);
      renderer.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
      tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column
      );
      configureRenderer(tableRenderer, value);
      return tableRenderer;
    }
  }
}
