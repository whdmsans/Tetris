package kr.ac.jbnu.se.tetris.Control;

import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Tetris;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControl extends KeyAdapter{
    boolean isLeft,isRight,isUp,isDown,isOne,isDrop;
    boolean isLeftP2,isRightP2,isUpP2,isDownP2,isOneP2,isDropP2;
    TetrisCanvas player1, player2;
    Tetris game;
    public KeyControl(Tetris game){
        this.game = game;

        this.player1 = game.getP1();
        isLeft=false;
        isRight=false;
        isUp=false;
        isDown=false;
        isOne=false;
        isDrop=false;

        this.player2 = game.getP2();
        isLeftP2=false;
        isRightP2=false;
        isUpP2=false;
        isDownP2=false;
        isOneP2=false;
        isDropP2=false;
    }
    public boolean isSingle(){ return player2 != null; }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(isSingle()){
            //추후 수정 요망 1플레이어만 구성함.
            if(key == KeyEvent.VK_J&&!isRightP2){isLeftP2 = false;
                TestMonitor.setLeftKey(isLeftP2);}
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
        if (!player1.isStarted() || getCurPiece(player1).getShape() == Tetrominoes.NoShape) {
            return;
        }
        else if(player2!=null){
            if((!player2.isStarted() || getCurPiece(player2).getShape() == Tetrominoes.NoShape))
                return;
        }
        int key = e.getKeyCode();
        if(key=='p'||key=='P'){ player1.pause(); if(player2!=null)player2.pause(); return; }
        if(isSingle()){
            if (player2.isStarted() || getCurPiece(player2).getShape() != Tetrominoes.NoShape || !player2.isPaused()) {
                if(key == KeyEvent.VK_J&&!isRightP2){isLeftP2 = true;TestMonitor.setLeftKey(isLeftP2);}
                if(key == KeyEvent.VK_L&&!isLeftP2){isRightP2 = true; TestMonitor.setRightKey(isRightP2);}
                if(key == KeyEvent.VK_I&&!isDownP2){isUpP2 = true; TestMonitor.setUpKey(isUpP2);}
                if(key == KeyEvent.VK_K&&!isUpP2){isDownP2 = true; TestMonitor.setDownKey(isDownP2);}
                if(key == ']'&&!isOneP2){isDropP2 = true; TestMonitor.setSpaceKey(isDropP2);}
                if(key == '['&&!isDropP2){isOneP2 = true; TestMonitor.setDKey(isOneP2);}
            }
        }
        if (player1.isStarted() || getCurPiece(player1).getShape() != Tetrominoes.NoShape || !player1.isPaused()) {
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
        if(isSingle()){
            if(isDropP2){player2.dropDown();return;}
            if(isLeftP2) player2.tryMove(getCurPiece(player2),getX(player2)-1,getY(player2));
            if(isRightP2) player2.tryMove(getCurPiece(player2),getX(player2)+1,getY(player2));
            if(isUpP2) player2.tryMove(getCurPiece(player2).rotateLeft(),getX(player2),getY(player2));
            if(isDownP2) player2.tryMove(getCurPiece(player2).rotateRight(),getX(player2),getY(player2));
            if(isOneP2) player2.tryMove(getCurPiece(player2),getX(player2),getY(player2)-1);
        }
        if(isDrop) {player1.dropDown(); return;}
        if(isLeft) player1.tryMove(getCurPiece(player1),getX(player1)-1,getY(player1));
        if(isRight) player1.tryMove(getCurPiece(player1),getX(player1)+1,getY(player1));
        if(isUp) player1.tryMove(getCurPiece(player1).rotateLeft(),getX(player1),getY(player1));
        if(isDown) player1.tryMove(getCurPiece(player1).rotateRight(),getX(player1),getY(player1));
        if(isOne) player1.tryMove(getCurPiece(player1),getX(player1),getY(player1)-1);
    }
    Entity getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }
}