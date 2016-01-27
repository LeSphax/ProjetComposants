/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.Color;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author kerbrase
 */
public class KiviattColorPropertyEditor extends PropertyEditorSupport {

    private Color myColor;

    private final String RED = "red";
    private final String BLUE = "blue";
    private final String GREEN = "green";
    private final String YELLOW = "yellow";
    private final String BLACK = "black";

    @Override
    public void setValue(Object obj) {
        myColor = ((Color) obj);
    }

    @Override
    public Object getValue() {
        return myColor;
    }

    @Override
    public String getJavaInitializationString() {
        return "new Color(" + myColor.getRed() + "," + myColor.getGreen() + "," + myColor.getBlue() + "," + 200 + ")";
    }

    @Override
    public String[] getTags() {
        String[] str = {RED, BLUE, GREEN, YELLOW, BLACK};
        return str;
    }

    @Override
    public String getAsText() {
        if (myColor == Color.RED) {
            return RED;
        } else if (myColor == Color.BLUE) {
            return BLUE;
        } else if (myColor == Color.GREEN) {
            return GREEN;
        } else if (myColor == Color.YELLOW) {
            return YELLOW;
        } else if (myColor == Color.BLACK) {
            return BLACK;
        } else {
            return RED;
        }

    }

    @Override
    public void setAsText(String str) {
        if (str.equals(RED)) {
            myColor = Color.RED;
        } else if (str.equals(BLUE)) {
            myColor = Color.BLUE;
        } else if (str.equals(GREEN)) {
            myColor = Color.GREEN;
        } else if (str.equals(YELLOW)) {
            myColor = Color.YELLOW;
        } else if (str.equals(BLACK)) {
            myColor = Color.BLACK;
        } else {
            myColor = Color.RED;
        }
        firePropertyChange();
    }

}
