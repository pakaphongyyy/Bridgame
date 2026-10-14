/*
 * ============================================================
 * ไฟล์: Player.java
 * หน้าที่หลัก: ข้อมูลผู้เล่น
 *
 * ภาพรวม:
 * เก็บชื่อ ด่าน หัวใจในด่าน ท่อสะสม สถานะจบเกม และเมธอดเปลี่ยนค่าเหล่านี้
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.model` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.model;

// ประกาศคลาส `Player` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class Player {
    // ประกาศ field `playerId` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int playerId;
    // ประกาศ field `name` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final String name;

    // ประกาศ field `currentLevel` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int currentLevel = 1;
    // ประกาศ field `levelHearts` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int levelHearts = 0;
    // ประกาศ field `totalPipes` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int totalPipes = 0;

    // ประกาศ field `finished` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean finished = false;
    // ประกาศ field `finishOrder` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int finishOrder = 0;

    // Constructor ของ `Player` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public Player(int playerId, String name) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.playerId` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.playerId = playerId;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.name` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.name = name;
    }

    // Getter `getPlayerId()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getPlayerId() {
        // ส่งค่า `playerId` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return playerId;
    }

    // Getter `getName()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public String getName() {
        // ส่งค่า `name` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return name;
    }

    // Getter `getCurrentLevel()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getCurrentLevel() {
        // ส่งค่า `currentLevel` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return currentLevel;
    }

    // Getter `getLevelHearts()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getLevelHearts() {
        // ส่งค่า `levelHearts` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return levelHearts;
    }

    // Getter `getTotalPipes()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getTotalPipes() {
        // ส่งค่า `totalPipes` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return totalPipes;
    }

    // เมธอด `isFinished()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isFinished() {
        // ส่งค่า `finished` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return finished;
    }

    // Getter `getFinishOrder()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getFinishOrder() {
        // ส่งค่า `finishOrder` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return finishOrder;
    }

    // ประกาศเมธอด `addHeart()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public void addHeart() {
        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        levelHearts++;
    }

    // ประกาศเมธอด `addPipe()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public void addPipe() {
        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        totalPipes++;
    }

    // ประกาศเมธอด `resetLevelHearts()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public void resetLevelHearts() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `levelHearts` เพื่อใช้ต่อในขั้นตอนถัดไป
        levelHearts = 0;
    }

    // ประกาศเมธอด `nextLevel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public void nextLevel() {
        // ตรวจเงื่อนไข `currentLevel < 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (currentLevel < 5) {
            // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
            currentLevel++;
            // เรียก `resetLevelHearts(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            resetLevelHearts();
        }
    }

    // ประกาศเมธอด `finishGame()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public void finishGame(int order) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `finished` เพื่อใช้ต่อในขั้นตอนถัดไป
        finished = true;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `finishOrder` เพื่อใช้ต่อในขั้นตอนถัดไป
        finishOrder = order;
    }
}
