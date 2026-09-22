package birdgame.model;

public class Nest {
    private int x;
    private int y;
    private boolean reached;

    public Nest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void checkReached(boolean reached) {
        this.reached = reached;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isReached() { return reached; }
}
