/*
 * จุดกลางสำหรับเพิ่มคะแนน
 * เก็บหัวใจ = เรียก Player.addHeart()
 * ผ่านท่อ = เรียก Player.addPipe()
 * ทำให้ GameEngine ไม่ต้องแก้ค่าของ Player โดยตรงทุกจุด
 */

package birdgame.service;

import birdgame.model.Player;

public class ScoreService {

    // ตัวกลางสำหรับ +1 หัวใจให้ Player หลัง GameEngine ยืนยันว่าเก็บจริง
    public void collectHeart(Player player) {

        player.addHeart();
    }

    // ตัวกลางสำหรับ +1 ท่อสะสมหลัง GameEngine ยืนยันว่าผ่านจริง
    public void passPipe(Player player) {

        player.addPipe();
    }

    public int getLevelHeart(Player player) {

        return player.getLevelHearts();
    }

    public int getTotalPipe(Player player) {

        return player.getTotalPipes();
    }
}
