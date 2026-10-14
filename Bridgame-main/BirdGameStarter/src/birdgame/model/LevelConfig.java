/*
 * รวมกติกาที่ขึ้นอยู่กับด่านไว้จุดเดียว
 * ตอนนี้ใช้กำหนดจำนวนหัวใจที่ต้องเก็บในแต่ละด่าน
 * ถ้าอาจารย์สั่งเปลี่ยน 10/15/20/25 ให้เริ่มแก้ที่ไฟล์นี้ก่อน
 */

package birdgame.model;

public final class LevelConfig {

    private LevelConfig() {}

    // รับหมายเลขด่านแล้วคืนจำนวนหัวใจที่ต้องเก็บเพื่อผ่านด่านนั้น ด่าน 5 ไม่ใช้หัวใจเพราะเป็นฉากจบ
    public static int getHeartRequired(int level) {

        return switch (level) {

            case 1 -> 10;

            case 2 -> 15;

            case 3 -> 20;

            case 4 -> 25;

            case 5 -> 0; 

            default -> throw new IllegalArgumentException("Invalid level: " + level);
        };
    }
}
