//package kr.ac.jbnu.se.tetris.Control.Handler;
//
//import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
//import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
//import kr.ac.jbnu.se.tetris.Tetris;
//import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Random;
//
//public class SurvivalModeHandler implements GameModeHandler {
//    private final Tetris tetris;
//    private final TetrisCanvas board;
//
//    private final Timer lineAdditionTimer;
//    private final Random random;
//    /*boundary.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
//    //boundary2.setPreferredSize(new Dimension(windowWidth/3,windowHeight));
//    //boundaryAI.setPreferredSize(new Dimension(windowWidth/3,windowHeight));z*/
//
//    public SurvivalModeHandler(Tetris tetris) {
//        this.tetris = tetris;
//        this.board = new TetrisCanvas(tetris);
//        this.random = new Random();
//        this.lineAdditionTimer = new Timer(2000, new LineAdditionListener()); // 2초마다 한 줄 추가
//    }
//
//    @Override
//    public void startGame() {
//        tetris.remove(board); // 이전 게임 보드 제거
//        tetris.add(board, BorderLayout.SOUTH); // 새 게임 보드 추가
//        getBoundary();
//        board.start();
//        lineAdditionTimer.start();
//        tetris.revalidate();
//        tetris.repaint();
//        board.requestFocusInWindow();
//    }
//
//    @Override
//    public void getBoundary() {
//        tetris.updateP1(this.board);
//    }
//
//    // 2초마다 한 줄 추가하는 ActionListener
//    class LineAdditionListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (TestMonitor.getScoreBar(false).getText().equals("game over")){
//                lineAdditionTimer.stop();
//                return;
//            }
//
//            int holePosition = random.nextInt(TetrisCanvas.BoardWidth); // 뚫린 위치
//            for (int x = 0; x < TetrisCanvas.BoardWidth; x++) {
//                for (int y = TetrisCanvas.BoardHeight - 1; y > 0; y--) {
//                    Tetrominoes shape = board.shapeAt(x, y - 1);
//                    board.board[y * TetrisCanvas.BoardWidth + x] = shape;
//                }
//            }
//
//            // 랜덤한 위치에 한 칸 뚫린 라인 생성
//            for (int x = 0; x < TetrisCanvas.BoardWidth; x++) {
//                if (x != holePosition) {
//                    board.board[x] = Tetrominoes.GrayLineShape;
//                } else {
//                    board.board[x] = Tetrominoes.NoShape;
//                }
//            }
//
//            board.repaint();
//        }
//    }
//}