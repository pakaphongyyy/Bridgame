/*
 * กล่องรวมสถานะของด่านที่กำลังเล่นอยู่
 * เก็บนก ท่อ หัวใจ รัง และผู้เล่นปัจจุบันไว้ด้วยกัน
 * เวลาเปลี่ยนด่าน เรารีเซตของในฉากตรงนี้ แต่ไม่ล้างคะแนนท่อสะสมของ Player
 */

package birdgame.model;

import java.util.ArrayList;

import java.util.List;

public class GameState {

    private Player currentPlayer;

    private Bird bird;

    private final List<Pipe> pipes = new ArrayList<>();

    private final List<Heart> hearts = new ArrayList<>();

    private Nest nest;

    private boolean gameRunning;

    private boolean levelCompleted;

    private boolean gameCompleted;

    // สร้างสถานะของเกมสำหรับผู้เล่นคนนี้ แล้วเตรียมด่านแรกทันทีด้วย resetLevel()
    public GameState(Player currentPlayer) {

        this.currentPlayer = currentPlayer;

        resetLevel();
    }

    // รีเซตเฉพาะของที่อยู่ในฉาก: สร้างนกใหม่ ล้างท่อ/หัวใจ และเอารังเก่าออก โดยไม่ไปแตะ totalPipes ของ Player
    public void resetLevel() {

        // เปลี่ยนด่านแล้วใช้นกชุดใหม่ เพื่อให้ตำแหน่ง/ความเร็วกลับสู่ค่าเริ่มต้น
        bird = new Bird(140, 260);

        // ของในฉากด่านเก่าต้องหายทั้งหมด ไม่ให้ตามไปด่านใหม่
        pipes.clear();

        hearts.clear();

        nest = null;

        levelCompleted = false;

        gameCompleted = false;
    }

    // บอกว่าสถานะของด่านเริ่มทำงานแล้ว
    public void startLevel() {

        gameRunning = true;
    }

    public void completeLevel() {

        levelCompleted = true;
    }

    // ใช้บอกว่าเกมทั้งรอบจบแล้ว และปิดสถานะ gameRunning
    public void completeGame() {

        gameCompleted = true;

        gameRunning = false;
    }

    public Player getCurrentPlayer() { return currentPlayer; }

    public Bird getBird() { return bird; }

    public List<Pipe> getPipes() { return pipes; }

    public List<Heart> getHearts() { return hearts; }

    public Nest getNest() { return nest; }

    public boolean isGameRunning() { return gameRunning; }

    public boolean isLevelCompleted() { return levelCompleted; }

    public boolean isGameCompleted() { return gameCompleted; }

    public void setNest(Nest nest) {

        this.nest = nest;
    }
}
