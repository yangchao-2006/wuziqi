package main.gobang.logic;

/**
 * AI 决策类，根据棋盘模型计算最佳落子位置。
 */
public class AIPlayer {
    private BoardModel model;
    private int aiColor;    // AI执子颜色：1=黑，2=白

    public AIPlayer(BoardModel model, int aiColor) {
        this.model = model;
        this.aiColor = aiColor;
    }

    public void setAiColor(int aiColor) {
        this.aiColor = aiColor;
    }

    public int getAiColor() {
        return aiColor;
    }

    public int[] getBestMove() {
        int bestScore = -1;
        int bestX = -1, bestY = -1;
        int opponent = (aiColor == 1) ? 2 : 1;
        int[][] board = model.getBoard();

        for (int i = 0; i < BoardModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardModel.BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    int attack = evaluatePosition(i, j, aiColor, board);
                    int defend = evaluatePosition(i, j, opponent, board);
                    int total = attack + defend * 2;
                    if (total > bestScore) {
                        bestScore = total;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        if (bestX == -1) return null;
        return new int[]{bestX, bestY};
    }

    private int evaluatePosition(int x, int y, int color, int[][] board) {
        int[][] dirs = {{1,0},{0,1},{1,1},{1,-1}};
        int total = 0;
        for (int[] dir : dirs) {
            int count = 1;
            count += countConsecutive(x, y, dir[0], dir[1], color, board);
            count += countConsecutive(x, y, -dir[0], -dir[1], color, board);
            if (count >= 5) return 100000;
            total += count * count;
        }
        return total;
    }

    private int countConsecutive(int x, int y, int dx, int dy, int color, int[][] board) {
        int count = 0;
        for (int step = 1; step <= 5; step++) {
            int nx = x + dx * step;
            int ny = y + dy * step;
            if (nx >= 0 && nx < BoardModel.BOARD_SIZE && ny >= 0 && ny < BoardModel.BOARD_SIZE && board[nx][ny] == color) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}