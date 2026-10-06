package birdgame.service;
 
import birdgame.model.GameState;
import birdgame.model.LevelConfig;
import birdgame.model.Player;
 
/**
 * ศูนย์กลางกติกาเกม — GamePanel แจ้งเหตุการณ์เข้ามา (เก็บหัวใจ / ผ่านท่อ / ชน / เข้ารัง)
 * แล้วถาม state กับค่าความยากของด่านปัจจุบันจากที่นี่
 */
public class GameEngine {
    private final Player player;
    private final LevelService levelService = new LevelService();
    private final ScoreService scoreService = new ScoreService();
 
    private GameState state = GameState.READY;
    private LevelConfig config;
 
    public GameEngine(Player player) {
        this.player = player;
        this.config = levelService.getConfig(player.getCurrentLevel());
    }
 
    /** เริ่ม (หรือเริ่มใหม่) ที่ด่านปัจจุบันของผู้เล่น — หัวใจรีเซ็ต ท่อสะสมไม่รีเซ็ต */
    public void startGame() {
        startLevel();
    }
 
    private void startLevel() {
        config = levelService.getConfig(player.getCurrentLevel());
        scoreService.resetHearts();
        state = config.isFinalLevel() ? GameState.ENDING : GameState.PLAYING;
    }
 
    /** @return true ถ้าเก็บหัวใจครั้งนี้แล้วผ่านด่าน (GamePanel ต้องเคลียร์ท่อ/หัวใจบนจอ) */
    public boolean collectHeart() {
        if (state != GameState.PLAYING) return false;
        scoreService.addHeart();
        if (levelService.isLevelComplete(scoreService.getHearts(), config)) {
            levelService.advance(player);
            startLevel();
            return true;
        }
        return false;
    }
 
    public void passPipe() {
        if (state != GameState.PLAYING) return;
        scoreService.addPipe(player);
    }
 
    public void gameOver() {
        if (state == GameState.PLAYING || state == GameState.ENDING) {
            state = GameState.GAME_OVER;
        }
    }
 
    /** นกเข้ารังในด่าน 5 */
    public boolean reachNest() {
        if (state != GameState.ENDING) return false;
        state = GameState.WON;
        return true;
    }
 
    // ---- ชื่อเดิมที่ GamePanel/ปุ่มทดสอบเรียกอยู่ ----
    public boolean collectHeartForTest() { return collectHeart(); }
    public void passPipeForTest() { passPipe(); }
 
    // ---- getters ----
    public String getHeartDisplay() {
        if (config.isFinalLevel()) return "บินเข้ารัง!";
        return scoreService.getHearts() + "/" + config.getHeartsRequired();
    }
 
    public GameState getState() { return state; }
    public LevelConfig getConfig() { return config; }
    public int getHearts() { return scoreService.getHearts(); }
    public boolean isFinalLevel() { return config.isFinalLevel(); }
    public boolean isRunning() { return state == GameState.PLAYING || state == GameState.ENDING; }
}