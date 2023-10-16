package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;

public class NormalModeHandler implements GameModeHandler{
    private final Tetris tetris;
    private final Board board;
    private final JLabel statusbar;

    public NormalModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.board = new Board(tetris);
        this.statusbar = tetris.getStatusBar();
    }

    @Override
    public void startGame() {
        tetris.add(board, BorderLayout.CENTER);
        statusbar.setText(" 0");
        board.start();
        tetris.revalidate();
        tetris.repaint();
        board.requestFocusInWindow();
        statusbar.setVisible(true);
    }
}
