/*
 * ============================================================
 * ไฟล์: CollisionService.java
 * หน้าที่หลัก: ระบบตรวจการชน
 *
 * ภาพรวม:
 * สร้าง hitbox ของ Bird/Pipe/Heart/Nest และตรวจชนพื้น ผ่านท่อ เก็บหัวใจ และถึงรัง
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.service` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.service;

// นำเข้า Bird เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Bird;
// นำเข้า Heart เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Heart;
// นำเข้า Nest เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Nest;
// นำเข้า Pipe เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Pipe;

// นำเข้า Rectangle เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.Rectangle;

// ประกาศคลาส `CollisionService` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class CollisionService {
    // ต้องตรงกับที่ GamePanel วาด (พื้น h-70, นก 40x40)
    // ประกาศค่าคงที่ `GROUND_HEIGHT` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int GROUND_HEIGHT = 70;
    // ประกาศค่าคงที่ `BIRD_SIZE` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    public static final int BIRD_SIZE = 40;
    // หด hitbox เข้ามาเล็กน้อย ไม่ให้ชนตอนแค่ขอบรูปเฉียด
    // ประกาศค่าคงที่ `PADDING` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final int PADDING = 4;

    // Getter `getBirdBounds()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Rectangle getBirdBounds(Bird bird) {
        // ส่งค่า `new Rectangle(` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Rectangle(
                bird.getX() + PADDING,
                bird.getY() + PADDING,
                BIRD_SIZE - 2 * PADDING,
                // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                BIRD_SIZE - 2 * PADDING);
    }

    /** ชนเพดาน หรือ ขอบล่างของนกแตะพื้น */
    // ตรวจว่านกชนพื้นหรือบินพ้นขอบบนหรือไม่
    public boolean checkBirdGround(Bird bird, int panelHeight) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Rectangle b` เพื่อใช้ต่อในขั้นตอนถัดไป
        Rectangle b = getBirdBounds(bird);
        // ส่งค่า `b.y < 0 || b.y + b.height >= panelHeight - GROUND_HEIGHT` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return b.y < 0 || b.y + b.height >= panelHeight - GROUND_HEIGHT;
    }

    // ตรวจ hitbox ของนกกับท่อบนและท่อล่าง
    public boolean checkBirdPipe(Bird bird, Pipe pipe) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Rectangle b` เพื่อใช้ต่อในขั้นตอนถัดไป
        Rectangle b = getBirdBounds(bird);
        // ส่งค่า `b.intersects(pipe.getTopBounds()) || b.intersects(pipe.getBottomBounds())` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return b.intersects(pipe.getTopBounds()) || b.intersects(pipe.getBottomBounds());
    }

    /** true = เก็บได้ในเฟรมนี้ (เก็บซ้ำไม่ได้) */
    // ตรวจว่านกชนหัวใจและทำเครื่องหมาย collected เมื่อเก็บได้
    public boolean checkBirdHeart(Bird bird, Heart heart) {
        // ตรวจเงื่อนไข `heart.isCollected()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (heart.isCollected()) return false;
        // ตรวจเงื่อนไข `getBirdBounds(bird).intersects(heart.getBounds())`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (getBirdBounds(bird).intersects(heart.getBounds())) {
            // เรียก `heart.collect(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            heart.collect();
            // ส่งค่า `true` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return true;
        }
        // ส่งค่า `false` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return false;
    }

    // ตรวจว่านกแตะรังในด่านสุดท้ายหรือไม่
    public boolean checkBirdNest(Bird bird, Nest nest) {
        // ส่งค่า `getBirdBounds(bird).intersects(nest.getBounds())` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return getBirdBounds(bird).intersects(nest.getBounds());
    }

    /** นกบินผ่านท่อแล้วหรือยัง (นับคะแนนครั้งเดียวต่อท่อ) */
    // ตรวจว่านกบินผ่านท่อแล้วและกันการนับซ้ำด้วยสถานะ passed
    public boolean checkPassedPipe(Bird bird, Pipe pipe) {
        // ตรวจเงื่อนไข `!pipe.isPassed() && bird.getX() > pipe.getX() + Pipe.WIDTH`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (!pipe.isPassed() && bird.getX() > pipe.getX() + Pipe.WIDTH) {
            // เรียก `pipe.setPassed(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            pipe.setPassed(true);
            // ส่งค่า `true` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return true;
        }
        // ส่งค่า `false` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return false;
    }
}
