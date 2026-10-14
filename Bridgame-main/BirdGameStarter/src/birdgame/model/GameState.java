package birdgame.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Player currentPlayer;
    private Bird bird;
    private final List<Pipe> pipes = new ArrayList<>();
    private final List<Heart> hearts = new ArrayList<>();
    private Nest nest;

    private boolean gameRunning;
    private boolean levelCompleted;
    private boolean gameCompleted;

    public GameState(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        resetLevel();
    }

    public void resetLevel() {
        bird = new Bird(140, 260);
        pipes.clear();
        hearts.clear();
        nest = null;
        levelCompleted = false;
        gameCompleted = false;
    }

    public void startLevel() {
        gameRunning = true;
    }

    public void completeLevel() {
        levelCompleted = true;
    }

    public void completeGame() {
        gameCompleted = true;
        gameRunning = false;
    }

    public Player getCurrentPlayer() { return currentPlayer; }
    public Bird getBird() { return bird; }
    public List<Pipe> getPipes() { return pipes; }
    public List<Heart> getHearts() { return hearts; }
    public Nest getNest() { return nest; }
    public boolean isGameRunning() { return gameRunning; }
    public boolean isLevelCompleted() { return levelCompleted; }
    public boolean isGameCompleted() { return gameCompleted; }

    public void setNest(Nest nest) {
        this.nest = nest;
    }
}
