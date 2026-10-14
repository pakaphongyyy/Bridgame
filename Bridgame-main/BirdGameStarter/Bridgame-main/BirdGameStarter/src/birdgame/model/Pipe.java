/*
 * ============================================================
 * ไฟล์: Pipe.java
 * หน้าที่หลัก: โมเดลท่อ
 *
 * ภาพรวม:
 * สร้างท่อแบบสุ่มช่องว่าง เคลื่อนท่อ ให้ hitbox ด้านบน/ล่าง ตรวจว่าผ่านแล้ว และวาดกราฟิกท่อ
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.model` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.model;

// นำเข้า Color เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.Color;
// นำเข้า Graphics เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.Graphics;
// นำเข้า Rectangle เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.Rectangle;
// นำเข้า Random เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.Random;

// ประกาศคลาส `Pipe` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class Pipe {
    // ประกาศค่าคงที่ `WIDTH` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int WIDTH = 80;
    // ประกาศค่าคงที่ `GAP` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int GAP = 180;        // ค่า default ของช่องว่าง (ด่านกำหนดเองได้)
    // ประกาศค่าคงที่ `MIN_MARGIN` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int MIN_MARGIN = 60;  // ระยะขั้นต่ำของช่องว่างจากขอบบน/พื้น
    // ประกาศค่าคงที่ `RANDOM` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Random RANDOM = new Random();

    // ประกาศ field `x` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int x;
    // ประกาศ field `gapY` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int gapY;         // ขอบบนของช่องว่าง
    // ประกาศ field `gap` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int gap;          // ความสูงช่องว่าง (มาจาก LevelConfig)
    // ประกาศ field `panelHeight` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int panelHeight;
    // ประกาศ field `speed` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final int speed;
    private boolean passed;         // นกบินผ่านแล้วหรือยัง (ใช้นับคะแนน)

    // Constructor ของ `Pipe` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public Pipe(int x, int gapY, int gap, int panelHeight, int speed) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.x` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.x = x;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.gapY` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.gapY = gapY;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.gap` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.gap = gap;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.panelHeight` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.panelHeight = panelHeight;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.speed` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.speed = speed;
    }

    /** สร้างท่อที่ขอบขวาของจอ พร้อมสุ่มตำแหน่งช่องว่าง */
    // ประกาศเมธอด `spawn()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed, int gap) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int minGapY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int minGapY = MIN_MARGIN;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int maxGapY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int maxGapY = panelHeight - groundHeight - gap - MIN_MARGIN;
        // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
        int range = Math.max(1, maxGapY - minGapY + 1);
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int gapY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int gapY = minGapY + RANDOM.nextInt(range);
        // ส่งค่า `new Pipe(panelWidth, gapY, gap, panelHeight, speed)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Pipe(panelWidth, gapY, gap, panelHeight, speed);
    }

    // ประกาศเมธอด `spawn()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public static Pipe spawn(int panelWidth, int panelHeight, int groundHeight, int speed) {
        // ส่งค่า `spawn(panelWidth, panelHeight, groundHeight, speed, GAP)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return spawn(panelWidth, panelHeight, groundHeight, speed, GAP);
    }

    /** เลื่อนจากขวาไปซ้าย */
    // อัปเดตสถานะของอ็อบเจ็กต์หนึ่งเฟรม เช่น ตำแหน่ง/ความเร็ว/Animation
    public void update() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `x -` เพื่อใช้ต่อในขั้นตอนถัดไป
        x -= speed;
    }

    // ตรวจว่าวัตถุเลื่อนออกนอกขอบซ้ายของหน้าจอแล้วหรือยัง
    public boolean isOffScreen() {
        // ส่งค่า `x + WIDTH < 0` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return x + WIDTH < 0;
    }

    // Getter `getTopBounds()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Rectangle getTopBounds() {
        // ส่งค่า `new Rectangle(x, 0, WIDTH, gapY)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Rectangle(x, 0, WIDTH, gapY);
    }

    // Getter `getBottomBounds()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Rectangle getBottomBounds() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int bottomY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int bottomY = gapY + gap;
        // ส่งค่า `new Rectangle(x, bottomY, WIDTH, panelHeight - bottomY)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Rectangle(x, bottomY, WIDTH, panelHeight - bottomY);
    }

    // ประกาศค่าคงที่ `CAP_WIDTH` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int CAP_WIDTH = 96;   // ปากท่อกว้างกว่าตัวท่อ (แค่ภาพ ไม่กระทบ hitbox)
    // ประกาศค่าคงที่ `CAP_HEIGHT` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int CAP_HEIGHT = 36;

    // วาดอ็อบเจ็กต์ลงบน Graphics ของ Swing
    public void draw(Graphics g) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Rectangle top` เพื่อใช้ต่อในขั้นตอนถัดไป
        Rectangle top = getTopBounds();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Rectangle bottom` เพื่อใช้ต่อในขั้นตอนถัดไป
        Rectangle bottom = getBottomBounds();

        // ตรวจเงื่อนไข `Sprites.PIPE_BODY == null || Sprites.PIPE_CAP == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (Sprites.PIPE_BODY == null || Sprites.PIPE_CAP == null) {   // ไม่มีภาพ → สี่เหลี่ยมเขียว
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g.setColor(new Color(60, 170, 60));
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g.fillRect(top.x, top.y, top.width, top.height);
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g.fillRect(bottom.x, bottom.y, bottom.width, bottom.height);
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int capX` เพื่อใช้ต่อในขั้นตอนถัดไป
        int capX = x + WIDTH / 2 - CAP_WIDTH / 2;
        // ตัวท่อ: ยืดภาพแถบแนวตั้งให้เต็มความสูง
        // วาดรูปภาพลงบนหน้าจอตามตำแหน่งและขนาดที่กำหนด
        g.drawImage(Sprites.PIPE_BODY, top.x, top.y, WIDTH, top.height, null);
        // วาดรูปภาพลงบนหน้าจอตามตำแหน่งและขนาดที่กำหนด
        g.drawImage(Sprites.PIPE_BODY, bottom.x, bottom.y, WIDTH, bottom.height, null);
        // ปากท่อ: ติดขอบช่องว่าง
        // วาดรูปภาพลงบนหน้าจอตามตำแหน่งและขนาดที่กำหนด
        g.drawImage(Sprites.PIPE_CAP, capX, top.y + top.height - CAP_HEIGHT, CAP_WIDTH, CAP_HEIGHT, null);
        // วาดรูปภาพลงบนหน้าจอตามตำแหน่งและขนาดที่กำหนด
        g.drawImage(Sprites.PIPE_CAP, capX, bottom.y, CAP_WIDTH, CAP_HEIGHT, null);
    }

    // Getter `getX()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getX() { return x; }
    // Getter `getGapY()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getGapY() { return gapY; }
    // Getter `getGap()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public int getGap() { return gap; }
    // เมธอด `isPassed()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isPassed() { return passed; }
    // Setter `setPassed()` ใช้เปลี่ยนค่าภายในอ็อบเจ็กต์อย่างเป็นทางการ
    public void setPassed(boolean passed) { this.passed = passed; }
}
