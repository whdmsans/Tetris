package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControl extends KeyAdapter{
    boolean isLeft,isRight,isUp,isDown,isOne,isDrop;
    boolean isLeftP2,isRightP2,isUpP2,isDownP2,isOneP2,isDropP2;
    Boundary player1, player2;
    Tetris game;
    public KeyControl(Tetris game){
        this.game = game;

        this.player1 = game.boundary;
        isLeft=false;
        isRight=false;
        isUp=false;
        isDown=false;
        isOne=false;
        isDrop=false;

        this.player2 = game.boundary2;
        isLeftP2=false;
        isRightP2=false;
        isUpP2=false;
        isDownP2=false;
        isOneP2=false;
        isDropP2=false;
    }
    public boolean isSingle(){return player2==null;}
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(!isSingle()){
            //추후 수정 요망 1플레이어만 구성함.
            if(key == KeyEvent.VK_J&&!isRightP2){isLeftP2 = false;TestMonitor.setLeftKey(isLeftP2);}
            if(key == KeyEvent.VK_L&&!isLeftP2){isRightP2 = false; TestMonitor.setRightKey(isRightP2);}
            if(key == KeyEvent.VK_I&&!isDownP2){isUpP2 = false; TestMonitor.setUpKey(isUpP2);}
            if(key == KeyEvent.VK_K&&!isUpP2){isDownP2 = false; TestMonitor.setDownKey(isDownP2);}
            if(key == ']'&&!isOneP2){isDropP2 = false; TestMonitor.setSpaceKey(isDropP2);}
            if(key == '['&&!isDropP2){isOneP2 = false; TestMonitor.setDKey(isOneP2);}
        }
        if(key == KeyEvent.VK_LEFT&&!isRight){isLeft = false; TestMonitor.setLeftKey(isLeft);}
        if(key == KeyEvent.VK_RIGHT&&!isLeft){isRight = false; TestMonitor.setRightKey(isRight);}
        if(key == KeyEvent.VK_UP&&!isDown){isUp = false; TestMonitor.setUpKey(isUp);}
        if(key == KeyEvent.VK_DOWN&&!isUp){isDown = false; TestMonitor.setDownKey(isDown);}
        if(key == KeyEvent.VK_SPACE&&!isOne){isDrop = false; TestMonitor.setSpaceKey(isDrop);}
        if(key == KeyEvent.VK_D&&!isDrop){isOne = false; TestMonitor.setDKey(isOne);}
        doKeyLogic();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if ((!player1.isStarted || player1.getCurPiece().getShape() == Tetrominoes.NoShape)&&
                (!player2.isStarted || player2.getCurPiece().getShape() == Tetrominoes.NoShape)) {
            return;
        }
        int key = e.getKeyCode();
        if(key=='p'||key=='P'){ player1.pause(); player2.pause(); return; }
        if(!isSingle()){
            if (player2.isStarted || player2.getCurPiece().getShape() != Tetrominoes.NoShape || !player2.isPaused()) {
                if(key == KeyEvent.VK_J&&!isRightP2){isLeftP2 = true;TestMonitor.setLeftKey(isLeftP2);}
                if(key == KeyEvent.VK_L&&!isLeftP2){isRightP2 = true; TestMonitor.setRightKey(isRightP2);}
                if(key == KeyEvent.VK_I&&!isDownP2){isUpP2 = true; TestMonitor.setUpKey(isUpP2);}
                if(key == KeyEvent.VK_K&&!isUpP2){isDownP2 = true; TestMonitor.setDownKey(isDownP2);}
                if(key == ']'&&!isOneP2){isDropP2 = true; TestMonitor.setSpaceKey(isDropP2);}
                if(key == '['&&!isDropP2){isOneP2 = true; TestMonitor.setDKey(isOneP2);}
            }
        }
        if (player1.isStarted || player1.getCurPiece().getShape() != Tetrominoes.NoShape || !player1.isPaused()) {
            if(key == KeyEvent.VK_LEFT&&!isRight){isLeft = true;TestMonitor.setLeftKey(isLeft);}
            if(key == KeyEvent.VK_RIGHT&&!isLeft){isRight = true; TestMonitor.setRightKey(isRight);}
            if(key == KeyEvent.VK_UP&&!isDown){isUp = true; TestMonitor.setUpKey(isUp);}
            if(key == KeyEvent.VK_DOWN&&!isUp){isDown = true; TestMonitor.setDownKey(isDown);}
            if(key == KeyEvent.VK_SPACE&&!isOne){isDrop = true; TestMonitor.setSpaceKey(isDrop);}
            if(key == KeyEvent.VK_D&&!isDrop){isOne = true; TestMonitor.setDKey(isOne);}
        }
        doKeyLogic();
    }
    public void doKeyLogic(){
        /**
         * 일시정지, 드롭다운은 즉각 처리. switch문 이전에 KeyPressed에서 처리함
         * 아래는 키값의 상수처리, 이를 합연산으로 처리 구분*/
        if(!isSingle()){
            if(isDropP2){player2.dropDown();return;}
            if(isLeftP2) player2.tryMove(player2.getCurPiece(),player2.getCurX()-1,player2.getCurY());
            if(isRightP2) player2.tryMove(player2.getCurPiece(),player2.getCurX()+1,player2.getCurY());
            if(isUpP2) player2.tryMove(player2.getCurPiece().rotateLeft(),player2.getCurX(),player2.getCurY());
            if(isDownP2) player2.tryMove(player2.getCurPiece().rotateRight(),player2.getCurX(),player2.getCurY());
            if(isOneP2) player2.tryMove(player2.getCurPiece(),player2.getCurX(),player2.getCurY()-1);
        }
        if(isDrop) {player1.dropDown(); return;}
        if(isLeft) player1.tryMove(player1.getCurPiece(),player1.getCurX()-1,player1.getCurY());
        if(isRight) player1.tryMove(player1.getCurPiece(),player1.getCurX()+1,player1.getCurY());
        if(isUp) player1.tryMove(player1.getCurPiece().rotateLeft(),player1.getCurX(),player1.getCurY());
        if(isDown) player1.tryMove(player1.getCurPiece().rotateRight(),player1.getCurX(),player1.getCurY());
        if(isOne) player1.tryMove(player1.getCurPiece(),player1.getCurX(),player1.getCurY()-1);
    }
}
