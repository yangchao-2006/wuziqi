package com.example.gobang.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GobangGame extends JFrame {
    private ChessBoard chessBoard;

    public GobangGame() {
        setTitle("Java五子棋");
        setSize(650, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JMenuBar menuBar = new JMenuBar();

        // 游戏菜单
        JMenu gameMenu = new JMenu("游戏");
        JMenuItem undoItem = new JMenuItem("悔棋");
        JMenuItem resetItem = new JMenuItem("重置游戏");
        undoItem.addActionListener(e -> chessBoard.undoMove());
        resetItem.addActionListener(e -> chessBoard.initBoard());
        gameMenu.add(undoItem);
        gameMenu.add(resetItem);

        // 模式菜单
        JMenu modeMenu = new JMenu("模式");
        JMenuItem aiModeItem = new JMenuItem("人机对战");
        JMenuItem doubleModeItem = new JMenuItem("双人对战");
        aiModeItem.addActionListener(e -> {
            chessBoard.setAIMode(true, 2);
            chessBoard.initBoard();
        });
        doubleModeItem.addActionListener(e -> {
            chessBoard.setAIMode(false, 0);
            chessBoard.initBoard();
        });
        modeMenu.add(aiModeItem);
        modeMenu.add(doubleModeItem);

        // 音乐菜单
        JMenu musicMenu = new JMenu("音乐");
        JMenuItem bgmOnItem = new JMenuItem("开启背景音乐");
        JMenuItem bgmOffItem = new JMenuItem("关闭背景音乐");
        bgmOnItem.addActionListener(e -> chessBoard.toggleBackgroundMusic(true));
        bgmOffItem.addActionListener(e -> chessBoard.toggleBackgroundMusic(false));
        musicMenu.add(bgmOnItem);
        musicMenu.add(bgmOffItem);

        menuBar.add(gameMenu);
        menuBar.add(modeMenu);
        menuBar.add(musicMenu);
        setJMenuBar(menuBar);

        chessBoard = new ChessBoard();
        add(chessBoard);

        chessBoard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                chessBoard.handleMouseClick(e.getX(), e.getY());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new GobangGame();
    }
}