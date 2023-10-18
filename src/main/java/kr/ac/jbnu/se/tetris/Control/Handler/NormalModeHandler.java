package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Tetris;

import javax.swing.*;
import java.awt.*;

import static kr.ac.jbnu.se.tetris.Tetris.*;

public class NormalModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final TetrisCanvas canvas;

    public NormalModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.canvas = new TetrisCanvas(tetris);
    }

    @Override
    public void startGame() {
        connectCanvas();
        tetris.inputGameUI(canvas);
        canvas.start();
        //UIBoard추후 삽입요망
        JPanel dummyUIBoard = new JPanel();
        dummyUIBoard.setPreferredSize(new Dimension(gameUIBlockWidth,gameUIBlockHeight));
        dummyUIBoard.setBackground(Color.GREEN);
        tetris.inputGameUI(dummyUIBoard);
        canvas.requestFocusInWindow();
    }

    @Override
    public void connectCanvas() { tetris.updateP1(this.canvas); }

    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
