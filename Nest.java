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
 
    public void draw(Graphics g) {
        g.setColor(new Color(139, 90, 43));
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(new Color(100, 60, 25));
        g.drawOval(x, y, WIDTH, HEIGHT);
    }
 
    public int getX() { return x; }
    public int getY() { return y; }
}
 