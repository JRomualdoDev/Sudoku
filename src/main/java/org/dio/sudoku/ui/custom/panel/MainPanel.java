package org.dio.sudoku.ui.custom.panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static java.awt.Color.black;

public class MainPanel extends JPanel {

    public MainPanel(final Dimension dimension) {
        this.setSize(dimension);
        this.setPreferredSize(dimension);
    }
}
