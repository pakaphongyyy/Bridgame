/*
 * ============================================================
 * ไฟล์: GameFrame.java
 * หน้าที่หลัก: หน้าต่างหลักระหว่างเล่น
 *
 * ภาพรวม:
 * สร้าง GamePanel รับ callback แพ้/ชนะ บันทึกผล และเปิด ResultFrame
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;
// นำเข้า PlayerRecord เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.PlayerRecord;
// นำเข้า FileStorageService เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.service.FileStorageService;

// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.*;
// นำเข้า LocalDateTime เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.time.LocalDateTime;
// นำเข้า DateTimeFormatter เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.time.format.DateTimeFormatter;

// ประกาศคลาส `GameFrame` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class GameFrame extends JFrame {

    // ประกาศ field `player` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final Player player;
    // ประกาศ field `storageService` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final FileStorageService storageService = new FileStorageService();
    // ประกาศ field `saved` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean saved = false;

    // Constructor ของ `GameFrame` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public GameFrame(Player player) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.player` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.player = player;

        // เรียก `setTitle(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setTitle("Bird Game - " + player.getName());
        // เรียก `setSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setSize(900, 650);
        // เรียก `setDefaultCloseOperation(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // เรียก `setLocationRelativeTo(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setLocationRelativeTo(null);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `GamePanel gamePanel` เพื่อใช้ต่อในขั้นตอนถัดไป
        GamePanel gamePanel = new GamePanel(
            player,
            this::finishWithoutNest,
            this::finishAtNest
        );

        // เรียก `add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        add(gamePanel, BorderLayout.CENTER);

        // เรียก `SwingUtilities.invokeLater(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    // จัดการกรณีเกมจบโดยยังไม่ถึงรัง แล้วบันทึกผล
    private void finishWithoutNest() {
        // เรียก `saveCurrentResult(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        saveCurrentResult();

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        dispose();
        // ทำให้หน้าต่างหรือ component นี้แสดงบนหน้าจอ
        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

    // จัดการกรณีชนะถึงรัง แล้วบันทึกผล
    private void finishAtNest() {
        // โปรแกรมเล่นทีละคน จึงไม่ต้องใช้ finishOrder แบบหลายคนในรอบเดียว
        // เรียก `player.finishGame(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        player.finishGame(1);

        // เรียก `saveCurrentResult(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        saveCurrentResult();

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        dispose();
        // ทำให้หน้าต่างหรือ component นี้แสดงบนหน้าจอ
        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

    // สร้าง PlayerRecord จาก Player ปัจจุบันและบันทึกเพียงครั้งเดียว
    private void saveCurrentResult() {
        // ตรวจเงื่อนไข `saved`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (saved) {
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String playedAt` เพื่อใช้ต่อในขั้นตอนถัดไป
        String playedAt = LocalDateTime.now()
                // เรียก `format(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // เรียก `storageService.saveRecord(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        storageService.saveRecord(new PlayerRecord(player, playedAt));
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `saved` เพื่อใช้ต่อในขั้นตอนถัดไป
        saved = true;
    }
}
