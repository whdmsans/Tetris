package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {//�������̽� = �׼Ǹ����� //���Ŭ���� = Jpanel

	/**
	 *  ���� ȭ���� �����ϴ� ĭ <br/>
	 * 	�ش� ĭ�� � ����� ����ִ��� �����ϴ� ���� <br/>
	 * 	�������� y, �������� x�� �������� ��, <br/>
	 * 	board�� �ε������� ���� �ڸ����� y, ���� �ڸ����� x�� ��Ÿ�� <br/>
	 * 	ex) (3,1)�� ��ġ�� �ε����Ϸ��� -> board[13]
	 */
	Tetrominoes[] board;

	/*
	ĭ�� ũ�� ����
	ȭ���� ũ�⿡ ������� �׻� (BoardWidth * BoardHeight)��ŭ�� ĭ�� ��
	 */
	/** ȭ���� ����ĭ �� */
	final int BoardWidth = 10;
	/** ȭ���� ����ĭ �� */
	final int BoardHeight = 22;


	Timer timer;//Ÿ�̸� Ŭ������ ����. -> Ÿ�ӷ��ڵ� �����Ұ��̶� �����. �ʿ� ���ҽ� = DB
	/** ture : ����� �ٴڿ� ���� ���� <br/>
	 * false : ����� �������� ���� */
	boolean isFallingFinished = false;

	/** ���� ���� ���� */
	boolean isStarted = false;
	/** ���� �Ͻ����� ���� */
	boolean isPaused = false;
	/** ������ ���� ���� */
	int numLinesRemoved = 0;
	int curX = 0;//�������� ����� x��ǥ
	int curY = 0;//�������� ����� y��ǥ
	JLabel statusbar;// Tetris Ŭ������ 'statusbar'���� ����
	Shape curPiece; // ���� �������� ���

	public Board(Tetris parent) {

		setFocusable(true);
		curPiece = new Shape();//���� ���
		timer = new Timer(400, this);// �̺�Ʈ�� ������ 400
		timer.start(); // start �޼��� ù��° ����(Board Ŭ������ start()���� �ߺ� �����)

		statusbar = parent.getStatusBar();
		board = new Tetrominoes[BoardWidth * BoardHeight]; // 1���� �迭�� ĭ ����
		addKeyListener(new TAdapter());
		clearBoard();
	}

	// 400delay���� �ݺ� ����
	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	/** ĭ�� ���� ���� */
	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;
	}
	/** ĭ�� ���� ���� */
	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}
	/** (x,y)�� ��� ���� */
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
		timer.start(); // start �޼��� �ι�° ����(Ŭ������ �����ڿ��� �ߺ� �����)
	}

	/** �Ͻ����� �޼ҵ� */
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

		// �׿��ִ� ��� ��ĥ
		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
				if (shape != Tetrominoes.NoShape)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		// �������� ��� ��ĥ
		if (curPiece.getShape() != Tetrominoes.NoShape) {
			for (int i = 0; i < 4; ++i) {
				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}

	/** ����� ���Ͻ�Ű�� �޼ҵ� */
	private void dropDown() {//
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	/** ����� ���� �Ʒ��� �������� �޼ҵ� */
	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped(); //�������� ����Ǵ� �޼ҵ�, ��Ӵٿ�� ����
	}

	/** ��� ĭ�� �� ����(NoShape���)���� �ʱ�ȭ */
	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}

	/** ���� ��ġ�� ����� ����� �޼ҵ� */
	private void pieceDropped() {
		// ���� ��ġ�� ��� ��ġ
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}

		// �ϼ��� ���� Ȯ��
		removeFullLines();

		// �ϼ��� ���� �ִٸ� �۵� ����
		if (!isFallingFinished)
			newPiece();
	}

	/** �� ��� ���� */
	private void newPiece() {
		// ��� ���� �� ��ġ ����
		curPiece.setRandomShape();
		curX = BoardWidth / 2 + 1;
		curY = BoardHeight - 1 + curPiece.minY();

		// ����� �������� ���� ��(���� ����)
		if (!tryMove(curPiece, curX, curY)) {//��� ���ٷ� ���ӿ�����.
			curPiece.setShape(Tetrominoes.NoShape); // �������� ��� ����
			timer.stop();
			isStarted = false;
			statusbar.setText("game over");
		}
	}

	/** ��� ������ �� �ִ��� ���� ��ȯ<br/>
	 *  ���� ������ �� �ִٸ� �����̴� �޼��� */
	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)//��Ʈ���� ��Ʈ�� ������ x,y�� ���� ����
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)//��Ʈ���� �ڵ鸵 ������ ��ũ�� �ƴҽ� ������ ������. �Ҹ��� ���� ����
				return false;
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();
		return true;
	}

	/** �ϼ��� �� ���� */
	private void removeFullLines() {
		int numFullLines = 0; // �ϼ��� ���� ��

		// ���������� �������鼭 ã��
		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			// i��° �࿡ ����ִ� ĭ�� ������ break �۵�
			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			// i��° �࿡ ��ĭ�� ���ٸ� ���ٵ��� �Ʒ��� ����(ä���� �� ����)
			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}

		// �ϼ��� ������ �ִٸ� UI������Ʈ
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			statusbar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);
			repaint();
		}
	}

	/** ĭ�� ����� ������ �°� ��ĥ�ϴ� �޼ҵ� */
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
	
	class TAdapter extends KeyAdapter {//Ű��ʹ� Ű�̺�Ʈ �ڵ鷯�� ó��
		public void keyPressed(KeyEvent e) {

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();//Ű�ڵ带 Ű�̺�Ʈ ��ü e���� �޾ƿ�

			if (keycode == 'p' || keycode == 'P') {//����Ű
				pause();
				return;
			}

			if (isPaused)//������½� Ű����ġ ���
				return;

			/*switch (keycode) {//Ű�Է�
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN://��ȸ��
				tryMove(curPiece.rotateRight(), curX, curY);
				break;
			case KeyEvent.VK_UP://��ȸ��
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();//��Ӵٿ������� ��� ���
				break;
			case 'd':
				oneLineDown();//��� �������� ����ȭ
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