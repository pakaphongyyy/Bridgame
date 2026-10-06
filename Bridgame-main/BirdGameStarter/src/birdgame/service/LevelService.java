package birdgame.service;

import birdgame.model.LevelConfig;
import birdgame.model.Player;

public class LevelService {

    public boolean checkLevelComplete(Player player) {
        if (isFinalLevel(player)) {
            return false;
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        return player.getLevelHearts() >= required;
    }

    /** ไปด่านถัดไป (Player.nextLevel() รีเซ็ตหัวใจให้แล้ว ท่อสะสมไม่รีเซ็ต) */
    public void goToNextLevel(Player player) {
        player.nextLevel();
    }

    public void resetLevelHeart(Player player) {
        player.resetLevelHearts();
    }

    public boolean isFinalLevel(Player player) {
        return LevelConfig.isFinalLevel(player.getCurrentLevel());
    }
}
