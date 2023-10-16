package kr.ac.jbnu.se.tetris;

import java.awt.*;

import javax.swing.*;

/*
����� 7���� ����� ���� ��ϰ� ������ ���� ������� �� ����(NoShape)�� ���� (Tetrominoes Ŭ������ ������ �̸��� ����)
����� 4���� ĭ���� ����
 */

public class Tetris extends JFrame {//��Ʈ���� Ŭ����

	public enum GameMode {
		NORMAL_MODE, ITEM_MODE, SURVIVAL_MODE, SPRINT_MODE, AI_MODE, PVP_MODE
	}

	private final GameMode currentMode = GameMode.NORMAL_MODE; // �⺻ ���� ����

	// ���� ���� ��带 �������� �޼���
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
	private JLabel statusbar; // ���¹� ����
	private GameModeHandler modeHandler;

	public Tetris() {
		modeSelectionPanel = new JPanel();
		startButton = new JButton("���� ����");
		normalModeButton = new JButton("�Ϲ� ���");
		itemModeButton = new JButton("������ ���");
		survivalModeButton = new JButton("���� ���");
		sprintModeButton = new JButton("������Ʈ ���");
		aiModeButton = new JButton("AI ���");
		pvpModeButton = new JButton("PVP ���");

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
				// AI ���
				break;
			case PVP_MODE:
				// PVP ���
				break;
		}
	}

	public JLabel getStatusBar() {
		return statusbar;
	}


	public static void main(String[] args) {//���ν����ڵ�
		SwingUtilities.invokeLater(() -> {
			Tetris game = new Tetris();
			game.setLocationRelativeTo(null);
			game.setVisible(true);
		});
	}
}