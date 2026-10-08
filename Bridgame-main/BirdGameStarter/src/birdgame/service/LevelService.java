package birdgame.service;

import birdgame.model.LevelConfig;
import birdgame.model.Player;

public class LevelService {

    public boolean checkLevelComplete(Player player) {
        if (player.getCurrentLevel() >= 5) {
            return false;
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        return player.getLevelHearts() >= required;
    }

    public void goToNextLevel(Player player) {
        player.nextLevel();
    }

    public void resetLevelHeart(Player player) {
        player.resetLevelHearts();
    }

    public boolean isFinalLevel(Player player) {
        return player.getCurrentLevel() == 5;
    }
}
