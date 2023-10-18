package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;

public interface GameModeHandler {
    void startGame();
    void connectCanvas();
    public TetrisCanvas getCanvas();
}