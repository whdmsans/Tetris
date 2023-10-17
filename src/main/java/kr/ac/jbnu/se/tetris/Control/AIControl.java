package kr.ac.jbnu.se.tetris.Control;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.AIMode.AIMode;
import kr.ac.jbnu.se.tetris.Entity.Entity;
/**
 * 이 클래스를 추가할 thread가 호출된 메소드쪽에 thread명.setDaemon(true);
 * 해주면 해당 메소드 종료시 소거. 예를 들어 main함수 종료시 같이 종료.
 * void run을 disable하고자 할 경우 이 runnable을 추가해줬던 thread구문에서
 * thread.interrupt(); 수행
 * */
public class AIControl implements Runnable {
    public static boolean isLeftAI, isRightAI, isUpAI, isDownAI, isOneAI, isDropAI;
    private TetrisCanvas canvas;
    private AIMode aiMode;
    public AIControl(TetrisCanvas canvas) {
        this.canvas = canvas;
        this.isLeftAI=false;
        this.isRightAI=false;
        this.isUpAI=false;
        this.isDownAI=false;
        this.isOneAI=false;
        this.isDropAI=false;
        aiMode = new AIMode(canvas);
    }
    /**
     * boolean static을 토글링해 제어, 해당 구문은 알고리즘 계산부에서 토글링하면 될 것이라 봄.
     * 구문 쓰레드 처리 또는 timer class로 구현 필요.
     * */
    private void doAiLogic() {
        Entity Piece = new Entity(canvas.getCurPiece());

        // 최적의 위치 찾기
        int[] goodPosition = aiMode.findGoodPosition(Piece);

        // 최적의 모양으로 회전
        for(int i = 0; i < goodPosition[2]; i++) {
            canvas.tryMove(getCurPiece(canvas).rotateRight(),getX(canvas),getY(canvas));
        }

        // 최적의 X좌표로 이동
        int num = getCurPiece(canvas).getCurX() - goodPosition[0];
        while (num != 0) {
            if (num > 0) {
                canvas.tryMove(getCurPiece(canvas),getX(canvas)-1,getY(canvas));
                num--;
            } else if (num < 0) {
                canvas.tryMove(getCurPiece(canvas),getX(canvas)+1,getY(canvas));
                num++;
            }
        }
    }

    private void doTestAiLogic() {
        Entity Piece = new Entity(canvas.getCurPiece());
        // 최적의 위치 찾기
        getCurPiece(canvas).copyEntity(aiMode.findGoodPositionTest(Piece));
    }

    Entity getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }

    private boolean disableBoolean() {
        isLeftAI=false;
        isRightAI=false;
        isUpAI=false;
        isDownAI=false;
        isOneAI=false;
        isDropAI=false;
        return true;
    }

    @Override
    public void run() {

        doAiLogic();
//        doTestAiLogic();

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
