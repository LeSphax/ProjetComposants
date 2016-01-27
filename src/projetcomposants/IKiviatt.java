/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetcomposants;

import java.awt.Color;
import javax.swing.table.TableModel;

/**
 *
 * @author kerbrase
 */
public interface IKiviatt {

    public static final int TITLE_COLUMN = 0;
    public static final int VALUE_COLUMN = 1;
    public static final int VALUE_MIN_COLUMN = 2;
    public static final int VALUE_MAX_COLUMN = 3;

    public static final int PREFERRED_SIZE = 300;

    public static final Color DEFAULT_COLOR = new Color(255, 0, 0, 200);

    public void setModel(TableModel model);
    

}
