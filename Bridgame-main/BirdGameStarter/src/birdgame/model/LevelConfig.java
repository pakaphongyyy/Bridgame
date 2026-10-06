package birdgame.model;

/** ค่าคงที่ของแต่ละด่าน: หัวใจที่ต้องเก็บ และความยาก */
public final class LevelConfig {
    public static final int FINAL_LEVEL = 5;

    private LevelConfig() {}

    public static int getHeartRequired(int level) {
        return switch (level) {
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 20;
            case 4 -> 25;
            case 5 -> 0; // ด่าน 5 ใช้เป็นฉากจบ: นกบินเข้ารัง
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        };
    }

    /** ความเร็วท่อ/หัวใจ/รัง (px ต่อเฟรม) */
    public static int getPipeSpeed(int level) {
        return switch (level) {
            case 1, 2 -> 3;
            case 3, 4 -> 4;
            default -> 3;
        };
    }

    /** ความสูงช่องว่างระหว่างท่อบน-ล่าง (ยิ่งน้อยยิ่งยาก) */
    public static int getPipeGap(int level) {
        return switch (level) {
            case 1 -> 190;
            case 2 -> 180;
            case 3 -> 170;
            case 4 -> 160;
            default -> 190;
        };
    }

    /** จำนวนเฟรมระหว่างท่อแต่ละคู่ (60 เฟรม ≈ 1 วินาที, ยิ่งน้อยท่อยิ่งถี่) */
    public static int getPipeInterval(int level) {
        return switch (level) {
            case 1 -> 100;
            case 2 -> 95;
            case 3 -> 85;
            case 4 -> 80;
            default -> 100;
        };
    }

    public static boolean isFinalLevel(int level) {
        return level >= FINAL_LEVEL;
    }
}
