package kr.ac.jbnu.se.tetris.Entity;

public enum LabelEnums {

    notYet("미개발단계");
    /*keyLeft("P1 Left : "), keyRight("P2 Right : "), keyUp, keyDown, keyD, keySpace, curDxP1, curDyP1, scoreBarP1,
    keyJ, keyL, keyI, keyK, keyBraceL,keyBraceR, curDxP2, curDyP2, scoreBarP2,
    keyP;*/
    private String labelString;
    LabelEnums(String string){
        this.labelString=string;
    }
}
