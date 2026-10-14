package birdgame.service;

import birdgame.model.Bird;
import birdgame.model.Heart;
import birdgame.model.Nest;
import birdgame.model.Pipe;

import java.awt.Rectangle;

public class CollisionService {
    // ต้องตรงกับที่ GamePanel วาด (พื้น h-70, นก 40x40)
    public static final int GROUND_HEIGHT = 70;
    public static final int BIRD_SIZE = 40;
    // หด hitbox เข้ามาเล็กน้อย ไม่ให้ชนตอนแค่ขอบรูปเฉียด
    private static final int PADDING = 4;

    public Rectangle getBirdBounds(Bird bird) {
        return new Rectangle(
                bird.getX() + PADDING,
                bird.getY() + PADDING,
                BIRD_SIZE - 2 * PADDING,
                BIRD_SIZE - 2 * PADDING);
    }

    /** ชนเพดาน หรือ ขอบล่างของนกแตะพื้น */
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

    /** นกบินผ่านท่อแล้วหรือยัง (นับคะแนนครั้งเดียวต่อท่อ) */
    public boolean checkPassedPipe(Bird bird, Pipe pipe) {
        if (!pipe.isPassed() && bird.getX() > pipe.getX() + Pipe.WIDTH) {
            pipe.setPassed(true);
            return true;
        }
        return false;
    }
}
