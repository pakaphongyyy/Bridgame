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
    private boolean saved = false;

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

    private void finishWithoutNest() {
        saveCurrentResult();

        dispose();
        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

    private void finishAtNest() {
        // โปรแกรมเล่นทีละคน จึงไม่ต้องใช้ finishOrder แบบหลายคนในรอบเดียว
        player.finishGame(1);

        saveCurrentResult();

        dispose();
        new ResultFrame(storageService.loadRecords(), player.getName()).setVisible(true);
    }

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
