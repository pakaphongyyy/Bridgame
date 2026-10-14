/*
 * ตัวแทนท่อหนึ่งคู่ในเกม
 * เก็บตำแหน่ง ช่องว่าง ความเร็ว และสถานะว่าเคยนับคะแนนผ่านท่อนี้หรือยัง
 * passed เป็นตัวสำคัญที่กันการนับท่อเดิมซ้ำทุกเฟรม
 */

package birdgame.model;

import java.awt.Color;

import java.awt.Graphics;

import java.awt.Rectangle;

import java.util.Random;

public class Pipe {

    public static final int WIDTH = 80;

    public static final int GAP = 180;        

    public static final int MIN_MARGIN = 60;  

    private static final Random RANDOM = new Random();

    private int x;

    private final int gapY;         

    private final int gap;          

    private final int panelHeight;

    private final int speed;
    private boolean passed;         

    public Pipe(int x, int gapY, int gap, int panelHeight, int speed) {

        this.x = x;

        this.gapY = gapY;

        this.gap = gap;

        this.panelHeight = panelHeight;

        this.speed = speed;
    }

    // สุ่มตำแหน่งช่องว่างของท่อ โดยเว้นระยะจากขอบบนและพื้นเพื่อให้ยังมีทางบินผ่าน
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed, int gap) {

        int minGapY = MIN_MARGIN;

        int maxGapY = panelHeight - groundHeight - gap - MIN_MARGIN;

        int range = Math.max(1, maxGapY - minGapY + 1);

        int gapY = minGapY + RANDOM.nextInt(range);

        return new Pipe(panelWidth, gapY, gap, panelHeight, speed);
    }

    // สุ่มตำแหน่งช่องว่างของท่อ โดยเว้นระยะจากขอบบนและพื้นเพื่อให้ยังมีทางบินผ่าน
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed) {

        return spawn(panelWidth, panelHeight, groundHeight, speed, GAP);
    }

    // ทุกเฟรมท่อจะเลื่อนไปทางซ้ายตาม speed
    public void update() {

        x -= speed;
    }

    public boolean isOffScreen() {

        return x + WIDTH < 0;
    }

    // hitbox ของท่อด้านบน
    public Rectangle getTopBounds() {

        return new Rectangle(x, 0, WIDTH, gapY);
    }

    // hitbox ของท่อด้านล่าง
    public Rectangle getBottomBounds() {

        int bottomY = gapY + gap;

        return new Rectangle(x, bottomY, WIDTH, panelHeight - bottomY);
    }

    public static final int CAP_WIDTH = 96;   

    public static final int CAP_HEIGHT = 36;

    public void draw(Graphics g) {

        Rectangle top = getTopBounds();

        Rectangle bottom = getBottomBounds();

        if (Sprites.PIPE_BODY == null || Sprites.PIPE_CAP == null) {   

            g.setColor(new Color(60, 170, 60));

            g.fillRect(top.x, top.y, top.width, top.height);

            g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);

            return;
        }

        int capX = x + WIDTH / 2 - CAP_WIDTH / 2;

        g.drawImage(Sprites.PIPE_BODY, top.x, top.y, WIDTH, top.height, null);

        g.drawImage(Sprites.PIPE_BODY, bottom.x, bottom.y, WIDTH, bottom.height, null);

        g.drawImage(Sprites.PIPE_CAP, capX, top.y + top.height - CAP_HEIGHT, CAP_WIDTH, CAP_HEIGHT, null);

        g.drawImage(Sprites.PIPE_CAP, capX, bottom.y, CAP_WIDTH, CAP_HEIGHT, null);
    }

    public int getX() { return x; }

    public int getGapY() { return gapY; }

    public int getGap() { return gap; }

    public boolean isPassed() { return passed; }

    // บันทึกว่าท่อนี้ถูกนับคะแนนไปแล้ว เพื่อไม่ให้ +1 ซ้ำในเฟรมถัดไป
    public void setPassed(boolean passed) { this.passed = passed; }
}
