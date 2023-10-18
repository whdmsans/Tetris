package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.AIControl;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;
import kr.ac.jbnu.se.tetris.Tetris;

public class TetrisCanvasAI extends TetrisCanvas {

	private AIControl aiControl;
	public TetrisCanvasAI(Tetris game) {
		super(game);
		aiControl = new AIControl(this);
	}

	@Override
	public void start() {
		keyCtrl = new KeyControl(game);
		addKeyListener(keyCtrl);
		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();

		timer.start();
		newPiece();
	}

	@Override
	protected void newPiece() {
		super.newPiece();
		if (isStarted){
			doControlLogic();
		}
	}

	public void doControlLogic() {
		int[] goodPosition = aiControl.findGoodPosition(getCurPiece());

//		for (int i = 0; i < 3; i++) {
//			System.out.println("goodPosition[" + i + "] : " + goodPosition[i]);
//		}
//		System.out.println("----------------------------");

		for (int i = goodPosition[2]; i > 0; i--) {
			curPiece.rotateRight();
		}
		int num = curPiece.getCurX() - goodPosition[0];
		while (num != 0){
			if (num > 0) {
				tryMove(curPiece, curPiece.getCurX() - 1, curPiece.getCurX());
				num--;
			} else if (num < 0) {
				tryMove(curPiece, curPiece.getCurX() + 1, curPiece.getCurX());
				num++;
			}
		}

//        canvas.dropDown();
	}

	public boolean move_down(Entity temp_block) {
		int newY = temp_block.getCurY();
		while (newY > 0) {
			if (!tryMove(temp_block, temp_block.getCurX(), newY - 1))
				break;
			--newY;
		}
		return true;
	}
	public boolean tryMoveAI(Entity newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		newPiece.setPosition(newX,newY);
		/*TestMonitor.setCurDxP1(curPiece.getCurX());
		TestMonitor.setCurDyP1(curPiece.getCurY());*/
		return true;
	}
}