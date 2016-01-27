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
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase:
 */
public class KiviattAxis extends JComponent {

    private static final float AXIS_RATIO_MINIMUM = 0.2f;

    private static final BasicStroke MY_BASIC_STROKE = new BasicStroke(1);

    private final int axisIndex;
    private final double angle;
    private final TableModel model;
    private final int valueMax;
    private final int valueMin;

    private String label;
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
        label = (String) model.getValueAt(axisIndex, IKiviatt.TITLE_COLUMN);
        refreshValuePosition();
    }

    @Override
    public void paint(Graphics _g) {
        super.paint(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.black);
        g.setStroke(MY_BASIC_STROKE);
        int xBorder = (int) Math.round(Math.cos(Math.toRadians(angle)) * getAxisSize() + getCenter().x);
        int yBorder = (int) Math.round(Math.sin(Math.toRadians(angle)) * getAxisSize() + getCenter().y);
        g.drawLine(getCenter().x, getCenter().y, xBorder, yBorder);
        // titre
        int decalage;
        if (Math.cos(Math.toRadians(angle)) < 0) {
            decalage = 5 + label.length() * 10;
        } else {
            decalage = 18;
        }
        int xBorderTitle = (int) Math.round(Math.cos(Math.toRadians(angle)) * (getAxisSize() + decalage)) + getCenter().x;

        if (Math.sin(Math.toRadians(angle)) <= 0) {
            decalage = 10;
        } else {
            decalage = 18;
        }
        int yBorderTitle = (int) Math.round(Math.sin(Math.toRadians(angle)) * (getAxisSize() + decalage)) + getCenter().y;
        g.drawString(label, xBorderTitle, yBorderTitle);

        // fin titre
        Point[][] marksPositions = getMarksPosition();
        for (int i = 0; i <= valueMax - valueMin; i++) {
            if (i == 0 || i == valueMax - valueMin) {
                g.setStroke(new BasicStroke(2));
            } else {
                g.setStroke(MY_BASIC_STROKE);
            }
            // System.out.println(marksPositions[i][0] + "     " + marksPositions[i][1]);
            g.drawLine(marksPositions[i][0].x, marksPositions[i][0].y, marksPositions[i][1].x, marksPositions[i][1].y);
        }
        g.fillOval(valuePosition.x - getSizeInteractor() / 2, valuePosition.y - getSizeInteractor() / 2,
                getSizeInteractor(), getSizeInteractor());
    }

    public Point getValuePosition() {
        return valuePosition;
    }

    public void setValue(int newValue) {
        if (newValue < valueMin) {
            newValue = valueMin;
        } else if (newValue > valueMax) {
            newValue = valueMax;
        }
        model.setValueAt(Integer.toString(newValue), axisIndex, Kiviatt.VALUE_COLUMN);
    }

    private int getAxisSize() {
        int size = Math.min(getWidth(), getHeight());
        return size / 2 - getBorderOffset();
    }

    private Point getCenter() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }

    private void refreshValuePosition() {

        valuePosition = computeValuePosition(value);
        repaint();
    }

    private Point computeValuePosition(double myValue) {
        float proportion = AXIS_RATIO_MINIMUM + (1 - AXIS_RATIO_MINIMUM) * (((float) myValue - valueMin) / (valueMax - valueMin));
        int xPos = (int) Math.round(Math.cos(Math.toRadians(angle)) * proportion * getAxisSize() + getCenter().x);
        int yPos = (int) Math.round(Math.sin(Math.toRadians(angle)) * proportion * getAxisSize() + getCenter().y);
        return new Point(xPos, yPos);
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
        int xPos = (int) Math.round(Math.cos(Math.toRadians(angle)) * proportion * getAxisSize() + getCenter().x);
        int yPos = (int) Math.round(Math.sin(Math.toRadians(angle)) * proportion * getAxisSize() + getCenter().y);
        Point markValuePosition = new Point(xPos, yPos);

        Point[] markPositions = new Point[2];

        xPos = markValuePosition.x + (int) Math.round(Math.cos(Math.toRadians(angle + 90)) * getSizeMarks() / 2);
        yPos = markValuePosition.y + (int) Math.round(Math.sin(Math.toRadians(angle + 90)) * getSizeMarks() / 2);
        markPositions[0] = new Point(xPos, yPos);

        xPos = markValuePosition.x + (int) Math.round(Math.cos(Math.toRadians(angle - 90)) * getSizeMarks() / 2);
        yPos = markValuePosition.y + (int) Math.round(Math.sin(Math.toRadians(angle - 90)) * getSizeMarks() / 2);
        markPositions[1] = new Point(xPos, yPos);

        return markPositions;
    }

    public void setOrthogonalProjectionValue(Point point) {

        int newValue = (int) Math.round(orthogonalProjectionValue(point));
        setValue(newValue);
    }

    private double orthogonalProjectionValue(Point point) {
        double distCenterPoint = point.distance(getCenter());
        double angleProjection = angle - (Math.toDegrees((2 * Math.PI - Math.atan2(getCenter().y - point.y, point.x - getCenter().x))) % 360);
        double distCenterValue = Math.cos(Math.toRadians(angleProjection)) * distCenterPoint - getAxisSize() * AXIS_RATIO_MINIMUM;
        double newValue = (valueMin + distCenterValue / ((1 - AXIS_RATIO_MINIMUM) * getAxisSize()) * ((valueMax - valueMin)));

        return newValue;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        refreshValuePosition();
    }

    @Override
    public boolean contains(Point p) {
        super.contains(p);
        double floatValue = orthogonalProjectionValue(p);
        if (floatValue >= valueMin) {
            Point orthogonalProjection = computeValuePosition(floatValue);
            return p.distance(orthogonalProjection) < getSizeInteractor() / 2;
        } else {
            return false;
        }
    }

    void updateValue() {
        value = Integer.parseInt((String) model.getValueAt(axisIndex, IKiviatt.VALUE_COLUMN));
        refreshValuePosition();
    }

    private int getSizeInteractor() {
        return 5 + Math.max(getWidth() / 100, getHeight() / 100);
    }

    private int getSizeMarks() {
        return 2 + Math.max(getWidth() / 100, getHeight() / 100);
    }

    private int getBorderOffset() {
        return 30 + Math.max(getWidth() / 100, getHeight() / 100);
    }

}
