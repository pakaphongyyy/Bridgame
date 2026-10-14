package birdgame.service;

import birdgame.model.Bird;
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
    private final CollisionService collisionService = new CollisionService();

    // ท่อ / หัวใจ / รัง
    private static final int PIPE_SPEED = 3;
    private static final int PIPE_INTERVAL = 100;

    private int tick;
    private boolean gameOver;
    private boolean won;

    public GameEngine(Player player) {
        this.gameState = new GameState(player);
    }

    public void startGame() {
        gameOver = false;
        won = false;
        tick = 0;
        gameState.startLevel();
    }

    /** เรียกทุกเฟรมจาก GamePanel */
    public void update(int panelWidth, int panelHeight) {
        if (gameOver || panelWidth <= 0 || panelHeight <= 0) {
            return;
        }

        Player player = gameState.getCurrentPlayer();
        Bird bird = gameState.getBird();
        int ground = CollisionService.GROUND_HEIGHT;

        if (player == null || bird == null) {
            return;
        }

        tick++;

        // นกเคลื่อนที่ตาม Gravity ทุกเฟรม
        if (bird.isAlive()) {
            bird.update();
        }

        // ==================== ด่าน 5 ====================
        if (player.getCurrentLevel() == 5) {
            if (gameState.getNest() == null) {
                gameState.setNest(
                    new Nest(panelWidth, panelHeight, ground, PIPE_SPEED, 100)
                );
            }

            Nest nest = gameState.getNest();
            nest.update();

            // ถึงรังจริง = ชนะ
            if (collisionService.checkBirdNest(bird, nest)) {
                player.finishGame(1);
                won = true;
                gameOver = true;
                return;
            }

            // ตกพื้น/ชนเพดานก่อนถึงรัง = แพ้
            if (collisionService.checkBirdGround(bird, panelHeight)) {
                bird.setAlive(false);
                gameOver = true;
            }

            return;
        }

        // ==================== ด่าน 1-4 ====================

        // สร้างท่อพร้อมหัวใจกลางช่อง
        if (tick % PIPE_INTERVAL == 0) {
            Pipe pipe = Pipe.spawn(panelWidth, panelHeight, ground, PIPE_SPEED);
            gameState.getPipes().add(pipe);
            gameState.getHearts().add(Heart.spawnInGap(pipe, PIPE_SPEED));
        }

        // ให้ท่อและหัวใจเคลื่อนจากขวาไปซ้าย
        for (Pipe pipe : gameState.getPipes()) {
            pipe.update();
        }

        for (Heart heart : gameState.getHearts()) {
            heart.update();
        }

        // 1) เก็บหัวใจจากการชนจริงของ Bird กับ Heart
        boolean collectedHeart = false;
        for (Heart heart : gameState.getHearts()) {
            if (collisionService.checkBirdHeart(bird, heart)) {
                scoreService.collectHeart(player);
                collectedHeart = true;
                break;
            }
        }

        // เก็บครบตามด่าน -> เปลี่ยนด่านและรีเซตหัวใจ
        if (collectedHeart && levelService.checkLevelComplete(player)) {
            levelService.goToNextLevel(player);
            tick = 0;
            gameState.resetLevel();
            return;
        }

        // 2) นับท่อจากการบินผ่านจริง นับครั้งเดียวต่อท่อ
        for (Pipe pipe : gameState.getPipes()) {
            if (collisionService.checkPassedPipe(bird, pipe)) {
                scoreService.passPipe(player);
            }
        }

        // 3) ชนท่อจริง = Game Over
        for (Pipe pipe : gameState.getPipes()) {
            if (collisionService.checkBirdPipe(bird, pipe)) {
                bird.setAlive(false);
                gameOver = true;
                return;
            }
        }

        // 4) ชนพื้นหรือเพดาน = Game Over
        if (collisionService.checkBirdGround(bird, panelHeight)) {
            bird.setAlive(false);
            gameOver = true;
            return;
        }

        // ลบวัตถุที่ออกนอกจอ
        gameState.getPipes().removeIf(Pipe::isOffScreen);
        gameState.getHearts().removeIf(Heart::isOffScreen);
    }

    public GameState getGameState() {
        return gameState;
    }

    /** ใช้นกตัวเดียวกับ GameState เพื่อให้การวาด/ชน/รีเซตด่านตรงกัน */
    public Bird getBird() {
        return gameState.getBird();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWon() {
        return won;
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
