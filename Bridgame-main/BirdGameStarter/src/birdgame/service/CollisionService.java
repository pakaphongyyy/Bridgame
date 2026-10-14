/*
 * รวมการตรวจชนทั้งหมดไว้ที่เดียว
 * ไฟล์นี้ไม่เพิ่มคะแนนเอง แต่ตอบกลับว่า “ชนหรือยัง / ผ่านหรือยัง”
 * GameEngine จะเป็นคนตัดสินใจต่อว่าจะเพิ่มคะแนน แพ้ หรือชนะ
 */

package birdgame.service;

import birdgame.model.Bird;

import birdgame.model.Heart;

import birdgame.model.Nest;

import birdgame.model.Pipe;

import java.awt.Rectangle;

public class CollisionService {

    public static final int GROUND_HEIGHT = 70;

    public static final int BIRD_SIZE = 40;

    // ลด hitbox ของนกเข้ามาจากขอบรูปเล็กน้อย ทำให้ชนตรงกับความรู้สึกของผู้เล่นมากขึ้น
    private static final int PADDING = 4;

    // hitbox ของนกตั้งใจให้เล็กกว่ารูปจริงเล็กน้อยด้วย PADDING เพื่อให้การชนไม่รู้สึกโหดเกินไป
    public Rectangle getBirdBounds(Bird bird) {

        return new Rectangle(
                bird.getX() + PADDING,
                bird.getY() + PADDING,
                BIRD_SIZE - 2 * PADDING,

                BIRD_SIZE - 2 * PADDING);
    }

    // ตรวจทั้งขอบบนและพื้นล่าง ถ้านกออกพื้นที่เล่นจะถือว่าชน
    public boolean checkBirdGround(Bird bird, int panelHeight) {

        Rectangle b = getBirdBounds(bird);

        return b.y < 0 || b.y + b.height >= panelHeight - GROUND_HEIGHT;
    }

    // ตรวจนกกับท่อบนและท่อล่าง ถ้าชนอย่างใดอย่างหนึ่งจะคืน true
    public boolean checkBirdPipe(Bird bird, Pipe pipe) {

        Rectangle b = getBirdBounds(bird);

        return b.intersects(pipe.getTopBounds()) || b.intersects(pipe.getBottomBounds());
    }

    // หัวใจดวงเดิมต้องนับได้ครั้งเดียว: ถ้าเก็บแล้วจะคืน false ทันที ถ้าเพิ่งชนจะ mark collected ก่อนคืน true
    public boolean checkBirdHeart(Bird bird, Heart heart) {

        if (heart.isCollected()) return false;

        if (getBirdBounds(bird).intersects(heart.getBounds())) {

            heart.collect();

            return true;
        }

        return false;
    }

    // ด่าน 5 ใช้เมธอดนี้ดูว่านกแตะ hitbox ของรังแล้วหรือยัง
    public boolean checkBirdNest(Bird bird, Nest nest) {

        return getBirdBounds(bird).intersects(nest.getBounds());
    }

    // นับว่าผ่านท่อเมื่อแกน x ของนกเลยขอบขวาของท่อ และ setPassed(true) ทันทีเพื่อกันนับซ้ำ
    public boolean checkPassedPipe(Bird bird, Pipe pipe) {

        if (!pipe.isPassed() && bird.getX() > pipe.getX() + Pipe.WIDTH) {

            pipe.setPassed(true);

            return true;
        }

        return false;
    }
}
