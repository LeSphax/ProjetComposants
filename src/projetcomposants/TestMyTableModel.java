/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package projetcomposants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author root
 */
public class TestMyTableModel extends JPanel {
    MyTableModel model;
    static Kiviatt kiviatt;
    String titres[] = {"Critere", "Valeur", "Vmin", "Vmax"};
    Object donnees[][] = {
        {"c1", "10", "0", "50"},
        {"c2", "20", "0", "50"},
        {"c3", "30", "0", "50"},
        {"c4", "50", "0", "50"}
    };
    static Kiviatt defaultKiviatt;
    
    TestMyTableModel(){        
        super(new GridLayout(1, 0));
        model = new MyTableModel(donnees, titres);
        JTable table = new JTable(model);
        kiviatt = new Kiviatt(model);        
        defaultKiviatt = new Kiviatt();        
        defaultKiviatt.setModel(model);
        
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(scrollPane);        
    }
    
    public static void main(String[] args) {
        
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        JFrame frame = new JFrame("Test JTable MyTableModel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JFrame frame2 = new JFrame("Test Kiviatt MyTableModel");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        //Create and set up the content pane.
        TestMyTableModel newContentPane = new TestMyTableModel();
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);
        
        
        kiviatt.setOpaque(true); 
        frame2.setContentPane(kiviatt);
        
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        frame2.pack();
        frame2.setVisible(true);
        
        // Kiviatt with Default model
        JFrame frame3 = new JFrame("Test Kiviatt DefaultModel");
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        defaultKiviatt.setOpaque(true);
        frame3.setContentPane(defaultKiviatt);
        
        frame3.pack();
        frame3.setVisible(true);
    }
}
