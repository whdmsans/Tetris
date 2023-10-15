package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.TetrisBot.TetrisBotMode;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

/*
블록은 7개의 모양을 가진 블록과 구현을 위해 만들어진 빈 공간(NoShape)이 있음 (Tetrominoes 클래스에 종류의 이름이 있음)
블록은 4개의 칸으로 구성
 */

public class Tetris extends JFrame {//테트리스 클래스

	JLabel statusbar; // 상태바 변수

	public Tetris() {

		statusbar = new JLabel(" 0");
		add(statusbar, BorderLayout.SOUTH); // 상태바 위치 설정
//		Board board = new Board(this); // 실제 게임 화면
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

	public static void main(String[] args) {//메인실행코드
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}