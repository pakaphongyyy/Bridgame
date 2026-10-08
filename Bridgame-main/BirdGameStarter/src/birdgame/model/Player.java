package birdgame.model;

public class Player {
    private final int playerId;
    private final String name;

    private int currentLevel = 1;
    private int levelHearts = 0;
    private int totalPipes = 0;

    private boolean finished = false;
    private int finishOrder = 0;

    public Player(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelHearts() {
        return levelHearts;
    }

    public int getTotalPipes() {
        return totalPipes;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getFinishOrder() {
        return finishOrder;
    }

    public void addHeart() {
        levelHearts++;
    }

    public void addPipe() {
        totalPipes++;
    }

    public void resetLevelHearts() {
        levelHearts = 0;
    }

    public void nextLevel() {
        if (currentLevel < 5) {
            currentLevel++;
            resetLevelHearts();
        }
    }

    public void finishGame(int order) {
        finished = true;
        finishOrder = order;
    }
}
