/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        MOUSEOVER,
        PRESSED,
    }

    private State state;

    private static final String titres[] = {"Critere", "Valeur", "Vmin", "Vmax"};
    private static final String l1[] = {"c1", "0", "0", "30"};
    private static final String l2[] = {"c2", "10", "0", "30"};
    private static final List<Object[]> donnees = Arrays.asList(
        l1,
        l2
    );
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
    public void setModel(TableModel m) {
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
        g.setColor(new Color(255,0,0,200));
        g.fillPolygon(xPointsKiviatt, yPointsKiviatt, xPointsKiviatt.length);
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
                        break;
                    case MOUSEOVER:
                        state = State.PRESSED;
                        axisTable[activeAxisIndex].setOrthogonalProjectionValue(e.getPoint());
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
                        axisTable[activeAxisIndex].setOrthogonalProjectionValue(e.getPoint());
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
                        axisTable[activeAxisIndex].setOrthogonalProjectionValue(e.getPoint());
                        setDefaultCursor();
                        activeAxisIndex = -1;
                        break;
                    default:
                        throw new AssertionError(state.name());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //System.out.println("MOVED");
                switch (state) {
                    case IDLE:
                        activeAxisIndex = -1;
                        for (int i = 0; i < axisTable.length; i++) {
                            if (axisTable[i].contains(e.getPoint())) {
                                activeAxisIndex = i;
                            }
                        }
                        if (activeAxisIndex != -1) {
                            state = State.MOUSEOVER;
                            setHandCursor();
                        }
                        break;
                    case PRESSED:
                        //impossible
                        break;
                    case MOUSEOVER:
                        activeAxisIndex = -1;
                        for (int i = 0; i < axisTable.length; i++) {
                            if (axisTable[i].contains(e.getPoint())) {
                                activeAxisIndex = i;
                            }
                        }
                        if (activeAxisIndex != -1) {
                            state = State.MOUSEOVER;
                            setHandCursor();
                        } else {
                            state = State.IDLE;
                            setDefaultCursor();
                        }
                        break;
                    default:
                        throw new AssertionError(state.name());
                }
            }

        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private void setHandCursor() {
        axisTable[0].setCursor(new Cursor(Cursor.HAND_CURSOR));
        // axisTable[axisTable.length-1].setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setDefaultCursor() {
        axisTable[0].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

}
