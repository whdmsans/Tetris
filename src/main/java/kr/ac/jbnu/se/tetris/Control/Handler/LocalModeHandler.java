package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Tetris;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final Tetris tetris;
    private final TetrisCanvas board2;
    private NormalModeHandler nomal;
    public LocalModeHandler(Tetris tetris){
        super(tetris);
        nomal = new NormalModeHandler(tetris);
        this.tetris = tetris;
        this.board2 = new TetrisCanvas(tetris);
    }
    @Override
    public void startGame() {
        nomal.startGame();
        this.connectCanvas();
        tetris.inputGameUI(board2);
        board2.start();
    }
    @Override
    public void connectCanvas() {
        tetris.updateP2(this.board2);
    }

    @Override
    public TetrisCanvas getCanvas() { return this.board2; }
}
