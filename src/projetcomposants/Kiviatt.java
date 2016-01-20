/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public class Kiviatt implements TableModelListener{
    
    private TableModelEvent currentData;
    
    
    public Kiviatt(TableModel model){
        model.addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        
    }
    
}
