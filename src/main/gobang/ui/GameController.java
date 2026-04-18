package main.gobang.ui;

import main.gobang.logic.BoardModel;
import main.gobang.logic.AIPlayer;
import main.gobang.logic.Gamelogic;
import javax.swing.*;
import java.util.Stack;

public class GameController {
    private BoardModel model;
    private AIPlayer aiPlayer;
    private boolean isBlackTurn = true;
    private boolean gameOver = false;
    private boolean isAIMode = false;
    private Stack<int[]> historyStack = new Stack<>();
    private AudioManager audioManager;
    private ChessBoard chessBoard;

    public GameController(BoardModel model, AIPlayer aiPlayer, AudioManager audioManager, ChessBoard chessBoard) {
        this.model = model;
        this.aiPlayer = aiPlayer;
        this.audioManager = audioManager;
        this.chessBoard = chessBoard;
    }

    public void initBoard() {
        model.clear();
        isBlackTurn = true;
        gameOver = false;
        historyStack.clear();
        chessBoard.repaint();
    }

    public void setAIMode(boolean enable, int aiColor) {
        this.isAIMode = enable;
        aiPlayer.setAiColor(aiColor);
        initBoard();
    }

    public void undoMove() {
        if (gameOver) {
            JOptionPane.showMessageDialog(chessBoard, "游戏已经结束，无法悔棋！");
            return;
        }
        if (historyStack.isEmpty()) {
            JOptionPane.showMessageDialog(chessBoard, "没有可以悔棋的步骤！");
            return;
        }
        int[] lastMove = historyStack.pop();
        int x = lastMove[0], y = lastMove[1];
        model.removePiece(x, y);
        isBlackTurn = !isBlackTurn;
        chessBoard.repaint();
        if (isAIMode && !gameOver && !isBlackTurn) {
            aiMove();
        }
    }

    public void handlePlayerMove(int x, int y) {
        if (gameOver) return;
        if (isAIMode && !isBlackTurn) return;
        if (model.getPiece(x, y) != 0) return;

        int playerColor = 1;
        model.placePiece(x, y, playerColor);
        historyStack.push(new int[]{x, y, playerColor});
        audioManager.playMoveSound();

        if (Gamelogic.checkWin(model.getBoard(), x, y, playerColor)) {
            gameOver = true;
            JOptionPane.showMessageDialog(chessBoard, "黑棋（玩家）获胜！");
        } else {
            isBlackTurn = false;
            chessBoard.repaint();
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(chessBoard, "平局！");
                return;
            }
            if (isAIMode && !gameOver) {
                aiMove();
            }
        }
        chessBoard.repaint();
    }

    private void aiMove() {
        if (gameOver) return;
        int aiColor = aiPlayer.getAiColor();
        if ((aiColor == 1 && !isBlackTurn) || (aiColor == 2 && isBlackTurn)) return;

        int[] best = aiPlayer.getBestMove();
        if (best == null) {
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(chessBoard, "平局！");
            }
            return;
        }

        int x = best[0], y = best[1];
        model.placePiece(x, y, aiColor);
        historyStack.push(new int[]{x, y, aiColor});
        audioManager.playMoveSound();

        if (Gamelogic.checkWin(model.getBoard(), x, y, aiColor)) {
            gameOver = true;
            String winner = (aiColor == 1) ? "黑棋（AI）" : "白棋（AI）";
            JOptionPane.showMessageDialog(chessBoard, winner + "获胜！");
        } else {
            isBlackTurn = !isBlackTurn;
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(chessBoard, "平局！");
            }
        }
        chessBoard.repaint();
    }

    public boolean isGameOver() { return gameOver; }
    public boolean isAIMode() { return isAIMode; }
    public boolean isBlackTurn() { return isBlackTurn; }
}