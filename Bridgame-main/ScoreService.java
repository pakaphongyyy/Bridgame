package birdgame.service;
 
import birdgame.model.Player;
 
/** นับหัวใจ (ต่อด่าน) และท่อ (สะสมใน Player) */
public class ScoreService {
    private int hearts;   // รีเซ็ตทุกครั้งที่เริ่มด่าน
 
    public void addHeart() {
        hearts++;
    }
 
    public void resetHearts() {
        hearts = 0;
    }
 
    public int getHearts() {
        return hearts;
    }
 
    public void addPipe(Player player) {
        player.addPipe();
    }
}
 