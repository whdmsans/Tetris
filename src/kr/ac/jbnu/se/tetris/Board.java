package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {//인터페이스 = 액션리스너 //상속클래스 = Jpanel

	/**
	 *  게임 화면을 구성하는 칸 <br/>
	 * 	해당 칸에 어떤 블록이 들어있는지 저장하는 변수 <br/>
	 * 	세로축을 y, 가로축을 x로 생각했을 때, <br/>
	 * 	board의 인덱스값은 십의 자릿수가 y, 일의 자릿수가 x를 나타냄 <br/>
	 * 	ex) (3,1)의 위치를 인덱싱하려면 -> board[13]
	 */
	Tetrominoes[] board;

	/*
	칸의 크기 지정
	화면의 크기에 관계없이 항상 (BoardWidth * BoardHeight)만큼의 칸이 들어감
	 */
	/** 화면의 가로칸 수 */
	final int BoardWidth = 10;
	/** 화면의 세로칸 수 */
	final int BoardHeight = 22;


	Timer timer;//타이머 클래스는 존재. -> 타임레코딩 가능할것이라 예상됨. 필요 리소스 = DB
	/** ture : 블록이 바닥에 닿은 상태 <br/>
	 * false : 블록이 낙하중인 상태 */
	boolean isFallingFinished = false;

	/** 게임 시작 여부 */
	boolean isStarted = false;
	/** 게임 일시정지 여부 */
	boolean isPaused = false;
	/** 지워진 라인 갯수 */
	int numLinesRemoved = 0;
	int curX = 0;//떨어지는 블록의 x좌표
	int curY = 0;//떨어지는 블록의 y좌표
	JLabel statusbar;// Tetris 클래스의 'statusbar'변수 저장
	Shape curPiece; // 현재 떨어지는 블록

	public Board(Tetris parent) {

		setFocusable(true);
		curPiece = new Shape();//현재 블록
		timer = new Timer(400, this);// 이벤트간 딜레이 400
		timer.start(); // start 메서드 첫번째 실행(Board 클래스의 start()에서 중복 실행됨)

		statusbar = parent.getStatusBar();
		board = new Tetrominoes[BoardWidth * BoardHeight]; // 1차원 배열의 칸 생성
		addKeyListener(new TAdapter());
		clearBoard();
	}

	// 400delay마다 반복 실행
	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	/** 칸의 가로 길이 */
	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;
	}
	/** 칸의 세로 길이 */
	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}
	/** (x,y)에 블록 종류 */
	Tetrominoes shapeAt(int x, int y) {
		return board[(y * BoardWidth) + x];
	}

	public void start() {
		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();

		newPiece();
		timer.start(); // start 메서드 두번째 실행(클래스의 생성자에서 중복 실행됨)
	}

	/** 일시정지 메소드 */
	private void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			statusbar.setText("paused");
		} else {
			timer.start();
			statusbar.setText(String.valueOf(numLinesRemoved));
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
				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}

	/** 블록을 낙하시키는 메소드 */
	private void dropDown() {//
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	/** 블록이 한줄 아래로 내려가는 메소드 */
	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped(); //떨어지면 수행되는 메소드, 드롭다운과 동일
	}

	/** 모든 칸을 빈 공간(NoShape블록)으로 초기화 */
	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}

	/** 현재 위치에 블록을 남기는 메소드 */
	private void pieceDropped() {
		// 현재 위치에 블록 배치
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
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
		curX = BoardWidth / 2 + 1;
		curY = BoardHeight - 1 + curPiece.minY();

		// 블록이 움직이지 못할 때(게임 종료)
		if (!tryMove(curPiece, curX, curY)) {//블록 과다로 게임오버시.
			curPiece.setShape(Tetrominoes.NoShape); // 떨어지는 블록 없앰
			timer.stop();
			isStarted = false;
			statusbar.setText("game over");
		}
	}

	/** 블록 움직일 수 있는지 여부 반환<br/>
	 *  만약 움직일 수 있다면 움직이는 메서드 */
	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;
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
			statusbar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);
			repaint();
		}
	}

	/** 칸을 블록의 종류에 맞게 색칠하는 메소드 */
	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0) };

		Color color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}
	
	class TAdapter extends KeyAdapter {//키어뎁터는 키이벤트 핸들러로 처리
		public void keyPressed(KeyEvent e) {

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();//키코드를 키이벤트 객체 e에서 받아옴

			if (keycode == 'p' || keycode == 'P') {//멈춤키
				pause();
				return;
			}

			if (isPaused)//멈춤상태시 키스위치 잠금
				return;

			/*switch (keycode) {//키입력
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN://우회전
				tryMove(curPiece.rotateRight(), curX, curY);
				break;
			case KeyEvent.VK_UP://좌회전
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();//드롭다운형식은 즉시 드랍
				break;
			case 'd':
				oneLineDown();//드롭 프레임의 가속화
				break;
			case 'D':
				oneLineDown();
				break;
			}*/
			if (keycode == KeyEvent.VK_LEFT) {
				tryMove(curPiece, curX - 1, curY);
			}
			if (keycode == KeyEvent.VK_RIGHT) {
				tryMove(curPiece, curX + 1, curY);
			}
			if (keycode == KeyEvent.VK_UP) {
				tryMove(curPiece.rotateRight(), curX, curY);
			}
			if (keycode == KeyEvent.VK_DOWN) {
				tryMove(curPiece.rotateLeft(), curX, curY);
			}
			if (keycode == KeyEvent.VK_SPACE) {
				dropDown();
			}
			if (keycode == KeyEvent.VK_D) {
				oneLineDown();
			}
		}
	}
	class tThread implements Runnable{
		public tThread(Board game){

		}
		@Override
		public void run() {

		}
	}
}