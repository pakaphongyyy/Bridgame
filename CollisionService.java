package birdgame.service;

import birdgame.model.Bird;
import birdgame.model.Heart;
import birdgame.model.Nest;
import birdgame.model.Pipe;

import java.awt.Rectangle;
 
public class CollisionService {
    public static final int GROUND_HEIGHT = 50;
 
    // ปรับขนาดตามนก (หรือใช้ bird.getWidth()/getHeight()) แทน
    private static final int BIRD_SIZE = 40;
    // หด hitbox เล็กน้อย ไม่ให้ชนตอนแค่ขอบรูปเฉียด
    private static final int PADDING = 4;
 
    public Rectangle getBirdBounds(Bird bird) {
        return new Rectangle(
                bird.getX() + PADDING,
                bird.getY() + PADDING,
                BIRD_SIZE - 2 * PADDING,
                BIRD_SIZE - 2 * PADDING);
    }
 
    /** ชนเพดาน ขอบล่างของนกแตะพื้น */
    public boolean checkBirdGround(Bird bird, int panelHeight) {
        Rectangle b = getBirdBounds(bird);
        return b.y < 0 || b.y + b.height >= panelHeight - GROUND_HEIGHT;
    }
 
    public boolean checkBirdPipe(Bird bird, Pipe pipe) {
        Rectangle b = getBirdBounds(bird);
        return b.intersects(pipe.getTopBounds()) || b.intersects(pipe.getBottomBounds());
    }
 
    /** true = เก็บได้ในเฟรมนี้ (เก็บซ้ำไม่ได้) */
    public boolean checkBirdHeart(Bird bird, Heart heart) {
        if (heart.isCollected()) return false;
        if (getBirdBounds(bird).intersects(heart.getBounds())) {
            heart.collect();
            return true;
        }
        return false;
    }
 
    public boolean checkBirdNest(Bird bird, Nest nest) {
        return getBirdBounds(bird).intersects(nest.getBounds());
    }
 
    /** เช็คว่านกบินผ่านท่อแล้วหรือยัง ถ้ายังนับคะแนนครั้งเดียวต่อหนึ่งท่อ */
    public boolean checkPassedPipe(Bird bird, Pipe pipe) {
        if (!pipe.isPassed() && bird.getX() > pipe.getX() + Pipe.WIDTH) {
            pipe.setPassed(true);
            return true;
        }
        return false;
    }
}
