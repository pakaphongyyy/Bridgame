package birdgame.ui;

import birdgame.model.Heart;
import birdgame.model.Pipe;
import birdgame.model.Player;
import birdgame.service.CollisionService;
import birdgame.service.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Player player;
    private final GameEngine gameEngine;

    private final Runnable onLose;
    private final Runnable onWin;

    private final JLabel playerLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JLabel heartLabel = new JLabel();
    private final JLabel pipeLabel = new JLabel();
    private JPanel center;   // เพิ่ม: Timer ต้องใช้ขนาดจอ

    public GamePanel(Player player, Runnable onLose, Runnable onWin) {
        this.player = player;
        this.gameEngine = new GameEngine(player);
        this.onLose = onLose;
        this.onWin = onWin;

        setFocusable(true);
        setLayout(new BorderLayout());
        setBackground(new Color(232, 247, 255));

        buildUI();
        bindKeys();

        gameEngine.startGame();
        updateStatus();

        // ===== เพิ่ม: game loop ~60 ครั้ง/วินาที =====
        new Timer(16, e -> {
            if (center.getWidth() == 0) return;     // ยังไม่ได้วาดจอครั้งแรก
            gameEngine.update(center.getWidth(), center.getHeight());
            center.repaint();
        }).start();
    }

    private void buildUI() {
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
        hud.add(playerLabel);
        hud.add(levelLabel);
        hud.add(heartLabel);
        hud.add(pipeLabel);

        center = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int w = getWidth();
                int h = getHeight();

                g.setColor(new Color(160, 220, 255));
                g.fillRect(0, 0, w, h);

                // ===== เพิ่ม: วาดท่อ หัวใจ รัง (ก่อนพื้น) =====
                var state = gameEngine.getGameState();
                for (Pipe pipe : state.getPipes()) pipe.draw(g);
                for (Heart heart : state.getHearts()) heart.draw(g);
                if (state.getNest() != null) state.getNest().draw(g);

                g.setColor(new Color(80, 180, 90));
                g.fillRect(0, h - CollisionService.GROUND_HEIGHT, w, CollisionService.GROUND_HEIGHT);

                // ลบ: วงกลมเหลือง (นก) — ยังไม่ใช้นก

                g.setColor(Color.DARK_GRAY);
                g.setFont(AppFont.bold(20));

                if (player.getCurrentLevel() < 5) {
                    g.drawString("ขั้นทดสอบ Logic", 330, h / 2 - 40);
                    g.drawString("H = เก็บหัวใจ   P = ผ่านท่อ   X = ชนท่อ/แพ้", 220, h / 2);
                } else {
                    g.drawString("LEVEL 5 - ENDING SCENE", 300, h / 2 - 30);
                    g.drawString("กด N เพื่อทดสอบนกบินเข้ารังและจบเกม", 255, h / 2 + 10);
                }
            }
        };

        center.setBackground(Color.WHITE);

        JLabel help = new JLabel(
            "เวอร์ชันเริ่มต้น: ใช้ H / P / X เพื่อทดสอบกติกา ก่อนต่อระบบฟิสิกส์และ Collision จริง",
            SwingConstants.CENTER
        );
        help.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        add(hud, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(help, BorderLayout.SOUTH);
    }

    private void bindKeys() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_H -> {
                        gameEngine.collectHeartForTest();
                        updateStatus();
                        repaint();
                    }
                    case KeyEvent.VK_P -> {
                        gameEngine.passPipeForTest();
                        updateStatus();
                    }
                    case KeyEvent.VK_X -> {
                        JOptionPane.showMessageDialog(
                            GamePanel.this,
                            player.getName() + " ชนท่อ / จบรอบ"
                        );
                        onLose.run();
                    }
                    case KeyEvent.VK_N -> {
                        if (player.getCurrentLevel() == 5) {
                            JOptionPane.showMessageDialog(
                                GamePanel.this,
                                player.getName() + " บินเข้ารังสำเร็จ!"
                            );
                            onWin.run();
                        }
                    }
                }
            }
        });
    }

    private void updateStatus() {
        playerLabel.setText("ผู้เล่น: " + player.getName());
        levelLabel.setText("ด่าน: " + player.getCurrentLevel());
        heartLabel.setText("หัวใจ: " + gameEngine.getHeartDisplay());
        pipeLabel.setText("ท่อสะสม: " + player.getTotalPipes());
    }
}
