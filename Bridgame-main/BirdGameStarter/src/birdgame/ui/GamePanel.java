/*
 * พื้นที่ที่ผู้เล่นมองเห็นและกดเล่นจริง
 * รับ SPACEBAR, วาด HUD/พื้นหลัง/นก/ท่อ/หัวใจ และมี Timer เป็น Game Loop
 * GamePanel คุม “หน้าจอ” ส่วนกติกาหลักส่งต่อให้ GameEngine จัดการ
 */

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

    private boolean gameStarted = false;

    private int readyTick = 0;

    // เตรียม GameEngine, หน้าจอ, ปุ่ม SPACE และ Timer จากนั้นเริ่มที่สถานะ READY ก่อนเล่นจริง
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

        // Timer ประมาณทุก 16 ms (~60 FPS) เป็นตัวขับเกมทีละเฟรม
        gameTimer = new Timer(16, e -> {

            if (center.getWidth() == 0 || resultHandled) {

                return;
            }

            // ก่อน SPACE ครั้งแรก เราแค่ให้นกลอยเบา ๆ และ repaint แต่ยังไม่เรียก GameEngine.update()
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

            // หลังเริ่มเล่นจริงจึงส่งขนาดพื้นที่เกมให้ Engine อัปเดต Logic
            gameEngine.update(center.getWidth(), center.getHeight());

            updateStatus();

            center.repaint();

            // จบเกมแล้วต้องหยุด Timer และเรียก callback แค่ครั้งเดียว ไม่อย่างนั้น ResultFrame อาจเปิดซ้ำ
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

    // ประกอบ HUD ด้านบนและพื้นที่ center ที่ใช้วาดฉากเกมทั้งหมด
    private void buildUI() {

        JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));

        hud.add(playerLabel);

        hud.add(levelLabel);

        hud.add(heartLabel);

        hud.add(pipeLabel);

        center = new JPanel() {
            @Override

            // ส่วนนี้มีหน้าที่วาดอย่างเดียว: พื้นหลัง วัตถุ นก พื้น และข้อความ READY ไม่ควรเพิ่มคะแนนหรือเปลี่ยนกติกาที่นี่
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                int w = getWidth();

                int h = getHeight();

                int level = player.getCurrentLevel();

                drawLevelBackground(g, w, h, level);

                var state = gameEngine.getGameState();

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

                drawLevelGround(g, w, h, level);

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

    // เลือกธีมพื้นหลังตาม currentLevel เพื่อให้แต่ละด่านมีบรรยากาศต่างกัน โดยไม่กระทบ Collision
    private void drawLevelBackground(Graphics g, int w, int h, int level) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int groundY = h - CollisionService.GROUND_HEIGHT;

        switch (level) {

            case 1 -> {

                g2.setColor(new Color(156, 222, 255));

                g2.fillRect(0, 0, w, h);

                drawSun(g2, w - 115, 70, new Color(255, 224, 92));

                drawCloud(g2, 90, 95, 1.0, new Color(250, 255, 255));

                drawCloud(g2, 520, 150, 0.75, new Color(250, 255, 255));

                drawHills(g2, w, groundY, new Color(126, 202, 122), new Color(91, 181, 102));
            }

            case 2 -> {

                GradientPaint sunset = new GradientPaint(

                    0, 0, new Color(247, 146, 111),

                    0, groundY, new Color(255, 211, 137)
                );

                g2.setPaint(sunset);

                g2.fillRect(0, 0, w, h);

                drawSun(g2, w - 150, 105, new Color(255, 241, 167));

                drawCloud(g2, 100, 110, 0.9, new Color(255, 225, 214));

                drawCloud(g2, 560, 180, 0.7, new Color(255, 220, 205));

                drawHills(g2, w, groundY, new Color(181, 153, 117), new Color(144, 132, 96));
            }

            case 3 -> {

                g2.setColor(new Color(36, 55, 105));

                g2.fillRect(0, 0, w, h);

                drawStars(g2, w, groundY);

                drawMoon(g2, w - 125, 85);

                drawCloud(g2, 120, 130, 0.8, new Color(97, 115, 153));

                drawHills(g2, w, groundY, new Color(55, 92, 91), new Color(42, 77, 70));
            }

            case 4 -> {

                GradientPaint storm = new GradientPaint(

                    0, 0, new Color(82, 69, 116),

                    0, groundY, new Color(131, 105, 139)
                );

                g2.setPaint(storm);

                g2.fillRect(0, 0, w, h);

                drawCloud(g2, 70, 85, 1.15, new Color(100, 95, 122));

                drawCloud(g2, 430, 120, 1.05, new Color(88, 84, 110));

                drawCloud(g2, 690, 70, 0.8, new Color(111, 103, 130));

                drawLightning(g2, w - 160, 150);

                drawHills(g2, w, groundY, new Color(70, 84, 79), new Color(51, 69, 61));
            }

            case 5 -> {

                GradientPaint dawn = new GradientPaint(

                    0, 0, new Color(119, 201, 231),

                    0, groundY, new Color(255, 224, 173)
                );

                g2.setPaint(dawn);

                g2.fillRect(0, 0, w, h);

                drawSun(g2, w / 2 + 170, 95, new Color(255, 230, 126));

                drawCloud(g2, 95, 125, 0.95, new Color(255, 248, 229));

                drawCloud(g2, 570, 155, 0.8, new Color(255, 248, 229));

                drawHills(g2, w, groundY, new Color(111, 176, 111), new Color(73, 149, 90));
            }

            default -> {

                g2.setColor(new Color(160, 220, 255));

                g2.fillRect(0, 0, w, h);
            }
        }

        g2.dispose();
    }

    // วาดพื้นให้สัมพันธ์กับ GROUND_HEIGHT เพื่อภาพกับจุดชนพื้นไม่เหลื่อมกัน
    private void drawLevelGround(Graphics g, int w, int h, int level) {

        Graphics2D g2 = (Graphics2D) g.create();

        int y = h - CollisionService.GROUND_HEIGHT;

        Color top;

        Color bottom;

        switch (level) {

            case 1 -> { top = new Color(85, 194, 91); bottom = new Color(55, 157, 72); }

            case 2 -> { top = new Color(164, 175, 82); bottom = new Color(126, 141, 72); }

            case 3 -> { top = new Color(55, 118, 76); bottom = new Color(38, 87, 61); }

            case 4 -> { top = new Color(68, 105, 72); bottom = new Color(46, 77, 57); }

            case 5 -> { top = new Color(95, 184, 94); bottom = new Color(63, 145, 78); }

            default -> { top = new Color(80, 180, 90); bottom = new Color(55, 145, 70); }
        }

        g2.setColor(top);

        g2.fillRect(0, y, w, CollisionService.GROUND_HEIGHT);

        g2.setColor(bottom);

        g2.fillRect(0, y + 18, w, CollisionService.GROUND_HEIGHT - 18);

        g2.setColor(top.brighter());

        for (int x = 0; x < w; x += 28) {

            g2.drawLine(x, y + 16, x + 8, y + 4);

            g2.drawLine(x + 8, y + 4, x + 15, y + 16);
        }

        g2.dispose();
    }

    private void drawSun(Graphics2D g2, int x, int y, Color color) {

        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));

        g2.fillOval(x - 18, y - 18, 86, 86);

        g2.setColor(color);

        g2.fillOval(x, y, 50, 50);
    }

    private void drawMoon(Graphics2D g2, int x, int y) {

        g2.setColor(new Color(255, 246, 195));

        g2.fillOval(x, y, 58, 58);

        g2.setColor(new Color(36, 55, 105));

        g2.fillOval(x + 18, y - 5, 55, 55);
    }

    private void drawStars(Graphics2D g2, int w, int groundY) {

        g2.setColor(new Color(255, 246, 190));

        int[][] stars = {
            {70, 70}, {145, 145}, {235, 82}, {330, 130}, {430, 65},
            {520, 112}, {625, 72}, {715, 152}, {800, 92}, {385, 210}
        };

        for (int[] s : stars) {

            if (s[0] < w && s[1] < groundY) {

                g2.fillOval(s[0], s[1], 4, 4);
            }
        }
    }

    private void drawCloud(Graphics2D g2, int x, int y, double scale, Color color) {

        int s = (int) (48 * scale);

        g2.setColor(color);

        g2.fillOval(x, y + s / 3, s, s / 2);

        g2.fillOval(x + s / 3, y, s, s);

        g2.fillOval(x + s, y + s / 4, s, (int) (s * 0.7));

        g2.fillRoundRect(x + s / 4, y + s / 2, (int) (s * 1.6), s / 2, s / 3, s / 3);
    }

    private void drawHills(Graphics2D g2, int w, int groundY, Color back, Color front) {

        g2.setColor(back);

        for (int x = -80; x < w; x += 180) {

            g2.fillOval(x, groundY - 115, 230, 150);
        }

        g2.setColor(front);

        for (int x = -130; x < w; x += 210) {

            g2.fillOval(x, groundY - 75, 260, 110);
        }
    }

    private void drawLightning(Graphics2D g2, int x, int y) {

        Polygon bolt = new Polygon();

        bolt.addPoint(x + 18, y);

        bolt.addPoint(x, y + 46);

        bolt.addPoint(x + 18, y + 43);

        bolt.addPoint(x + 4, y + 88);

        bolt.addPoint(x + 52, y + 30);

        bolt.addPoint(x + 31, y + 34);

        bolt.addPoint(x + 45, y);

        g2.setColor(new Color(255, 235, 112));

        g2.fillPolygon(bolt);
    }

    // แสดง GET READY ก่อนเกมเริ่มจริง
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

    // ผูก SPACEBAR กับการบิน ครั้งแรกจะเปลี่ยนจาก READY เป็น PLAYING และ jump ในครั้งเดียว
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

    // อ่านค่าล่าสุดจาก Player/GameEngine แล้วอัปเดต HUD ไม่ได้เป็นคนเพิ่มคะแนนเอง
    private void updateStatus() {

        playerLabel.setText("ผู้เล่น: " + player.getName());

        levelLabel.setText("ด่าน: " + player.getCurrentLevel());

        heartLabel.setText("หัวใจ: " + gameEngine.getHeartDisplay());

        pipeLabel.setText("ท่อสะสม: " + player.getTotalPipes());
    }
}
