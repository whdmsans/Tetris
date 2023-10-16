package kr.ac.jbnu.se.tetris;

import java.util.Random;

public class Shape { // ShapeŬ������ �ν��Ͻ��� �ϳ��� ���

	private Tetrominoes pieceShape; // ����� ������ �����ϴ� ����
	private int coords[][];
	/*
	���� ��� ����� �����ϴ� 2���� �迭
	coords�� ���� ĭ�� ��Ÿ���� �ε��� (0 ~ 3) 4��
	���� ĭ�� ��ġ�� ��Ÿ���� �ε��� (0, 1) 2��
	0�� x��ǥ, 1�� y��ǥ
	 */

	private int[][][] coordsTable; // ��� ����� ����� �����ص� 3���� �迭

	public Shape() { // ������
		coords = new int[4][2];
		setShape(Tetrominoes.NoShape); // �� �������� �ʱ�ȭ
	}

	public void setShape(Tetrominoes shape) {

		coordsTable = new int[][][] { // ��� ����� ����� �����ص� 3���� �迭
				{{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }// �� ����
				},
				{{ 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 }//�����
				},
				{{ 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 }//�׸���
				},
				{{ 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }//����
				},
				{{ -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 }//���κ�
				},
				{{ 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } //���ú�
				},
				{{ -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }//��ī�̺�
				},
				{{ 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }//��������
				},
				{{ 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }
				}
		};

		// �Ķ���ͷ� ���� ��� ������ Ŭ���� ����
		for (int i = 0; i < 4; i++) { // ����� ��� ����
			for (int j = 0; j < 2; ++j) {
				coords[i][j] = coordsTable[shape.ordinal()][i][j];
			}
		}
		pieceShape = shape; // ����� ���� ����

	}
	private void setX(int index, int x) {
		coords[index][0] = x; // ����� index��° ĭ�� x��ǥ ����
	}

	private void setY(int index, int y) {
		coords[index][1] = y; // ����� index��° ĭ�� y��ǥ ����
	}

	public int x(int index) {
		return coords[index][0]; // ����� index��° ĭ�� x��ǥ ��ȯ
	}

	public int y(int index) {
		return coords[index][1]; // ����� index��° ĭ�� y��ǥ ��ȯ
	}

	public Tetrominoes getShape() {
		return pieceShape; // ����� ������ ��ȯ
	}

	public void setRandomShape() { // ���� x�� ����� 7���� ��ϵ� �߿� �������� ����
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 7 + 1;
		Tetrominoes[] values = Tetrominoes.values();
		setShape(values[x]);
	}

	public int minX() { // ����� ���� ���� ĭ�� x��ǥ�� ��ȯ
		int m = coords[0][0];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][0]);
		}
		return m;
	}

	public int minY() { // ����� ���� �Ʒ��� ĭ�� y��ǥ�� ��ȯ
		int m = coords[0][1];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][1]);
		}
		return m;
	}

	public Shape rotateLeft() { // ���� �������� ȸ���ϴ� �ż��� (������ �� ������ ����)
		if (pieceShape == Tetrominoes.SquareShape) // ����� �簢���� ��� ����
			return this;

		Shape result = new Shape();
		result.pieceShape = pieceShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, y(i));
			result.setY(i, -x(i));
		}
		return result;
	}

	public Shape rotateRight() { // ���� ���������� ȸ���ϴ� �ż��� (������ �� ������ ����)
		if (pieceShape == Tetrominoes.SquareShape) // ����� �簢���� ��� ����
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