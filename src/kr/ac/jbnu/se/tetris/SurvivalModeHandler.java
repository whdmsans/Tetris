package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SurvivalModeHandler implements GameModeHandler {
    private final Tetris tetris;
    private final Board board;
    private final JLabel statusbar;

    private final Timer lineAdditionTimer;
    private final Random random;

    public SurvivalModeHandler(Tetris tetris) {
        this.tetris = tetris;
        this.board = new Board(tetris);
        this.statusbar = tetris.getStatusBar();
        this.random = new Random();
        this.lineAdditionTimer = new Timer(2000, new LineAdditionListener()); // 2초마다 한 줄 추가
    }

    @Override
    public void startGame() {
        tetris.remove(board); // 이전 게임 보드 제거
        tetris.add(board, BorderLayout.CENTER); // 새 게임 보드 추가
        statusbar.setText(" 0");
        board.start();
        lineAdditionTimer.start();
        tetris.revalidate();
        tetris.repaint();
        board.requestFocusInWindow();
        statusbar.setVisible(true);
    }

    // 2초마다 한 줄 추가하는 ActionListener
    class LineAdditionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (statusbar.getText().equals("game over")){
                lineAdditionTimer.stop();
                return;
            }

            int holePosition = random.nextInt(board.BoardWidth); // 뚫린 위치
            for (int x = 0; x < board.BoardWidth; x++) {
                for (int y = board.BoardHeight - 1; y > 0; y--) {
                    Tetrominoes shape = board.shapeAt(x, y - 1);
                    board.board[y * board.BoardWidth + x] = shape;
                }
            }

            // 랜덤한 위치에 한 칸 뚫린 라인 생성
            for (int x = 0; x < board.BoardWidth; x++) {
                if (x != holePosition) {
                    board.board[x] = Tetrominoes.GrayLineShape;
                } else {
                    board.board[x] = Tetrominoes.NoShape;
                }
            }

            board.repaint();
        }
    }
}