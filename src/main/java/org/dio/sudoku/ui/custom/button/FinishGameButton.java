package org.dio.sudoku.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FinishGameButton extends JButton {

    public FinishGameButton(final ActionListener actionListener) {
        this.setText("Concluir");
        this.addActionListener(actionListener);
    }
}
