/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.util.List;
import javax.swing.table.AbstractTableModel;

class MyTableModel extends AbstractTableModel {

    private final boolean DEBUG = false;

    private List<Object[]> donnees;
    String titres[];

    public MyTableModel(List<Object[]> data, String titres[]) {
        this.donnees = data;
        this.titres = titres;
    }

    @Override
    public int getColumnCount() {
        return titres.length;
    }

    @Override
    public Object getValueAt(int parm1, int parm2) {
        return donnees.get(parm1)[parm2];
    }

    public void addRow(String[] row) {
        donnees.add(0, row);
        fireTableRowsInserted(0, 0);
    }

    @Override
    public int getRowCount() {
        return donnees.size();
    }

    @Override
    public String getColumnName(int col) {
        return titres[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                    + " to " + value + " (an instance of "
                    + value.getClass() + ")");
        }
        System.out.println(row + "   " + col);
        donnees.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }
}
