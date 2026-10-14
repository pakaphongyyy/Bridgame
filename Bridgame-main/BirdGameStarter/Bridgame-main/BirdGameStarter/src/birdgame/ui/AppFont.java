/*
 * ============================================================
 * ไฟล์: AppFont.java
 * หน้าที่หลัก: ระบบฟอนต์กลางของ UI
 *
 * ภาพรวม:
 * ค้นหาฟอนต์ที่รองรับภาษาไทยและนำไปใช้กับ Swing ทั้งโปรแกรม
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า FontUIResource เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.plaf.FontUIResource;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.*;
// นำเข้า Arrays เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.Arrays;
// นำเข้า Enumeration เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.Enumeration;
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `AppFont` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public final class AppFont {

    // ประกาศค่าคงที่ `PREFERRED_FONTS` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final List<String> PREFERRED_FONTS = List.of(
            "Tahoma",
            "Leelawadee UI",
            "Noto Sans Thai",
            "Noto Sans",
            "Arial",
            "Dialog"
    );

    // ประกาศค่าคงที่ `FONT_NAME` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final String FONT_NAME = findAvailableFont();

    // Constructor ของ `AppFont` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    private AppFont() {}

    // เลือกฟอนต์ตัวแรกจากรายการที่มีติดตั้งจริงในเครื่อง
    private static String findAvailableFont() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String[] installed` เพื่อใช้ต่อในขั้นตอนถัดไป
        String[] installed = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                // เรียก `getAvailableFontFamilyNames(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                .getAvailableFontFamilyNames();

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (String preferred : PREFERRED_FONTS) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `boolean found` เพื่อใช้ต่อในขั้นตอนถัดไป
            boolean found = Arrays.stream(installed)
                    // เรียก `anyMatch(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    .anyMatch(name -> name.equalsIgnoreCase(preferred));

            // ตรวจเงื่อนไข `found`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (found) {
                // ส่งค่า `preferred` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
                return preferred;
            }
        }

        // ส่งค่า `Font.DIALOG` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return Font.DIALOG;
    }

    // ประกาศเมธอด `plain()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public static Font plain(int size) {
        // ส่งค่า `new Font(FONT_NAME, Font.PLAIN, size)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

    // ประกาศเมธอด `bold()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    public static Font bold(int size) {
        // ส่งค่า `new Font(FONT_NAME, Font.BOLD, size)` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    // แทนฟอนต์เริ่มต้นของ Swing components ด้วยฟอนต์ที่โปรแกรมเลือก
    public static void applyToSwing() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontUIResource font` เพื่อใช้ต่อในขั้นตอนถัดไป
        FontUIResource font = new FontUIResource(plain(16));

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Enumeration<Object> keys` เพื่อใช้ต่อในขั้นตอนถัดไป
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        // วนทำซ้ำตราบใดที่เงื่อนไขใน while ยังเป็นจริง
        while (keys.hasMoreElements()) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Object key` เพื่อใช้ต่อในขั้นตอนถัดไป
            Object key = keys.nextElement();
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Object value` เพื่อใช้ต่อในขั้นตอนถัดไป
            Object value = UIManager.get(key);

            // ตรวจเงื่อนไข `value instanceof FontUIResource`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (value instanceof FontUIResource) {
                // เรียก `UIManager.put(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                UIManager.put(key, font);
            }
        }
    }

    // Getter `getFontName()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public static String getFontName() {
        // ส่งค่า `FONT_NAME` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return FONT_NAME;
    }
}
