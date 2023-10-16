package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Control.ModePack;
import kr.ac.jbnu.se.tetris.Entity.Chromosome;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Entity.GlobalStorage;
import kr.ac.jbnu.se.tetris.Tetris;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TetrisCanvas extends JPanel implements ActionListener {//인터페이스 = 액션리스너 //상속클래스 = Jpanel
	/**
	 *  게임 화면을 구성하는 칸 <br/>
	 *    해당 칸에 어떤 블록이 들어있는지 저장하는 변수 <br/>
	 *    세로축을 y, 가로축을 x로 생각했을 때, <br/>
	 *    board의 인덱스값은 십의 자릿수가 y, 일의 자릿수가 x를 나타냄 <br/>
	 *    ex) (3,1)의 위치를 인덱싱하려면 -> board[13]
	 */

	public Tetrominoes[] board;
	/** 모드 클래스 */
	private ModePack aiMode;
	/** 화면의 가로칸 수 */
	public static final int BoardWidth = 10;
	/** 화면의 세로칸 수 */
	public static final int BoardHeight = 22;
	/** 기본 프레임 딜레이 400 */ //딜레이 구성 변경 로직 구현하여 난이도 조절 가능할 것이라고 추측됨.
	Timer timer;//타이머 클래스는 존재. -> 타임레코딩 가능할것이라 예상됨. 필요 리소스 = DB
	/** ture : 블록이 바닥에 닿은 상태 <br/>
	 * false : 블록이 낙하중인 상태 */
	boolean isFallingFinished = false;
	/** 게임 시작 여부 */ //KeyControl 클래스가 static이어서, 한 보드가 false되버리면 키 감지 로직 멈추는 현상 있음, 확인 요망
	boolean isStarted = false;
	/** 2P 모드 */
	boolean isP2 = false;
	/** AI 모드 */
	boolean isAI = false;
	/** 게임 일시정지 여부 */
	boolean isPaused = false;
	/** 지워진 라인 갯수 */
	int numLinesRemoved = 0;
	/** 현재 떨어지는 블록 */
	Entity curPiece;
	Tetris game;
	GlobalStorage globalStorage;
	FirebaseTool firebaseTool;
	public TetrisCanvas(Tetris game, int modeNumber) {
		selectMode(modeNumber);
		this.game = game;
		globalStorage = GlobalStorage.getInstance();
		firebaseTool = FirebaseTool.getInstance();
		//setFocusable(true); // 키입력 강제로 받도록 설정.
		curPiece = new Entity(Tetrominoes.NoShape); // 현재 블록
		timer = new Timer(400, this); // 이벤트간 딜레이 400
		timer.start(); // start 메서드 첫번째 실행(Board 클래스의 start()에서 중복 실행됨)

		board = new Tetrominoes[BoardWidth * BoardHeight]; // 1차원 배열의 칸 생성
		clearBoard();
	}
	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}
	/** 칸의 가로 길이 */
	int squareWidth() { return (int) getSize().getWidth() / BoardWidth; }
	/** 칸의 세로 길이 */
	int squareHeight() { return (int) getSize().getHeight() / BoardHeight; }
	/** (x,y)에 블록 종류 */
	public Tetrominoes shapeAt(int x, int y) { return board[(y * BoardWidth) + x]; }
	public void start(int aiSeed) {
		if(aiSeed > -1){
			aiMode = new ModePack(this);
			aiMode.initWeight(aiSeed);
		}
		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();

		newPiece();
		timer.start(); // start 메서드 두번째 실행(클래스의 생성자에서 중복 실행됨)
	}
	/**
	 * 일시정지 메소드
	 * 추후 pause이펙트를 공통으로 걸려면, timer에 대한 처리가 필요할 것으로 보임.
	 * */
	public void pause() {
		if (!isStarted)
			return;
		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			TestMonitor.setPKey(isPaused);
		} else {
			timer.start();
			TestMonitor.setPKey(isPaused);
		}
		repaint();
	}
	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		// 쌓여있는 블록 색칠
		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
				if (shape != Tetrominoes.NoShape)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		// 떨어지는 블록 색칠
		if (curPiece.getShape() != Tetrominoes.NoShape) {
			for (int i = 0; i < 4; ++i) {
				int x = curPiece.getCurX() + curPiece.x(i);
				int y = curPiece.getCurY() - curPiece.y(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}
	public boolean dropDown() {
		int newY = curPiece.getCurY();
		while (newY > 0) {
			if (!tryMove(curPiece, curPiece.getCurX(), newY - 1))
				break;
			--newY;
		}
		pieceDropped();
		return true;
	}
	/** 블록이 한줄 아래로 내려가는 메소드*/
	private void oneLineDown() {
		if (!tryMove(curPiece, curPiece.getCurX(), curPiece.getCurY() - 1))
			pieceDropped(); //떨어지면 수행되는 메소드, 드롭다운과 동일
	}
	/** 모든 칸을 빈 공간(NoShape블록)으로 초기화 */
	public void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}
	/** 현재 위치에 블록을 남기는 메소드 */
	private void pieceDropped() {
		// 현재 위치에 블록 배치
		for (int i = 0; i < 4; ++i) {
			int x = curPiece.getCurX() + curPiece.x(i);
			int y = curPiece.getCurY() - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}
		// 완성된 라인 확인
		removeFullLines();
		// 완성된 줄이 있다면 작동 안함
		if (!isFallingFinished)
			newPiece();
	}
	/** 새 블록 생성 */
	private void newPiece() {
		// 블록 종류 및 위치 수정
		curPiece.setRandomShape();
		// 블록이 움직이지 못할 때(게임 종료)
		if (!tryMove(curPiece, curPiece.getCurX(), curPiece.getCurY())) {//블록 과다로 게임오버시.
			curPiece = new Entity(Tetrominoes.NoShape); // 떨어지는 블록 없앰
			timer.stop();
			isStarted = false;
			if(!isP2()||Integer.parseInt(globalStorage.getUserBestScore())<numLinesRemoved) {
				globalStorage.setUserBestScore(String.valueOf(numLinesRemoved));
				firebaseTool.setUserBestScore(globalStorage.getUserID(), String.valueOf(numLinesRemoved));// 베스트 스코어 업데이트
			}
			TestMonitor.setScore(-1,isP2);
		}
	}
	/** 블록 움직일 수 있는지 여부 반환<br/>
	 *  만약 움직일 수 있다면 움직이는 메서드 */
	public boolean tryMove(Entity newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		curPiece = newPiece;
		curPiece.setPosition(newX,newY);
		TestMonitor.setCurDxP1(curPiece.getCurX());
		TestMonitor.setCurDyP1(curPiece.getCurY());
		repaint();
		return true;
	}
	/** 완성된 줄 제거 */
	private void removeFullLines() {
		int numFullLines = 0; // 완성된 줄의 수

		// 위에서부터 내려오면서 찾기
		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;
			// i번째 행에 비어있는 칸이 있으면 break 작동
			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}
			// i번째 행에 빈칸이 없다면 윗줄들을 아래로 내림(채워진 줄 삭제)
			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}
		// 완성된 라인이 있다면 UI업데이트
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			TestMonitor.setScore(numLinesRemoved,isP2);
			isFallingFinished = true;
			curPiece = new Entity(Tetrominoes.NoShape);
			repaint();
		}
	}
	/** 칸을 블록의 종류에 맞게 색칠하는 메소드 */
	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color tmpcolor = shape.getColor();
		g.setColor(tmpcolor);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		g.setColor(tmpcolor.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);
		g.setColor(tmpcolor.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}
	public void setCurPiece(Entity curPiece) {
		this.curPiece = curPiece;
	}
	public Entity getCurPiece(){ return curPiece; }
	public boolean isFallingFinished() { return isFallingFinished; }
	public boolean isPaused(){ return isPaused; }
	protected boolean isP2(){ return isP2; }
	public boolean isStarted(){ return isStarted; }
	private void selectMode(int i){
		switch (i){
			case 1:
				isP2 = true;
				isAI = false;
				break;
			case 2:
				isP2 = false;
				isAI = true;
				break;
			default:
				isP2 = false;
				isAI = false;
		}
	}
}