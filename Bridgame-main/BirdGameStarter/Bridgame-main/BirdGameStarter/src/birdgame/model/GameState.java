/*
 * ============================================================
 * ไฟล์: GameState.java
 * หน้าที่หลัก: ศูนย์รวมสถานะเกมปัจจุบัน
 *
 * ภาพรวม:
 * เก็บ Player, Bird, รายการ Pipe/Heart, Nest และสถานะ running/completed เพื่อให้ GameEngine ใช้งาน
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.model` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.model;

// นำเข้า ArrayList เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.ArrayList;
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `GameState` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class GameState {
    // ประกาศ field `currentPlayer` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private Player currentPlayer;
    // ประกาศ field `bird` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private Bird bird;
    // ประกาศ field `pipes` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final List<Pipe> pipes = new ArrayList<>();
    // ประกาศ field `hearts` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final List<Heart> hearts = new ArrayList<>();
    // ประกาศ field `nest` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private Nest nest;

    // ประกาศ field `gameRunning` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean gameRunning;
    // ประกาศ field `levelCompleted` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean levelCompleted;
    // ประกาศ field `gameCompleted` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean gameCompleted;

    // Constructor ของ `GameState` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public GameState(Player currentPlayer) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.currentPlayer` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.currentPlayer = currentPlayer;
        // เรียก `resetLevel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        resetLevel();
    }

    // ล้างวัตถุของด่านเดิมและสร้างสถานะเริ่มต้นของด่านใหม่
    public void resetLevel() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `bird` เพื่อใช้ต่อในขั้นตอนถัดไป
        bird = new Bird(140, 260);
        // ล้างข้อมูลทั้งหมดใน collection เพื่อเริ่มสถานะใหม่
        pipes.clear();
        // ล้างข้อมูลทั้งหมดใน collection เพื่อเริ่มสถานะใหม่
        hearts.clear();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `nest` เพื่อใช้ต่อในขั้นตอนถัดไป
        nest = null;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `levelCompleted` เพื่อใช้ต่อในขั้นตอนถัดไป
        levelCompleted = false;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameCompleted` เพื่อใช้ต่อในขั้นตอนถัดไป
        gameCompleted = false;
    }

    // ตั้งสถานะว่าด่านกำลังทำงาน
    public void startLevel() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameRunning` เพื่อใช้ต่อในขั้นตอนถัดไป
        gameRunning = true;
    }

    // ทำเครื่องหมายว่าด่านปัจจุบันเสร็จแล้ว
    public void completeLevel() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `levelCompleted` เพื่อใช้ต่อในขั้นตอนถัดไป
        levelCompleted = true;
    }

    // ทำเครื่องหมายว่าเกมจบและหยุดสถานะ running
    public void completeGame() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameCompleted` เพื่อใช้ต่อในขั้นตอนถัดไป
        gameCompleted = true;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameRunning` เพื่อใช้ต่อในขั้นตอนถัดไป
        gameRunning = false;
    }

    // Getter `getCurrentPlayer()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Player getCurrentPlayer() { return currentPlayer; }
    // Getter `getBird()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Bird getBird() { return bird; }
    // Getter `getPipes()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public List<Pipe> getPipes() { return pipes; }
    // Getter `getHearts()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public List<Heart> getHearts() { return hearts; }
    // Getter `getNest()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Nest getNest() { return nest; }
    // เมธอด `isGameRunning()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isGameRunning() { return gameRunning; }
    // เมธอด `isLevelCompleted()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isLevelCompleted() { return levelCompleted; }
    // เมธอด `isGameCompleted()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isGameCompleted() { return gameCompleted; }

    // Setter `setNest()` ใช้เปลี่ยนค่าภายในอ็อบเจ็กต์อย่างเป็นทางการ
    public void setNest(Nest nest) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.nest` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.nest = nest;
    }
}
