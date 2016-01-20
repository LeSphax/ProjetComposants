/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public final class Kiviatt extends JPanel implements TableModelListener, IKiviatt {

    private TableModelEvent currentData;
    private final TableModel model;

    private KiviattAxis[] axisTable;

    public Kiviatt(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getColumn() == TableModelEvent.ALL_COLUMNS) {
            initAxis();
        }
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        int xPointsKiviatt[] = new int[axisTable.length];
        int yPointsKiviatt[] = new int[axisTable.length];

        for (int i = 0; i < axisTable.length; i++) {
            xPointsKiviatt[i] = axisTable[i].getValuePosition().x;
            yPointsKiviatt[i] = axisTable[i].getValuePosition().y;
        }
        g.drawPolygon(xPointsKiviatt,yPointsKiviatt,xPointsKiviatt.length);
        g.fillPolygon(xPointsKiviatt,yPointsKiviatt,xPointsKiviatt.length);
    }

    public void setModelPosition(int axis, int position) {
        model.setValueAt(position, axis, VALUE_COLUMN);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        for (KiviattAxis axis : axisTable) {
            axis.setBounds(0, 0, width, height);
        }
    }

    private void initAxis() {
        axisTable = new KiviattAxis[model.getRowCount() - 1];

        //The first row is for the labels, hence the i=1;
        for (int i = 1; i < model.getRowCount(); i++) {

            double angle = 360 / (model.getRowCount() - 1) * i;
            axisTable[i - 1] = new KiviattAxis(i, angle, model);
        }
    }

}
