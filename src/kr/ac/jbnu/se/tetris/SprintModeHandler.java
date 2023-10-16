package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SprintModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final Board board;
    private final JLabel statusbar;
    private final JLabel sprintModeStatusbar;
    private JLabel gameClearStatusLabel;
    private int targetLineCount; // 목표 라인 개수
    private boolean gameClearAchieved; // Game Clear 상태 여부
    private final Random random;


    public SprintModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.board = new Board(tetris);
        this.statusbar = tetris.getStatusBar();
        this.sprintModeStatusbar = new JLabel();
        this.gameClearAchieved = false;
        this.random = new Random();
        initGameClearStatusLabel();
        updateTargetLineCount();

        // 게임 클리어 확인용 타이머 초기화 (1초마다 체크)
        Timer gameClearCheckTimer; // 게임 클리어 확인용 타이머
        gameClearCheckTimer = new Timer(1000, e -> checkGameClear());
        gameClearCheckTimer.setInitialDelay(1000); // 최초 딜레이 설정
        gameClearCheckTimer.start();
    }

    @Override
    public void startGame() {
        tetris.add(board, BorderLayout.CENTER);
        statusbar.setText(" 0");
        tetris.add(sprintModeStatusbar, BorderLayout.NORTH);
        board.start();
        updateTargetLineCount();
        updateStatusbarText();
        gameClearAchieved = false;
        this.board.setLayout(new OverlayLayout(this.board));
        this.board.add(gameClearStatusLabel);
        gameClearStatusLabel.setVisible(false);
        checkGameClear();
        board.requestFocusInWindow();
        statusbar.setVisible(true);
        sprintModeStatusbar.setVisible(true);
    }

    public void checkGameClear() {
        if (board.getNumLinesRemoved() >= targetLineCount && !gameClearAchieved) {
            gameClearAchieved = true;
            gameClearStatusLabel.setText("Game Clear!");
            gameClearStatusLabel.setVisible(true);
            board.timer.stop();
            board.setEnabled(false);
            tetris.repaint();
        }
    }

    private void updateTargetLineCount() {
        // 랜덤으로 20 또는 40 선택
        targetLineCount = random.nextBoolean() ? 20 : 40;
    }

    private void updateStatusbarText() {
        sprintModeStatusbar.setText("Remove " + targetLineCount + " lines!");
    }

    private void initGameClearStatusLabel() {
        this.gameClearStatusLabel = new JLabel("Game Clear!");
        this.gameClearStatusLabel.setForeground(Color.YELLOW);
        this.gameClearStatusLabel.setBackground(Color.BLACK);
        this.gameClearStatusLabel.setOpaque(true);
        this.gameClearStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        this.gameClearStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 50)); // 폰트 및 크기 설정
    }
}
