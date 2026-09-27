package birdgame.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
 
public class Pipe {
    public static final int WIDTH = 80;
    public static final int GAP = 180;        // ความสูงช่องว่างให้นกบินผ่าน
    public static final int MIN_MARGIN = 60;  // ระยะขั้นต่ำของช่องว่างจากขอบบน/พื้น
    private static final Random RANDOM = new Random();
 
    private int x;
    private final int gapY;         // ขอบบนของช่องว่าง
    private final int panelHeight;
    private final int speed;
    private boolean passed;         // นกบินผ่านแล้วหรือยัง (ใช้นับคะแนน)
 
    public Pipe(int x, int gapY, int panelHeight, int speed) {
        this.x = x;
        this.gapY = gapY;
        this.panelHeight = panelHeight;
        this.speed = speed;
    }
 
    /** สร้างท่อที่ขอบขวาของจอ พร้อมสุ่มตำแหน่งช่องว่าง */
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed) {
        int minGapY = MIN_MARGIN;
        int maxGapY = panelHeight - groundHeight - GAP - MIN_MARGIN;
        int range = Math.max(1, maxGapY - minGapY + 1);
        int gapY = minGapY + RANDOM.nextInt(range);
        return new Pipe(panelWidth, gapY, panelHeight, speed);
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
        int bottomY = gapY + GAP;
        return new Rectangle(x, bottomY, WIDTH, panelHeight - bottomY);
    }
 
    public void draw(Graphics g) {
        g.setColor(new Color(60, 170, 60));
        Rectangle top = getTopBounds();
        Rectangle bottom = getBottomBounds();
        g.fillRect(top.x, top.y, top.width, top.height);
        g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);
    }
 
    public int getX() { return x; }
    public int getGapY() { return gapY; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
 