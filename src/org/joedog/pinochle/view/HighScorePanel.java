package org.joedog.pinochle.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.SwingUtilities;

import org.joedog.pinochle.control.*;
import org.joedog.pinochle.view.actions.*;
import org.joedog.pinochle.model.HighScoreTableModel;

public class HighScorePanel extends JFrame implements TableModelListener {
  private JLabel header = new JLabel("Highest Hands"); 
  private JTable table;
  private JPanel south;
  private JButton okay;
  private JScrollPane scroll;
  private HighScoreTableModel model;
  private Container main;
  static final long serialVersionUID = -687991492884005033L;
  
  public HighScorePanel () {
    super("Hall of Fame");
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui();
      }
    });
  }

  public void createAndShowGui() {
    main = this.getContentPane();
    main.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(280,280));
    this.model = new HighScoreTableModel();
    this.model.addTableModelListener(this);
    this.table = new JTable(model);
    TableColumn nameColumn = table.getColumnModel().getColumn(0);
    nameColumn.setCellEditor(new TextFieldEditor());
    this.add(header, BorderLayout.NORTH);
    Dimension dim   = Toolkit.getDefaultToolkit().getScreenSize();
    int w = this.getSize().width;
    int h = this.getSize().height;
    int x = (dim.width-w)/2;
    int y = (dim.height-h)/2;
    this.setLocation(x-50, y-50);
    this.setDefaultCloseOperation(model.save());
    this.scroll = new JScrollPane(table);
    this.south  = new JPanel();
    this.okay   = new JButton("Okay"); 
    this.okay.addActionListener(new HighScoreCloseAction(this, model));
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
    static final long serialVersionUID = -600008878624005033L;

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
