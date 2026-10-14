/*
 * ============================================================
 * ไฟล์: LevelService.java
 * หน้าที่หลัก: บริการจัดการด่าน
 *
 * ภาพรวม:
 * ตรวจว่าเก็บหัวใจครบหรือยัง เปลี่ยนด่าน รีเซตหัวใจ และตรวจด่านสุดท้าย
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.service` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.service;

// นำเข้า LevelConfig เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.LevelConfig;
// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;

// ประกาศคลาส `LevelService` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class LevelService {

    // ตรวจว่าหัวใจในด่านถึงจำนวนที่ LevelConfig กำหนดหรือยัง
    public boolean checkLevelComplete(Player player) {
        // ตรวจเงื่อนไข `player.getCurrentLevel() >= 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (player.getCurrentLevel() >= 5) {
            // ส่งค่า `false` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return false;
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int required` เพื่อใช้ต่อในขั้นตอนถัดไป
        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        // ส่งค่า `player.getLevelHearts() >= required` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return player.getLevelHearts() >= required;
    }

    // สั่ง Player ไปด่านถัดไป
    public void goToNextLevel(Player player) {
        // เรียก `player.nextLevel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        player.nextLevel();
    }

    // รีเซตจำนวนหัวใจของด่านปัจจุบัน
    public void resetLevelHeart(Player player) {
        // เรียก `player.resetLevelHearts(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        player.resetLevelHearts();
    }

    // ตรวจว่าขณะนี้อยู่ด่าน 5 หรือไม่
    public boolean isFinalLevel(Player player) {
        // ส่งค่า `player.getCurrentLevel() == 5` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return player.getCurrentLevel() == 5;
    }
}
