package birdgame.model;

public final class LevelConfig {
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
}
