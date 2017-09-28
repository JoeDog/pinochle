package org.joedog.pinochle.view;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
 
public class Renderer extends DefaultTableCellRenderer{
 
  @Override
  public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected, 
    boolean hasFocus, int row, int column) {

    if (column == 0) {
      JCheckBox cb = new JCheckBox(""); 
      if (isSelected) {
        cb.setBackground(UIManager.getColor("Table.selectionBackground"));
      } else {
        cb.setBackground(UIManager.getColor("Table.background"));
      }
      cb.setHorizontalAlignment(SwingConstants.CENTER);
      cb.setSelected((value != null && ((Boolean)value).booleanValue()));
      return cb;
    } else {
      JLabel label = new JLabel();
      table.setRowHeight(64);
  
      label.setOpaque(true);
      label.setFont(new Font("Dialog", Font.PLAIN, 12));
      if (value != null && value instanceof String) {
        label.setText(value.toString());
      }
      label.setHorizontalAlignment(SwingConstants.CENTER);
      if (isSelected) {
        label.setBackground(UIManager.getColor("Table.selectionBackground"));
      } else {
        label.setBackground(UIManager.getColor("Table.background"));
      }
      return label;
    }
  }
}

