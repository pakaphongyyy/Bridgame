package birdgame.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Nest {
    public static final int WIDTH = 90;
    public static final int HEIGHT = 50;

    private int x;
    private final int y;
    private final int speed;
    private final int stopX;   // รังหยุดเลื่อนเมื่อถึงตำแหน่งนี้

    /** รังเกิดที่ขอบขวา วางบนพื้น แล้วเลื่อนเข้ามาจนถึง stopX */
    public Nest(int panelWidth, int panelHeight, int groundHeight, int speed, int stopX) {
        this.x = panelWidth;
        this.y = panelHeight - groundHeight - HEIGHT;
        this.speed = speed;
        this.stopX = stopX;
    }

    public void update() {
        if (x > stopX) {
            x = Math.max(stopX, x - speed);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    private static final int DRAW_WIDTH = 126;   // ภาพกว้างกว่า hitbox (กิ่งไม้ยื่นออก)
    private static final int DRAW_HEIGHT = 70;

    public void draw(Graphics g) {
        if (Sprites.NEST != null) {
            int drawX = x + WIDTH / 2 - DRAW_WIDTH / 2;
            int drawY = y + HEIGHT - DRAW_HEIGHT + 6;   // ก้นรังจมพื้นนิดหน่อย
            g.drawImage(Sprites.NEST, drawX, drawY, DRAW_WIDTH, DRAW_HEIGHT, null);
            return;
        }

        g.setColor(new Color(139, 90, 43));          // ไม่มีภาพ → วงรีน้ำตาล
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(new Color(100, 60, 25));
        g.drawOval(x, y, WIDTH, HEIGHT);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
