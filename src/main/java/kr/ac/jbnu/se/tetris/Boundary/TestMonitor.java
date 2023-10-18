package kr.ac.jbnu.se.tetris.Boundary;

import javax.swing.*;
import java.util.*;

/**
 * 레이블 명 넘겨주면 찾을 수 있음.
 * 단, value는 절대 직접설정하지 말것. updateBoard의
 * 1번째 인자로 key값을 찾고, 2번째인자로 JLabel을 setText함.
 * */
public class TestMonitor extends JPanel {
    static Map<String, JLabel> labelMap = new HashMap<>();
    String[] labelTag = {
            "keyLeft", "keyRight", "keyUp", "keyDown", "keyD", "keySpace", "curDxP1", "curDyP1", "scoreBarP1",
            "keyJ", "keyL", "keyI", "keyK", "keyBraceL", "keyBraceR", "curDxP2", "curDyP2", "scoreBarP2",
            "keyP"
    };
    public TestMonitor(){ initBoard(); }
    public static void updateBoard(String name, String content){
        labelMap.get(name).setText(content);
    }
    /**
     * 이니시에이션 메소드. testMonitor에 뿌려줄 JLabel 인스턴스들의 초기화과정
     */
    private void initBoard(){
        for(int i = 0; i<labelTag.length; i++){
            labelMap.put(labelTag[i],new JLabel("null"));
            add(labelMap.get(labelTag[i]));
        }
    }
}