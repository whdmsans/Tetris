package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Tetris;

import javax.swing.*;
import java.awt.*;

import static kr.ac.jbnu.se.tetris.Tetris.*;

public class NormalModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final TetrisCanvas canvas;
    static JPanel uiBoard;

    public NormalModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.canvas = new TetrisCanvas(tetris);
    }

    @Override
    public void startGame() {
        connectCanvas();
        tetris.inputGameUI(canvas);
        canvas.start();
        uiBoard = new JPanel();
        uiBoard.setPreferredSize(new Dimension(gameUIBlockWidth,gameUIBlockHeight));
        uiBoard.setBackground(Color.YELLOW);
        tetris.inputGameUI(uiBoard);
        canvas.requestFocusInWindow();
    }

    @Override
    public void connectCanvas() { tetris.updateP1(this.canvas); }

    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
    public static void addUI(JFrame component){
        uiBoard.add(component);
        uiBoard.revalidate();
        uiBoard.repaint();
    }

}
