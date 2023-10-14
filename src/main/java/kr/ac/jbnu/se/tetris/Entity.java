package kr.ac.jbnu.se.tetris;

import java.util.Random;

public class Entity {
    private Tetrominoes shape;
    protected int coords[][];
    public Entity(Tetrominoes shape){
        initFunc(shape);
    }
    private void initFunc(Tetrominoes shape){
        this.shape = shape;
        this.coords = shape.getShapeArr();
    }
    private void updateX(int index, int newX){ coords[index][0] = newX; }
    private void updateY(int index, int newY){ coords[index][1] = newY; }
    public int x( int index ){ return coords[index][0]; }
    public int y( int index ){ return coords[index][1]; }
    public Tetrominoes getShape(){ return shape; }
    public int minX() { // 블록의 가장 왼쪽 칸의 x좌표를 반환
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }
    public int minY() { // 블록의 가장 아래쪽 칸의 y좌표를 반환
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }
    public Entity rotateLeft() { // 블럭을 왼쪽으로 회전하는 매서드 (구현에 별 내용은 없음)
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
    public Entity rotateRight() { // 블럭을 오른쪽으로 회전하는 매서드 (구현에 별 내용은 없음)
        if (getShape() == Tetrominoes.SquareShape) // 블록이 사각형인 경우 종료
            return this;
        Entity result = new Entity(this.shape);
        for (int i = 0; i < 4; ++i) {
            result.updateX(i, -y(i));
            result.updateY(i, x(i));
        }
        return result;
    }
    public void setRandomShape() { // 난수 x를 만들어 7개의 블록들 중에 랜덤으로 설정
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values();
        initFunc(values[x]);
    }
}
