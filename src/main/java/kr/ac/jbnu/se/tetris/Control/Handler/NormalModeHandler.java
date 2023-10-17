package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Tetris;

import javax.swing.*;
import java.awt.*;

import static kr.ac.jbnu.se.tetris.Tetris.*;

public class NormalModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final TetrisCanvas board;

    public NormalModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.board = new TetrisCanvas(tetris);
    }

    @Override
    public void startGame() {
        getBoundary();
        tetris.inputGameUI(board);
        board.start();
        //UIBoard추후 삽입요망
        JPanel dummyUIBoard = new JPanel();
        dummyUIBoard.setPreferredSize(new Dimension(gameUIBlockWidth,gameUIBlockHeight));
        dummyUIBoard.setBackground(Color.GREEN);
        tetris.inputGameUI(dummyUIBoard);
    }

    @Override
    public void getBoundary() {
        tetris.updateP1(this.board);
    }
}
