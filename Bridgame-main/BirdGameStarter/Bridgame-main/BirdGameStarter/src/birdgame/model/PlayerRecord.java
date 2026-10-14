/*
 * ============================================================
 * ไฟล์: PlayerRecord.java
 * หน้าที่หลัก: ข้อมูลประวัติการเล่น
 *
 * ภาพรวม:
 * แปลงสถานะ Player หลังเล่นจบให้เป็นข้อมูลที่พร้อมบันทึกลง CSV และนำไปแสดงในตาราง
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.model` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.model;

// ประกาศคลาส `PlayerRecord` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class PlayerRecord {
    // ประกาศ field `playerName` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final String playerName;
    // ประกาศ field `finished` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final boolean finished;
    // ประกาศ field `highestLevel` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int highestLevel;
    // ประกาศ field `totalPipes` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int totalPipes;
    // ประกาศ field `playedAt` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final String playedAt;

    public PlayerRecord(String playerName,
                        boolean finished,
                        int highestLevel,
                        int totalPipes,
                        String playedAt) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.playerName` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.playerName = playerName;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.finished` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.finished = finished;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.highestLevel` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.highestLevel = highestLevel;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.totalPipes` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.totalPipes = totalPipes;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.playedAt` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.playedAt = playedAt;
    }

    // Constructor ของ `PlayerRecord` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public PlayerRecord(Player player, String playedAt) {
        this(
            player.getName(),
            player.isFinished(),
            player.getCurrentLevel(),
            player.getTotalPipes(),
            playedAt
        );
    }

    // Getter `getPlayerName()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public String getPlayerName() {
        // ส่งค่า `playerName` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return playerName;
    }

    // เมธอด `isFinished()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isFinished() {
        // ส่งค่า `finished` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return finished;
    }

    // Getter `getHighestLevel()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getHighestLevel() {
        // ส่งค่า `highestLevel` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return highestLevel;
    }

    // Getter `getTotalPipes()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getTotalPipes() {
        // ส่งค่า `totalPipes` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return totalPipes;
    }

    // Getter `getPlayedAt()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public String getPlayedAt() {
        // ส่งค่า `playedAt` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return playedAt;
    }
}
