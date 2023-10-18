package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Tetris;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final Tetris tetris;
    private final TetrisCanvas canvas;
    NormalModeHandler normal;
    public LocalModeHandler(Tetris tetris){
        super(tetris);
        this.tetris = tetris;
        this.normal = new NormalModeHandler(tetris);
        this.canvas = new TetrisCanvas(tetris);
    }
    @Override
    public void startGame() {
        connectCanvas();
        normal.startGame();
        tetris.inputGameUI(canvas);
        canvas.start();
        tetris.getP1().requestFocusInWindow();
    }
    @Override
    public void connectCanvas() {tetris.updateP2(this.canvas);}
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
