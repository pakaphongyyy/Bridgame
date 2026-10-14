/*
 * ============================================================
 * ไฟล์: GameEngine.java
 * หน้าที่หลัก: หัวใจของ Game Logic
 *
 * ภาพรวม:
 * ควบคุม Game Loop, Gravity, spawn ท่อ/หัวใจ, Collision, คะแนน, เปลี่ยนด่าน, Game Over และด่าน 5
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.service` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.service;

// นำเข้า Bird เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Bird;
// นำเข้า GameState เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.GameState;
// นำเข้า Heart เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Heart;
// นำเข้า LevelConfig เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.LevelConfig;
// นำเข้า Nest เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Nest;
// นำเข้า Pipe เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Pipe;
// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;

// ประกาศคลาส `GameEngine` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class GameEngine {
    // ประกาศ field `gameState` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final GameState gameState;
    // ประกาศ field `scoreService` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final ScoreService scoreService = new ScoreService();
    // ประกาศ field `levelService` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final LevelService levelService = new LevelService();
    // ประกาศ field `collisionService` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final CollisionService collisionService = new CollisionService();

    // ท่อ / หัวใจ / รัง
    // ประกาศค่าคงที่ `PIPE_SPEED` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final int PIPE_SPEED = 3;
    // ประกาศค่าคงที่ `PIPE_INTERVAL` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final int PIPE_INTERVAL = 100;

    // ประกาศ field `tick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int tick;
    // ประกาศ field `gameOver` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean gameOver;
    // ประกาศ field `won` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean won;

    // Constructor ของ `GameEngine` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public GameEngine(Player player) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.gameState` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.gameState = new GameState(player);
    }

    // เริ่มเกมหรือเปลี่ยนไปหน้าถัดไปตามบริบทของคลาส
    public void startGame() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameOver` เพื่อใช้ต่อในขั้นตอนถัดไป
        gameOver = false;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `won` เพื่อใช้ต่อในขั้นตอนถัดไป
        won = false;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `tick` เพื่อใช้ต่อในขั้นตอนถัดไป
        tick = 0;
        // เรียก `gameState.startLevel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        gameState.startLevel();
    }

    /** เรียกทุกเฟรมจาก GamePanel */
    // อัปเดตสถานะของอ็อบเจ็กต์หนึ่งเฟรม เช่น ตำแหน่ง/ความเร็ว/Animation
    public void update(int panelWidth, int panelHeight) {
        // ตรวจเงื่อนไข `gameOver || panelWidth <= 0 || panelHeight <= 0`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (gameOver || panelWidth <= 0 || panelHeight <= 0) {
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Player player` เพื่อใช้ต่อในขั้นตอนถัดไป
        Player player = gameState.getCurrentPlayer();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Bird bird` เพื่อใช้ต่อในขั้นตอนถัดไป
        Bird bird = gameState.getBird();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int ground` เพื่อใช้ต่อในขั้นตอนถัดไป
        int ground = CollisionService.GROUND_HEIGHT;

        // ตรวจเงื่อนไข `player == null || bird == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (player == null || bird == null) {
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        tick++;

        // นกเคลื่อนที่ตาม Gravity ทุกเฟรม
        // ตรวจเงื่อนไข `bird.isAlive()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (bird.isAlive()) {
            // เรียก `bird.update(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            bird.update();
        }

        // ==================== ด่าน 5 ====================
        // ตรวจเงื่อนไข `player.getCurrentLevel() == 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (player.getCurrentLevel() == 5) {
            // ตรวจเงื่อนไข `gameState.getNest() == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (gameState.getNest() == null) {
                gameState.setNest(
                    new Nest(panelWidth, panelHeight, ground, PIPE_SPEED, 100)
                );
            }

            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Nest nest` เพื่อใช้ต่อในขั้นตอนถัดไป
            Nest nest = gameState.getNest();
            // เรียก `nest.update(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            nest.update();

            // ถึงรังจริง = ชนะ
            // ตรวจเงื่อนไข `collisionService.checkBirdNest(bird, nest)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (collisionService.checkBirdNest(bird, nest)) {
                // เรียก `player.finishGame(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                player.finishGame(1);
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `won` เพื่อใช้ต่อในขั้นตอนถัดไป
                won = true;
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameOver` เพื่อใช้ต่อในขั้นตอนถัดไป
                gameOver = true;
                // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                return;
            }

            // ตกพื้น/ชนเพดานก่อนถึงรัง = แพ้
            // ตรวจเงื่อนไข `collisionService.checkBirdGround(bird, panelHeight)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (collisionService.checkBirdGround(bird, panelHeight)) {
                // เรียก `bird.setAlive(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                bird.setAlive(false);
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameOver` เพื่อใช้ต่อในขั้นตอนถัดไป
                gameOver = true;
            }

            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // ==================== ด่าน 1-4 ====================

        // สร้างท่อพร้อมหัวใจกลางช่อง
        // ตรวจเงื่อนไข `tick % PIPE_INTERVAL == 0`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (tick % PIPE_INTERVAL == 0) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Pipe pipe` เพื่อใช้ต่อในขั้นตอนถัดไป
            Pipe pipe = Pipe.spawn(panelWidth, panelHeight, ground, PIPE_SPEED);
            // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
            gameState.getPipes().add(pipe);
            // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
            gameState.getHearts().add(Heart.spawnInGap(pipe, PIPE_SPEED));
        }

        // ให้ท่อและหัวใจเคลื่อนจากขวาไปซ้าย
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (Pipe pipe : gameState.getPipes()) {
            // เรียก `pipe.update(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            pipe.update();
        }

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (Heart heart : gameState.getHearts()) {
            // เรียก `heart.update(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            heart.update();
        }

        // 1) เก็บหัวใจจากการชนจริงของ Bird กับ Heart
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `boolean collectedHeart` เพื่อใช้ต่อในขั้นตอนถัดไป
        boolean collectedHeart = false;
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (Heart heart : gameState.getHearts()) {
            // ตรวจเงื่อนไข `collisionService.checkBirdHeart(bird, heart)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (collisionService.checkBirdHeart(bird, heart)) {
                // เรียก `scoreService.collectHeart(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                scoreService.collectHeart(player);
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `collectedHeart` เพื่อใช้ต่อในขั้นตอนถัดไป
                collectedHeart = true;
                // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                break;
            }
        }

        // เก็บครบตามด่าน -> เปลี่ยนด่านและรีเซตหัวใจ
        // ตรวจเงื่อนไข `collectedHeart && levelService.checkLevelComplete(player)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (collectedHeart && levelService.checkLevelComplete(player)) {
            // เรียก `levelService.goToNextLevel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            levelService.goToNextLevel(player);
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `tick` เพื่อใช้ต่อในขั้นตอนถัดไป
            tick = 0;
            // เรียก `gameState.resetLevel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            gameState.resetLevel();
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // 2) นับท่อจากการบินผ่านจริง นับครั้งเดียวต่อท่อ
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (Pipe pipe : gameState.getPipes()) {
            // ตรวจเงื่อนไข `collisionService.checkPassedPipe(bird, pipe)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (collisionService.checkPassedPipe(bird, pipe)) {
                // เรียก `scoreService.passPipe(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                scoreService.passPipe(player);
            }
        }

        // 3) ชนท่อจริง = Game Over
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (Pipe pipe : gameState.getPipes()) {
            // ตรวจเงื่อนไข `collisionService.checkBirdPipe(bird, pipe)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (collisionService.checkBirdPipe(bird, pipe)) {
                // เรียก `bird.setAlive(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                bird.setAlive(false);
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameOver` เพื่อใช้ต่อในขั้นตอนถัดไป
                gameOver = true;
                // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                return;
            }
        }

        // 4) ชนพื้นหรือเพดาน = Game Over
        // ตรวจเงื่อนไข `collisionService.checkBirdGround(bird, panelHeight)`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (collisionService.checkBirdGround(bird, panelHeight)) {
            // เรียก `bird.setAlive(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            bird.setAlive(false);
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameOver` เพื่อใช้ต่อในขั้นตอนถัดไป
            gameOver = true;
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // ลบวัตถุที่ออกนอกจอ
        // ลบสมาชิกที่ตรงตามเงื่อนไข เช่น วัตถุที่ออกนอกจอ เพื่อไม่ให้รายการโตขึ้นเรื่อย ๆ
        gameState.getPipes().removeIf(Pipe::isOffScreen);
        // ลบสมาชิกที่ตรงตามเงื่อนไข เช่น วัตถุที่ออกนอกจอ เพื่อไม่ให้รายการโตขึ้นเรื่อย ๆ
        gameState.getHearts().removeIf(Heart::isOffScreen);
    }

    // Getter `getGameState()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public GameState getGameState() {
        // ส่งค่า `gameState` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return gameState;
    }

    /** ใช้นกตัวเดียวกับ GameState เพื่อให้การวาด/ชน/รีเซตด่านตรงกัน */
    // Getter `getBird()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
    public Bird getBird() {
        // ส่งค่า `gameState.getBird()` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return gameState.getBird();
    }

    // เมธอด `isGameOver()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isGameOver() {
        // ส่งค่า `gameOver` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return gameOver;
    }

    // เมธอด `isWon()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
    public boolean isWon() {
        // ส่งค่า `won` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return won;
    }

    // สร้างข้อความหัวใจสำหรับ HUD เช่น 3/10 หรือ FINAL
    public String getHeartDisplay() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Player player` เพื่อใช้ต่อในขั้นตอนถัดไป
        Player player = gameState.getCurrentPlayer();

        // ตรวจเงื่อนไข `player.getCurrentLevel() == 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (player.getCurrentLevel() == 5) {
            // ส่งค่า `"FINAL"` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
            return "FINAL";
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int required` เพื่อใช้ต่อในขั้นตอนถัดไป
        int required = LevelConfig.getHeartRequired(player.getCurrentLevel());
        // ส่งค่า `player.getLevelHearts() + "/" + required` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return player.getLevelHearts() + "/" + required;
    }
}
