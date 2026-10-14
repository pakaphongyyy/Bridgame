/*
 * ============================================================
 * ไฟล์: FileStorageService.java
 * หน้าที่หลัก: ระบบบันทึก/อ่านข้อมูล
 *
 * ภาพรวม:
 * จัดการไฟล์ player_history.csv แปลง PlayerRecord เป็น CSV และโหลดกลับมาเป็นอ็อบเจ็กต์
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.service` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.service;

// นำเข้า PlayerRecord เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.PlayerRecord;

// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.io.*;
// นำเข้า StandardCharsets เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.nio.charset.StandardCharsets;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.nio.file.*;
// นำเข้า ArrayList เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.ArrayList;
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `FileStorageService` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class FileStorageService {

    // ประกาศค่าคงที่ `DATA_DIR` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Path DATA_DIR = Paths.get("data");
    // ประกาศค่าคงที่ `HISTORY_FILE` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Path HISTORY_FILE = DATA_DIR.resolve("player_history.csv");

    // Constructor ของ `FileStorageService` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public FileStorageService() {
        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // เกี่ยวข้องกับการอ่าน/เขียนไฟล์บนดิสก์สำหรับเก็บข้อมูลถาวร
            Files.createDirectories(DATA_DIR);

            // ตรวจเงื่อนไข `!Files.exists(HISTORY_FILE)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (!Files.exists(HISTORY_FILE)) {
                // เกี่ยวข้องกับการอ่าน/เขียนไฟล์บนดิสก์สำหรับเก็บข้อมูลถาวร
                Files.writeString(
                    HISTORY_FILE,
                    "playerName,finished,highestLevel,totalPipes,playedAt\n",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE
                );
            }
        } catch (IOException e) {
            // เรียก `RuntimeException(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            throw new RuntimeException("Cannot initialize player history storage.", e);
        }
    }

    // บันทึก PlayerRecord หนึ่งรายการต่อท้ายไฟล์ CSV
    public void saveRecord(PlayerRecord record) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String line` เพื่อใช้ต่อในขั้นตอนถัดไป
        String line = csv(record.getPlayerName()) + ","
                + record.isFinished() + ","
                + record.getHighestLevel() + ","
                + record.getTotalPipes() + ","
                + csv(record.getPlayedAt())
                // เรียก `System.lineSeparator(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                + System.lineSeparator();

        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // เกี่ยวข้องกับการอ่าน/เขียนไฟล์บนดิสก์สำหรับเก็บข้อมูลถาวร
            Files.writeString(
                HISTORY_FILE,
                line,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            // เรียก `RuntimeException(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            throw new RuntimeException("Cannot save player history.", e);
        }
    }

    // อ่านไฟล์ CSV แล้วสร้างรายการ PlayerRecord กลับมา
    public List<PlayerRecord> loadRecords() {
        // สร้าง ArrayList ว่างสำหรับเก็บอ็อบเจ็กต์หลายตัวแบบลำดับ
        List<PlayerRecord> records = new ArrayList<>();

        // ตรวจเงื่อนไข `!Files.exists(HISTORY_FILE)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (!Files.exists(HISTORY_FILE)) {
            // ส่งค่า `records` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return records;
        }

        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // เกี่ยวข้องกับการอ่าน/เขียนไฟล์บนดิสก์สำหรับเก็บข้อมูลถาวร
            List<String> lines = Files.readAllLines(HISTORY_FILE, StandardCharsets.UTF_8);

            // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
            for (int i = 1; i < lines.size(); i++) {
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String line` เพื่อใช้ต่อในขั้นตอนถัดไป
                String line = lines.get(i).trim();

                // ตรวจเงื่อนไข `line.isBlank()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (line.isBlank()) {
                    // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                    continue;
                }

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `List<String> cols` เพื่อใช้ต่อในขั้นตอนถัดไป
                List<String> cols = parseCsvLine(line);

                // ตรวจเงื่อนไข `cols.size() < 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (cols.size() < 5) {
                    // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                    continue;
                }

                records.add(new PlayerRecord(
                    cols.get(0),
                    Boolean.parseBoolean(cols.get(1)),
                    Integer.parseInt(cols.get(2)),
                    Integer.parseInt(cols.get(3)),
                    cols.get(4)
                // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                ));
            }
        } catch (IOException | NumberFormatException e) {
            // เรียก `RuntimeException(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            throw new RuntimeException("Cannot load player history.", e);
        }

        // ส่งค่า `records` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return records;
    }

    // แปลงข้อความให้ปลอดภัยสำหรับรูปแบบ CSV
    private String csv(String value) {
        // ตรวจเงื่อนไข `value == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (value == null) {
            // ส่งค่า `"\"\""` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return "\"\"";
        }

        // ส่งค่า `"\"" + value.replace("\"", "\"\"") + "\""` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // แยกหนึ่งบรรทัด CSV ออกเป็นคอลัมน์โดยคำนึงถึงเครื่องหมายคำพูด
    private List<String> parseCsvLine(String line) {
        // สร้าง ArrayList ว่างสำหรับเก็บอ็อบเจ็กต์หลายตัวแบบลำดับ
        List<String> result = new ArrayList<>();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `StringBuilder current` เพื่อใช้ต่อในขั้นตอนถัดไป
        StringBuilder current = new StringBuilder();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `boolean inQuotes` เพื่อใช้ต่อในขั้นตอนถัดไป
        boolean inQuotes = false;

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int i = 0; i < line.length(); i++) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `char c` เพื่อใช้ต่อในขั้นตอนถัดไป
            char c = line.charAt(i);

            // ตรวจเงื่อนไข `c == '"'`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (c == '"') {
                // ตรวจเงื่อนไข `inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"'`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // เรียก `current.append(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    current.append('"');
                    // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                    i++;
                } else {
                    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `inQuotes` เพื่อใช้ต่อในขั้นตอนถัดไป
                    inQuotes = !inQuotes;
                }
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `} else if (c` เพื่อใช้ต่อในขั้นตอนถัดไป
            } else if (c == ',' && !inQuotes) {
                // เรียก `result.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                result.add(current.toString());
                // เรียก `current.setLength(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                current.setLength(0);
            } else {
                // เรียก `current.append(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                current.append(c);
            }
        }

        // เรียก `result.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        result.add(current.toString());
        // ส่งค่า `result` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return result;
    }
}
