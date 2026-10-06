package birdgame.model;
 
/** ข้อมูลผู้เล่นที่ต้องอยู่ข้ามรอบ/ข้ามด่าน */
public class Player {
    public static final int FIRST_LEVEL = 1;
 
    private final String name;
    private int currentLevel = FIRST_LEVEL;
    private int totalPipes;            // ท่อสะสม ไม่รีเซ็ตเมื่อเปลี่ยนด่านหรือแพ้
 
    public Player(String name) {
        this.name = name;
    }
 
    public void addPipe() {
        totalPipes++;
    }
 
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }
 
    public String getName() { return name; }
    public int getCurrentLevel() { return currentLevel; }
    public int getTotalPipes() { return totalPipes; }
}
 