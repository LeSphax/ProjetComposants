/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package projetcomposants;

import javax.swing.table.AbstractTableModel;



class MyTableModel extends AbstractTableModel {
    private final boolean DEBUG = false;
    
    Object donnees[][];
    String titres[];
    public MyTableModel(
            Object donnees[][], String titres[]){
        this.donnees = donnees;
        this.titres = titres;
    }
    @Override
    public int getColumnCount(){
        return donnees[0].length;
    }
    @Override
    public Object getValueAt(int parm1, int parm2){
        return donnees[parm1][parm2];
    }
    @Override
    public int getRowCount() {
        return donnees.length;
    }
    @Override
    public String getColumnName(int col){
        return titres[col];
    }
    
    @Override
    public boolean isCellEditable( int row, int col) {
        if (col == Kiviatt.VALUE_COLUMN){
            return true;
        }
        return false;
    }
    
    
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                    + " to " + value + " (an instance of "
                    + value.getClass() + ")");
        }
        
        donnees[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}

