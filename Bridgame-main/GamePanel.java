package birdgame.ui;
 
import birdgame.model.Bird;
import birdgame.model.Heart;
import birdgame.model.LevelConfig;
import birdgame.model.Nest;
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
import java.util.ArrayList;
import java.util.List;
 
public class GamePanel extends JPanel {
    // ความเร็ว / ช่องว่าง / ระยะห่างท่อ มาจาก gameEngine.getConfig() (คนที่ 4)
    private static final int BANNER_FRAMES = 90;   // โชว์ "ด่าน X" ~1.5 วินาที
 
    private final Player player;
    private final GameEngine gameEngine;
    private final CollisionService collisionService = new CollisionService();
 
    private final Runnable onLose;
    private final Runnable onWin;
 
    private final JLabel playerLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JLabel heartLabel = new JLabel();
    private final JLabel pipeLabel = new JLabel();
 
    private JPanel center;
    private final Timer timer;
 
    private Bird bird;                               // สร้างตอนเฟรมแรก (ต้องรู้ความสูงจอก่อน)
    private final List<Pipe> pipes = new ArrayList<>();
    private final List<Heart> hearts = new ArrayList<>();
    private Nest nest;
    private int tick;
    private int bannerTimer = BANNER_FRAMES;
    private boolean ended;
 
    // ---- ภาพนก 2 เฟรม ----
    private static final int BIRD_DRAW_SIZE = 72;  // ขนาดภาพ (ใหญ่กว่า hitbox 40 เพราะปีกยื่นออกนอกตัว)
    private static final int FLAP_FRAMES = 8;      // โชว์ภาพปีกยกกี่เฟรมหลังกด SPACE (~130ms)
    private final BufferedImage birdFlapImg = loadImage("/birdgame/assets/bird_flap.png"); // ปีกยก
    private final BufferedImage birdGlideImg = loadImage("/birdgame/assets/bird_glide.png"); // ปีกกาง
    private int flapTimer;
 
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
 
