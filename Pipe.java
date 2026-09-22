package birdgame.model;

public class Pipe {
    private int x;
    private final int gapY;
    private final int width;
    private boolean passed;

    public Pipe(int x, int gapY, int width) {
        this.x = x;
        this.gapY = gapY;
        this.width = width;
    }

    public void move(int speed) {
        x -= speed;
    }

    public int getX() { return x; }
    public int getGapY() { return gapY; }
    public int getWidth() { return width; }
    public boolean isPassed() { return passed; }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
