/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.Point;

/**
 *
 * @author kerbrase
 */
public interface IKiviattAxis {
    
    public Point getValuePosition();

    public void setModelValue(int value);
    public static final int BORDER_OFFSET = 10;

}
