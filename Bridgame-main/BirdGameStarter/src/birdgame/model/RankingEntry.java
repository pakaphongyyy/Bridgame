/*
 * รูปแบบข้อมูลที่เตรียมไว้สำหรับแสดงผลการจัดอันดับ
 * แยกออกจาก Player เพื่อให้หน้าแสดงผลไม่ต้องรู้รายละเอียดภายใน Player ทั้งหมด
 */

package birdgame.model;

public class RankingEntry {

    private final String playerName;

    private final boolean finished;

    private final int finishOrder;

    private final int highestLevel;

    private final int totalPipes;

    // เก็บข้อมูลที่หน้าจัดอันดับต้องใช้ โดยไม่ต้องส่ง Player ทั้งก้อนเข้า UI
    public RankingEntry(Player player) {

        this.playerName = player.getName();

        this.finished = player.isFinished();

        this.finishOrder = player.getFinishOrder();

        this.highestLevel = player.getCurrentLevel();

        this.totalPipes = player.getTotalPipes();
    }

    public String getPlayerName() {

        return playerName;
    }

    public boolean isFinished() {

        return finished;
    }

    public int getFinishOrder() {

        return finishOrder;
    }

    public int getHighestLevel() {

        return highestLevel;
    }

    public int getTotalPipes() {

        return totalPipes;
    }
}
