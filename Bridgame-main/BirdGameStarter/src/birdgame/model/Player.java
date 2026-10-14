/*
 * เก็บข้อมูลของผู้เล่นหนึ่งคนตลอดรอบการเล่น
 * หัวใจเป็นคะแนนเฉพาะด่านและต้องรีเซตตอนขึ้นด่านใหม่
 * แต่ totalPipes เป็นคะแนนสะสมทั้งเกม จึงห้ามรีเซตตอนเปลี่ยนด่าน
 */

package birdgame.model;

public class Player {

    private final int playerId;

    private final String name;

    private int currentLevel = 1;

    // หัวใจตัวนี้นับเฉพาะด่านปัจจุบัน จึงต้องกลับเป็น 0 ตอนขึ้นด่านใหม่
    private int levelHearts = 0;

    // ท่อเป็นคะแนนสะสมทั้งเกม ห้ามรีเซตตอนเปลี่ยนด่าน
    private int totalPipes = 0;

    private boolean finished = false;

    private int finishOrder = 0;

    // สร้างผู้เล่นใหม่ โดยด่านเริ่มที่ 1 หัวใจและท่อเริ่มที่ 0 จากค่าเริ่มต้นของ field
    public Player(int playerId, String name) {

        this.playerId = playerId;

        this.name = name;
    }

    public int getPlayerId() {

        return playerId;
    }

    public String getName() {

        return name;
    }

    public int getCurrentLevel() {

        return currentLevel;
    }

    public int getLevelHearts() {

        return levelHearts;
    }

    public int getTotalPipes() {

        return totalPipes;
    }

    public boolean isFinished() {

        return finished;
    }

    public int getFinishOrder() {

        return finishOrder;
    }

    // เรียกเมื่อเก็บหัวใจจริง เพิ่มเฉพาะหัวใจของด่านปัจจุบัน
    public void addHeart() {

        levelHearts++;
    }

    // เรียกเมื่อบินผ่านท่อจริง เพิ่ม totalPipes ซึ่งต้องสะสมต่อเนื่องข้ามด่าน
    public void addPipe() {

        totalPipes++;
    }

    // รีเซตเฉพาะหัวใจประจำด่านกลับเป็น 0
    public void resetLevelHearts() {

        levelHearts = 0;
    }

    // ขึ้นด่านใหม่ได้ไม่เกินด่าน 5 และรีเซตหัวใจทันที แต่ไม่แตะจำนวนท่อสะสม
    public void nextLevel() {

        if (currentLevel < 5) {

            currentLevel++;

            resetLevelHearts();
        }
    }

    // บันทึกว่าผู้เล่นถึงรังสำเร็จแล้ว พร้อมเก็บลำดับการจบ
    public void finishGame(int order) {

        finished = true;

        finishOrder = order;
    }
}
