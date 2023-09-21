package kr.ac.jbnu.se.tetris;

import java.awt.*;

public enum Tetrominoes {
	// NoShape는 빈 공간을 의미
	NoShape(0), ZShape(1), SShape(2), LineShape(3), TShape(4), SquareShape(5), LShape(6), MirroredLShape(7);
	private int idx;
	Tetrominoes(int idx){
		this.idx = idx;
	}
	public Color getColor(){
		Color color;
		switch(idx) {
			case 0:
				color = new Color(0, 0, 0);
				break;
			case 1:
				color = new Color(204, 102, 102);
				break;
			case 2:
				color = new Color(102, 204, 102);
				break;
			case 3:
				color = new Color(102, 102, 204);
				break;
			case 4:
				color = new Color(204, 204, 102);
				break;
			case 5:
				color = new Color(204, 102, 204);
				break;
			case 6:
				color = new Color(102, 204, 204);
				break;
			case 7:
				color = new Color(218, 170, 0);
				break;
			default:
				color = null;
		}
		return color;
	}
	public int[][] getShapeArr() {
		int[][] shape;
		switch(idx){
			case 0:
				shape = new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } };
				break;
			case 1:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }.clone();
				break;
			case 2:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }.clone();
				break;
			case 3:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }.clone();
				break;
			case 4:
				shape = new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }.clone();
				break;
			case 5:
				shape = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }.clone();
				break;
			case 6:
				shape = new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }.clone();
				break;
			case 7:
				shape = new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }.clone();
				break;
			default:
				return null;
		}
		return shape;
	}
}
