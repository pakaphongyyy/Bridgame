package birdgame.model;

public class Heart {
    private int x;
    private int y;
    private boolean collected;

    public Heart(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int speed) {
        x -= speed;
    }

    public void collect() {
        collected = true;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCollected() { return collected; }
}
