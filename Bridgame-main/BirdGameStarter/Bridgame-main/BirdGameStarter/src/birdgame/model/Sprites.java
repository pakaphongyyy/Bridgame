/*
 * ============================================================
 * ไฟล์: Sprites.java
 * หน้าที่หลัก: คลังรูปภาพของเกม
 *
 * ภาพรวม:
 * โหลด sprite จากโฟลเดอร์ resources เพียงครั้งเดียว เพื่อให้โมเดลต่าง ๆ นำไปวาดได้
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.model` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.model;

// นำเข้า ImageIO เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.imageio.ImageIO;
// นำเข้า BufferedImage เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.image.BufferedImage;
// นำเข้า URL เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.net.URL;

/** โหลดภาพจาก src/birdgame/assets ครั้งเดียว (โหลดไม่ได้ = null → แต่ละคลาสวาดรูปทรงสำรองแทน) */
final class Sprites {
    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `static final BufferedImage PIPE_BODY` เพื่อใช้ต่อในขั้นตอนถัดไป
    static final BufferedImage PIPE_BODY = load("pipe_body.png");
    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `static final BufferedImage PIPE_CAP` เพื่อใช้ต่อในขั้นตอนถัดไป
    static final BufferedImage PIPE_CAP = load("pipe_cap.png");
    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `static final BufferedImage HEART` เพื่อใช้ต่อในขั้นตอนถัดไป
    static final BufferedImage HEART = load("heart.png");
    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `static final BufferedImage NEST` เพื่อใช้ต่อในขั้นตอนถัดไป
    static final BufferedImage NEST = load("nest.png");
    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `static final BufferedImage BIRD` เพื่อใช้ต่อในขั้นตอนถัดไป
    static final BufferedImage BIRD = load("bird.png");
    

    // Constructor ของ `Sprites` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    private Sprites() {}

    // ประกาศเมธอด `load()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    private static BufferedImage load(String name) {
        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // ค้นหา resource ที่อยู่ใน classpath ของโปรเจกต์ เช่น รูปในโฟลเดอร์ assets
            URL url = Sprites.class.getResource("/birdgame/assets/" + name);
            // ส่งค่า `url == null ? null : ImageIO.read(url)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return url == null ? null : ImageIO.read(url);
        } catch (Exception e) {
            // ส่งค่า `null` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return null;
        }
    }
}
