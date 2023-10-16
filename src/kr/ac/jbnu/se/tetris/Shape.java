package kr.ac.jbnu.se.tetris;

import java.util.Random;

public class Shape { // Shape클래스의 인스턴스가 하나의 블록

	private Tetrominoes pieceShape; // 블록의 종류를 저장하는 변수
	private int coords[][];
	/*
	현재 블록 모양을 저장하는 2차원 배열
	coords의 행은 칸을 나타내는 인덱스 (0 ~ 3) 4개
	열은 칸의 위치를 나타내는 인덱스 (0, 1) 2개
	0은 x좌표, 1은 y좌표
	 */

	private int[][][] coordsTable; // 모든 블록의 모양을 저장해둔 3차원 배열

	public Shape() { // 생성자
		coords = new int[4][2];
		setShape(Tetrominoes.NoShape); // 빈 공간으로 초기화
	}

	public void setShape(Tetrominoes shape) {

		coordsTable = new int[][][] { // 모든 블록의 모양을 저장해둔 3차원 배열
				{{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }// 빈 공간
				},
				{{ 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 }//레드블럭
				},
				{{ 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 }//그린블럭
				},
				{{ 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }//블루블럭
				},
				{{ -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 }//옐로블럭
				},
				{{ 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } //퍼플블럭
				},
				{{ -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }//스카이블럭
				},
				{{ 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }//오렌지블럭
				},
				{{ 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }
				}
		};

		// 파라미터로 받은 블록 변수로 클래스 설정
		for (int i = 0; i < 4; i++) { // 블록의 모양 설정
			for (int j = 0; j < 2; ++j) {
				coords[i][j] = coordsTable[shape.ordinal()][i][j];
			}
		}
		pieceShape = shape; // 블록의 종류 설정

	}
	private void setX(int index, int x) {
		coords[index][0] = x; // 블록의 index번째 칸의 x좌표 설정
	}

	private void setY(int index, int y) {
		coords[index][1] = y; // 블록의 index번째 칸의 y좌표 리턴
	}

	public int x(int index) {
		return coords[index][0]; // 블록의 index번째 칸의 x좌표 반환
	}

	public int y(int index) {
		return coords[index][1]; // 블록의 index번째 칸의 y좌표 반환
	}

	public Tetrominoes getShape() {
		return pieceShape; // 블록의 종류를 반환
	}

	public void setRandomShape() { // 난수 x를 만들어 7개의 블록들 중에 랜덤으로 설정
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 7 + 1;
		Tetrominoes[] values = Tetrominoes.values();
		setShape(values[x]);
	}

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

	public Shape rotateLeft() { // 블럭을 왼쪽으로 회전하는 매서드 (구현에 별 내용은 없음)
		if (pieceShape == Tetrominoes.SquareShape) // 블록이 사각형인 경우 종료
			return this;

		Shape result = new Shape();
		result.pieceShape = pieceShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, y(i));
			result.setY(i, -x(i));
		}
		return result;
	}

	public Shape rotateRight() { // 블럭을 오른쪽으로 회전하는 매서드 (구현에 별 내용은 없음)
		if (pieceShape == Tetrominoes.SquareShape) // 블록이 사각형인 경우 종료
			return this;

		Shape result = new Shape();
		result.pieceShape = pieceShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, -y(i));
			result.setY(i, x(i));
		}
		return result;
	}

}