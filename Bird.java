package birdgame.model;

public class Bird {
    private int x;
    private int y;
    private double velocityY;
    private boolean alive = true;
    private boolean flyingToNest = false;

    private static final double GRAVITY = 0.45;
    private static final double JUMP_POWER = -7.5;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocityY = 0;
    }

    public void jump() {
        if (!alive) return;
        velocityY = JUMP_POWER;
    }

    public void update() {
        if (!alive) return;
        velocityY += GRAVITY;
        y += (int) velocityY;
    }

    public void fall() {
        update();
    }

    public void flyToNest() {
        flyingToNest = true;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public double getVelocityY() { return velocityY; }
    public boolean isAlive() { return alive; }
    public boolean isFlyingToNest() { return flyingToNest; }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
