/*
 * ============================================================
 * ไฟล์: GameManager.java
 * หน้าที่หลัก: ตัวจัดการลำดับผู้เล่น
 *
 * ภาพรวม:
 * เก็บรายชื่อผู้เล่นและ index ของผู้เล่นปัจจุบัน เพื่อขยับไปผู้เล่นคนถัดไป
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

// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `GameManager` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class GameManager {
    // ประกาศ field `players` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final List<Player> players;
    // ประกาศ field `currentPlayerIndex` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int currentPlayerIndex = 0;

    // Constructor ของ `GameManager` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public GameManager(List<Player> players) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.players` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.players = players;
    }

    // Getter `getCurrentPlayer()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Player getCurrentPlayer() {
        // ส่งค่า `players.get(currentPlayerIndex)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return players.get(currentPlayerIndex);
    }

    // ประกาศเมธอด `hasNextPlayer()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public boolean hasNextPlayer() {
        // ส่งค่า `currentPlayerIndex + 1 < players.size()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return currentPlayerIndex + 1 < players.size();
    }

    // ประกาศเมธอด `startNextPlayer()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public Player startNextPlayer() {
        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        currentPlayerIndex++;
        // ส่งค่า `getCurrentPlayer()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return getCurrentPlayer();
    }

    // Getter `getPlayers()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public List<Player> getPlayers() {
        // ส่งค่า `players` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return players;
    }

    // Getter `getCurrentPlayerIndex()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getCurrentPlayerIndex() {
        // ส่งค่า `currentPlayerIndex` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return currentPlayerIndex;
    }
}
