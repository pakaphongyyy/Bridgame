package birdgame.model;

import java.util.ArrayList;
import java.util.List;

/** ทุกอย่างที่อยู่บนจอในรอบนี้ + สถานะเกม (GamePanel อ่านไปวาด, GameEngine เป็นคนแก้) */
public class GameState {
    public static final int BIRD_START_X = 120;
    public static final int BIRD_START_Y = 220;

    private final Player currentPlayer;
    private Bird bird;
    private final List<Pipe> pipes = new ArrayList<>();
    private final List<Heart> hearts = new ArrayList<>();
    private Nest nest;

    private boolean gameRunning;
    private boolean levelCompleted;
    private boolean gameCompleted;   // เข้ารังสำเร็จ
    private boolean gameOver;        // ชนท่อ / ตกพื้น
    private String gameOverReason = "";

    public GameState(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        resetLevel();
    }

    /** เริ่มด่านใหม่: นกกลับจุดเริ่ม ล้างท่อ/หัวใจ/รัง */
    public void resetLevel() {
        bird = new Bird(BIRD_START_X, BIRD_START_Y);
        pipes.clear();
        hearts.clear();
        nest = null;
        levelCompleted = false;
        gameCompleted = false;
    }

    public void startLevel() {
        gameRunning = true;
        gameOver = false;
        gameOverReason = "";
    }

    public void completeLevel() {
        levelCompleted = true;
    }

    public void completeGame() {
        gameCompleted = true;
        gameRunning = false;
    }

    public void setGameOver(String reason) {
        gameOver = true;
        gameOverReason = reason;
        gameRunning = false;
        bird.setAlive(false);
    }

    public Player getCurrentPlayer() { return currentPlayer; }
    public Bird getBird() { return bird; }
    public List<Pipe> getPipes() { return pipes; }
    public List<Heart> getHearts() { return hearts; }
    public Nest getNest() { return nest; }
    public boolean isGameRunning() { return gameRunning; }
    public boolean isLevelCompleted() { return levelCompleted; }
    public boolean isGameCompleted() { return gameCompleted; }
    public boolean isGameOver() { return gameOver; }
    public String getGameOverReason() { return gameOverReason; }

    public void setNest(Nest nest) {
        this.nest = nest;
    }
}
