//package kr.ac.jbnu.se.tetris.Control.Handler;
//
//import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
//import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
//import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;
//import kr.ac.jbnu.se.tetris.Tetris;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Random;
//
//public class ItemModeHandler implements GameModeHandler {
//    private final Tetris tetris;
//    private final TetrisCanvas board;
//    private final Timer itemTimer;
//
//    public ItemModeHandler(Tetris tetris) {
//        this.tetris = tetris;
//        this.board = new TetrisCanvas(tetris);
//
//        // 아이템 타이머 생성 및 리스너 등록
//        itemTimer = new Timer(10000, e -> removeRandomLine());
//
//        // statusbar의 텍스트 변경을 모니터링하는 리스너 등록
//        TestMonitor.getScoreBar(false).addPropertyChangeListener("text", evt -> {
//            if ("game over".equals(evt.getNewValue())) {
//                itemTimer.stop(); // statusbar의 텍스트가 "game over"로 변경되면 아이템 타이머를 중지
//            }
//        });
//    }
//
//    @Override
//    public void startGame() {
//        tetris.add(board, BorderLayout.CENTER);
//        getBoundary();
//        board.start();
//        tetris.revalidate();
//        tetris.repaint();
//        board.requestFocusInWindow();
//
//        itemTimer.start();
//    }
//
//    @Override
//    public void getBoundary() {
//        tetris.updateP1(this.board);
//    }
//
//    // 랜덤한 가로줄 또는 세로줄 제거
//    private void removeRandomLine() {
//        Random random = new Random();
//        int item = random.nextInt(2); // 0 또는 1 (가로줄 또는 세로줄)
//
//        if (item == 0) {
//            removeRandomRow();
//        } else {
//            removeRandomColumn();
//        }
//    }
//
//    // 랜덤한 가로줄 제거
//    private void removeRandomRow() {
//        int rowToRemove = (int) (Math.random() * TetrisCanvas.BoardHeight);
//        for (int i = 0; i < TetrisCanvas.BoardWidth; i++) {
//            board.board[rowToRemove * TetrisCanvas.BoardWidth + i] = Tetrominoes.NoShape;
//        }
//        board.repaint();
//    }
//
//    // 랜덤한 세로줄 제거
//    private void removeRandomColumn() {
//        int colToRemove = (int) (Math.random() * TetrisCanvas.BoardWidth);
//        for (int i = 0; i < TetrisCanvas.BoardHeight; i++) {
//            board.board[i * TetrisCanvas.BoardWidth + colToRemove] = Tetrominoes.NoShape;
//        }
//        board.repaint();
//    }
//}