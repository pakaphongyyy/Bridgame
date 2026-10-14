/*
 * ============================================================
 * ไฟล์: ScoreService.java
 * หน้าที่หลัก: บริการจัดการคะแนน
 *
 * ภาพรวม:
 * เป็นตัวกลางสำหรับเพิ่มหัวใจ เพิ่มท่อ และอ่านค่าคะแนนจาก Player
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.service` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.service;

// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;

// ประกาศคลาส `ScoreService` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class ScoreService {
    // เพิ่มจำนวนหัวใจให้ Player
    public void collectHeart(Player player) {
        // เรียก `player.addHeart(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        player.addHeart();
    }

    // เพิ่มจำนวนท่อสะสมให้ Player
    public void passPipe(Player player) {
        // เรียก `player.addPipe(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        player.addPipe();
    }

    // Getter `getLevelHeart()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getLevelHeart(Player player) {
        // ส่งค่า `player.getLevelHearts()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return player.getLevelHearts();
    }

    // Getter `getTotalPipe()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getTotalPipe(Player player) {
        // ส่งค่า `player.getTotalPipes()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return player.getTotalPipes();
    }
}
