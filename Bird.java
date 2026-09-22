package birdgame.model;

public class Bird {
    private int x;
    private int y;
    private double velocityY;
    private boolean alive = true;
    private boolean flyingToNest = false;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void jump() {
        velocityY = -7.5;
    }

    public void update() {
        velocityY += 0.45;
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
}
