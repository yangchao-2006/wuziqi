package main.gobang.ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private Clip bgmClip;
    private boolean bgmEnabled = true;

    private File getAudioFile(String filename) {
        File dir = new File("C:\\Users\\19064\\java\\Hello\\res");
        File file = new File(dir, filename);
        if (file.exists()) return file;
        file = new File(filename);
        if (file.exists()) return file;
        System.out.println("未找到音频文件: " + filename);
        return null;
    }

    // 落子音效已移除，此方法不再播放任何声音
    public void playMoveSound() {
        // 如需保留简单蜂鸣，取消下行注释
        // java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public void startBackgroundMusic() {
        if (!bgmEnabled) return;
        stopBackgroundMusic();
        File musicFile = getAudioFile("bgm.wav");
        if (musicFile == null) return;
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
            System.out.println("背景音乐播放成功：" + musicFile.getAbsolutePath());
        } catch (UnsupportedAudioFileException e) {
            System.out.println("背景音乐格式不支持，请转换为 PCM WAV");
        } catch (IOException | LineUnavailableException e) {
            System.out.println("背景音乐播放失败: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }

    public void toggleBackgroundMusic(boolean enable) {
        this.bgmEnabled = enable;
        if (enable) startBackgroundMusic();
        else stopBackgroundMusic();
    }
}