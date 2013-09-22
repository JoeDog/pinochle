package org.joedog.pinochle.view;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;
import javax.swing.SwingConstants;
//import javax.swing.border.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;

import org.joedog.pinochle.controller.GameController;
import org.joedog.pinochle.game.Pinochle;
import org.joedog.pinochle.model.Score;
import org.joedog.pinochle.model.ScoreTableModel;

public class ScorePad extends JPanel implements View, TableModelListener {
  private JTable          table = null;
  private JScrollPane     panel = null;
  private ScoreTableModel model = null;
  private GameController  controller = null;

  public ScorePad(GameController controller) {
    this.controller = controller;
    this.setBackground(new Color(239,236,157));
    this.setLayout(new BorderLayout());
    this.display();
  }

  public void setMeldScore(Score s) {
    System.out.println("Setting the meld score....");
    model.setValueAt(s.getA(), 0, 0); 
    model.setValueAt(s.getB(), 0, 1); 
  }

  public void resetScore() {
    this.model.resetScore();
  }

  private void display() {
    this.model = new ScoreTableModel(
      controller.getName(Pinochle.NORTH),
      controller.getName(Pinochle.SOUTH),
      controller.getName(Pinochle.EAST),
      controller.getName(Pinochle.WEST)
    );
    this.model.addTableModelListener(this);
    if (this.table == null) {
      this.table = new JTable(model);
      table.setBackground(new Color(239,236,157));
      table.setGridColor(new Color(123,145,83));
      //JTableHeader header = table.getTableHeader();
      //header.setBackground(new Color(239,236,157));
      final TableCellRenderer tcrOs = table.getTableHeader().getDefaultRenderer();
      table.getTableHeader().setDefaultRenderer(new TableCellRenderer() {
        { 
          setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          JLabel lbl = (JLabel) tcrOs.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column
          );
          lbl.setForeground(Color.black);
          lbl.setBorder(BorderFactory.createCompoundBorder(
            lbl.getBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0))
          );
          lbl.setHorizontalAlignment(SwingConstants.RIGHT);
          if (isSelected) {
            lbl.setForeground(new Color(123, 145,83));
            lbl.setBackground(Color.yellow);
          } else {
            lbl.setForeground(Color.black);
            lbl.setBackground(new Color(239,236,157));
          }
          return lbl;
        }
      });
      table.getTableHeader().setReorderingAllowed(false);
      table.getTableHeader().setResizingAllowed(false);
      table.getTableHeader().setOpaque(true);
    }
    TableColumn nameColumn = table.getColumnModel().getColumn(0);
    if (this.panel == null) {
      panel = new JScrollPane(table);
    }
    panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    fitToContentWidth(table, 0);
    fitToContentWidth(table, 1);
    this.add(panel, BorderLayout.CENTER);
  }

  public static void fitToContentWidth(final JTable table, final int column) {
    int width = 0;
    for (int row = 0; row < table.getRowCount(); row++) {
      final Object cellValue = table.getValueAt(row, column);
      final TableCellRenderer renderer = table.getCellRenderer(row, column);
      final Component comp = renderer.getTableCellRendererComponent(
        table, cellValue, false, false, row, column
      );
      width = Math.max(width, comp.getPreferredSize().width);
    }
    final TableColumn tc = table.getColumn(table.getColumnName(column));
    width += table.getIntercellSpacing().width * 2;
    tc.setPreferredWidth(width);
    tc.setMinWidth(width);
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
    if (e.getPropertyName().equals(controller.TRUMP)) {
    }
  }

  public void tableChanged(TableModelEvent e) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      int col = e.getColumn();
      if (col == 0) {
        String name = ((String)table.getValueAt(row, col));
        table.setValueAt(name, row, col);
      }
    }
  }

  class TextFieldEditor extends DefaultCellEditor {
    public TextFieldEditor() {
      super(new JTextField());
      this.setClickCountToStart(1);
    }

    public Component
    getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
      JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, col);
      editor.setText("");
      return editor;
    }
  }
}
