/*
 * หน้าต่างที่ครอบ GamePanel ตอนเล่นจริง
 * รับสัญญาณแพ้/ชนะจาก GamePanel แล้วบันทึกผล ก่อนเปิด ResultFrame
 * ตัวแปร saved ใช้กันไม่ให้ผลรอบเดียวถูกเขียน CSV ซ้ำ
 */

package birdgame.ui;

import birdgame.model.Player;

import birdgame.model.PlayerRecord;

import birdgame.service.FileStorageService;

import javax.swing.*;

import java.awt.*;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

public class GameFrame extends JFrame {

    private final Player player;

    private final FileStorageService storageService = new FileStorageService();

    // กันการบันทึกประวัติรอบเดียวกันซ้ำ ถ้า callback ถูกเรียกมากกว่าหนึ่งครั้ง
    private boolean saved = false;

    // สร้าง GamePanel และส่ง callback สองแบบเข้าไป: แพ้ และ ชนะถึงรัง
    public GameFrame(Player player) {

        this.player = player;

        setTitle("Bird Game - " + player.getName());

        setSize(900, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel(
            player,
            this::finishWithoutNest,
            this::finishAtNest
        );

        add(gamePanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    // กรณีแพ้: บันทึกผลตามสถานะปัจจุบัน แล้วเปิดหน้าประวัติ
    private void finishWithoutNest() {

        saveCurrentResult();

        dispose();

        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

    // กรณีชนะ: mark Player ว่าถึงรังก่อน แล้วค่อยบันทึกผล
    private void finishAtNest() {

        player.finishGame(1);

        saveCurrentResult();

        dispose();

        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

    // saved เป็นด่านกันซ้ำ ถ้าเคยบันทึกรอบนี้แล้วจะไม่เขียน CSV อีกรอบ
    private void saveCurrentResult() {

        if (saved) {

            return;
        }

        String playedAt = LocalDateTime.now()

                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        storageService.saveRecord(new PlayerRecord(player, playedAt));

        saved = true;
    }
}
