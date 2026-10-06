package birdgame.model;
 
/** ค่าความยากของแต่ละด่าน (อ่านอย่างเดียว) */
public class LevelConfig {
    private final int level;
    private final int heartsRequired;   // 0 = ด่านจบ (บินเข้ารัง)
    private final int pipeSpeed;        // px ต่อเฟรม
    private final int pipeGap;          // ความสูงช่องว่างระหว่างท่อ
    private final int pipeInterval;     // เฟรมระหว่างท่อแต่ละอัน (60 เฟรม ≈ 1 วินาที)
 
    public LevelConfig(int level, int heartsRequired, int pipeSpeed, int pipeGap, int pipeInterval) {
        this.level = level;
        this.heartsRequired = heartsRequired;
        this.pipeSpeed = pipeSpeed;
        this.pipeGap = pipeGap;
        this.pipeInterval = pipeInterval;
    }
 
    public boolean isFinalLevel() {
        return heartsRequired == 0;
    }
 
    public int getLevel() { return level; }
    public int getHeartsRequired() { return heartsRequired; }
    public int getPipeSpeed() { return pipeSpeed; }
    public int getPipeGap() { return pipeGap; }
    public int getPipeInterval() { return pipeInterval; }
}
 
