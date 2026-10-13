package birdgame.model;
import java.awt.Graphics;

public class Bird {

    private int x;
    private int y;

    // ความเร็วขึ้นลงของนก
    private double velocityY;

    
    private boolean alive = true;
    private boolean flyingToNest = false;
    private int animationTick = 0;  //เก็บเฟรมนก
    private int animationFrame = 0;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // กด Space = นกบินขึ้น
    public void jump() {
        velocityY = -7.5;
    }

    public void reset() {
    x = 120;
    y = 200;
    velocityY = 0;
    alive = true;
    flyingToNest = false;
}
    public void update() {
    velocityY += 0.45;
    y += (int) velocityY;

    // Animation นกกะพือปีก3เฟรม
    animationTick++;

    if (animationTick >= 6) {
        animationTick = 0;
        animationFrame++;

        if (animationFrame >= 3) {
            animationFrame = 0;
        }
    }
}

    public void draw(Graphics g) {

    if (Sprites.BIRD == null) {
        return;
    }
       //ตัดรูป 
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