package org.joedog.pinochle.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.DefaultListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.joedog.pinochle.control.Game;


public class HandSelector extends JPanel {
  private Game controller = null;
  private static final int CHECK_COL = 0;
  private static final Object[][] DATA = {
    {
      Boolean.FALSE, 
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"
    }, 
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    },
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    }, 
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    },
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    }, 
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    },
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: QH JH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH AH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    }, 
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: AH QH JH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: 10H KH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    },
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: AH QH JH AC AC KC JC 9C QD JD AS QS<br />"+
      "south: 10H KH QH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"

    }, 
    {
      Boolean.FALSE,
      "<html>north: 10H KH 9H 9H 10C QC JC KD KD 9D AS KS<br />"+
      "east: AH QH JH 9C AD JD 9D 10S KS JS 9S 9S<br />"+
      "south: 10H KH QH AC AC KC JC 9C QD JD AS QS<br />"+
      "west: AH JH 10C KC QC AD 10D 10D QD 10S QS JS</html>"
    }
  };
  private static final String[] COLUMNS = {"Select", "Hands"};
  private DataModel dataModel = new DataModel(DATA, COLUMNS);
  private JTable table = new JTable(dataModel);
  private DefaultListSelectionModel selectionModel;

  public HandSelector(Game controller) {
    super(new BorderLayout());
    this.controller = controller;
    this.add(new JScrollPane(table));
    this.add(new ControlPanel(), BorderLayout.SOUTH);
    for (int i = 0; i < COLUMNS.length; i++) {
      TableColumn col = table.getColumnModel().getColumn(i);
      col.setCellRenderer(new Renderer());
      if (i == 0) col.setMaxWidth(64);
    }
    table.setPreferredScrollableViewportSize(new Dimension(450, 195));
    selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
    table.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        JTable  target = (JTable)e.getSource();
        int     row    = target.getSelectedRow();
        int     col    = target.getSelectedColumn();
        boolean val    = (boolean)dataModel.getValueAt(row, CHECK_COL);
        dataModel.setValueAt(!val, row, CHECK_COL);
      }
    });
    JFrame frame = new JFrame("Hand Selector");
    frame.add(this);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

  }

  private class DataModel extends DefaultTableModel {
    public DataModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex == CHECK_COL) {
        return getValueAt(0, CHECK_COL).getClass();
      }
      return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return column == CHECK_COL;
    }
  }

  private class ControlPanel extends JPanel {
    public ControlPanel() {
      JLabel help = new JLabel("<html>Select individual hands by clicking the row.<br />Select every hand by pressing the All button.</html>");
      help.setOpaque(true);
      help.setFont(new Font("Dialog", Font.PLAIN, 10));
      this.add(help);
      this.add(new JLabel("  "));
      this.add(new JLabel("Selection:"));
      this.add(new JButton(new SelectionAction("Clear", false)));
      this.add(new JButton(new SelectionAction("All",   true)));
      this.add(new JButton("Done"));
    }
  }
  
  private class SelectionAction extends AbstractAction {
    boolean value;

    public SelectionAction(String name, boolean value) {
      super(name);
      this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < dataModel.getRowCount(); i++) {
        dataModel.setValueAt(value, i, CHECK_COL);
      }
    }
  }

  /*private static void createAndShowUI() {
    JFrame frame = new JFrame("Hand Selector");
    frame.add(new HandSelector());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }*/
}
