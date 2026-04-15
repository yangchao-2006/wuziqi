package com.example.gobang.ui;

import com.example.gobang.logic.BoardModel;
import java.awt.*;

public class BoardRenderer {
    private BoardModel model;
    private static final int CELL_SIZE = ChessBoard.CELL_SIZE;

    public BoardRenderer(BoardModel model) {
        this.model = model;
    }

    public void draw(Graphics g) {
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

        int starSize = 6;
        g2d.setColor(Color.BLACK);
        g2d.fillOval(CELL_SIZE / 2 + 7 * CELL_SIZE - starSize / 2,
                CELL_SIZE / 2 + 7 * CELL_SIZE - starSize / 2,
                starSize, starSize);
        int[][] starPositions = {{3, 3}, {11, 3}, {3, 11}, {11, 11}};
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
                if (piece == 1) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(new Color(80, 80, 80));
                    g2d.fillOval(x - radius / 2, y - radius / 2, radius / 2, radius / 2);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - radius / 3, y - radius / 3, radius / 2, radius / 2);
                }
            }
        }
    }
}