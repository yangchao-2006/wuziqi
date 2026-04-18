package main.gobang.logic;

/**
 * 棋盘数据模型，负责存储棋盘状态并提供基础操作。
 */
public class BoardModel {
    public static final int BOARD_SIZE = 15;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    public BoardModel() {
        clear();
    }

    // 清空棋盘
    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
    }

    // 落子
    public boolean placePiece(int x, int y, int color) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) return false;
        if (board[x][y] != 0) return false;
        board[x][y] = color;
        return true;
    }

    // 移除棋子（悔棋）
    public void removePiece(int x, int y) {
        if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
            board[x][y] = 0;
        }
    }

    // 获取棋子颜色
    public int getPiece(int x, int y) {
        return board[x][y];
    }

    // 获取整个棋盘数组（供AI使用）
    public int[][] getBoard() {
        return board;
    }

    // 判断棋盘是否已满
    public boolean isFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }
}