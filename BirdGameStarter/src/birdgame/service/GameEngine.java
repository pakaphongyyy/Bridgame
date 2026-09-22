package birdgame.service;

import birdgame.model.GameState;
import birdgame.model.LevelConfig;
import birdgame.model.Player;

public class GameEngine {
    private final GameState gameState;
    private final ScoreService scoreService = new ScoreService();
    private final LevelService levelService = new LevelService();

    public GameEngine(Player player) {
        this.gameState = new GameState(player);
    }

    public void startGame() {
        gameState.startLevel();
    }

    public void collectHeartForTest() {
        Player player = gameState.getCurrentPlayer();

        if (player.getCurrentLevel() == 5) {
            return;
        }

        scoreService.collectHeart(player);

        if (levelService.checkLevelComplete(player)) {
            levelService.goToNextLevel(player);
            gameState.resetLevel();
        }
    }

    public void passPipeForTest() {
        scoreService.passPipe(gameState.getCurrentPlayer());
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getHeartDisplay() {
        Player player = gameState.getCurrentPlayer();

        if (player.getCurrentLevel() == 5) {
            return "FINAL";
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        return player.getLevelHearts() + "/" + required;
    }
}
