package birdgame.model;
 
public enum GameState {
    READY,      // ยังไม่เริ่ม
    PLAYING,    // ด่าน 1-4: บินผ่านท่อ เก็บหัวใจ
    ENDING,     // ด่าน 5: ไม่มีท่อ บินเข้ารัง
    GAME_OVER,  // ชนท่อ / ตกพื้น
    WON         // เข้ารังสำเร็จ จบเกม
}