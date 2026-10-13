package birdgame.service;

import birdgame.model.Player;

import java.util.List;

public class GameManager {
    private final List<Player> players;
    private int currentPlayerIndex = 0;

    public GameManager(List<Player> players) {
        this.players = players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean hasNextPlayer() {
        return currentPlayerIndex + 1 < players.size();
    }

    public Player startNextPlayer() {
        currentPlayerIndex++;
        return getCurrentPlayer();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
