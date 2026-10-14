/*
 * จุดเริ่มต้นของโปรแกรม
 * หน้าที่ของไฟล์นี้มีอย่างเดียวคือเตรียม Swing แล้วเปิดหน้า StartFrame
 * ถ้าโปรแกรมเปิดไม่ขึ้นเลย ไฟล์นี้เป็นหนึ่งในจุดแรกที่ควรเช็ก
 */

package birdgame;

import birdgame.ui.AppFont;

import birdgame.ui.StartFrame;

import javax.swing.SwingUtilities;

public class Main {

    // เริ่มโปรแกรมบน Swing Event Dispatch Thread เพื่อให้การสร้าง/แก้ GUI ทำงานใน thread ที่ถูกต้อง
    public static void main(String[] args) {

        AppFont.applyToSwing();

        SwingUtilities.invokeLater(() -> {

            StartFrame frame = new StartFrame();

            frame.setVisible(true);
        });
    }
}
