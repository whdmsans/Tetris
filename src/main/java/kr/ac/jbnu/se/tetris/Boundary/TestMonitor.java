package kr.ac.jbnu.se.tetris.Boundary;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMonitor extends JPanel {
    /** Brace Left is P2 OneLineDrop, and Right is P2 DropDown key */
    static JLabel
            keyLeft, keyRight, keyUp, keyDown, keyD, keySpace, curDxP1, curDyP1, scoreBarP1,
            keyJ, keyL, keyI, keyK, keyBraceL,keyBraceR, curDxP2, curDyP2, scoreBarP2,
            keyP;
    /*ArrayList<JLabel> labelArr = new ArrayList<JLabel>();
    HashMap<JLabel, String> keyMap = new HashMap<JLabel, String>();*/
    public TestMonitor(){
        initBoard();
    }
    /** 플레이어1 & 2 방향타 및 블럭키 , 일시정지버튼 : boolean <~ KeyControl.KeyPressed & Released */
    public static void setLeftKey(boolean yn) { keyLeft.setText("Left : "+yn); }
    public static void setRightKey(boolean yn) { keyRight.setText("Right : "+yn); }
    public static void setUpKey(boolean yn) { keyUp.setText("Up : "+yn); }
    public static void setDownKey(boolean yn) { keyDown.setText("Down : "+yn); }
    public static void setJKey(boolean yn) { keyJ.setText("Left 2 : "+yn); }
    public static void setLKey(boolean yn) { keyL.setText("Right 2 : "+yn); }
    public static void setIKey(boolean yn) { keyI.setText("Up 2 : "+yn); }
    public static void setKKey(boolean yn) { keyK.setText("Down 2 : "+yn); }
    public static void setSpaceKey(boolean yn) { keySpace.setText("Drop : "+yn); }
    public static void setDKey(boolean yn) { keyD.setText("OneLineDown : "+yn); }
    public static void setBraceRKey(boolean yn) { keyBraceR.setText("Drop 2 : "+yn); }
    public static void setBraceLKey(boolean yn) { keyBraceL.setText("OneLineDown 2 : "+yn); }
    public static void setPKey(boolean yn) { keyP.setText("Pause : "+yn); }
    /** { 플레이어1 & 2 좌표계, 플레이어1 & 2 스코어 } : int <~ Boundary.tryMove & removeFullLines */
    public static void setCurDxP1(int num) { curDxP1.setText(Integer.toString(num)); }
    public static void setCurDxP2(int num) { curDxP2.setText(Integer.toString(num)); }
    public static void setCurDyP1(int num) { curDyP1.setText(Integer.toString(num)); }
    public static void setCurDyP2(int num) { curDyP2.setText(Integer.toString(num)); }
    //boolean isP2는 추후 통합시킬때 삭제하여 인덱싱으로 구분 및 구분자는 boundary에서 비교후 수행. 로컬 및 소켓 플레이에서 챌린저가 2P
    public static void setScore(int num,boolean isP2){
        if(isP2) scoreBarP2.setText(Integer.toString(num));
        else scoreBarP1.setText(Integer.toString(num));
    }
    public static JLabel getScoreBar(boolean isP2){
        JLabel value = new JLabel();
        value = isP2 ? scoreBarP2 : scoreBarP1;
        return value;
    }
    /**
     * 이니시에이션 메소드. testMonitor에 뿌려줄 JLabel 인스턴스들의 초기화과정
     * 현재 하드코딩 상태로, 추후 HashSet + enum 이나 final 형태로 인덱스 상수화하여 묶음정리 요망
     * */
    private void initBoard(){
        /*JLabel[] tmpArr = {keyLeft, keyRight, keyUp, keyDown, keyD, keySpace, curDxP1, curDyP1, scoreBarP1,
                keyJ, keyL, keyI, keyK, keyBraceL,keyBraceR, curDxP2, curDyP2, scoreBarP2,
                keyP};
        for(int i = 0; i<tmpArr.length; i++){
            labelArr.add(tmpArr[i]);
        }
        JLabel[] keyLabelArr = {keyLeft, keyRight, keyUp, keyDown, keyD, keySpace,
                keyJ, keyL, keyI, keyK, keyBraceL, keyBraceR, keyP};
        for(int i = 0; i<keyLabelArr.length; i++){
            keyMap.put(keyLabelArr[i],);
        }*/
        scoreBarP1 = new JLabel("0");
        scoreBarP2 = new JLabel("0");
        curDxP1 = new JLabel("0");
        curDyP1 = new JLabel("0");
        curDxP2 = new JLabel("0");
        curDyP2 = new JLabel("0");
        keyLeft = new JLabel("null");
        keyRight = new JLabel("null");
        keyUp = new JLabel("null");
        keyDown = new JLabel("null");;
        keySpace = new JLabel("null");
        keyD = new JLabel("null");
        keyJ = new JLabel("null");
        keyL = new JLabel("null");
        keyI = new JLabel("null");
        keyK = new JLabel("null");;
        keyBraceL = new JLabel("null");
        keyBraceR = new JLabel("null");
        keyP = new JLabel("running");
        add(scoreBarP1);
        add(scoreBarP2);
        add(curDxP1);//P1 x pos
        add(curDxP2);//P2 x pos
        add(curDyP1);//P1 y pos
        add(curDyP2);//P2 y pos
        add(keyLeft);//P1 Left x--
        add(keyRight);//P1 Right x++
        add(keyUp);//P1 Up = LotateLeft
        add(keyDown);//P1 Down = LotateRight
        add(keySpace);//P1 DropDown
        add(keyD);//P1 oneLineDrop
        add(keyJ);//P2 Left x--
        add(keyL);//P2 Right x++
        add(keyI);//P2 Up = LotateLeft
        add(keyK);//P2 Down = LotateRight
        add(keyBraceL);//P2 DropDown
        add(keyBraceR);//P2 oneLineDrop
        add(keyP);//pause
    }
}