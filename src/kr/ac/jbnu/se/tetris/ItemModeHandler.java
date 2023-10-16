package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ItemModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final Board board;
    private final JLabel statusbar;

    private final Timer itemTimer;

    public ItemModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.board = new Board(tetris);
        this.statusbar = tetris.getStatusBar();

        // 아이템 타이머 생성 및 리스너 등록
        itemTimer = new Timer(10000, e -> removeRandomLine());

        // statusbar의 텍스트 변경을 모니터링하는 리스너 등록
        statusbar.addPropertyChangeListener("text", evt -> {
            if ("game over".equals(evt.getNewValue())) {
                itemTimer.stop(); // statusbar의 텍스트가 "game over"로 변경되면 아이템 타이머를 중지
            }
        });
    }

    @Override
    public void startGame() {
        tetris.add(board, BorderLayout.CENTER);
        statusbar.setText(" 0");
        board.start();
        tetris.revalidate();
        tetris.repaint();
        board.requestFocusInWindow();
        statusbar.setVisible(true);

        itemTimer.start();
    }

    // 랜덤한 가로줄 또는 세로줄 제거
    private void removeRandomLine() {
        Random random = new Random();
        int item = random.nextInt(2); // 0 또는 1 (가로줄 또는 세로줄)

        if (item == 0) {
            removeRandomRow();
        } else {
            removeRandomColumn();
        }
    }

    // 랜덤한 가로줄 제거
    private void removeRandomRow() {
        int rowToRemove = (int) (Math.random() * board.BoardHeight);
        for (int i = 0; i < board.BoardWidth; i++) {
            board.board[rowToRemove * board.BoardWidth + i] = Tetrominoes.NoShape;
        }
        board.repaint();
    }

    // 랜덤한 세로줄 제거
    private void removeRandomColumn() {
        int colToRemove = (int) (Math.random() * board.BoardWidth);
        for (int i = 0; i < board.BoardHeight; i++) {
            board.board[i * board.BoardWidth + colToRemove] = Tetrominoes.NoShape;
        }
        board.repaint();
    }
}