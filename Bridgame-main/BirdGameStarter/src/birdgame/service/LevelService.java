/*
 * รวมคำสั่งเกี่ยวกับการผ่านด่าน
 * ไม่เก็บคะแนนเอง แต่ใช้ข้อมูลจาก Player และ LevelConfig มาตัดสินว่าเก็บหัวใจครบหรือยัง
 */

package birdgame.service;

import birdgame.model.LevelConfig;

import birdgame.model.Player;

public class LevelService {

    // ดูจำนวนหัวใจของ Player เทียบกับค่าที่ LevelConfig กำหนด ด่าน 5 จะไม่ใช้เงื่อนไขนี้
    public boolean checkLevelComplete(Player player) {

        if (player.getCurrentLevel() >= 5) {

            return false;
        }

        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());

        return player.getLevelHearts() >= required;
    }

    // ให้ Player จัดการขึ้นด่านเอง เพื่อให้กติการีเซตหัวใจอยู่ที่ Player จุดเดียว
    public void goToNextLevel(Player player) {

        player.nextLevel();
    }

    public void resetLevelHeart(Player player) {

        player.resetLevelHearts();
    }

    // ใช้เช็กแบบอ่านง่ายว่าตอนนี้เป็นด่าน 5 หรือไม่
    public boolean isFinalLevel(Player player) {

        return player.getCurrentLevel() == 5;
    }
}
