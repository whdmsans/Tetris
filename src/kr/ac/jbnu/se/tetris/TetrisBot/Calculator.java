package kr.ac.jbnu.se.tetris.TetrisBot;

import java.util.*;

public class Calculator {
    private int[] board;

    private final int boardWidth;
    private final int boardHeight;

    public Calculator (int[] board, int boardWidth, int boardHeight) {
        this.board = board;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public Double blockFitness(double[] weight) {
        double result = 0.0;

        int hc = hole_count();
        int cl = complete_line();

        int[] height = new int[boardHeight];
        int ah = aggregate_height(height);
        int b = bumpiness(height);

        result += ah * weight[0];
        result += hc * weight[1];
        result += b * weight[2];
        result += cl * weight[3];

        //System.out.println("h : " + hc + " c : " + cl + " bw : " + b + " ah : " + ah);
        return result;
    }

    // 완성된 줄을 찾는 메소드
    private int complete_line() {
        int ret = 0;
        for (int i = 0; i < boardHeight; i++) {
            int j;
            for (j = 0; j < boardWidth; j++) {
                if (board[j * boardWidth + i] == 0)
                    break;
            }

            if (j == boardWidth)
                ret++;
        }

        return ret;
    }

    private int bumpiness(int height[]) {
        int ret = 0;
        for (int i = 1; i < boardWidth; i++) {
            ret += Math.abs(height[i - 1] - height[i]);
        }
        return ret;
    }

    private int aggregate_height(int height[]) {
        for (int i = 0; i < boardWidth; i++) {
            int high = boardHeight - 1;
            while (high >= 0) {
                if (board[high * boardWidth + i] != 0) {
                    break;
                }
                high--;
            }
            height[i] = high + 1;
        }

        int ret = 0;
        for (int i = 0; i < boardHeight; i++) {
            ret += height[i];
        }
        return ret;
    }

    // 테트리스 블럭들 사이에 존재하는 구멍을 구하는 메소드
    private int hole_count() {
        boolean[][] visited = new boolean[boardHeight][boardWidth];

        // 테트리스 보드판에 0이 아닌 지점을 찾는다.
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (board[i * boardWidth + j] != 0)
                    visited[i][j] = true;
            }
        }

        // (0,4)를 기준으로 해서 bfs를 진행한다.
        bfs(visited);

        int ret = 0;

        // bfs를 진행하고 boolean 값이 false인 값은 구멍이다.
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (!visited[i][j])
                    ret++;
            }
        }

        return ret;
    }

    private void bfs(boolean[][] visited) {
        int ud[] = { -1, 0, 1, 0 };
        int rl[] = { 0, 1, 0, -1 };

        Queue<Point> q = new LinkedList<>();
        q.add(new Point(0, 4));
        visited[0][4] = true;

        while (!q.isEmpty()) {
            Point cur = q.poll();

            for (int i = 0; i < 4; i++) {
                int nx = cur.x + ud[i];
                int ny = cur.y + rl[i];

                if (nx < 0 || nx >= boardHeight || ny < 0 || ny >= boardWidth || visited[nx][ny])
                    continue;

                visited[nx][ny] = true;
                q.add(new Point(nx, ny));
            }
        }
    }
}