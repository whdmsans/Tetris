package kr.ac.jbnu.se.tetris;

import java.awt.*;

import javax.swing.*;

/*
블록은 7개의 모양을 가진 블록과 구현을 위해 만들어진 빈 공간(NoShape)이 있음 (Tetrominoes 클래스에 종류의 이름이 있음)
블록은 4개의 칸으로 구성
 */

public class Tetris extends JFrame {//테트리스 클래스

	public enum GameMode {
		NORMAL_MODE, ITEM_MODE, SURVIVAL_MODE, SPRINT_MODE, AI_MODE, PVP_MODE
	}

	private final GameMode currentMode = GameMode.NORMAL_MODE; // 기본 모드로 시작

	// 현재 게임 모드를 가져오는 메서드
	public GameMode getCurrentMode() {
		return currentMode;
	}

	private final JPanel modeSelectionPanel;
	private final JButton startButton;
	private final JButton normalModeButton;
	private final JButton itemModeButton;
	private final JButton survivalModeButton;
	private final JButton sprintModeButton;
	private final JButton aiModeButton;
	private final JButton pvpModeButton;
	private JLabel statusbar; // 상태바 변수
	private GameModeHandler modeHandler;

	public Tetris() {
		modeSelectionPanel = new JPanel();
		startButton = new JButton("게임 시작");
		normalModeButton = new JButton("일반 모드");
		itemModeButton = new JButton("아이템 모드");
		survivalModeButton = new JButton("생존 모드");
		sprintModeButton = new JButton("스프린트 모드");
		aiModeButton = new JButton("AI 모드");
		pvpModeButton = new JButton("PVP 모드");

		statusbar = new JLabel(" 0");
		add(statusbar, BorderLayout.SOUTH);
		statusbar.setVisible(false);

		add(startButton, BorderLayout.NORTH);
		add(modeSelectionPanel, BorderLayout.CENTER);
		modeSelectionPanel.add(normalModeButton);
		modeSelectionPanel.add(itemModeButton);
		modeSelectionPanel.add(survivalModeButton);
		modeSelectionPanel.add(sprintModeButton);
		modeSelectionPanel.add(aiModeButton);
		modeSelectionPanel.add(pvpModeButton);
		modeSelectionPanel.setVisible(false);

		startButton.addActionListener(e -> showModeSelection());
		normalModeButton.addActionListener(e -> startGame(GameMode.NORMAL_MODE));
		itemModeButton.addActionListener(e -> startGame(GameMode.ITEM_MODE));
		survivalModeButton.addActionListener(e -> startGame(GameMode.SURVIVAL_MODE));
		sprintModeButton.addActionListener(e -> startGame(GameMode.SPRINT_MODE));
		aiModeButton.addActionListener(e -> startGame(GameMode.AI_MODE));
		pvpModeButton.addActionListener(e -> startGame(GameMode.PVP_MODE));

		setSize(400, 800);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void showModeSelection() {
		startButton.setVisible(false);
		modeSelectionPanel.setVisible(true);
	}

	private void startGame(GameMode mode) {
		modeSelectionPanel.setVisible(false);
		switch (mode) {
			case NORMAL_MODE:
				modeHandler = new NormalModeHandler(this);
				modeHandler.startGame();
				break;
			case ITEM_MODE:
				modeHandler = new ItemModeHandler(this);
				modeHandler.startGame();
				break;
			case SURVIVAL_MODE:
				modeHandler = new SurvivalModeHandler(this);
				modeHandler.startGame();
				break;
			case SPRINT_MODE:
				modeHandler = new SprintModeHandler(this);
				modeHandler.startGame();
				break;
			case AI_MODE:
				// AI 모드
				break;
			case PVP_MODE:
				// PVP 모드
				break;
		}
	}

	public JLabel getStatusBar() {
		return statusbar;
	}


	public static void main(String[] args) {//메인실행코드
		SwingUtilities.invokeLater(() -> {
			Tetris game = new Tetris();
			game.setLocationRelativeTo(null);
			game.setVisible(true);
		});
	}
}