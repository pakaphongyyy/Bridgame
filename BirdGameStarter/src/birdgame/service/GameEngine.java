package birdgame.service;

import birdgame.model.GameState;
import birdgame.model.Heart;
import birdgame.model.LevelConfig;
import birdgame.model.Nest;
import birdgame.model.Pipe;
import birdgame.model.Player;

public class GameEngine {
    private final GameState gameState;
    private final ScoreService scoreService = new ScoreService();
    private final LevelService levelService = new LevelService();

    // ===== เพิ่ม: ท่อ / หัวใจ / รัง =====
    private static final int PIPE_SPEED = 3;       // px ต่อเฟรม
    private static final int PIPE_INTERVAL = 100;  // เฟรมระหว่างท่อ (~1.6 วินาที)
    private int tick;

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

    // ===== เพิ่ม: เรียกทุกเฟรมจาก GamePanel =====
    public void update(int panelWidth, int panelHeight) {
        int ground = CollisionService.GROUND_HEIGHT;
        tick++;

        if (gameState.getCurrentPlayer().getCurrentLevel() == 5) {
            // ด่าน 5: ไม่มีท่อ มีรังเลื่อนเข้ามา
            if (gameState.getNest() == null) {
                gameState.setNest(new Nest(panelWidth, panelHeight, ground, PIPE_SPEED, 100));
            }
            gameState.getNest().update();
        } else if (tick % PIPE_INTERVAL == 0) {
            // ด่าน 1-4: ท่อใหม่ + หัวใจกลางช่อง
            Pipe pipe = Pipe.spawn(panelWidth, panelHeight, ground, PIPE_SPEED);
            gameState.getPipes().add(pipe);
            gameState.getHearts().add(Heart.spawnInGap(pipe, PIPE_SPEED));
        }

        for (Pipe pipe : gameState.getPipes()) pipe.update();
        for (Heart heart : gameState.getHearts()) heart.update();

        gameState.getPipes().removeIf(Pipe::isOffScreen);
        gameState.getHearts().removeIf(Heart::isOffScreen);
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
