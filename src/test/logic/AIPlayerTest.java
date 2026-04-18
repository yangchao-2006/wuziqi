package test.logic;

import main.gobang.logic.AIPlayer;
import main.gobang.logic.BoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {
    private BoardModel model;
    private AIPlayer ai;

    @BeforeEach
    void setUp() {
        model = new BoardModel();
        ai = new AIPlayer(model, 2); // AI 执白
    }

    @Test
    void testGetBestMoveOnEmptyBoard() {
        int[] move = ai.getBestMove();
        assertNotNull(move);
        // 空棋盘通常下在天元附近
        int center = BoardModel.BOARD_SIZE / 2;
        assertTrue(Math.abs(move[0] - center) <= 1);
        assertTrue(Math.abs(move[1] - center) <= 1);
    }

    @Test
    void testAiBlocksPlayerWin() {
        // 模拟黑棋（玩家）已有四连，AI 必须阻挡
        int[][] board = model.getBoard();
        for (int i = 0; i < 4; i++) {
            board[i][7] = 1; // 黑棋水平四连
        }
        int[] move = ai.getBestMove();
        // 应该堵在 (4,7) 或 (-1,7) 边界，实际应堵在 (4,7)
        assertNotNull(move);
        assertEquals(4, move[0]);
        assertEquals(7, move[1]);
    }

    @Test
    void testAiWinsIfPossible() {
        // 布置白棋四连，AI 应直接下成五连
        int[][] board = model.getBoard();
        for (int i = 0; i < 4; i++) {
            board[i][10] = 2; // 白棋四连
        }
        int[] move = ai.getBestMove();
        assertNotNull(move);
        assertEquals(4, move[0]);
        assertEquals(10, move[1]);
    }

    @Test
    void testNoMoveWhenBoardFull() {
        for (int i = 0; i < BoardModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardModel.BOARD_SIZE; j++) {
                model.placePiece(i, j, 1);
            }
        }
        assertNull(ai.getBestMove());
    }

    @Test
    void testAiColorSwitch() {
        ai.setAiColor(1);
        assertEquals(1, ai.getAiColor());
        // 黑棋 AI 在空棋盘的行为
        int[] move = ai.getBestMove();
        assertNotNull(move);
    }
}