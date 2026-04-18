package main.gobang.ui;

import main.gobang.logic.BoardModel;
import main.gobang.logic.AIPlayer;
import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int CELL_SIZE = 40;

    private GameController controller;
    private BoardRenderer renderer;
    private AudioManager audioManager;

    public ChessBoard() {
        setPreferredSize(new Dimension(CELL_SIZE * BoardModel.BOARD_SIZE,
                CELL_SIZE * BoardModel.BOARD_SIZE));
        BoardModel model = new BoardModel();
        AIPlayer aiPlayer = new AIPlayer(model, 2);
        audioManager = new AudioManager();
        this.controller = new GameController(model, aiPlayer, audioManager, this);
        this.renderer = new BoardRenderer(model);
        controller.initBoard();
        audioManager.startBackgroundMusic();
    }

    public void initBoard() { controller.initBoard(); }
    public void setAIMode(boolean enable, int aiColor) { controller.setAIMode(enable, aiColor); }
    public void undoMove() { controller.undoMove(); }
    public void toggleBackgroundMusic(boolean enable) { audioManager.toggleBackgroundMusic(enable); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(245, 222, 179));
        g.fillRect(0, 0, getWidth(), getHeight());
        renderer.draw(g);
    }

    public void handleMouseClick(int mouseX, int mouseY) {
        if (controller.isGameOver()) return;
        if (controller.isAIMode() && !controller.isBlackTurn()) return;

        int x = (mouseX - CELL_SIZE / 4) / CELL_SIZE;
        int y = (mouseY - CELL_SIZE / 4) / CELL_SIZE;
        if (x < 0 || x >= BoardModel.BOARD_SIZE || y < 0 || y >= BoardModel.BOARD_SIZE) return;
        controller.handlePlayerMove(x, y);
    }
}