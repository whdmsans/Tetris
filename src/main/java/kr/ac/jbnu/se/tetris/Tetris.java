package kr.ac.jbnu.se.tetris;

import com.google.firebase.FirebaseApp;

import java.awt.*;
import javax.swing.*;

/*
블록은 7개의 모양을 가진 블록과 구현을 위해 만들어진 빈 공간(NoShape)이 있음 (Tetrominoes 클래스에 종류의 이름이 있음)
블록은 4개의 칸으로 구성
 */

public class Tetris extends JFrame {//테트리스 클래스
	final int windowWidth = 600;
	final int windowHeight = 400;
	protected Boundary boundary, boundary2;
	protected KeyControl keyCtrl;
	protected FirebaseTool firebaseTool;
	public Tetris() {
		firebaseTool=FirebaseTool.getInstance();
		if(!firebaseTool.logIn("hiyd125@jbnu.ac.kr","rltn12"))firebaseTool.signUp("hiyd125@jbnu.ac.kr","rltn12");
		//boundary와 control로 GUI, Logic을 구분짓고자 변수명 변경. 추후 수정할 시 요망.
		boundary = new Boundary(this,false); // 실제 게임 화면
		boundary2 = new Boundary(this,true);
		keyCtrl = new KeyControl(this);
		TestMonitor display = new TestMonitor();
		boundary.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		boundary2.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		display.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
		add(boundary,BorderLayout.WEST);
		add(boundary2,BorderLayout.EAST);
		add(display,BorderLayout.CENTER);
		boundary.start();
		boundary2.start();

		setSize(windowWidth, windowHeight);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {//메인실행코드
		Tetris game = new Tetris();
		game.setFocusable(true);
		game.addKeyListener(game.keyCtrl);
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}