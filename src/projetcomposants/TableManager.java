/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author frinar
 */
public class TableManager extends JPanel {

    private static final String titres[] = {"Critere", "Valeur", "Vmin", "Vmax"};
    private static final String l1[] = {"c1", "0", "0", "30"};
    private static final String l2[] = {"c2", "10", "0", "30"};
    private String newRow[];
    private List<Object[]> donnees = new ArrayList();
    MyTableModel model;
    JTable table;
    JOptionPane d = new JOptionPane();
    Kiviatt kiviatt;

    public static void main(String[] args) {
        TableManager ra = new TableManager();
        //ra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ra.setSize(400, 300);
        ra.setVisible(true);
    }

    public TableModel getModel() {
        return model;
    }

    public TableManager() {
        donnees.add(l1);
        donnees.add(l2);
        donnees.add(l2);
        model = new MyTableModel(donnees, titres);
        table = new JTable(model);
       // new NewJFrame1(model).setVisible(true);
        //kiviatt = new Kiviatt(model);
        /*
         JLayeredPane layeredPane = getLayeredPane();
         layeredPane.add(kiviatt,0);*/

        final JButton addButton = new JButton("Add row");
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                /*
                 newRow = new String[titres.length];
                 for(int i=0 ; i<titres.length ; i++){
                 newRow[i] = d.showInputDialog("Entrez " + titres[i]);  
                 }
                 List<Object[]> newDonnees = new ArrayList(Arrays.asList(l1,l2));
                 table.setModel(new MyTableModel(newDonnees, titres));
                 */
                newRow =  new String[]{"c2", "10", "0", "30"};
                /*for (int i = 0; i < model.getColumnCount(); i++) {
                    newRow[i] = d.showInputDialog("Entrez " + titres[i]);
                }*/
                model.addRow(newRow);

            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

}
