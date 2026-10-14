/*
 * หัวใจของ Game Logic
 * update() ถูกเรียกซ้ำทุกเฟรมจาก GamePanel แล้วจัดการทุกอย่างตามลำดับ:
 * นกเคลื่อน -> ท่อ/หัวใจเคลื่อน -> ตรวจเก็บหัวใจ -> เปลี่ยนด่าน
 * -> นับท่อ -> ตรวจชน -> จบเกม
 */

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

    private static final int PIPE_SPEED = 3;

    private static final int PIPE_INTERVAL = 100;

    private int tick;

    private boolean gameOver;

    private boolean won;

    // สร้าง GameState ชุดเดียวสำหรับผู้เล่นคนนี้ เพื่อให้นกที่วาด นกที่ชน และนกที่ขยับเป็นตัวเดียวกัน
    public GameEngine(Player player) {

        this.gameState = new GameState(player);
    }

    // เริ่มรอบใหม่โดยล้างสถานะแพ้/ชนะและรีเซตตัวนับ tick ก่อนให้ด่านเริ่ม
    public void startGame() {

        gameOver = false;

        won = false;

        tick = 0;

        gameState.startLevel();
    }

    // เมธอดสำคัญที่สุดของเกม ถูกเรียกทุกเฟรมและประมวลผลกติกาตามลำดับ หาก gameOver แล้วจะหยุดทันที
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

        // tick ใช้นับจำนวนเฟรม เพื่อกำหนดจังหวะเกิดท่อ/หัวใจ
        tick++;

        // ถ้านกยังไม่ตาย ค่อยอัปเดตแรงโน้มถ่วงและตำแหน่ง
        if (bird.isAlive()) {

            bird.update();
        }

        // ด่าน 5 แยกออกจากด่านปกติ: ไม่มีท่อใหม่ มีแต่รังและเงื่อนไขชนะ
        if (player.getCurrentLevel() == 5) {

            if (gameState.getNest() == null) {
                gameState.setNest(
                    new Nest(panelWidth, panelHeight, ground, PIPE_SPEED, 100)
                );
            }

            Nest nest = gameState.getNest();

            nest.update();

            if (collisionService.checkBirdNest(bird, nest)) {

                player.finishGame(1);

                won = true;

                gameOver = true;

                return;
            }

            if (collisionService.checkBirdGround(bird, panelHeight)) {

                bird.setAlive(false);

                gameOver = true;
            }

            return;
        }

        // ด่าน 1-4: ทุก ๆ PIPE_INTERVAL เฟรม สร้างท่อใหม่พร้อมหัวใจหนึ่งดวงในช่อง
        if (tick % PIPE_INTERVAL == 0) {

            Pipe pipe = Pipe.spawn(panelWidth, panelHeight, ground, PIPE_SPEED);

            gameState.getPipes().add(pipe);

            gameState.getHearts().add(Heart.spawnInGap(pipe, PIPE_SPEED));
        }

        for (Pipe pipe : gameState.getPipes()) {

            pipe.update();
        }

        for (Heart heart : gameState.getHearts()) {

            heart.update();
        }

        // ตรวจเก็บหัวใจจริงจาก Collision; ถ้าได้หัวใจดวงหนึ่งแล้วพอสำหรับเฟรมนี้
        boolean collectedHeart = false;

        for (Heart heart : gameState.getHearts()) {

            if (collisionService.checkBirdHeart(bird, heart)) {

                scoreService.collectHeart(player);

                collectedHeart = true;

                break;
            }
        }

        // ถ้าหัวใจที่เพิ่งเก็บทำให้ครบเกณฑ์ ให้ขึ้นด่านทันที ล้างของในฉาก และเริ่ม tick ใหม่
        if (collectedHeart && levelService.checkLevelComplete(player)) {

            levelService.goToNextLevel(player);

            tick = 0;

            gameState.resetLevel();

            return;
        }

        for (Pipe pipe : gameState.getPipes()) {

            if (collisionService.checkPassedPipe(bird, pipe)) {

                scoreService.passPipe(player);
            }
        }

        for (Pipe pipe : gameState.getPipes()) {

            if (collisionService.checkBirdPipe(bird, pipe)) {

                bird.setAlive(false);

                gameOver = true;

                return;
            }
        }

        if (collisionService.checkBirdGround(bird, panelHeight)) {

            bird.setAlive(false);

            gameOver = true;

            return;
        }

        // ลบวัตถุที่ออกนอกจอแล้ว เพื่อไม่ให้ List โตขึ้นเรื่อย ๆ ตอนเล่นนาน
        gameState.getPipes().removeIf(Pipe::isOffScreen);

        gameState.getHearts().removeIf(Heart::isOffScreen);
    }

    public GameState getGameState() {

        return gameState;
    }

    // คืน Bird จาก GameState โดยตรง จุดนี้ช่วยให้ทั้ง UI และ Collision ใช้นกตัวเดียวกัน
    public Bird getBird() {

        return gameState.getBird();
    }

    public boolean isGameOver() {

        return gameOver;
    }

    public boolean isWon() {

        return won;
    }

    // เตรียมข้อความสำหรับ HUD เช่น 3/10 และใช้ FINAL เมื่อถึงด่าน 5
    public String getHeartDisplay() {

        Player player = gameState.getCurrentPlayer();

        if (player.getCurrentLevel() == 5) {

            return "FINAL";
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());

        return player.getLevelHearts() + "/" + required;
    }
}
