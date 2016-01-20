/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLayeredPane;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public class KiviattAxis extends JLayeredPane implements IKiviattAxis {

    private static final int SIZE_INTERACTOR = 20;

    private final int axisIndex;
    private final double angle;
    private final TableModel model;
    private final int valueMax;
    private final int valueMin;

    private int value;
    private Point valuePosition;

    public KiviattAxis(int axisIndex, double angle, TableModel model) {
        this.axisIndex = axisIndex;
        this.angle = angle;
        this.model = model;
        value = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_COLUMN));
        valueMin = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_MIN_COLUMN));
        valueMax = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_MAX_COLUMN));

        initListeners();
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        int xBorder = (int) Math.round(Math.cos(Math.toRadians(angle)) * getWidth() / 2) + getWidth() / 2;
        int yBorder = (int) Math.round(Math.sin(Math.toRadians(angle)) * getWidth() / 2) + getWidth() / 2;
        g.drawLine(getWidth() / 2, getWidth() / 2, xBorder, yBorder);

        g.drawRect(valuePosition.x - SIZE_INTERACTOR / 2, valuePosition.y - SIZE_INTERACTOR / 2,
                SIZE_INTERACTOR, SIZE_INTERACTOR);
    }

    @Override
    public Point getValuePosition() {
        return valuePosition;
    }

    @Override
    public void setModelValue(int value) {

    }

    private void setValuePosition() {

        float proportion = ((float) value) / (valueMax - valueMin);
        int xPos = (int) Math.round(Math.cos(Math.toRadians(angle)) * proportion * getWidth() / 2) + getWidth() / 2;
        int yPos = (int) Math.round(Math.sin(Math.toRadians(angle)) * proportion * getWidth() / 2) + getWidth() / 2;
        valuePosition = new Point(xPos, yPos);

    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        setValuePosition();
    }

    @Override
    public boolean contains(Point p) {
        if (p.x > valuePosition.x - SIZE_INTERACTOR && p.x < valuePosition.x + SIZE_INTERACTOR) {
            if (p.y > valuePosition.y - SIZE_INTERACTOR && p.y < valuePosition.y + SIZE_INTERACTOR) {
                return true;
            }
        }
        return false;
    }

    private void initListeners() {

        MouseListener adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        addMouseListener(null);
    }

    void updateValue() {
        value = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_COLUMN));
        System.out.println("New value : " + value);
        setValuePosition();
    }

}
