package birdgame;

import birdgame.ui.AppFont;
import birdgame.ui.StartFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // ใช้ฟอนต์ที่รองรับภาษาไทยทั้งโปรแกรม
        AppFont.applyToSwing();

        SwingUtilities.invokeLater(() -> {
            StartFrame frame = new StartFrame();
            frame.setVisible(true);
        });
    }
}
