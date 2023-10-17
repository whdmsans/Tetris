package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Control.KeyControl;

import java.awt.*;
import javax.swing.*;

/*
블록은 7개의 모양을 가진 블록과 구현을 위해 만들어진 빈 공간(NoShape)이 있음 (Tetrominoes 클래스에 종류의 이름이 있음)
블록은 4개의 칸으로 구성
 */

public class Tetris extends JFrame {//테트리스 클래스
	final int windowWidth = 600;
	final int windowHeight = 400;
	protected TetrisCanvas boundary, boundary2, boundaryAI;
	protected KeyControl keyCtrl;
	protected FirebaseTool firebaseTool;
	public Tetris() {
		firebaseTool=FirebaseTool.getInstance();
		if(!firebaseTool.logIn("hiyd125@jbnu.ac.kr","rltn12"))firebaseTool.signUp("hiyd125@jbnu.ac.kr","rltn12");

		boundary = new TetrisCanvas(this,0); // 실제 게임 화면
		//boundary2 = new TetrisCanvas(this,1); // 2P 구성
		boundaryAI = new TetrisCanvas(this,2);

		keyCtrl = new KeyControl(this);
		TestMonitor display = new TestMonitor();

		boundary.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		//boundary2.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		boundaryAI.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		display.setPreferredSize(new Dimension(windowWidth/3,windowHeight));

		add(boundary,BorderLayout.WEST);
		add(display,BorderLayout.CENTER);
		//add(boundary2,BorderLayout.EAST);
		add(boundaryAI,BorderLayout.EAST);

		boundary.start();
		//boundary2.start();
		boundaryAI.start();

		setSize(windowWidth, windowHeight);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public TetrisCanvas getP1(){ return boundary; }
	public TetrisCanvas getP2(){ return boundary2 != null ? boundary2 : null; }
	public static void main(String[] args) {//메인실행코드
		Tetris game = new Tetris();
		game.setFocusable(true);
		game.addKeyListener(game.keyCtrl);
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}