package birdgame.model;

public class PlayerRecord {
    private final String playerName;
    private final boolean finished;
    private final int highestLevel;
    private final int totalPipes;
    private final String playedAt;

    public PlayerRecord(String playerName,
                        boolean finished,
                        int highestLevel,
                        int totalPipes,
                        String playedAt) {
        this.playerName = playerName;
        this.finished = finished;
        this.highestLevel = highestLevel;
        this.totalPipes = totalPipes;
        this.playedAt = playedAt;
    }

    public PlayerRecord(Player player, String playedAt) {
        this(
            player.getName(),
            player.isFinished(),
            player.getCurrentLevel(),
            player.getTotalPipes(),
            playedAt
        );
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getHighestLevel() {
        return highestLevel;
    }

    public int getTotalPipes() {
        return totalPipes;
    }

    public String getPlayedAt() {
        return playedAt;
    }
}
