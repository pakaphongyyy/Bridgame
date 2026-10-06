package birdgame.service;
 
import birdgame.model.LevelConfig;
import birdgame.model.Player;
 
/** กติกาด่าน: ด่านไหนต้องใช้กี่หัวใจ ยากแค่ไหน และผ่านด่านหรือยัง */
public class LevelService {
    public static final int FINAL_LEVEL = 5;
 
    //                                level hearts speed gap interval
    private static final LevelConfig[] LEVELS = {
            new LevelConfig(1, 10, 3, 190, 100),
            new LevelConfig(2, 15, 3, 180,  95),
            new LevelConfig(3, 20, 4, 170,  85),
            new LevelConfig(4, 25, 4, 160,  80),
            new LevelConfig(5,  0, 3,   0,   0)   // ด่าน 5 = บินเข้ารัง ไม่มีท่อ
    };
 
    public LevelConfig getConfig(int level) {
        int index = Math.max(1, Math.min(level, FINAL_LEVEL)) - 1;
        return LEVELS[index];
    }
 
    public boolean isLevelComplete(int hearts, LevelConfig config) {
        return !config.isFinalLevel() && hearts >= config.getHeartsRequired();
    }
 
    /** เลื่อนผู้เล่นไปด่านถัดไป คืนค่าด่านใหม่ */
    public int advance(Player player) {
        int next = Math.min(player.getCurrentLevel() + 1, FINAL_LEVEL);
        player.setCurrentLevel(next);
        return next;
    }
}
