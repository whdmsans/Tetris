package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.Tetris;

public class AIModeHandler extends NormalModeHandler implements GameModeHandler{
    private final Tetris tetris;
    private final TetrisCanvas canvas;
    private NormalModeHandler AI;
    public AIModeHandler(Tetris tetris){
        super(tetris);
        AI = new NormalModeHandler(tetris);
        this.tetris = tetris;
        this.canvas = new TetrisCanvasAI(tetris);
    }
    @Override
    public void startGame() {
        AI.startGame();
        this.connectCanvas();
        tetris.inputGameUI(canvas);
        canvas.start();
        AI.getCanvas().requestFocusInWindow();
    }
    @Override
    public void connectCanvas() {
        tetris.updateP2(this.canvas);
    }

    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
