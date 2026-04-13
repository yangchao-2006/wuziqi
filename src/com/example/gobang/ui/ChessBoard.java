package com.example.gobang.ui;

import com.example.gobang.logic.BoardModel;
import com.example.gobang.logic.AIPlayer;
import com.example.gobang.logic.Gamelogic;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class ChessBoard extends JPanel {
    public static final int CELL_SIZE = 40;

    private BoardModel model;
    private AIPlayer aiPlayer;
    private boolean isBlackTurn = true;   // true=黑棋走，false=白棋走
    private boolean gameOver = false;
    private boolean isAIMode = false;      // 是否人机对战模式
    private Stack<int[]> historyStack = new Stack<>(); // 历史记录 {x, y, color}

    public ChessBoard() {
        setPreferredSize(new Dimension(CELL_SIZE * BoardModel.BOARD_SIZE,
                CELL_SIZE * BoardModel.BOARD_SIZE));
        model = new BoardModel();
        aiPlayer = new AIPlayer(model, 2); // 默认AI执白
        initBoard();
    }

    // 重置游戏
    public void initBoard() {
        model.clear();
        isBlackTurn = true;
        gameOver = false;
        historyStack.clear();
        repaint();
    }

    // 设置AI模式
    public void setAIMode(boolean enable, int aiColor) {
        this.isAIMode = enable;
        aiPlayer.setAiColor(aiColor);
        initBoard();
    }

    // 悔棋
    public void undoMove() {
        if (gameOver) {
            JOptionPane.showMessageDialog(this, "游戏已经结束，无法悔棋！");
            return;
        }
        if (historyStack.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有可以悔棋的步骤！");
            return;
        }

        int[] lastMove = historyStack.pop();
        int x = lastMove[0], y = lastMove[1];
        model.removePiece(x, y);
        isBlackTurn = !isBlackTurn;   // 切换回合
        repaint();

        // 悔棋后，如果是AI模式且轮到AI，则AI立即落子
        if (isAIMode && !gameOver && !isBlackTurn) {
            aiMove();
        }
    }

    // 绘制棋盘和棋子
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 背景
        g.setColor(new Color(245, 222, 179)); // 杏色
        g.fillRect(0, 0, getWidth(), getHeight());
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(new Color(80, 50, 20));

        int size = BoardModel.BOARD_SIZE;
        for (int i = 0; i < size; i++) {
            int x1 = CELL_SIZE / 2 + i * CELL_SIZE;
            int y1 = CELL_SIZE / 2;
            int x2 = CELL_SIZE / 2 + i * CELL_SIZE;
            int y2 = CELL_SIZE / 2 + (size - 1) * CELL_SIZE;
            g2d.drawLine(x1, y1, x2, y2);

            x1 = CELL_SIZE / 2;
            y1 = CELL_SIZE / 2 + i * CELL_SIZE;
            x2 = CELL_SIZE / 2 + (size - 1) * CELL_SIZE;
            y2 = CELL_SIZE / 2 + i * CELL_SIZE;
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 星位
        int starSize = 6;
        g2d.setColor(Color.BLACK);
        // 天元
        g2d.fillOval(CELL_SIZE / 2 + 7 * CELL_SIZE - starSize / 2,
                CELL_SIZE / 2 + 7 * CELL_SIZE - starSize / 2,
                starSize, starSize);
        int[][] starPositions = {{3,3}, {11,3}, {3,11}, {11,11}};
        for (int[] pos : starPositions) {
            g2d.fillOval(CELL_SIZE / 2 + pos[0] * CELL_SIZE - starSize / 2,
                    CELL_SIZE / 2 + pos[1] * CELL_SIZE - starSize / 2,
                    starSize, starSize);
        }
    }

    private void drawPieces(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = BoardModel.BOARD_SIZE;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int piece = model.getPiece(i, j);
                if (piece == 0) continue;
                int x = i * CELL_SIZE + CELL_SIZE / 2;
                int y = j * CELL_SIZE + CELL_SIZE / 2;
                int radius = 14;
                if (piece == 1) { // 黑子
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(new Color(80, 80, 80));
                    g2d.fillOval(x - radius/2, y - radius/2, radius/2, radius/2);
                } else { // 白子
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - radius/3, y - radius/3, radius/2, radius/2);
                }
            }
        }
    }

    // 玩家点击处理
    public void handleMouseClick(int mouseX, int mouseY) {
        if (gameOver) return;
        if (isAIMode && !isBlackTurn) return; // AI模式且非玩家回合

        int x = (mouseX - CELL_SIZE / 4) / CELL_SIZE;
        int y = (mouseY - CELL_SIZE / 4) / CELL_SIZE;
        if (x < 0 || x >= BoardModel.BOARD_SIZE || y < 0 || y >= BoardModel.BOARD_SIZE) return;
        if (model.getPiece(x, y) != 0) return;

        int playerColor = 1; // 玩家执黑
        model.placePiece(x, y, playerColor);
        historyStack.push(new int[]{x, y, playerColor});
        Toolkit.getDefaultToolkit().beep();

        if (Gamelogic.checkWin(model.getBoard(), x, y, playerColor)) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "黑棋（玩家）获胜！");
        } else {
            isBlackTurn = false;
            repaint();
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "平局！");
                return;
            }
            if (isAIMode && !gameOver) {
                aiMove();
            }
        }
        repaint();
    }

    // AI落子
    private void aiMove() {
        if (gameOver) return;
        // 检查是否轮到AI
        int aiColor = aiPlayer.getAiColor(); // 需要给AIPlayer添加getter，或者直接访问成员变量，这里假设有getter
        // 为了简洁，我们直接使用已知逻辑：AI颜色是2（白），当前如果 isBlackTurn==false 则轮到白棋
        // 但为了通用，最好在AIPlayer中添加getter，下面简单处理：
        if ((aiColor == 1 && !isBlackTurn) || (aiColor == 2 && isBlackTurn)) {
            return; // 未轮到AI
        }

        int[] best = aiPlayer.getBestMove();
        if (best == null) {
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "平局！");
            }
            return;
        }

        int x = best[0], y = best[1];
        model.placePiece(x, y, aiColor);
        historyStack.push(new int[]{x, y, aiColor});
        Toolkit.getDefaultToolkit().beep();

        if (Gamelogic.checkWin(model.getBoard(), x, y, aiColor)) {
            gameOver = true;
            String winner = (aiColor == 1) ? "黑棋（AI）" : "白棋（AI）";
            JOptionPane.showMessageDialog(this, winner + "获胜！");
        } else {
            isBlackTurn = !isBlackTurn;
            if (model.isFull()) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "平局！");
            }
        }
        repaint();
    }
}