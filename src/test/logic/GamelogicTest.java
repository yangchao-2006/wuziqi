package test.logic;

import main.gobang.logic.Gamelogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GamelogicTest {

    @Test
    void testHorizontalWin() {
        int[][] board = new int[15][15];
        for (int i = 0; i < 5; i++) {
            board[i][5] = 1;
        }
        assertTrue(Gamelogic.checkWin(board, 2, 5, 1));
        assertTrue(Gamelogic.checkWin(board, 4, 5, 1));
        assertFalse(Gamelogic.checkWin(board, 0, 5, 2));
    }

    @Test
    void testVerticalWin() {
        int[][] board = new int[15][15];
        for (int j = 0; j < 5; j++) {
            board[7][j] = 2;
        }
        assertTrue(Gamelogic.checkWin(board, 7, 3, 2));
    }

    @Test
    void testDiagonalWin() {
        int[][] board = new int[15][15];
        for (int i = 0; i < 5; i++) {
            board[i][i] = 1;
        }
        assertTrue(Gamelogic.checkWin(board, 2, 2, 1));
    }

    @Test
    void testAntiDiagonalWin() {
        int[][] board = new int[15][15];
        for (int i = 0; i < 5; i++) {
            board[i][14 - i] = 1;
        }
        assertTrue(Gamelogic.checkWin(board, 2, 12, 1));
    }

    @Test
    void testNoWin() {
        int[][] board = new int[15][15];
        board[0][0] = 1;
        board[0][1] = 1;
        board[0][2] = 1;
        board[0][3] = 1;
        // 只有四连，未赢
        assertFalse(Gamelogic.checkWin(board, 0, 3, 1));
    }

    @Test
    void testBoundaryWin() {
        int[][] board = new int[15][15];
        for (int i = 10; i < 15; i++) {
            board[i][10] = 2;
        }
        assertTrue(Gamelogic.checkWin(board, 12, 10, 2));
    }
}