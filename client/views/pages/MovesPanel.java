package views.pages;

import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import views.components.ContentPanel;

public class MovesPanel extends ContentPanel {
    
    String[] columnNames = {"#", "White", "Black"};

    JTable table;
    JScrollPane pane;

    DefaultTableModel movesList;
    int numMoves = 0;

    public MovesPanel() {

        this.setBounds(660, 120, 240, 120);

        movesList = new DefaultTableModel();

        for (String column : columnNames) {
            movesList.addColumn(column);
        }

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        table = new JTable(movesList);

        table.setRowHeight(15);

        pane = new JScrollPane(table);           
        pane.setBounds(0, 0, 240, 120);
        table.setFillsViewportHeight(true);
        
        this.add(pane);
    }

    public void addMove(String move) {
        if (numMoves % 2 == 0) {
            this.movesList.addRow(new Object[]{(numMoves/2 + 1) + ".", move, ""});
        } else {
            this.movesList.setValueAt(move, numMoves/2, 2);
        }
        
        table.getSelectionModel().setSelectionInterval(numMoves/2, numMoves/2);
        table.scrollRectToVisible(new Rectangle(table.getCellRect(numMoves/2, 0, true)));

        numMoves++;
    }
}