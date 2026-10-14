/*
 * ============================================================
 * ไฟล์: GamePanel.java
 * หน้าที่หลัก: พื้นที่เล่นและ GUI ของเกม
 *
 * ภาพรวม:
 * วาดฉากแต่ละด่าน วาดวัตถุ รับ SPACEBAR ทำ READY STATE อัปเดต HUD และขับ GameEngine ผ่าน Timer
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า Bird เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Bird;
// นำเข้า Heart เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Heart;
// นำเข้า Pipe เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Pipe;
// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;
// นำเข้า CollisionService เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.service.CollisionService;
// นำเข้า GameEngine เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.service.GameEngine;

// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.*;

// ประกาศคลาส `GamePanel` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class GamePanel extends JPanel {
    // ประกาศ field `player` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final Player player;
    // ประกาศ field `gameEngine` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final GameEngine gameEngine;

    // ประกาศ field `onLose` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final Runnable onLose;
    // ประกาศ field `onWin` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final Runnable onWin;

    // ประกาศ field `playerLabel` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final JLabel playerLabel = new JLabel();
    // ประกาศ field `levelLabel` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final JLabel levelLabel = new JLabel();
    // ประกาศ field `heartLabel` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final JLabel heartLabel = new JLabel();
    // ประกาศ field `pipeLabel` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final JLabel pipeLabel = new JLabel();

    // ประกาศ field `center` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private JPanel center;
    // ประกาศ field `gameTimer` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private Timer gameTimer;
    // ประกาศ field `resultHandled` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean resultHandled = false;

    // ก่อนกด SPACE เกมจะอยู่ใน READY STATE
    // ประกาศ field `gameStarted` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private boolean gameStarted = false;
    // ประกาศ field `readyTick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int readyTick = 0;

    // Constructor ของ `GamePanel` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public GamePanel(Player player, Runnable onLose, Runnable onWin) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.player` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.player = player;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.gameEngine` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.gameEngine = new GameEngine(player);
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.onLose` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.onLose = onLose;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `this.onWin` เพื่อใช้ต่อในขั้นตอนถัดไป
        this.onWin = onWin;

        // เรียก `setFocusable(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setFocusable(true);
        // เรียก `setLayout(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setLayout(new BorderLayout());
        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        setBackground(new Color(232, 247, 255));

        // เรียก `buildUI(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        buildUI();
        // เรียก `bindKeys(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bindKeys();

        // เรียก `gameEngine.startGame(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        gameEngine.startGame();
        // เรียก `updateStatus(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        updateStatus();

        // Game loop ~60 FPS
        // สร้าง Swing Timer เพื่อเรียกโค้ดตามช่วงเวลาที่กำหนด ใช้เป็น Game Loop/Animation โดยไม่บล็อกหน้าจอ
        gameTimer = new Timer(16, e -> {
            // ตรวจเงื่อนไข `center.getWidth() == 0 || resultHandled`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (center.getWidth() == 0 || resultHandled) {
                // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                return;
            }

            // READY STATE: นกลอยกลางอากาศ แต่เกมยังไม่เดิน
            // ตรวจเงื่อนไข `!gameStarted`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (!gameStarted) {
                // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
                readyTick++;

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Bird bird` เพื่อใช้ต่อในขั้นตอนถัดไป
                Bird bird = gameEngine.getBird();
                // ตรวจเงื่อนไข `bird != null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (bird != null) {
                    // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
                    int baseY = Math.max(150, center.getHeight() / 2 - 45);
                    // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
                    int bob = (int) (Math.sin(readyTick * 0.08) * 6);
                    // เรียก `bird.setPosition(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    bird.setPosition(140, baseY + bob);
                }

                // ขอให้ Swing วาด component ใหม่ เพื่อให้ภาพบนหน้าจอตรงกับสถานะล่าสุด
                center.repaint();
                // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                return;
            }

            // เริ่มเล่นจริง: Gravity / ท่อ / หัวใจ / Collision / คะแนน
            // เรียก `gameEngine.update(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            gameEngine.update(center.getWidth(), center.getHeight());

            // เรียก `updateStatus(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            updateStatus();
            // ขอให้ Swing วาด component ใหม่ เพื่อให้ภาพบนหน้าจอตรงกับสถานะล่าสุด
            center.repaint();

            // ตรวจเงื่อนไข `gameEngine.isGameOver()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (gameEngine.isGameOver()) {
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `resultHandled` เพื่อใช้ต่อในขั้นตอนถัดไป
                resultHandled = true;
                // สั่งหยุดการทำงานของ Timer เพื่อไม่ให้ Game Loop ทำงานต่อ
                gameTimer.stop();

                // ตรวจเงื่อนไข `gameEngine.isWon()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (gameEngine.isWon()) {
                    // เรียก `onWin.run(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    onWin.run();
                } else {
                    // เรียก `onLose.run(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    onLose.run();
                }
            }
        });
        // สั่งเริ่มการทำงานของอ็อบเจ็กต์ที่รองรับ start() เช่น Timer
        gameTimer.start();
    }

    // สร้างและจัดวางองค์ประกอบ GUI ของหน้าจอนี้
    private void buildUI() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel hud` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        hud.add(playerLabel);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        hud.add(levelLabel);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        hud.add(heartLabel);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        hud.add(pipeLabel);

        // ประกาศเมธอด `JPanel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        center = new JPanel() {
            @Override
            // เมธอดที่ Swing เรียกทุกครั้งที่ต้องวาด JPanel ใหม่
            protected void paintComponent(Graphics g) {
                // เรียก `super.paintComponent(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                super.paintComponent(g);

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int w` เพื่อใช้ต่อในขั้นตอนถัดไป
                int w = getWidth();
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int h` เพื่อใช้ต่อในขั้นตอนถัดไป
                int h = getHeight();

                // พื้นหลังเปลี่ยนตามด่าน
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int level` เพื่อใช้ต่อในขั้นตอนถัดไป
                int level = player.getCurrentLevel();
                // เรียก `drawLevelBackground(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawLevelBackground(g, w, h, level);

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `var state` เพื่อใช้ต่อในขั้นตอนถัดไป
                var state = gameEngine.getGameState();

                // ก่อนเริ่มจะยังไม่มีท่อ/หัวใจเกิดใหม่ เพราะ GameEngine.update() ยังไม่ถูกเรียก
                // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
                for (Pipe pipe : state.getPipes()) {
                    // เรียก `pipe.draw(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    pipe.draw(g);
                }

                // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
                for (Heart heart : state.getHearts()) {
                    // เรียก `heart.draw(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    heart.draw(g);
                }

                // ตรวจเงื่อนไข `state.getNest() != null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (state.getNest() != null) {
                    // เรียก `state.getNest(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    state.getNest().draw(g);
                }

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Bird bird` เพื่อใช้ต่อในขั้นตอนถัดไป
                Bird bird = gameEngine.getBird();
                // ตรวจเงื่อนไข `bird != null && bird.isAlive()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (bird != null && bird.isAlive()) {
                    // เรียก `bird.draw(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    bird.draw(g);
                }

                // พื้นของแต่ละด่านใช้สีต่างกัน แต่ความสูงเดิม
                // เรียก `drawLevelGround(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawLevelGround(g, w, h, level);

                // ตรวจเงื่อนไข `!gameStarted`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (!gameStarted) {
                    // เรียก `drawReadyOverlay(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    drawReadyOverlay(g, w, h);
                } else {
                    // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                    g.setColor(Color.DARK_GRAY);
                    // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
                    g.setFont(AppFont.bold(20));

                    // ตรวจเงื่อนไข `player.getCurrentLevel() < 5`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                    if (player.getCurrentLevel() < 5) {
                        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
                        g.drawString("SPACEBAR = บิน", 340, 45);
                    } else {
                        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
                        g.drawString("LEVEL 5 - บินเข้ารัง", 310, 45);
                    }
                }
            }
        };

        // กำหนดสีพื้นหลังของ Swing component
        center.setBackground(Color.WHITE);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel help` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel help = new JLabel(
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `"SPACEBAR` เพื่อใช้ต่อในขั้นตอนถัดไป
            "SPACEBAR = เริ่มเกม / บินขึ้น • เก็บหัวใจและบินผ่านท่อเพื่อทำคะแนน",
            SwingConstants.CENTER
        );
        // กำหนดกรอบและระยะขอบของ Swing component
        help.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        // เรียก `add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        add(hud, BorderLayout.NORTH);
        // เรียก `add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        add(center, BorderLayout.CENTER);
        // เรียก `add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        add(help, BorderLayout.SOUTH);
    }


    /** วาดพื้นหลังให้แต่ละด่านมีบรรยากาศต่างกัน โดยไม่กระทบ Game Logic */
    // วาดพื้นหลังตามหมายเลขด่านโดยไม่เปลี่ยน Game Logic
    private void drawLevelBackground(Graphics g, int w, int h, int level) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Graphics2D g2` เพื่อใช้ต่อในขั้นตอนถัดไป
        Graphics2D g2 = (Graphics2D) g.create();
        // เรียก `g2.setRenderingHint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int groundY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int groundY = h - CollisionService.GROUND_HEIGHT;

        // เลือกเส้นทางการทำงานตามค่าที่ส่งเข้า switch
        switch (level) {
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 1 -> {
                // DAY SKY - ด่านเริ่มต้น สดใส
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(156, 222, 255));
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawSun(g2, w - 115, 70, new Color(255, 224, 92));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 90, 95, 1.0, new Color(250, 255, 255));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 520, 150, 0.75, new Color(250, 255, 255));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawHills(g2, w, groundY, new Color(126, 202, 122), new Color(91, 181, 102));
            }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 2 -> {
                // SUNSET - สีส้มชมพู
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `GradientPaint sunset` เพื่อใช้ต่อในขั้นตอนถัดไป
                GradientPaint sunset = new GradientPaint(
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, 0, new Color(247, 146, 111),
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, groundY, new Color(255, 211, 137)
                );
                // เรียก `g2.setPaint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                g2.setPaint(sunset);
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawSun(g2, w - 150, 105, new Color(255, 241, 167));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 100, 110, 0.9, new Color(255, 225, 214));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 560, 180, 0.7, new Color(255, 220, 205));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawHills(g2, w, groundY, new Color(181, 153, 117), new Color(144, 132, 96));
            }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 3 -> {
                // NIGHT - กลางคืน มีดาวและพระจันทร์
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(36, 55, 105));
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
                // เรียก `drawStars(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawStars(g2, w, groundY);
                // เรียก `drawMoon(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawMoon(g2, w - 125, 85);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 120, 130, 0.8, new Color(97, 115, 153));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawHills(g2, w, groundY, new Color(55, 92, 91), new Color(42, 77, 70));
            }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 4 -> {
                // STORM - ม่วงเข้ม ดูยากขึ้น
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `GradientPaint storm` เพื่อใช้ต่อในขั้นตอนถัดไป
                GradientPaint storm = new GradientPaint(
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, 0, new Color(82, 69, 116),
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, groundY, new Color(131, 105, 139)
                );
                // เรียก `g2.setPaint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                g2.setPaint(storm);
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 70, 85, 1.15, new Color(100, 95, 122));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 430, 120, 1.05, new Color(88, 84, 110));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 690, 70, 0.8, new Color(111, 103, 130));
                // เรียก `drawLightning(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawLightning(g2, w - 160, 150);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawHills(g2, w, groundY, new Color(70, 84, 79), new Color(51, 69, 61));
            }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 5 -> {
                // FINAL - รุ่งเช้า / บรรยากาศอบอุ่นก่อนเข้ารัง
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `GradientPaint dawn` เพื่อใช้ต่อในขั้นตอนถัดไป
                GradientPaint dawn = new GradientPaint(
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, 0, new Color(119, 201, 231),
                    // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                    0, groundY, new Color(255, 224, 173)
                );
                // เรียก `g2.setPaint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                g2.setPaint(dawn);
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawSun(g2, w / 2 + 170, 95, new Color(255, 230, 126));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 95, 125, 0.95, new Color(255, 248, 229));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawCloud(g2, 570, 155, 0.8, new Color(255, 248, 229));
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                drawHills(g2, w, groundY, new Color(111, 176, 111), new Color(73, 149, 90));
            }
            // กรณีสำรองของ switch เมื่อไม่มี case ใดตรง
            default -> {
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(160, 220, 255));
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);
            }
        }

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        g2.dispose();
    }

    // วาดพื้น/หญ้าของแต่ละด่าน
    private void drawLevelGround(Graphics g, int w, int h, int level) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Graphics2D g2` เพื่อใช้ต่อในขั้นตอนถัดไป
        Graphics2D g2 = (Graphics2D) g.create();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int y` เพื่อใช้ต่อในขั้นตอนถัดไป
        int y = h - CollisionService.GROUND_HEIGHT;

        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        Color top;
        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        Color bottom;

        // เลือกเส้นทางการทำงานตามค่าที่ส่งเข้า switch
        switch (level) {
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 1 -> { top = new Color(85, 194, 91); bottom = new Color(55, 157, 72); }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 2 -> { top = new Color(164, 175, 82); bottom = new Color(126, 141, 72); }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 3 -> { top = new Color(55, 118, 76); bottom = new Color(38, 87, 61); }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 4 -> { top = new Color(68, 105, 72); bottom = new Color(46, 77, 57); }
            // กรณีหนึ่งของ switch: เมื่อค่าตรงกับ case นี้จะใช้คำสั่งชุดนี้
            case 5 -> { top = new Color(95, 184, 94); bottom = new Color(63, 145, 78); }
            // กรณีสำรองของ switch เมื่อไม่มี case ใดตรง
            default -> { top = new Color(80, 180, 90); bottom = new Color(55, 145, 70); }
        }

        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(top);
        // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
        g2.fillRect(0, y, w, CollisionService.GROUND_HEIGHT);

        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(bottom);
        // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
        g2.fillRect(0, y + 18, w, CollisionService.GROUND_HEIGHT - 18);

        // เส้นหญ้าเล็ก ๆ ให้ดูเป็นธีมเกมมากขึ้น
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(top.brighter());
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int x = 0; x < w; x += 28) {
            // เรียก `g2.drawLine(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            g2.drawLine(x, y + 16, x + 8, y + 4);
            // เรียก `g2.drawLine(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            g2.drawLine(x + 8, y + 4, x + 15, y + 16);
        }

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        g2.dispose();
    }

    // วาดพระอาทิตย์ด้วยวงกลมและแสงรอบ ๆ
    private void drawSun(Graphics2D g2, int x, int y, Color color) {
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x - 18, y - 18, 86, 86);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(color);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x, y, 50, 50);
    }

    // วาดพระจันทร์เสี้ยวด้วยวงกลมซ้อนกัน
    private void drawMoon(Graphics2D g2, int x, int y) {
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(255, 246, 195));
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x, y, 58, 58);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(36, 55, 105));
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x + 18, y - 5, 55, 55);
    }

    // วาดจุดดาวในฉากกลางคืน
    private void drawStars(Graphics2D g2, int w, int groundY) {
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(255, 246, 190));
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int[][] stars` เพื่อใช้ต่อในขั้นตอนถัดไป
        int[][] stars = {
            {70, 70}, {145, 145}, {235, 82}, {330, 130}, {430, 65},
            {520, 112}, {625, 72}, {715, 152}, {800, 92}, {385, 210}
        };
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int[] s : stars) {
            // ตรวจเงื่อนไข `s[0] < w && s[1] < groundY`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (s[0] < w && s[1] < groundY) {
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(s[0], s[1], 4, 4);
            }
        }
    }

    // วาดเมฆจากวงรีหลายวงและสี่เหลี่ยมมุมมน
    private void drawCloud(Graphics2D g2, int x, int y, double scale, Color color) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int s` เพื่อใช้ต่อในขั้นตอนถัดไป
        int s = (int) (48 * scale);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(color);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x, y + s / 3, s, s / 2);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x + s / 3, y, s, s);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x + s, y + s / 4, s, (int) (s * 0.7));
        // เรียก `g2.fillRoundRect(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        g2.fillRoundRect(x + s / 4, y + s / 2, (int) (s * 1.6), s / 2, s / 3, s / 3);
    }

    // วาดเนินเขาซ้อนกันเพื่อสร้างฉากหลัง
    private void drawHills(Graphics2D g2, int w, int groundY, Color back, Color front) {
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(back);
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int x = -80; x < w; x += 180) {
            // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
            g2.fillOval(x, groundY - 115, 230, 150);
        }
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(front);
        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int x = -130; x < w; x += 210) {
            // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
            g2.fillOval(x, groundY - 75, 260, 110);
        }
    }

    // วาดรูปสายฟ้าด้วย Polygon
    private void drawLightning(Graphics2D g2, int x, int y) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Polygon bolt` เพื่อใช้ต่อในขั้นตอนถัดไป
        Polygon bolt = new Polygon();
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 18, y);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x, y + 46);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 18, y + 43);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 4, y + 88);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 52, y + 30);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 31, y + 34);
        // เรียก `bolt.addPoint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        bolt.addPoint(x + 45, y);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(255, 235, 112));
        // เรียก `g2.fillPolygon(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        g2.fillPolygon(bolt);
    }

    // วาดข้อความ GET READY และคำสั่ง PRESS SPACE TO START
    private void drawReadyOverlay(Graphics g, int w, int h) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Graphics2D g2` เพื่อใช้ต่อในขั้นตอนถัดไป
        Graphics2D g2 = (Graphics2D) g.create();
        // เรียก `g2.setRenderingHint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String title` เพื่อใช้ต่อในขั้นตอนถัดไป
        String title = "GET READY!";
        // สร้าง Font ใหม่พร้อมกำหนดตระกูล รูปแบบ และขนาดตัวอักษร
        Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 36);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        g2.setFont(titleFont);
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics titleMetrics` เพื่อใช้ต่อในขั้นตอนถัดไป
        FontMetrics titleMetrics = g2.getFontMetrics();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int titleX` เพื่อใช้ต่อในขั้นตอนถัดไป
        int titleX = (w - titleMetrics.stringWidth(title)) / 2;
        // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
        int titleY = Math.max(95, h / 2 - 130);

        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(68, 49, 68));
        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
        g2.drawString(title, titleX + 4, titleY + 5);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(Color.WHITE);
        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
        g2.drawString(title, titleX, titleY);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String hint` เพื่อใช้ต่อในขั้นตอนถัดไป
        String hint = "PRESS SPACE TO START";
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics hintMetrics` เพื่อใช้ต่อในขั้นตอนถัดไป
        FontMetrics hintMetrics = g2.getFontMetrics();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int hintX` เพื่อใช้ต่อในขั้นตอนถัดไป
        int hintX = (w - hintMetrics.stringWidth(hint)) / 2;
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int hintY` เพื่อใช้ต่อในขั้นตอนถัดไป
        int hintY = titleY + 42;

        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(68, 49, 68));
        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
        g2.drawString(hint, hintX + 3, hintY + 3);
        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(255, 244, 177));
        // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
        g2.drawString(hint, hintX, hintY);

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        g2.dispose();
    }

    /** ปุ่มเล่นจริงเหลือเฉพาะ SPACEBAR */
    // ผูกปุ่มคีย์บอร์ดกับ Action ของเกม
    private void bindKeys() {
        // ลงทะเบียน mapping ของปุ่มคีย์บอร์ดหรือ Action เพื่อให้ Swing เรียกคำสั่งเมื่อกดปุ่ม
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            // สร้าง KeyStroke จากชื่อปุ่ม เพื่อใช้ผูกคีย์บอร์ดกับ Action
            KeyStroke.getKeyStroke("SPACE"),
            "jump"
        );

        // Getter `getActionMap()` คืนค่าข้อมูลภายในคลาสให้ส่วนอื่นอ่านได้โดยไม่เข้าถึง field โดยตรง
        getActionMap().put("jump", new AbstractAction() {
            @Override
            // ประกาศเมธอด `actionPerformed()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Bird bird` เพื่อใช้ต่อในขั้นตอนถัดไป
                Bird bird = gameEngine.getBird();

                // ตรวจเงื่อนไข `bird == null || !bird.isAlive() || gameEngine.isGameOver()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (bird == null || !bird.isAlive() || gameEngine.isGameOver()) {
                    // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                    return;
                }

                // SPACE ครั้งแรก = เริ่มเกม + กระโดดครั้งแรกทันที
                // ตรวจเงื่อนไข `!gameStarted`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
                if (!gameStarted) {
                    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `gameStarted` เพื่อใช้ต่อในขั้นตอนถัดไป
                    gameStarted = true;
                    // เรียก `bird.jump(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    bird.jump();
                    // ขอให้ Swing วาด component ใหม่ เพื่อให้ภาพบนหน้าจอตรงกับสถานะล่าสุด
                    center.repaint();
                    // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                    return;
                }

                // เรียก `bird.jump(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                bird.jump();
            }
        });
    }

    // อัปเดตข้อความ HUD ให้ตรงกับข้อมูล Player/GameEngine ล่าสุด
    private void updateStatus() {
        // เปลี่ยนข้อความของ JLabel/Component ให้แสดงค่าปัจจุบัน
        playerLabel.setText("ผู้เล่น: " + player.getName());
        // เปลี่ยนข้อความของ JLabel/Component ให้แสดงค่าปัจจุบัน
        levelLabel.setText("ด่าน: " + player.getCurrentLevel());
        // เปลี่ยนข้อความของ JLabel/Component ให้แสดงค่าปัจจุบัน
        heartLabel.setText("หัวใจ: " + gameEngine.getHeartDisplay());
        // เปลี่ยนข้อความของ JLabel/Component ให้แสดงค่าปัจจุบัน
        pipeLabel.setText("ท่อสะสม: " + player.getTotalPipes());
    }
}
