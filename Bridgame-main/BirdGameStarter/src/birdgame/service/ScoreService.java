package birdgame.service;

import birdgame.model.Player;

public class ScoreService {
    public void collectHeart(Player player) {
        player.addHeart();
    }

    public void passPipe(Player player) {
        player.addPipe();
    }

    public int getLevelHeart(Player player) {
        return player.getLevelHearts();
    }

    public int getTotalPipe(Player player) {
        return player.getTotalPipes();
    }
}
