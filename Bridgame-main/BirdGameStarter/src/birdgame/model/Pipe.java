package birdgame.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Pipe {
    public static final int WIDTH = 80;
    public static final int GAP = 180;        // ค่า default ของช่องว่าง (ด่านกำหนดเองได้)
    public static final int MIN_MARGIN = 60;  // ระยะขั้นต่ำของช่องว่างจากขอบบน/พื้น
    private static final Random RANDOM = new Random();

    private int x;
    private final int gapY;         // ขอบบนของช่องว่าง
    private final int gap;          // ความสูงช่องว่าง (มาจาก LevelConfig)
    private final int panelHeight;
    private final int speed;
    private boolean passed;         // นกบินผ่านแล้วหรือยัง (ใช้นับคะแนน)

    public Pipe(int x, int gapY, int gap, int panelHeight, int speed) {
        this.x = x;
        this.gapY = gapY;
        this.gap = gap;
        this.panelHeight = panelHeight;
        this.speed = speed;
    }

    /** สร้างท่อที่ขอบขวาของจอ พร้อมสุ่มตำแหน่งช่องว่าง */
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed, int gap) {
        int minGapY = MIN_MARGIN;
        int maxGapY = panelHeight - groundHeight - gap - MIN_MARGIN;
        int range = Math.max(1, maxGapY - minGapY + 1);
        int gapY = minGapY + RANDOM.nextInt(range);
        return new Pipe(panelWidth, gapY, gap, panelHeight, speed);
    }

    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed) {
        return spawn(panelWidth, panelHeight, groundHeight, speed, GAP);
    }

    /** เลื่อนจากขวาไปซ้าย */
    public void update() {
        x -= speed;
    }

    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }

    public Rectangle getTopBounds() {
        return new Rectangle(x, 0, WIDTH, gapY);
    }

    public Rectangle getBottomBounds() {
        int bottomY = gapY + gap;
        return new Rectangle(x, bottomY, WIDTH, panelHeight - bottomY);
    }

    public static final int CAP_WIDTH = 96;   // ปากท่อกว้างกว่าตัวท่อ (แค่ภาพ ไม่กระทบ hitbox)
    public static final int CAP_HEIGHT = 36;

    public void draw(Graphics g) {
        Rectangle top = getTopBounds();
        Rectangle bottom = getBottomBounds();

        if (Sprites.PIPE_BODY == null || Sprites.PIPE_CAP == null) {   // ไม่มีภาพ → สี่เหลี่ยมเขียว
            g.setColor(new Color(60, 170, 60));
            g.fillRect(top.x, top.y, top.width, top.height);
            g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);
            return;
        }

        int capX = x + WIDTH / 2 - CAP_WIDTH / 2;
        // ตัวท่อ: ยืดภาพแถบแนวตั้งให้เต็มความสูง
        g.drawImage(Sprites.PIPE_BODY, top.x, top.y, WIDTH, top.height, null);
        g.drawImage(Sprites.PIPE_BODY, bottom.x, bottom.y, WIDTH, bottom.height, null);
        // ปากท่อ: ติดขอบช่องว่าง
        g.drawImage(Sprites.PIPE_CAP, capX, top.y + top.height - CAP_HEIGHT, CAP_WIDTH, CAP_HEIGHT, null);
        g.drawImage(Sprites.PIPE_CAP, capX, bottom.y, CAP_WIDTH, CAP_HEIGHT, null);
    }

    public int getX() { return x; }
    public int getGapY() { return gapY; }
    public int getGap() { return gap; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
