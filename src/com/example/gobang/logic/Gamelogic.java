package com.example.gobang.logic;   // 注意小写

public class Gamelogic {
    public static boolean checkWin(int[][] board, int x, int y, int player) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = 1;
            // 正方向
            for (int step = 1; step < 5; step++) {
                int nx = x + dir[0] * step;
                int ny = y + dir[1] * step;
                if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length && board[nx][ny] == player) {
                    count++;
                } else break;
            }
            // 反方向
            for (int step = 1; step < 5; step++) {
                int nx = x - dir[0] * step;
                int ny = y - dir[1] * step;
                if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length && board[nx][ny] == player) {
                    count++;
                } else break;
            }
            if (count >= 5) return true;
        }
        return false;
    }
}