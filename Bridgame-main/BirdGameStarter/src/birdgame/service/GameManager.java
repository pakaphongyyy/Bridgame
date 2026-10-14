/*
 * ตัวช่วยจัดลำดับผู้เล่นหลายคน
 * เก็บรายชื่อผู้เล่นทั้งหมดและบอกว่าตอนนี้ถึงคิวของใคร
 * ในโหมดเล่นทีละคนบางส่วนอาจไม่ได้ถูกใช้บ่อย แต่ยังเป็นโครงสร้างรองรับหลายผู้เล่น
 */

package birdgame.service;

import birdgame.model.Player;

import java.util.List;

public class GameManager {

    private final List<Player> players;

    private int currentPlayerIndex = 0;

    // รับรายชื่อผู้เล่นทั้งหมดไว้ แล้วเริ่ม index ที่ -1 เพราะยังไม่มีใครเริ่มเล่น
    public GameManager(List<Player> players) {

        this.players = players;
    }

    public Player getCurrentPlayer() {

        return players.get(currentPlayerIndex);
    }

    public boolean hasNextPlayer() {

        return currentPlayerIndex + 1 < players.size();
    }

    // ขยับ index ไปคนถัดไป ถ้าไม่มีคนถัดไปแล้วจะคืน null
    public Player startNextPlayer() {

        currentPlayerIndex++;

        return getCurrentPlayer();
    }

    public List<Player> getPlayers() {

        return players;
    }

    public int getCurrentPlayerIndex() {

        return currentPlayerIndex;
    }
}
