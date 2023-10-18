package kr.ac.jbnu.se.tetris.Entity;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;

import java.util.Random;

public class Entity {

    /**
     * Boundary클래스의 curX=0, curY=1가 Entity에게 승계
     */
    public int[] position = new int[2];
    /**
     * 블럭 형상 정보 식별자
     */
    private Tetrominoes shape;
    /**
     * 블럭 형상 정보 배열 n칸 x=0 ,y=1 좌표
     */
    protected int coords[][];

    public Entity(Tetrominoes shape) {
        initFunc(shape);
    }

    public void copyEntity(Entity entity) {
        this.shape = entity.getShape();
        setPosition(entity.getCurX(), entity.getCurY());
        setShapeArr(entity.getShapeArr());
    }

    /**
     * Tetrominoes에 고정 정보들에 대한 접근 및 복사
     */
    private void initFunc(Tetrominoes shape) {
        this.shape = shape;
        this.coords = shape.getShapeArr();
        setPosition(TetrisCanvas.BoardWidth / 2 + 1, TetrisCanvas.BoardHeight - 1 - maxY());
    }

    /**
     * 형상 배열 index행 x값=0 인덱싱후 수정
     */
    private void updateX(int index, int newX) {
        coords[index][0] = newX;
    }

    /**
     * 형상 배열 index행 y값=1 인덱싱후 수정
     */
    private void updateY(int index, int newY) {
        coords[index][1] = newY;
    }

    public void updateCurX(int x) {
        this.position[0] = x;
    }

    public void updateCurY(int y) {
        this.position[1] = y;
    }

    public int getCurX() {
        return this.position[0];
    }

    public int getCurY() {
        return this.position[1];
    }

    /**
     * 형상 배열 index행 x값=0 인덱싱후 리턴
     */
    public int x(int index) {
        return coords[index][0];
    }

    /**
     * 형상 배열 index행 y값=1 인덱싱후 리턴
     */
    public int y(int index) {
        return coords[index][1];
    }

    /**
     * 블럭 형상 식별자 반환 Tetrominoes 타입
     */
    public Tetrominoes getShape() {
        return shape;
    }

    /**
     * 블럭 형상 배열 반환
     */
    public int[][] getShapeArr() {
        return coords;
    }


    public void setShapeArr(int[][] coords) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                this.coords[i][j] = coords[i][j];
            }
        }
    }

    /**
     * 블록의 가장 왼쪽 칸의 x좌표를 반환
     */
    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    /**
     * 최저높이 Y값 리턴
     */
    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    /**
     * 최고높이 Y값 리턴
     */
    public int maxY() { // 블록의 가장 위쪽 칸의 y좌표를 반환 - 구현 동기가 맞는지 확인 요청
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.max(m, coords[i][1]);
        }
        return m;
    }

    /**
     * 복사된 entity를 newPiece에 전달, tryMove가 처리. 좌회전
     */
    public Entity rotateLeft() {
        if (getShape() == Tetrominoes.SquareShape) // 블록이 사각형인 경우 종료
            return this;
        Entity result = new Entity(this.shape);
        /*
         * color는 고정으로 얕은 복사여도 되나, shape는 clone()으로 깊은복사해서 entity에 던져주는게 맞음.
         * enum type은 공유되는 공간으로써 인스턴스의 변수들은 공유되는 것으로 사료됨.
         * */
        for (int i = 0; i < 4; ++i) {
            result.updateX(i, y(i));
            result.updateY(i, -x(i));
        }
        return result;
    }

    /**
     * 복사된 entity를 newPiece에 전달, tryMove가 처리. 우회전
     */
    public void rotateRight() {
        if (getShape() == Tetrominoes.SquareShape) // 블록이 사각형인 경우 종료
            return;
        int[][] result = new int[4][2];
        for (int i = 0; i < 4; ++i) {
            result[i][0] = -coords[i][1];
            result[i][1] = coords[i][0];
        }
        setShapeArr(result);
        return;
    }

    /**
     * 블럭 초기화시점에 랜덤화
     */
    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values();
        initFunc(values[x]);
    }


    //추가된 코드
    public int getNumOfRotate() {
        //switch (pieceShape) { origin code
        switch (getShape()) {
            case TShape:
            case LShape:
            case MirroredLShape:
                return 4;
            case ZShape:
            case SShape:
            case LineShape:
                return 2;
            case SquareShape:
                return 1;
            case NoShape:
            default:
                return 0;
        }
    }

    public void setPosition(int x, int y) {
        updateCurX(x);
        updateCurY(y);
    }
}