        timer = new Timer(16, e -> gameLoop());
        timer.start();
    }
 
    // ---------------- game loop ----------------
 
    private void gameLoop() {
        if (ended) return;
        int w = center.getWidth();
        int h = center.getHeight();
        if (w == 0 || h == 0) return;               // ยังไม่ได้ layout
 
        if (bird == null) {
            bird = new Bird(120, h / 2 - CollisionService.BIRD_SIZE / 2);
        }
 
        LevelConfig cfg = gameEngine.getConfig();
        int speed = cfg.getPipeSpeed();
 
        bird.update();
        if (flapTimer > 0) flapTimer--;
        if (bannerTimer > 0) bannerTimer--;
        tick++;
 
        // spawn: ด่าน 1-4 = ท่อ + หัวใจกลางช่องทุกท่อ, ด่าน 5 = รัง
        if (!cfg.isFinalLevel() && tick % cfg.getPipeInterval() == 0) {
            Pipe pipe = Pipe.spawn(w, h, CollisionService.GROUND_HEIGHT, speed, cfg.getPipeGap());
            pipes.add(pipe);
            hearts.add(Heart.spawnInGap(pipe, speed));
        }
        if (cfg.isFinalLevel() && nest == null) {
            nest = new Nest(w, h, CollisionService.GROUND_HEIGHT, speed, w - 200);
        }
 
        // update + collision
        for (Pipe pipe : pipes) {
            pipe.update();
            if (collisionService.checkBirdPipe(bird, pipe)) {
                lose("ชนท่อ");
                return;
            }
            if (collisionService.checkPassedPipe(bird, pipe)) {
                gameEngine.passPipe();
            }
        }
 
        boolean levelUp = false;
        for (Heart heart : hearts) {
            heart.update();
            if (collisionService.checkBirdHeart(bird, heart) && gameEngine.collectHeart()) {
                levelUp = true;
                break;                               // ห้ามแก้ list ระหว่างวน → เคลียร์หลังลูป
            }
        }
        if (levelUp) {
            onLevelUp();
            return;
        }
 
        if (nest != null) {
            nest.update();
            if (collisionService.checkBirdNest(bird, nest)) {   // เช็กก่อนพื้น เพราะรังวางบนพื้น
                win();
                return;
            }
        }
        if (collisionService.checkBirdGround(bird, h)) {
            lose("ตกพื้น / ชนเพดาน");
            return;
        }
 
        pipes.removeIf(Pipe::isOffScreen);
        hearts.removeIf(hh -> hh.isCollected() || hh.isOffScreen());
 
        updateStatus();
        center.repaint();
    }
 
    /** เข้าด่านใหม่: ล้างของบนจอ เริ่มนับท่อใหม่ โชว์ป้ายด่าน */
    private void onLevelUp() {
        pipes.clear();
        hearts.clear();
        nest = null;
        tick = 0;
        bannerTimer = BANNER_FRAMES;
        updateStatus();
        center.repaint();
    }
 
    private void lose(String reason) {
        gameEngine.gameOver();
        endGame();
        JOptionPane.showMessageDialog(this, player.getName() + " " + reason + " / จบรอบ");
        onLose.run();
    }
 
    private void win() {
        if (!gameEngine.reachNest()) return;
        endGame();
        JOptionPane.showMessageDialog(this, player.getName() + " บินเข้ารังสำเร็จ!");
        onWin.run();
    }
 
    private void endGame() {
        ended = true;
        timer.stop();
        if (bird != null) bird.setAlive(false);
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
                int w = getWidth();
                int h = getHeight();
 
                g.setColor(new Color(160, 220, 255));
                g.fillRect(0, 0, w, h);
 
                for (Pipe pipe : pipes) pipe.draw(g);
                for (Heart heart : hearts) heart.draw(g);
                if (nest != null) nest.draw(g);
 
                g.setColor(new Color(80, 180, 90));
                g.fillRect(0, h - CollisionService.GROUND_HEIGHT, w, CollisionService.GROUND_HEIGHT);
 
                if (bird != null) drawBird(g);
 
                if (bannerTimer > 0) {                             // ป้ายตอนเริ่มด่าน
                    g.setColor(Color.DARK_GRAY);
                    g.setFont(AppFont.bold(28));
                    String title = gameEngine.isFinalLevel()
                            ? "ด่าน 5 - บินเข้ารัง!"
                            : "ด่าน " + player.getCurrentLevel()
                              + "  (เก็บ " + gameEngine.getConfig().getHeartsRequired() + " หัวใจ)";
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString(title, (w - fm.stringWidth(title)) / 2, h / 2 - 60);
                    g.setFont(AppFont.bold(18));
                    g.drawString("กด SPACE เพื่อบิน", w / 2 - 70, h / 2 - 25);
                }
            }
        };
        center.setBackground(Color.WHITE);
 
        JLabel help = new JLabel(
            "SPACE = บิน   |   ทดสอบ: H = เก็บหัวใจ  P = ผ่านท่อ  X = แพ้  N = เข้ารัง (ด่าน 5)",
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
                if (ended) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE, KeyEvent.VK_UP -> {
                        if (bird != null) {
                            bird.jump();
                            flapTimer = FLAP_FRAMES;   // สลับเป็นภาพปีกยก
                        }
                    }
                    // ปุ่มทดสอบเดิม เก็บไว้ debug
                    case KeyEvent.VK_H -> {
                        if (gameEngine.collectHeartForTest()) onLevelUp();
                        updateStatus();
                    }
                    case KeyEvent.VK_P -> {
                        gameEngine.passPipeForTest();
                        updateStatus();
                    }
                    case KeyEvent.VK_X -> lose("ชนท่อ (ทดสอบ)");
                    case KeyEvent.VK_N -> {
                        if (gameEngine.isFinalLevel()) win();
                    }
                }
            }
        });
    }
 
    // ---------------- bird sprite ----------------
 
    private BufferedImage loadImage(String path) {
        try {
            URL url = getClass().getResource(path);
            return url == null ? null : ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }
 
    private void drawBird(Graphics g) {
        BufferedImage img = flapTimer > 0 ? birdFlapImg : birdGlideImg;
        int size = CollisionService.BIRD_SIZE;
 
        if (img == null) {                             // โหลดภาพไม่ได้ → วาดวงกลมเหลืองแทน
            g.setColor(Color.YELLOW);
            g.fillOval(bird.getX(), bird.getY(), size, size);
            return;
        }
 
        // จัดภาพให้อยู่กึ่งกลาง hitbox
        int drawX = bird.getX() + size / 2 - BIRD_DRAW_SIZE / 2;
        int drawY = bird.getY() + size / 2 - BIRD_DRAW_SIZE / 2;
 
        // ภาพ PNG หันขวาอยู่แล้ว วาดตรง ๆ ได้เลย
        g.drawImage(img, drawX, drawY, BIRD_DRAW_SIZE, BIRD_DRAW_SIZE, null);
    }
 
    private void updateStatus() {
        playerLabel.setText("ผู้เล่น: " + player.getName());
        levelLabel.setText("ด่าน: " + player.getCurrentLevel());
        heartLabel.setText("หัวใจ: " + gameEngine.getHeartDisplay());
        pipeLabel.setText("ท่อสะสม: " + player.getTotalPipes());
    }
}