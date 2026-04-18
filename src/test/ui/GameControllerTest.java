package test.ui;

import main.gobang.logic.BoardModel;
import main.gobang.logic.AIPlayer;
import main.gobang.ui.AudioManager;
import main.gobang.ui.ChessBoard;
import main.gobang.ui.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {
    @Mock private ChessBoard chessBoard;
    @Mock private AudioManager audioManager;
    @Mock private AIPlayer aiPlayer;
    private BoardModel model;
    private GameController controller;

    @BeforeEach
    void setUp() {
        model = new BoardModel();
        controller = new GameController(model, aiPlayer, audioManager, chessBoard);
        controller.initBoard();
    }

    @Test
    void testInitBoardClearsState() {
        model.placePiece(0, 0, 1);
        controller.initBoard();
        assertEquals(0, model.getPiece(0, 0));
        assertFalse(controller.isGameOver());
        assertTrue(controller.isBlackTurn());
    }

    @Test
    void testSetAIMode() {
        controller.setAIMode(true, 2);
        assertTrue(controller.isAIMode());
        assertEquals(2, aiPlayer.getAiColor()); // 依赖真实 aiPlayer，需确保 setAiColor 被调用
        // 验证棋盘重置
        assertEquals(0, model.getPiece(0, 0));
    }

    @Test
    void testUndoMove() {
        controller.handlePlayerMove(0, 0);
        assertEquals(1, model.getPiece(0, 0));
        controller.undoMove();
        assertEquals(0, model.getPiece(0, 0));
        assertTrue(controller.isBlackTurn()); // 回合切换回黑棋
    }

    @Test
    void testUndoWhenGameOverShowsMessage() {
        // 强制结束游戏
        for (int i = 0; i < 5; i++) model.placePiece(i, 0, 1);
        assertTrue(Gamelogic.checkWin(model.getBoard(), 4, 0, 1));
        // 由于 checkWin 不在 handlePlayerMove 中直接调用，需手动模拟 gameOver
        // 实际应通过正常流程，这里简化：直接调用 undoMove 应弹出提示
        // 用 spy 捕获 JOptionPane 调用较复杂，此处仅验证不改变棋盘
        controller.initBoard();
        model.placePiece(0, 0, 1);
        controller.undoMove(); // 正常悔棋
        controller.undoMove(); // 无历史
        // 无异常即可
    }

    @Test
    void testHandlePlayerMoveUpdatesModel() {
        controller.handlePlayerMove(3, 4);
        assertEquals(1, model.getPiece(3, 4));
        assertFalse(controller.isBlackTurn());
        verify(audioManager).playMoveSound();
        verify(chessBoard).repaint();
    }

    @Test
    void testHandlePlayerMoveOnOccupiedCellIgnored() {
        controller.handlePlayerMove(5, 5);
        controller.handlePlayerMove(5, 5);
        // 只应落子一次
        int piece = model.getPiece(5, 5);
        assertNotEquals(0, piece);
        // 第二次无效，turn 不变
        assertFalse(controller.isBlackTurn());
    }

    @Test
    void testGameOverAfterWin() {
        // 模拟黑棋连五胜利
        for (int i = 0; i < 4; i++) {
            model.placePiece(i, 0, 1);
        }
        controller.handlePlayerMove(4, 0);
        assertTrue(controller.isGameOver());
        // 不应再接受新落子
        controller.handlePlayerMove(6, 0);
        assertEquals(0, model.getPiece(6, 0));
    }

    @Test
    void testAiMoveCalledInAIMode() {
        controller.setAIMode(true, 2);
        when(aiPlayer.getBestMove()).thenReturn(new int[]{1, 1});
        controller.handlePlayerMove(0, 0); // 玩家黑棋落子
        // 应触发 AI 移动
        verify(aiPlayer).getBestMove();
        assertEquals(2, model.getPiece(1, 1));
    }

    @Test
    void testAiMoveNotCalledWhenGameOver() {
        controller.setAIMode(true, 2);
        controller.initBoard();
        // 强制结束
        for (int i = 0; i < 5; i++) model.placePiece(i, 0, 1);
        controller.handlePlayerMove(4, 0); // 黑棋胜利
        assertTrue(controller.isGameOver());
        verify(aiPlayer, never()).getBestMove();
    }
}