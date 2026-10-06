package birdgame.ui;

import birdgame.model.Bird;
import birdgame.model.GameState;
import birdgame.model.Heart;
import birdgame.model.LevelConfig;
import birdgame.model.Pipe;
import birdgame.model.Player;
import birdgame.service.CollisionService;
import birdgame.service.GameEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

/** วาดเกม + รับคีย์บอร์ด — กติกาทั้งหมดอยู่ใน GameEngine */
public class GamePanel extends JPanel {
    private static final int FRAME_MS = 16;          // ~60 fps
    private static final int BANNER_FRAMES = 90;     // ป้าย "ด่าน X" ~1.5 วินาที

    // ---- ภาพนก 2 เฟรม ----
    private static final int BIRD_DRAW_SIZE = 72;    // ใหญ่กว่า hitbox 40 เพราะปีกยื่นออกนอกตัว
    private static final int FLAP_FRAMES = 8;        // โชว์ภาพปีกยก ~130ms หลังกด SPACE
    private final BufferedImage birdFlapImg = loadImage("/birdgame/assets/bird_flap.png");
    private final BufferedImage birdGlideImg = loadImage("/birdgame/assets/bird_glide.png");
    private int flapTimer;

    private final Player player;
    private final GameEngine gameEngine;
    private final GameState gameState;

    private final Runnable onLose;
    private final Runnable onWin;

    private final JLabel playerLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JLabel heartLabel = new JLabel();
    private final JLabel pipeLabel = new JLabel();

    private JPanel center;
    private final Timer timer;

    private int shownLevel;
    private int bannerTimer = BANNER_FRAMES;
    private boolean ended;

    public GamePanel(Player player, Runnable onLose, Runnable onWin) {
        this.player = player;
        this.gameEngine = new GameEngine(player);
        this.gameState = gameEngine.getGameState();
        this.onLose = onLose;
        this.onWin = onWin;
        this.shownLevel = player.getCurrentLevel();

        setFocusable(true);
        setLayout(new BorderLayout());
        setBackground(new Color(232, 247, 255));

        buildUI();
        bindKeys();

        gameEngine.startGame();
        updateStatus();

        timer = new Timer(FRAME_MS, e -> onFrame());
        timer.start();
    }

    // ---------------- ทุกเฟรม ----------------

    private void onFrame() {
        if (ended) return;
        int w = center.getWidth();
        int h = center.getHeight();
        if (w == 0 || h == 0) return;                // ยังไม่ได้ layout

        gameEngine.update(w, h);

        if (flapTimer > 0) flapTimer--;
        if (bannerTimer > 0) bannerTimer--;

        if (player.getCurrentLevel() != shownLevel) { // เพิ่งผ่านด่าน
            shownLevel = player.getCurrentLevel();
            bannerTimer = BANNER_FRAMES;
        }

        updateStatus();
        center.repaint();

        if (gameState.isGameOver()) {
            finish(player.getName() + " " + gameState.getGameOverReason() + " / จบรอบ", onLose);
        } else if (gameState.isGameCompleted()) {
            finish(player.getName() + " บินเข้ารังสำเร็จ!", onWin);
        }
    }

    private void finish(String message, Runnable next) {
        ended = true;
        timer.stop();
        JOptionPane.showMessageDialog(this, message);
        next.run();
    }

    // ---------------- UI ----------------

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
                drawGame(g, getWidth(), getHeight());
            }
        };

        JLabel help = new JLabel(
            GameEngine.BIRD_ENABLED
                ? "SPACE / ↑ = บิน   |   ทดสอบ: H = เก็บหัวใจ  P = ผ่านท่อ  X = แพ้  N = เข้ารัง (ด่าน 5)"
                : "ยังไม่มีนก   |   H = เก็บหัวใจ (ครบแล้วขึ้นด่าน)  P = ผ่านท่อ  X = แพ้  N = เข้ารัง (ด่าน 5)",
            SwingConstants.CENTER
        );
        help.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        add(hud, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(help, BorderLayout.SOUTH);
    }

    private void drawGame(Graphics g, int w, int h) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setColor(new Color(160, 220, 255));
        g.fillRect(0, 0, w, h);

        for (Pipe pipe : gameState.getPipes()) pipe.draw(g);
        for (Heart heart : gameState.getHearts()) heart.draw(g);
        if (gameState.getNest() != null) gameState.getNest().draw(g);

        g.setColor(new Color(80, 180, 90));
        g.fillRect(0, h - CollisionService.GROUND_HEIGHT, w, CollisionService.GROUND_HEIGHT);

        if (GameEngine.BIRD_ENABLED) drawBird(g, gameState.getBird());

        if (bannerTimer > 0 || gameEngine.isWaitingForFirstJump()) {
            String title = gameEngine.isFinalLevel()
                    ? "ด่าน 5 - บินเข้ารัง!"
                    : "ด่าน " + player.getCurrentLevel()
                      + "  (เก็บ " + LevelConfig.getHeartRequired(player.getCurrentLevel()) + " หัวใจ)";
            g.setColor(Color.DARK_GRAY);
            g.setFont(AppFont.bold(28));
            drawCentered(g, title, w, h / 2 - 60);
            g.setFont(AppFont.bold(18));
            if (GameEngine.BIRD_ENABLED) drawCentered(g, "กด SPACE เพื่อบิน", w, h / 2 - 25);
        }
    }

    private void drawCentered(Graphics g, String text, int w, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (w - fm.stringWidth(text)) / 2, y);
    }

    private void drawBird(Graphics g, Bird bird) {
        BufferedImage img = flapTimer > 0 ? birdFlapImg : birdGlideImg;
        int size = CollisionService.BIRD_SIZE;

        if (img == null) {                           // โหลดภาพไม่ได้ → วงกลมเหลืองแทน
            g.setColor(Color.YELLOW);
            g.fillOval(bird.getX(), bird.getY(), size, size);
            return;
        }

        // จัดภาพให้อยู่กึ่งกลาง hitbox
        int drawX = bird.getX() + size / 2 - BIRD_DRAW_SIZE / 2;
        int drawY = bird.getY() + size / 2 - BIRD_DRAW_SIZE / 2;
        g.drawImage(img, drawX, drawY, BIRD_DRAW_SIZE, BIRD_DRAW_SIZE, null);
    }

    private BufferedImage loadImage(String path) {
        try {
            URL url = getClass().getResource(path);
            return url == null ? null : ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------- คีย์บอร์ด ----------------

    private void bindKeys() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (ended) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE, KeyEvent.VK_UP -> {
                        gameEngine.jump();
                        flapTimer = FLAP_FRAMES;
                    }
                    // ปุ่มทดสอบ (ลบออกได้ตอนส่งงาน)
                    case KeyEvent.VK_H -> gameEngine.collectHeartForTest();
                    case KeyEvent.VK_P -> gameEngine.passPipeForTest();
                    case KeyEvent.VK_X -> gameEngine.gameOver("ชนท่อ (ทดสอบ)");
                    case KeyEvent.VK_N -> {
                        if (gameEngine.isFinalLevel()) gameEngine.reachNest();
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
