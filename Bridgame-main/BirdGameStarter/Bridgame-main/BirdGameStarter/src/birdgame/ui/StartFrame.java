/*
 * ============================================================
 * ไฟล์: StartFrame.java
 * หน้าที่หลัก: หน้าเมนูเริ่มเกม
 *
 * ภาพรวม:
 * วาดธีมเกม เล่น Animation นกลอย และมีปุ่ม START/EXIT
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า ImageIO เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.imageio.ImageIO;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า LineBorder เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.border.LineBorder;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.*;
// นำเข้า MouseAdapter เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.event.MouseAdapter;
// นำเข้า MouseEvent เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.event.MouseEvent;
// นำเข้า BufferedImage เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.image.BufferedImage;
// นำเข้า URL เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.net.URL;

// ประกาศคลาส `StartFrame` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class StartFrame extends JFrame {

    // ประกาศ field `birdSprite` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private BufferedImage birdSprite;
    // ประกาศ field `birdFrame` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int birdFrame = 0;
    // ประกาศ field `animationTick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int animationTick = 0;
    // ประกาศ field `floatTick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int floatTick = 0;
    // ประกาศ field `menuPanel` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private GameMenuPanel menuPanel;

    // Constructor ของ `StartFrame` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public StartFrame() {
        // เรียก `setTitle(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setTitle("Bird Game");
        // เรียก `setSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setSize(760, 700);
        // เรียก `setDefaultCloseOperation(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // เรียก `setLocationRelativeTo(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setLocationRelativeTo(null);
        // เรียก `setResizable(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setResizable(false);

        // เรียก `loadBirdSprite(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        loadBirdSprite();
        // เรียก `buildUI(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        buildUI();
        // เรียก `startMenuAnimation(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        startMenuAnimation();
    }

    // โหลดไฟล์ bird.png จาก resources เพื่อใช้ในหน้าเมนู
    private void loadBirdSprite() {
        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // ค้นหา resource ที่อยู่ใน classpath ของโปรเจกต์ เช่น รูปในโฟลเดอร์ assets
            URL url = StartFrame.class.getResource("/birdgame/assets/bird.png");
            // ตรวจเงื่อนไข `url != null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (url != null) {
                // อ่านไฟล์รูปภาพเข้าหน่วยความจำเป็น BufferedImage
                birdSprite = ImageIO.read(url);
            }
        } catch (Exception ignored) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `birdSprite` เพื่อใช้ต่อในขั้นตอนถัดไป
            birdSprite = null;
        }
    }

    // สร้างและจัดวางองค์ประกอบ GUI ของหน้าจอนี้
    private void buildUI() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `menuPanel` เพื่อใช้ต่อในขั้นตอนถัดไป
        menuPanel = new GameMenuPanel();
        // กำหนด Layout Manager เพื่อควบคุมวิธีจัดวาง component ภายใน container
        menuPanel.setLayout(null);

        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        JButton startButton = createGameButton("START", new Color(238, 238, 238));
        // เรียก `startButton.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        startButton.setBounds(215, 440, 330, 72);
        // ผูกเหตุการณ์คลิกปุ่มกับโค้ดที่จะทำเมื่อผู้ใช้กดปุ่ม
        startButton.addActionListener(e -> startGame());

        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        JButton exitButton = createGameButton("EXIT", new Color(238, 238, 238));
        // เรียก `exitButton.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        exitButton.setBounds(215, 525, 330, 72);
        // จบโปรแกรม Java ทั้งหมดด้วยสถานะปกติ (exit code 0)
        exitButton.addActionListener(e -> System.exit(0));

        // เรียก `menuPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        menuPanel.add(startButton);
        // เรียก `menuPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        menuPanel.add(exitButton);

        // เรียก `setContentPane(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setContentPane(menuPanel);
    }

    // สร้าง JButton ที่ตั้งค่าสี ฟอนต์ กรอบ และพฤติกรรม hover ให้เหมือนกัน
    private JButton createGameButton(String text, Color baseColor) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JButton button` เพื่อใช้ต่อในขั้นตอนถัดไป
        JButton button = new JButton(text);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        button.setForeground(new Color(68, 49, 68));
        // กำหนดสีพื้นหลังของ Swing component
        button.setBackground(baseColor);
        // เรียก `button.setFocusPainted(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setFocusPainted(false);
        // กำหนดกรอบและระยะขอบของ Swing component
        button.setBorder(new LineBorder(new Color(68, 49, 68), 5, false));
        // เรียก `button.setCursor(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ประกาศเมธอด `addMouseListener()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        button.addMouseListener(new MouseAdapter() {
            @Override
            // ประกาศเมธอด `mouseEntered()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
            public void mouseEntered(MouseEvent e) {
                // กำหนดสีพื้นหลังของ Swing component
                button.setBackground(new Color(255, 245, 188));
            }

            @Override
            // ประกาศเมธอด `mouseExited()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
            public void mouseExited(MouseEvent e) {
                // กำหนดสีพื้นหลังของ Swing component
                button.setBackground(baseColor);
            }
        });

        // ส่งค่า `button` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return button;
    }

    // สร้าง Timer สำหรับ Animation นกในหน้าเมนู
    private void startMenuAnimation() {
        // สร้าง Swing Timer เพื่อเรียกโค้ดตามช่วงเวลาที่กำหนด ใช้เป็น Game Loop/Animation โดยไม่บล็อกหน้าจอ
        new Timer(80, e -> {
            // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
            animationTick++;
            // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
            floatTick++;

            // ตรวจเงื่อนไข `animationTick >= 2`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (animationTick >= 2) {
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `animationTick` เพื่อใช้ต่อในขั้นตอนถัดไป
                animationTick = 0;
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `birdFrame` เพื่อใช้ต่อในขั้นตอนถัดไป
                birdFrame = (birdFrame + 1) % 3;
            }

            // ตรวจเงื่อนไข `menuPanel != null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (menuPanel != null) {
                // ขอให้ Swing วาด component ใหม่ เพื่อให้ภาพบนหน้าจอตรงกับสถานะล่าสุด
                menuPanel.repaint();
            }
        // สั่งเริ่มการทำงานของอ็อบเจ็กต์ที่รองรับ start() เช่น Timer
        }).start();
    }

    // เริ่มเกมหรือเปลี่ยนไปหน้าถัดไปตามบริบทของคลาส
    private void startGame() {
        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        dispose();
        // ทำให้หน้าต่างหรือ component นี้แสดงบนหน้าจอ
        new PlayerSetupFrame().setVisible(true);
    }

    // ประกาศคลาส `GameMenuPanel` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
    private class GameMenuPanel extends JPanel {

        // ประกาศเมธอด `GameMenuPanel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        GameMenuPanel() {
            // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
            setBackground(new Color(86, 191, 204));
        }

        @Override
        // เมธอดที่ Swing เรียกทุกครั้งที่ต้องวาด JPanel ใหม่
        protected void paintComponent(Graphics g) {
            // เรียก `super.paintComponent(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            super.paintComponent(g);

            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Graphics2D g2` เพื่อใช้ต่อในขั้นตอนถัดไป
            Graphics2D g2 = (Graphics2D) g.create();
            // เรียก `g2.setRenderingHint(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int w` เพื่อใช้ต่อในขั้นตอนถัดไป
            int w = getWidth();
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int h` เพื่อใช้ต่อในขั้นตอนถัดไป
            int h = getHeight();

            // ท้องฟ้า
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(86, 191, 204));
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 0, w, h);

            // เมฆแบบบล็อกด้านล่าง
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(235, 255, 218));
            // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
            for (int x = -40; x < w + 80; x += 105) {
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x, 555, 130, 95);
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x + 45, 535, 120, 110);
            }

            // พื้นหญ้า
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(84, 215, 108));
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 600, w, 48);

            // แถบดิน
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(228, 219, 151));
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 648, w, h - 648);

            // เส้นพื้นสีเข้ม
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(83, 65, 83));
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 598, w, 5);

            // โลโก้ BIRD GAME พร้อมเงา
            // สร้าง Font ใหม่พร้อมกำหนดตระกูล รูปแบบ และขนาดตัวอักษร
            Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 66);
            // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
            g2.setFont(titleFont);
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String title` เพื่อใช้ต่อในขั้นตอนถัดไป
            String title = "BIRD GAME";
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics fm` เพื่อใช้ต่อในขั้นตอนถัดไป
            FontMetrics fm = g2.getFontMetrics();
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int titleX` เพื่อใช้ต่อในขั้นตอนถัดไป
            int titleX = (w - fm.stringWidth(title)) / 2;
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int titleY` เพื่อใช้ต่อในขั้นตอนถัดไป
            int titleY = 125;

            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(78, 57, 78));
            // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
            g2.drawString(title, titleX + 6, titleY + 7);
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(250, 250, 246));
            // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
            g2.drawString(title, titleX, titleY);

            // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(78, 57, 78));
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String sub` เพื่อใช้ต่อในขั้นตอนถัดไป
            String sub = "FLY • COLLECT HEARTS • REACH THE NEST";
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics sfm` เพื่อใช้ต่อในขั้นตอนถัดไป
            FontMetrics sfm = g2.getFontMetrics();
            // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
            g2.drawString(sub, (w - sfm.stringWidth(sub)) / 2, 165);

            // นกตรงกลาง ลอยขึ้นลงเบา ๆ
            // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
            int birdY = 255 + (int) (Math.sin(floatTick * 0.18) * 8);
            // เรียก `drawMenuBird(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            drawMenuBird(g2, w / 2 - 45, birdY, 90, 90);

            // คำใบ้เล็ก ๆ
            // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
            g2.setFont(AppFont.bold(18));
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(78, 57, 78));
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String hint` เพื่อใช้ต่อในขั้นตอนถัดไป
            String hint = "Choose START to enter the game";
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics hfm` เพื่อใช้ต่อในขั้นตอนถัดไป
            FontMetrics hfm = g2.getFontMetrics();
            // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
            g2.drawString(hint, (w - hfm.stringWidth(hint)) / 2, 390);

            // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
            g2.dispose();
        }

        // ประกาศเมธอด `drawMenuBird()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        private void drawMenuBird(Graphics2D g2, int x, int y, int width, int height) {
            // ตรวจเงื่อนไข `birdSprite == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (birdSprite == null) {
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(255, 221, 64));
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x, y, width, height - 15);
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(Color.BLACK);
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x + width - 28, y + 20, 8, 8);
                // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
                return;
            }

            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int[] frameX` เพื่อใช้ต่อในขั้นตอนถัดไป
            int[] frameX = {81, 480, 1028};
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int[] frameWidth` เพื่อใช้ต่อในขั้นตอนถัดไป
            int[] frameWidth = {312, 462, 313};

            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int sx` เพื่อใช้ต่อในขั้นตอนถัดไป
            int sx = frameX[birdFrame];
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int sw` เพื่อใช้ต่อในขั้นตอนถัดไป
            int sw = frameWidth[birdFrame];

            // วาดรูปภาพลงบนหน้าจอตามตำแหน่งและขนาดที่กำหนด
            g2.drawImage(
                birdSprite,
                x,
                y,
                x + width,
                y + height,
                sx,
                0,
                sx + sw,
                birdSprite.getHeight(),
                null
            );
        }
    }
}
