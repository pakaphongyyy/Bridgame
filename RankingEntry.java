package birdgame.model;

public class RankingEntry {
    private final String playerName;
    private final boolean finished;
    private final int finishOrder;
    private final int highestLevel;
    private final int totalPipes;

    public RankingEntry(Player player) {
        this.playerName = player.getName();
        this.finished = player.isFinished();
        this.finishOrder = player.getFinishOrder();
        this.highestLevel = player.getCurrentLevel();
        this.totalPipes = player.getTotalPipes();
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getFinishOrder() {
        return finishOrder;
    }

    public int getHighestLevel() {
        return highestLevel;
    }

    public int getTotalPipes() {
        return totalPipes;
    }
}
