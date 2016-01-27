/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public final class Kiviatt extends JLayeredPane implements TableModelListener, IKiviatt {

    enum State {

        IDLE,
        PRESSED,
    }

    private State state;
    
    private static final String titres[] = {"Critere", "Valeur", "Vmin", "Vmax"};
    private static final Object donnees[][] = {
        {"c1", "10", "0", "30"},
        {"c2", "20", "0", "30"},
        {"c3", "30", "0", "30"}
    };
    private static final MyTableModel DEFAULT_MODEL = new MyTableModel(donnees, titres);
    private TableModelEvent currentData;
    private TableModel model;

    private KiviattAxis[] axisTable;
    private int activeAxisIndex;
    
    public Kiviatt() {
        this(DEFAULT_MODEL);
    }

    public Kiviatt(TableModel model) {
        super();
        axisTable = new KiviattAxis[0];
        this.model = model;
        model.addTableModelListener(this);
        initAxis();
        initListeners();
        state = State.IDLE;
    }
    
    @Override
    public void setModel(TableModel m){
        this.model = m;
        model.addTableModelListener(this);
        initAxis();
        repaint();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getColumn() == VALUE_COLUMN) {
            axisTable[e.getFirstRow()].updateValue();
            this.repaint();
        }
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(2));
        int xPointsKiviatt[] = new int[axisTable.length];
        int yPointsKiviatt[] = new int[axisTable.length];

        for (int i = 0; i < axisTable.length; i++) {
            xPointsKiviatt[i] = axisTable[i].getValuePosition().x;
            yPointsKiviatt[i] = axisTable[i].getValuePosition().y;

        }
        g.drawPolygon(xPointsKiviatt, yPointsKiviatt, xPointsKiviatt.length);
        // g.fillPolygon(xPointsKiviatt, yPointsKiviatt, xPointsKiviatt.length);
    }

    public void setModelPosition(int axis, int position) {
        model.setValueAt(position, axis, VALUE_COLUMN);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_SIZE, PREFERRED_SIZE);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        for (KiviattAxis axis : axisTable) {
            axis.setBounds(0, 0, width, height);
        }
    }

    private void initAxis() {
        axisTable = new KiviattAxis[model.getRowCount()];

        for (int i = 0; i < model.getRowCount(); i++) {

            double angle = 360 / (model.getRowCount()) * i;
            axisTable[i] = new KiviattAxis(i, angle, model);
            add(axisTable[i]);
        }
    }

    private void initListeners() {

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (state) {
                    case IDLE:
                        activeAxisIndex = -1;
                        for (int i = 0; i < axisTable.length; i++){
                            if (axisTable[i].contains(e.getPoint())){
                                activeAxisIndex = i;
                            }
                        }
                        if (activeAxisIndex != -1){
                            state = State.PRESSED;
                            axisTable[activeAxisIndex].setOrthogonalValueProjection(e.getPoint());
                        }
                        break;
                    case PRESSED:
                        //impossible
                        break;
                    default:
                        throw new AssertionError(state.name());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                switch (state) {
                    case IDLE:
                        //impossible
                        break;
                    case PRESSED:
                        state = State.PRESSED;
                        axisTable[activeAxisIndex].setOrthogonalValueProjection(e.getPoint());
                        break;
                    default:
                        throw new AssertionError(state.name());

                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (state) {
                    case IDLE:
                        //impossible
                        break;
                    case PRESSED:
                        state = State.IDLE;
                        axisTable[activeAxisIndex].setOrthogonalValueProjection(e.getPoint());
                        activeAxisIndex = -1;
                        break;
                    default:
                        throw new AssertionError(state.name());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

}
