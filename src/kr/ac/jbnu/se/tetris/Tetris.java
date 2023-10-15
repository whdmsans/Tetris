package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.TetrisBot.TetrisBotMode;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

/*
����� 7���� ����� ���� ��ϰ� ������ ���� ������� �� ����(NoShape)�� ���� (Tetrominoes Ŭ������ ������ �̸��� ����)
����� 4���� ĭ���� ����
 */

public class Tetris extends JFrame {//��Ʈ���� Ŭ����

	JLabel statusbar; // ���¹� ����

	public Tetris() {

		statusbar = new JLabel(" 0");
		add(statusbar, BorderLayout.SOUTH); // ���¹� ��ġ ����
//		Board board = new Board(this); // ���� ���� ȭ��
//		add(board);
//		board.start();

		TetrisBotMode TBM = new TetrisBotMode(this);
		TBM.start(10);

		setSize(200, 400);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	public static void main(String[] args) {//���ν����ڵ�
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}