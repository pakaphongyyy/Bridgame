/*
 * เก็บข้อมูลและการเคลื่อนที่ของนก
 * ตรงนี้เป็นที่อยู่ของตำแหน่งนก ความเร็วตอนตก/กระโดด และเฟรม Animation
 * GamePanel มีหน้าที่วาด แต่การเคลื่อนที่จริงของนกมาจากคลาสนี้
 */

package birdgame.model;

import java.awt.Graphics;

public class Bird {

    private int x;

    private int y;

    private double velocityY;

    private boolean alive = true;

    private boolean flyingToNest = false;

    private int animationTick = 0;  

    private int animationFrame = 0;

    // ตอนสร้างนก เราเก็บตำแหน่งเริ่มต้นไว้ก่อน ส่วนความเร็วจะเริ่มที่ 0
    public Bird(int x, int y) {

        this.x = x;

        this.y = y;
    }

    // เวลาผู้เล่นกด SPACE เราไม่ย้าย y ขึ้นทันที แต่เปลี่ยนความเร็วให้เป็นค่าติดลบ แล้วปล่อยให้ update() คำนวณการบินต่อ
    public void jump() {

        velocityY = -7.5;
    }

    // ใช้คืนค่านกกลับสู่สถานะเริ่มต้น เช่น ตำแหน่ง ความเร็ว และสถานะว่ายังมีชีวิต
    public void reset() {

    x = 120;

    y = 200;

    velocityY = 0;

    alive = true;

    flyingToNest = false;
}

    // ถูกเรียกทุกเฟรม: เพิ่มแรงโน้มถ่วงลงใน velocityY แล้วนำความเร็วนั้นไปขยับตำแหน่ง y
    public void update() {

    velocityY += 0.45;

    y += (int) velocityY;

    animationTick++;

    if (animationTick >= 6) {

        animationTick = 0;

        animationFrame++;

        if (animationFrame >= 3) {

            animationFrame = 0;
        }
    }
}

    // วาดเฟรมปัจจุบันของนกจาก sprite sheet ถ้ารูปโหลดไม่สำเร็จเมธอดนี้จะไม่วาดรูป
    public void draw(Graphics g) {

    if (Sprites.BIRD == null) {

        return;
    }

    int[] frameX = {81, 480, 1028};

    int[] frameWidth = {312, 462, 313};

    int sx = frameX[animationFrame];

    int sw = frameWidth[animationFrame];

    g.drawImage(
            Sprites.BIRD,
            x,
            y,
            x + 55,
            y + 55,
            sx,
            0,
            sx + sw,
            Sprites.BIRD.getHeight(),
            null
    );
}

    public void fall() {

        update();
    }

    // เก็บสถานะไว้รองรับฉากจบว่าตอนนี้นกกำลังเข้าสู่ช่วงบินไปหารัง
    public void flyToNest() {

        flyingToNest = true;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public double getVelocityY() {

        return velocityY;
    }

    public boolean isAlive() {

        return alive;
    }

    public boolean isFlyingToNest() {

        return flyingToNest;
    }

    public void setAlive(boolean alive) {

        this.alive = alive;
    }

    public void setPosition(int x, int y) {

        this.x = x;

        this.y = y;
    }
}
