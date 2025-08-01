package org.dio.sudoku.ui.custom.screen;

import org.dio.sudoku.model.Space;
import org.dio.sudoku.model.service.BoardService;
import org.dio.sudoku.model.service.EventEnum;
import org.dio.sudoku.model.service.NotifierService;
import org.dio.sudoku.ui.custom.button.CheckGameStatusButton;
import org.dio.sudoku.ui.custom.button.FinishGameButton;
import org.dio.sudoku.ui.custom.button.ResetButton;
import org.dio.sudoku.ui.custom.frame.MainFrame;
import org.dio.sudoku.ui.custom.input.NumberText;
import org.dio.sudoku.ui.custom.panel.MainPanel;
import org.dio.sudoku.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static org.dio.sudoku.model.service.EventEnum.CLEAR_SPACE;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r=0; r<9; r+=3) {
            var endRow = r + 2;
            for (int c=0; c<9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow) {
        List<Space> spaceSector = new ArrayList<>();
        for ( int r=initRow; r <= endRow; r++) {
            for ( int c=initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces) {
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
           if (boardService.gameIsFinished()) {
               JOptionPane.showMessageDialog(null, "Parabéns você concluiu o jogo");
               resetButton.setEnabled(false);
               checkGameStatusButton.setEnabled(false);
               finishGameButton.setEnabled(false);
           } else {
               JOptionPane.showMessageDialog(null, "Seu jogo tem alguma inconsistência, ajuste e tente novamente");
           }
        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case IMCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if ( dialogResult == 0 ) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });

        mainPanel.add(resetButton);
    }
}
