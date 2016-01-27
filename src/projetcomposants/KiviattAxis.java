/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public class KiviattAxis extends JComponent {

    private static final int SIZE_INTERACTOR = 10;
    private static final int BORDER_OFFSET = 30;
    private static final int SIZE_MARKS = 5;
    private static final float AXIS_RATIO_MINIMUM = 0.1f;

    private final int axisIndex;
    private final double angle;
    private final TableModel model;
    private final int valueMax;
    private final int valueMin;

    private Label label;
    private int value;
    private Point valuePosition;

    enum State {

        IDLE,
        PRESSED,
    }

    private State state;

    public KiviattAxis(int axisIndex, double angle, TableModel model) {
        this.axisIndex = axisIndex;
        this.angle = angle;
        this.model = model;
        value = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_COLUMN));
        valueMin = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_MIN_COLUMN));
        valueMax = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_MAX_COLUMN));
        state = State.IDLE;
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);

        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1));
        int xBorder = (int) Math.round(Math.cos(Math.toRadians(angle)) * getAxisSize()) + getCenter().x;
        int yBorder = (int) Math.round(Math.sin(Math.toRadians(angle)) * getAxisSize()) + getCenter().y;
        g.drawLine(getCenter().x, getCenter().y, xBorder, yBorder);

        Point[][] marksPositions = getMarksPosition();
        for (int i = 0; i <= valueMax - valueMin; i++) {
            // System.out.println(marksPositions[i][0] + "     " + marksPositions[i][1]);
            g.drawLine(marksPositions[i][0].x, marksPositions[i][0].y, marksPositions[i][1].x, marksPositions[i][1].y);
        }
        g.fillOval(valuePosition.x - SIZE_INTERACTOR / 2, valuePosition.y - SIZE_INTERACTOR / 2,
                SIZE_INTERACTOR, SIZE_INTERACTOR);
    }

    public Point getValuePosition() {
        return valuePosition;
    }

    public void setValue(int newValue) {
        if (newValue < valueMin) {
            this.value = valueMin;
        } else if (newValue > valueMax) {
            this.value = valueMax;
        } else {
            this.value = newValue;
        }
        refreshValuePosition();
    }

    private int getAxisSize() {
        int size = Math.min(getWidth(), getHeight());
        return size / 2 - BORDER_OFFSET;
    }

    private Point getCenter() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }

    private void refreshValuePosition() {

        float proportion = AXIS_RATIO_MINIMUM + (1 - AXIS_RATIO_MINIMUM) * (((float) value - valueMin) / (valueMax - valueMin));
        int xPos = (int) Math.round(Math.cos(Math.toRadians(angle)) * proportion * getAxisSize()) + getCenter().x;
        int yPos = (int) Math.round(Math.sin(Math.toRadians(angle)) * proportion * getAxisSize()) + getCenter().y;
        valuePosition = new Point(xPos, yPos);
        repaint();
    }

    private Point[][] getMarksPosition() {

        Point[][] marksPositions = new Point[valueMax - valueMin + 1][];
        for (int i = valueMin; i <= valueMax; i++) {
            marksPositions[i - valueMin] = getMarkPositionFromValue(i);
        }

        return marksPositions;
    }

    private Point[] getMarkPositionFromValue(int value) {
        float proportion = AXIS_RATIO_MINIMUM + (1 - AXIS_RATIO_MINIMUM) * (((float) value - valueMin) / (valueMax - valueMin));
        int xPos = (int) Math.round(Math.cos(Math.toRadians(angle)) * proportion * getAxisSize()) + getCenter().x;
        int yPos = (int) Math.round(Math.sin(Math.toRadians(angle)) * proportion * getAxisSize()) + getCenter().y;
        Point markValuePosition = new Point(xPos, yPos);

        Point[] markPositions = new Point[2];

        xPos = markValuePosition.x + (int) Math.round(Math.sin(Math.toRadians(angle + 90))) * SIZE_MARKS / 2;
        yPos = markValuePosition.y + (int) Math.round(Math.cos(Math.toRadians(angle + 90))) * SIZE_MARKS / 2;
        markPositions[0] = new Point(xPos, yPos);

        xPos = markValuePosition.x + (int) Math.round(Math.sin(Math.toRadians(angle - 90))) * SIZE_MARKS / 2;
        yPos = markValuePosition.y + (int) Math.round(Math.cos(Math.toRadians(angle - 90))) * SIZE_MARKS / 2;
        markPositions[1] = new Point(xPos, yPos);
        System.out.println(Math.sin(Math.toRadians(angle - 90)) + "     " + Math.sin(Math.toRadians(angle + 90)));
        System.out.println(angle + "   " + markPositions[0] + "     " + markPositions[1]);
        return markPositions;
    }

    public void setOrthogonalValueProjection(Point point) {

        double distCenterPoint = point.distance(getCenter()) - getAxisSize() * AXIS_RATIO_MINIMUM;

        double angleProjection = angle - (Math.toDegrees((2 * Math.PI - Math.atan2(getCenter().y - point.y, point.x - getCenter().x))) % 360);
        double distCenterValue = Math.cos(Math.toRadians(angleProjection)) * distCenterPoint;

        int newValue = valueMin + (int) Math.round(distCenterValue / getAxisSize() * ((valueMax - valueMin)));
        setValue(newValue);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        refreshValuePosition();
    }

    @Override
    public boolean contains(Point p) {
        super.contains(p);
        if (p.distance(valuePosition) < SIZE_INTERACTOR / 2) {
            return true;
        }

        return false;
    }

}
