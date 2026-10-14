/*
 * ============================================================
 * ไฟล์: RankingService.java
 * หน้าที่หลัก: บริการคำนวณอันดับ
 *
 * ภาพรวม:
 * กำหนดลำดับผู้ที่จบเกมและเรียงผู้เล่นตามกติกา Finished > Level > Pipes
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
// นำเข้า RankingEntry เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.RankingEntry;

// นำเข้า ArrayList เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.ArrayList;
// นำเข้า Comparator เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.Comparator;
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `RankingService` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class RankingService {
    // ประกาศ field `finishCounter` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int finishCounter = 0;

    // กำหนดลำดับการจบเกมให้ผู้เล่นที่ชนะ
    public void recordWinner(Player player) {
        // ตรวจเงื่อนไข `!player.isFinished()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (!player.isFinished()) {
            // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
            finishCounter++;
            // เรียก `player.finishGame(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            player.finishGame(finishCounter);
        }
    }

    // สร้างและเรียงข้อมูลอันดับตามกติกาของเกม
    public List<RankingEntry> calculateRanking(List<Player> players) {
        // สร้าง ArrayList ว่างสำหรับเก็บอ็อบเจ็กต์หลายตัวแบบลำดับ
        List<Player> copy = new ArrayList<>(players);

        // เรียงลำดับรายการตาม Comparator/กติกาที่กำหนด
        copy.sort(
            // สร้างกติกาเปรียบเทียบเพื่อใช้เรียงอันดับข้อมูล
            Comparator
                .comparing(Player::isFinished).reversed()
                .thenComparingInt(p -> p.isFinished() ? p.getFinishOrder() : 0)
                .thenComparing(
                    // สร้างกติกาเปรียบเทียบเพื่อใช้เรียงอันดับข้อมูล
                    Comparator.comparingInt(Player::getCurrentLevel).reversed()
                )
                .thenComparing(
                    // สร้างกติกาเปรียบเทียบเพื่อใช้เรียงอันดับข้อมูล
                    Comparator.comparingInt(Player::getTotalPipes).reversed()
                )
                .thenComparingInt(Player::getPlayerId)
        );

        // ส่งค่า `copy.stream()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return copy.stream()
                .map(RankingEntry::new)
                // เรียก `toList(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                .toList();
    }
}
