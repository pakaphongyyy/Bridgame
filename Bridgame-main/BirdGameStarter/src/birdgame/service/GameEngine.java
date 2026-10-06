package birdgame.service;

import birdgame.model.Bird;
import birdgame.model.GameState;
import birdgame.model.Heart;
import birdgame.model.LevelConfig;
import birdgame.model.Nest;
import birdgame.model.Pipe;
import birdgame.model.Player;

/**
 * Game Loop + กติกาเกมทั้งหมด
 * GamePanel เรียก update() ทุกเฟรม แล้วเช็ก GameState ว่าแพ้ / ชนะ / เปลี่ยนด่านหรือยัง
 */
public class GameEngine {
    /**
     * ปิดนกไว้ก่อน (ระหว่างรอคนที่ 2) — มีแค่ท่อ หัวใจ รัง เลื่อนบนจอ ไม่มีการชน
     * เปลี่ยนเป็น true เมื่อจะใช้นกจริง: นกจะโผล่ บินด้วย SPACE และระบบชนทำงานทั้งหมด
     */
    public static final boolean BIRD_ENABLED = false;

    // นกอยู่ที่ x คงที่ รังจึงต้องเลื่อนมาหยุดใต้นกพอดี (กึ่งกลางรัง = กึ่งกลางนก)
    private static final int NEST_STOP_X =
            GameState.BIRD_START_X + CollisionService.BIRD_SIZE / 2 - Nest.WIDTH / 2;

    private final GameState gameState;
    private final ScoreService scoreService = new ScoreService();
    private final LevelService levelService = new LevelService();
    private final CollisionService collisionService = new CollisionService();

    private int tick;   // นับเฟรมในด่านปัจจุบัน (ใช้กำหนดจังหวะเกิดท่อ)
    private boolean waitingForFirstJump = BIRD_ENABLED;   // ต้นด่าน: นกลอยนิ่งจนกว่าจะกด SPACE

    public GameEngine(Player player) {
        this.gameState = new GameState(player);
    }

    public void startGame() {
        gameState.startLevel();
    }

    // ================= Game Loop =================

    /** เรียกทุกเฟรม (~60 ครั้ง/วินาที) */
    public void update(int panelWidth, int panelHeight) {
        if (!gameState.isGameRunning() || waitingForFirstJump) return;

        Player player = gameState.getCurrentPlayer();
        int level = player.getCurrentLevel();
        int speed = LevelConfig.getPipeSpeed(level);
        Bird bird = gameState.getBird();

        if (BIRD_ENABLED) bird.update();
        tick++;

        spawnObjects(panelWidth, panelHeight, level, speed);

        // ท่อ: ชน = แพ้, ผ่าน = +1 ท่อสะสม
        for (Pipe pipe : gameState.getPipes()) {
            pipe.update();
            if (BIRD_ENABLED && collisionService.checkBirdPipe(bird, pipe)) {
                gameOver("ชนท่อ");
                return;
            }
            if (collisionService.checkPassedPipe(bird, pipe)) {   // ไม่มีนกก็ยังนับท่อที่เลื่อนผ่านเส้นนก
                passPipe();
            }
        }

        // หัวใจ: เก็บแล้วอาจผ่านด่าน → ด่านใหม่ล้าง list ทั้งหมด จึงต้องหยุดเฟรมนี้ทันที
        for (Heart heart : gameState.getHearts()) {
            heart.update();
            if (BIRD_ENABLED && collisionService.checkBirdHeart(bird, heart) && collectHeart()) {
                return;
            }
        }

        // รัง (ด่าน 5): เช็กก่อนพื้น เพราะรังวางอยู่บนพื้น
        Nest nest = gameState.getNest();
        if (nest != null) {
            nest.update();
            if (BIRD_ENABLED && collisionService.checkBirdNest(bird, nest)) {
                reachNest();
                return;
            }
        }

        if (BIRD_ENABLED && collisionService.checkBirdGround(bird, panelHeight)) {
            gameOver("ตกพื้น / ชนเพดาน");
            return;
        }

        gameState.getPipes().removeIf(Pipe::isOffScreen);
        gameState.getHearts().removeIf(h -> h.isCollected() || h.isOffScreen());
    }

    /** ด่าน 1-4: ท่อ + หัวใจกลางช่องทุกท่อ / ด่าน 5: รัง */
    private void spawnObjects(int panelWidth, int panelHeight, int level, int speed) {
        if (LevelConfig.isFinalLevel(level)) {
            if (gameState.getNest() == null) {
                gameState.setNest(new Nest(panelWidth, panelHeight, CollisionService.GROUND_HEIGHT,
                        speed, NEST_STOP_X));
            }
            return;
        }

        if (tick % LevelConfig.getPipeInterval(level) == 0) {
            Pipe pipe = Pipe.spawn(panelWidth, panelHeight, CollisionService.GROUND_HEIGHT,
                    speed, LevelConfig.getPipeGap(level));
            gameState.getPipes().add(pipe);
            gameState.getHearts().add(Heart.spawnInGap(pipe, speed));
        }
    }

    public void jump() {
        if (BIRD_ENABLED && gameState.isGameRunning()) {
            waitingForFirstJump = false;
            gameState.getBird().jump();
        }
    }

    // ================= กติกา =================

    /** @return true ถ้าเก็บดวงนี้แล้วผ่านด่าน */
    public boolean collectHeart() {
        Player player = gameState.getCurrentPlayer();
        if (!gameState.isGameRunning() || levelService.isFinalLevel(player)) {
            return false;
        }

        scoreService.collectHeart(player);

        if (levelService.checkLevelComplete(player)) {
            gameState.completeLevel();
            levelService.goToNextLevel(player);   // หัวใจรีเซ็ต, ท่อสะสมคงเดิม
            gameState.resetLevel();
            tick = 0;
            waitingForFirstJump = BIRD_ENABLED;
            return true;
        }
        return false;
    }

    public void passPipe() {
        if (gameState.isGameRunning()) {
            scoreService.passPipe(gameState.getCurrentPlayer());
        }
    }

    public void gameOver(String reason) {
        if (gameState.isGameRunning()) {
            gameState.setGameOver(reason);
        }
    }

    public void reachNest() {
        Player player = gameState.getCurrentPlayer();
        if (gameState.isGameRunning() && levelService.isFinalLevel(player)) {
            gameState.getBird().flyToNest();
            gameState.completeGame();
        }
    }

    // ชื่อเดิมจาก starter (ปุ่มทดสอบ H / P)
    public boolean collectHeartForTest() { return collectHeart(); }
    public void passPipeForTest() { passPipe(); }

    // ================= ข้อมูลให้ GamePanel =================

    public GameState getGameState() {
        return gameState;
    }

    public boolean isWaitingForFirstJump() {
        return waitingForFirstJump;
    }

    public boolean isFinalLevel() {
        return levelService.isFinalLevel(gameState.getCurrentPlayer());
    }

    public String getHeartDisplay() {
        Player player = gameState.getCurrentPlayer();

        if (isFinalLevel()) {
            return "FINAL";
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        return player.getLevelHearts() + "/" + required;
    }
}
