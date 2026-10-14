package birdgame.ui;

import birdgame.model.Bird;
import birdgame.model.Heart;
import birdgame.model.Pipe;
import birdgame.model.Player;
import birdgame.service.CollisionService;
import birdgame.service.GameEngine;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final Player player;
    private final GameEngine gameEngine;

    private final Runnable onLose;
    private final Runnable onWin;

    private final JLabel playerLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JLabel heartLabel = new JLabel();
    private final JLabel pipeLabel = new JLabel();

    private JPanel center;
    private Timer gameTimer;
    private boolean resultHandled = false;

    // ก่อนกด SPACE เกมจะอยู่ใน READY STATE
    private boolean gameStarted = false;
    private int readyTick = 0;

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

        // Game loop ~60 FPS
        gameTimer = new Timer(16, e -> {
            if (center.getWidth() == 0 || resultHandled) {
                return;
            }

            // READY STATE: นกลอยกลางอากาศ แต่เกมยังไม่เดิน
            if (!gameStarted) {
                readyTick++;

                Bird bird = gameEngine.getBird();
                if (bird != null) {
                    int baseY = Math.max(150, center.getHeight() / 2 - 45);
                    int bob = (int) (Math.sin(readyTick * 0.08) * 6);
                    bird.setPosition(140, baseY + bob);
                }

                center.repaint();
                return;
            }

            // เริ่มเล่นจริง: Gravity / ท่อ / หัวใจ / Collision / คะแนน
            gameEngine.update(center.getWidth(), center.getHeight());

            updateStatus();
            center.repaint();

            if (gameEngine.isGameOver()) {
                resultHandled = true;
                gameTimer.stop();

                if (gameEngine.isWon()) {
                    onWin.run();
                } else {
                    onLose.run();
                }
            }
        });
        gameTimer.start();
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

                // ท้องฟ้า
                g.setColor(new Color(160, 220, 255));
                g.fillRect(0, 0, w, h);

                var state = gameEngine.getGameState();

                // ก่อนเริ่มจะยังไม่มีท่อ/หัวใจเกิดใหม่ เพราะ GameEngine.update() ยังไม่ถูกเรียก
                for (Pipe pipe : state.getPipes()) {
                    pipe.draw(g);
                }

                for (Heart heart : state.getHearts()) {
                    heart.draw(g);
                }

                if (state.getNest() != null) {
                    state.getNest().draw(g);
                }

                Bird bird = gameEngine.getBird();
                if (bird != null && bird.isAlive()) {
                    bird.draw(g);
                }

                // พื้น
                g.setColor(new Color(80, 180, 90));
                g.fillRect(
                    0,
                    h - CollisionService.GROUND_HEIGHT,
                    w,
                    CollisionService.GROUND_HEIGHT
                );

                if (!gameStarted) {
                    drawReadyOverlay(g, w, h);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.setFont(AppFont.bold(20));

                    if (player.getCurrentLevel() < 5) {
                        g.drawString("SPACEBAR = บิน", 340, 45);
                    } else {
                        g.drawString("LEVEL 5 - บินเข้ารัง", 310, 45);
                    }
                }
            }
        };

        center.setBackground(Color.WHITE);

        JLabel help = new JLabel(
            "SPACEBAR = เริ่มเกม / บินขึ้น • เก็บหัวใจและบินผ่านท่อเพื่อทำคะแนน",
            SwingConstants.CENTER
        );
        help.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        add(hud, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(help, BorderLayout.SOUTH);
    }

    private void drawReadyOverlay(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String title = "GET READY!";
        Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 36);
        g2.setFont(titleFont);
        FontMetrics titleMetrics = g2.getFontMetrics();
        int titleX = (w - titleMetrics.stringWidth(title)) / 2;
        int titleY = Math.max(95, h / 2 - 130);

        g2.setColor(new Color(68, 49, 68));
        g2.drawString(title, titleX + 4, titleY + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(title, titleX, titleY);

        String hint = "PRESS SPACE TO START";
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        FontMetrics hintMetrics = g2.getFontMetrics();
        int hintX = (w - hintMetrics.stringWidth(hint)) / 2;
        int hintY = titleY + 42;

        g2.setColor(new Color(68, 49, 68));
        g2.drawString(hint, hintX + 3, hintY + 3);
        g2.setColor(new Color(255, 244, 177));
        g2.drawString(hint, hintX, hintY);

        g2.dispose();
    }

    /** ปุ่มเล่นจริงเหลือเฉพาะ SPACEBAR */
    private void bindKeys() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("SPACE"),
            "jump"
        );

        getActionMap().put("jump", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Bird bird = gameEngine.getBird();

                if (bird == null || !bird.isAlive() || gameEngine.isGameOver()) {
                    return;
                }

                // SPACE ครั้งแรก = เริ่มเกม + กระโดดครั้งแรกทันที
                if (!gameStarted) {
                    gameStarted = true;
                    bird.jump();
                    center.repaint();
                    return;
                }

                bird.jump();
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
