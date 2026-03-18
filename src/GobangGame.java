import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GobangGame extends JFrame {
    private ChessBoard chessBoard; // 自定义棋盘面板

    public GobangGame() {
        setTitle("Java五子棋");          // 窗口标题
        setSize(670, 700);               // 窗口大小
        setLocationRelativeTo(null);      // 窗口居中
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);              // 禁止缩放

        chessBoard = new ChessBoard();
        add(chessBoard);                  // 将棋盘面板添加到窗口

        // 为棋盘面板添加鼠标监听器
        chessBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chessBoard.handleMouseClick(e.getX(), e.getY());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new GobangGame();
    }
}