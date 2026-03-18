import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int BOARD_SIZE = 20;      // 15x15 标准棋盘
    public static final int CELL_SIZE = 40;        // 每个格子像素大小
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE]; // 0空 1黑 2白
    private boolean isBlackTurn = true;            // 黑棋先手
    private boolean gameOver = false;

    public ChessBoard() {
        setPreferredSize(new Dimension(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE));
        initBoard();
    }

    // 初始化或重置棋盘
    public void initBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
        isBlackTurn = true;
        gameOver = false;
        repaint();
    }

    // 绘制棋盘和棋子
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    // 画棋盘网格
    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < BOARD_SIZE; i++) {
            // 画竖线
            g.drawLine(CELL_SIZE / 2 + i * CELL_SIZE, CELL_SIZE / 2,
                    CELL_SIZE / 2 + i * CELL_SIZE, CELL_SIZE / 2 + (BOARD_SIZE - 1) * CELL_SIZE);
            // 画横线
            g.drawLine(CELL_SIZE / 2, CELL_SIZE / 2 + i * CELL_SIZE,
                    CELL_SIZE / 2 + (BOARD_SIZE - 1) * CELL_SIZE, CELL_SIZE / 2 + i * CELL_SIZE);
        }
        // 画五个星位（天元和小目）
        g.fillOval(CELL_SIZE / 2 + 3 * CELL_SIZE - 4, CELL_SIZE / 2 + 3 * CELL_SIZE - 4, 8, 8);
        g.fillOval(CELL_SIZE / 2 + 11 * CELL_SIZE - 4, CELL_SIZE / 2 + 3 * CELL_SIZE - 4, 8, 8);
        g.fillOval(CELL_SIZE / 2 + 3 * CELL_SIZE - 4, CELL_SIZE / 2 + 11 * CELL_SIZE - 4, 8, 8);
        g.fillOval(CELL_SIZE / 2 + 11 * CELL_SIZE - 4, CELL_SIZE / 2 + 11 * CELL_SIZE - 4, 8, 8);
        g.fillOval(CELL_SIZE / 2 + 7 * CELL_SIZE - 4, CELL_SIZE / 2 + 7 * CELL_SIZE - 4, 8, 8);
    }

    // 根据board数组画棋子
    private void drawPieces(Graphics g) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) continue;
                int x = i * CELL_SIZE + CELL_SIZE / 2;
                int y = j * CELL_SIZE + CELL_SIZE / 2;
                if (board[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x - 15, y - 15, 30, 30);
                } else if (board[i][j] == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x - 15, y - 15, 30, 30);
                    g.setColor(Color.BLACK);
                    g.drawOval(x - 15, y - 15, 30, 30); // 白棋加个黑边更清晰
                }
            }
        }
    }

    // 处理鼠标点击事件
    public void handleMouseClick(int mouseX, int mouseY) {
        if (gameOver) return;

        // 计算点击位置对应的棋盘索引
        int xIndex = (mouseX - CELL_SIZE / 4) / CELL_SIZE;
        int yIndex = (mouseY - CELL_SIZE / 4) / CELL_SIZE;

        // 检查点击是否在棋盘有效范围内，且该位置没有棋子
        if (xIndex >= 0 && xIndex < BOARD_SIZE && yIndex >= 0 && yIndex < BOARD_SIZE && board[xIndex][yIndex] == 0) {
            // 落子
            board[xIndex][yIndex] = isBlackTurn ? 1 : 2;

            // 判断胜负
            if (checkWin(xIndex, yIndex)) {
                gameOver = true;
                String winner = isBlackTurn ? "黑棋" : "白棋";
                JOptionPane.showMessageDialog(this, winner + "获胜！");
            } else {
                // 切换玩家
                isBlackTurn = !isBlackTurn;
            }
            repaint(); // 重新绘制棋盘
        }
    }

    // 胜负判断（核心算法）
    private boolean checkWin(int x, int y) {
        int currentPlayer = board[x][y];
        // 四个方向向量：水平(1,0)、垂直(0,1)、主对角线(1,1)、副对角线(1,-1)
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1; // 当前棋子算一个
            // 正方向延伸
            for (int step = 1; step < 5; step++) {
                int nx = x + dir[0] * step;
                int ny = y + dir[1] * step;
                if (nx >= 0 && nx < BOARD_SIZE && ny >= 0 && ny < BOARD_SIZE && board[nx][ny] == currentPlayer) {
                    count++;
                } else {
                    break;
                }
            }
            // 反方向延伸
            for (int step = 1; step < 5; step++) {
                int nx = x - dir[0] * step;
                int ny = y - dir[1] * step;
                if (nx >= 0 && nx < BOARD_SIZE && ny >= 0 && ny < BOARD_SIZE && board[nx][ny] == currentPlayer) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                return true; // 五子连珠，获胜
            }
        }
        return false;
    }
}