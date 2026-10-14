/*
 * ตัวแทนหัวใจหนึ่งดวงในเกม
 * คลาสนี้รู้ว่าหัวใจอยู่ตรงไหน เคลื่อนที่อย่างไร และถูกเก็บไปแล้วหรือยัง
 * สถานะ collected สำคัญมาก เพราะช่วยกันไม่ให้หัวใจดวงเดียวถูกนับซ้ำหลายเฟรม
 */

package birdgame.model;

import java.awt.Color;

import java.awt.Graphics;

import java.awt.Rectangle;

import java.util.Random;

public class Heart {

    public static final int SIZE = 30;

    private static final Random RANDOM = new Random();

    private int x;

    private final int y;

    private final int speed;

    private boolean collected;

    public Heart(int x, int y, int speed) {

        this.x = x;

        this.y = y;

        this.speed = speed;
    }

    // สร้างหัวใจไว้กลางช่องระหว่างท่อบนกับท่อล่าง เพื่อให้ตำแหน่งสัมพันธ์กับท่อคู่นั้นโดยตรง
    public static Heart spawnInGap(Pipe pipe, int speed) {

        int hx = pipe.getX() + Pipe.WIDTH / 2 - SIZE / 2;

        int hy = pipe.getGapY() + pipe.getGap() / 2 - SIZE / 2;

        return new Heart(hx, hy, speed);
    }

    public static Heart spawnRandom(int panelWidth, int panelHeight, int groundHeight, int speed) {

        int minY = 40;

        int maxY = panelHeight - groundHeight - SIZE - 40;

        int hy = minY + RANDOM.nextInt(Math.max(1, maxY - minY + 1));

        return new Heart(panelWidth, hy, speed);
    }

    // เลื่อนหัวใจจากขวาไปซ้ายด้วยความเร็วเดียวกับที่กำหนดตอนสร้าง
    public void update() {

        x -= speed;
    }

    public boolean isOffScreen() {

        return x + SIZE < 0;
    }

    // กรอบสี่เหลี่ยมนี้คือ hitbox ที่ CollisionService ใช้ตรวจว่านกแตะหัวใจหรือยัง
    public Rectangle getBounds() {

        return new Rectangle(x, y, SIZE, SIZE);
    }

    // ทำเครื่องหมายว่าหัวใจดวงนี้ถูกเก็บแล้ว เฟรมถัดไปจึงไม่ควรนับซ้ำ
    public void collect() { collected = true; }

    public boolean isCollected() { return collected; }

    private static final int DRAW_SIZE = 40;   

    // ถ้า collected เป็น true จะไม่วาดหัวใจอีก ทำให้รูปหายทันทีหลังเก็บ
    public void draw(Graphics g) {

        if (collected) return;

        if (Sprites.HEART != null) {

            int off = (DRAW_SIZE - SIZE) / 2;

            g.drawImage(Sprites.HEART, x - off, y - off, DRAW_SIZE, DRAW_SIZE, null);

            return;
        }

        g.setColor(Color.RED);                     

        int half = SIZE / 2;

        g.fillOval(x, y, half + 2, half + 2);

        g.fillOval(x + half - 2, y, half + 2, half + 2);
        g.fillPolygon(new int[]{x, x + SIZE, x + half},

                      new int[]{y + half / 2 + 3, y + half / 2 + 3, y + SIZE}, 3);
    }

    public int getX() { return x; }

    public int getY() { return y; }
}
