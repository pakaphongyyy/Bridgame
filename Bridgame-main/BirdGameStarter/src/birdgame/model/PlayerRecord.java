/*
 * ข้อมูลหนึ่งแถวที่ใช้บันทึกลงประวัติผู้เล่น
 * ค่าพวกนี้เป็น snapshot หลังจบรอบ เช่น ชื่อ ด่านสูงสุด ท่อสะสม และเวลาที่เล่น
 */

package birdgame.model;

public class PlayerRecord {

    private final String playerName;

    private final boolean finished;

    private final int highestLevel;

    private final int totalPipes;

    private final String playedAt;

    // ตอนจบรอบ เราคัดค่าที่จำเป็นจาก Player มาเก็บเป็นประวัติหนึ่งรายการ เพื่อใช้เขียน CSV และแสดงตาราง
    public PlayerRecord(String playerName,
                        boolean finished,
                        int highestLevel,
                        int totalPipes,
                        String playedAt) {

        this.playerName = playerName;

        this.finished = finished;

        this.highestLevel = highestLevel;

        this.totalPipes = totalPipes;

        this.playedAt = playedAt;
    }

    // ตอนจบรอบ เราคัดค่าที่จำเป็นจาก Player มาเก็บเป็นประวัติหนึ่งรายการ เพื่อใช้เขียน CSV และแสดงตาราง
    public PlayerRecord(Player player, String playedAt) {
        this(
            player.getName(),
            player.isFinished(),
            player.getCurrentLevel(),
            player.getTotalPipes(),
            playedAt
        );
    }

    public String getPlayerName() {

        return playerName;
    }

    public boolean isFinished() {

        return finished;
    }

    public int getHighestLevel() {

        return highestLevel;
    }

    public int getTotalPipes() {

        return totalPipes;
    }

    public String getPlayedAt() {

        return playedAt;
    }
}
