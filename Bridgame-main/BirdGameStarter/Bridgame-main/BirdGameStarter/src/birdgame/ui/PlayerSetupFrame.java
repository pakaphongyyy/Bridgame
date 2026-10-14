/*
 * ============================================================
 * ไฟล์: PlayerSetupFrame.java
 * หน้าที่หลัก: หน้ากรอกชื่อผู้เล่น
 *
 * ภาพรวม:
 * รับชื่อ แสดงประวัติเดิมด้วย JTable และเปิด GameFrame โดยคงธีมเดียวกับหน้า Start
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า Player เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.Player;
// นำเข้า PlayerRecord เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.PlayerRecord;
// นำเข้า FileStorageService เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.service.FileStorageService;

// นำเข้า ImageIO เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.imageio.ImageIO;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า LineBorder เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.border.LineBorder;
// นำเข้า DefaultTableCellRenderer เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.table.DefaultTableCellRenderer;
// นำเข้า DefaultTableModel เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.table.DefaultTableModel;
// นำเข้า JTableHeader เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.table.JTableHeader;
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
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `PlayerSetupFrame` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class PlayerSetupFrame extends JFrame {

    // ประกาศ field `playerNameField` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final JTextField playerNameField = new JTextField();
    // ประกาศ field `storageService` แบบ final หมายถึงหลังสร้างอ็อบเจ็กต์แล้วจะไม่เปลี่ยนการอ้างอิง field นี้
    private final FileStorageService storageService = new FileStorageService();

    // ประกาศ field `birdSprite` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private BufferedImage birdSprite;
    // ประกาศ field `birdFrame` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int birdFrame = 0;
    // ประกาศ field `animationTick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int animationTick = 0;
    // ประกาศ field `floatTick` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private int floatTick = 0;
    // ประกาศ field `backgroundPanel` เพื่อเก็บสถานะที่คลาสนี้ต้องใช้ระหว่างการทำงาน
    private PlayerThemePanel backgroundPanel;

    // ประกาศค่าคงที่ `SKY` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color SKY = new Color(86, 191, 204);
    // ประกาศค่าคงที่ `DARK` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color DARK = new Color(78, 57, 78);
    // ประกาศค่าคงที่ `CREAM` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color CREAM = new Color(250, 250, 246);
    // ประกาศค่าคงที่ `GRASS` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color GRASS = new Color(84, 215, 108);
    // ประกาศค่าคงที่ `DIRT` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color DIRT = new Color(228, 219, 151);
    // ประกาศค่าคงที่ `HOVER` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color HOVER = new Color(255, 245, 188);

    // Constructor ของ `PlayerSetupFrame` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public PlayerSetupFrame() {
        // เรียก `setTitle(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setTitle("Bird Game - Player");
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
        // เรียก `startAnimation(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        startAnimation();

        // เรียก `SwingUtilities.invokeLater(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        SwingUtilities.invokeLater(playerNameField::requestFocusInWindow);
    }

    // โหลดไฟล์ bird.png จาก resources เพื่อใช้ในหน้าเมนู
    private void loadBirdSprite() {
        // เริ่มบล็อก try สำหรับคำสั่งที่อาจเกิด Exception
        try {
            // ค้นหา resource ที่อยู่ใน classpath ของโปรเจกต์ เช่น รูปในโฟลเดอร์ assets
            URL url = PlayerSetupFrame.class.getResource("/birdgame/assets/bird.png");
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
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `backgroundPanel` เพื่อใช้ต่อในขั้นตอนถัดไป
        backgroundPanel = new PlayerThemePanel();
        // กำหนด Layout Manager เพื่อควบคุมวิธีจัดวาง component ภายใน container
        backgroundPanel.setLayout(null);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel title` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel title = new JLabel("PLAYER SETUP", SwingConstants.CENTER);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 42));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        title.setForeground(CREAM);
        // เรียก `title.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        title.setBounds(160, 42, 440, 56);
        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(title);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel subtitle` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel subtitle = new JLabel(
                "ใส่ชื่อผู้เล่น แล้วกด START GAME",
                SwingConstants.CENTER
        );
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        subtitle.setFont(AppFont.bold(18));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        subtitle.setForeground(DARK);
        // เรียก `subtitle.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        subtitle.setBounds(145, 105, 470, 32);
        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(subtitle);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel nameCard` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel nameCard = new JPanel(null);
        // กำหนดสีพื้นหลังของ Swing component
        nameCard.setBackground(new Color(250, 250, 246));
        // กำหนดกรอบและระยะขอบของ Swing component
        nameCard.setBorder(new LineBorder(DARK, 5));
        // เรียก `nameCard.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        nameCard.setBounds(125, 160, 510, 120);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel nameLabel` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel nameLabel = new JLabel("PLAYER NAME");
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        nameLabel.setForeground(DARK);
        // เรียก `nameLabel.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        nameLabel.setBounds(25, 18, 200, 30);
        // เรียก `nameCard.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        nameCard.add(nameLabel);

        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        playerNameField.setFont(AppFont.bold(20));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        playerNameField.setForeground(DARK);
        // กำหนดสีพื้นหลังของ Swing component
        playerNameField.setBackground(Color.WHITE);
        // เรียก `playerNameField.setCaretColor(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        playerNameField.setCaretColor(DARK);
        // กำหนดกรอบและระยะขอบของ Swing component
        playerNameField.setBorder(new LineBorder(DARK, 3));
        // เรียก `playerNameField.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        playerNameField.setBounds(25, 55, 460, 42);
        // ผูกเหตุการณ์คลิกปุ่มกับโค้ดที่จะทำเมื่อผู้ใช้กดปุ่ม
        playerNameField.addActionListener(e -> startGame());
        // เรียก `nameCard.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        nameCard.add(playerNameField);

        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(nameCard);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel historyTitle` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel historyTitle = new JLabel("PLAYER HISTORY", SwingConstants.CENTER);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        historyTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        historyTitle.setForeground(DARK);
        // เรียก `historyTitle.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        historyTitle.setBounds(215, 300, 330, 34);
        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(historyTitle);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JTable historyTable` เพื่อใช้ต่อในขั้นตอนถัดไป
        JTable historyTable = createHistoryTable();
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JScrollPane scrollPane` เพื่อใช้ต่อในขั้นตอนถัดไป
        JScrollPane scrollPane = new JScrollPane(historyTable);
        // กำหนดกรอบและระยะขอบของ Swing component
        scrollPane.setBorder(new LineBorder(DARK, 5));
        // กำหนดสีพื้นหลังของ Swing component
        scrollPane.getViewport().setBackground(new Color(250, 250, 246));
        // เรียก `scrollPane.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        scrollPane.setBounds(60, 340, 640, 205);
        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(scrollPane);

        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        JButton startButton = createGameButton("START GAME", new Color(238, 238, 238));
        // เรียก `startButton.setBounds(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        startButton.setBounds(210, 575, 340, 64);
        // ผูกเหตุการณ์คลิกปุ่มกับโค้ดที่จะทำเมื่อผู้ใช้กดปุ่ม
        startButton.addActionListener(e -> startGame());
        // เรียก `backgroundPanel.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        backgroundPanel.add(startButton);

        // เรียก `setContentPane(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setContentPane(backgroundPanel);
    }

    // สร้าง JButton ที่ตั้งค่าสี ฟอนต์ กรอบ และพฤติกรรม hover ให้เหมือนกัน
    private JButton createGameButton(String text, Color baseColor) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JButton button` เพื่อใช้ต่อในขั้นตอนถัดไป
        JButton button = new JButton(text);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        button.setForeground(DARK);
        // กำหนดสีพื้นหลังของ Swing component
        button.setBackground(baseColor);
        // เรียก `button.setFocusPainted(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setFocusPainted(false);
        // กำหนดกรอบและระยะขอบของ Swing component
        button.setBorder(new LineBorder(DARK, 5));
        // เรียก `button.setCursor(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ประกาศเมธอด `addMouseListener()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        button.addMouseListener(new MouseAdapter() {
            @Override
            // ประกาศเมธอด `mouseEntered()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
            public void mouseEntered(MouseEvent e) {
                // กำหนดสีพื้นหลังของ Swing component
                button.setBackground(HOVER);
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

    // สร้าง JTable สำหรับแสดงประวัติการเล่น
    private JTable createHistoryTable() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String[] columns` เพื่อใช้ต่อในขั้นตอนถัดไป
        String[] columns = {
                "ลำดับ",
                "ชื่อผู้เล่น",
                "ผล",
                "ด่านสูงสุด",
                "ท่อสะสม",
                "เวลาเล่น"
        };

        // ประกาศเมธอด `DefaultTableModel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            // เมธอด `isCellEditable()` คืนค่า boolean เพื่อบอกว่าสถานะตามชื่อเมธอดเป็นจริงหรือไม่
            public boolean isCellEditable(int row, int column) {
                // ส่งค่า `false` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
                return false;
            }
        };

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `List<PlayerRecord> records` เพื่อใช้ต่อในขั้นตอนถัดไป
        List<PlayerRecord> records = storageService.loadRecords();

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int i = records.size() - 1; i >= 0; i--) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `PlayerRecord r` เพื่อใช้ต่อในขั้นตอนถัดไป
            PlayerRecord r = records.get(i);

            model.addRow(new Object[] {
                    i + 1,
                    r.getPlayerName(),
                    r.isFinished() ? "ถึงรัง" : "ยังไม่ถึงรัง",
                    r.getHighestLevel(),
                    r.getTotalPipes(),
                    r.getPlayedAt()
            });
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JTable table` เพื่อใช้ต่อในขั้นตอนถัดไป
        JTable table = new JTable(model);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        table.setFont(AppFont.plain(14));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        table.setForeground(DARK);
        // กำหนดสีพื้นหลังของ Swing component
        table.setBackground(new Color(250, 250, 246));
        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        table.setGridColor(new Color(180, 170, 180));
        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        table.setSelectionBackground(new Color(255, 245, 188));
        // เรียก `table.setSelectionForeground(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setSelectionForeground(DARK);
        // เรียก `table.setRowHeight(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setRowHeight(29);
        // เรียก `table.setShowVerticalLines(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setShowVerticalLines(true);
        // เรียก `table.setShowHorizontalLines(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setShowHorizontalLines(true);
        // เรียก `table.setFillsViewportHeight(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setFillsViewportHeight(true);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JTableHeader header` เพื่อใช้ต่อในขั้นตอนถัดไป
        JTableHeader header = table.getTableHeader();
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        header.setFont(AppFont.bold(14));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        header.setForeground(CREAM);
        // กำหนดสีพื้นหลังของ Swing component
        header.setBackground(DARK);
        // เรียก `header.setReorderingAllowed(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        header.setReorderingAllowed(false);
        // เรียก `header.setPreferredSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `DefaultTableCellRenderer centerRenderer` เพื่อใช้ต่อในขั้นตอนถัดไป
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        // เรียก `centerRenderer.setHorizontalAlignment(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int i = 0; i < table.getColumnCount(); i++) {
            // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(1).setPreferredWidth(105);
        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(2).setPreferredWidth(105);
        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(4).setPreferredWidth(75);
        // เรียก `table.getColumnModel(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.getColumnModel().getColumn(5).setPreferredWidth(145);

        // ส่งค่า `table` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return table;
    }

    // ประกาศเมธอด `startAnimation()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
    private void startAnimation() {
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

            // ตรวจเงื่อนไข `backgroundPanel != null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (backgroundPanel != null) {
                // ขอให้ Swing วาด component ใหม่ เพื่อให้ภาพบนหน้าจอตรงกับสถานะล่าสุด
                backgroundPanel.repaint();
            }
        // สั่งเริ่มการทำงานของอ็อบเจ็กต์ที่รองรับ start() เช่น Timer
        }).start();
    }

    // เริ่มเกมหรือเปลี่ยนไปหน้าถัดไปตามบริบทของคลาส
    private void startGame() {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String name` เพื่อใช้ต่อในขั้นตอนถัดไป
        String name = playerNameField.getText().trim();

        // ตรวจเงื่อนไข `name.isBlank()`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
        if (name.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "กรุณาใส่ชื่อผู้เล่นก่อนเริ่มเกม"
            );
            // เรียก `playerNameField.requestFocusInWindow(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            playerNameField.requestFocusInWindow();
            // ออกจากเมธอดทันทีโดยไม่คืนค่า ใช้หยุดการทำงานส่วนที่เหลือของเมธอด
            return;
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Player player` เพื่อใช้ต่อในขั้นตอนถัดไป
        Player player = new Player(1, name);

        // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
        dispose();
        // ทำให้หน้าต่างหรือ component นี้แสดงบนหน้าจอ
        new GameFrame(player).setVisible(true);
    }

    // ประกาศคลาส `PlayerThemePanel` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
    private class PlayerThemePanel extends JPanel {

        // ประกาศเมธอด `PlayerThemePanel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        PlayerThemePanel() {
            // เรียก `setBackground(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            setBackground(SKY);
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

            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(SKY);
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 0, w, h);

            // เงาหัวข้อ เพื่อให้ได้อารมณ์ pixel/game แบบหน้า Start
            // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 42));
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `FontMetrics titleFm` เพื่อใช้ต่อในขั้นตอนถัดไป
            FontMetrics titleFm = g2.getFontMetrics();
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `String title` เพื่อใช้ต่อในขั้นตอนถัดไป
            String title = "PLAYER SETUP";
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int titleX` เพื่อใช้ต่อในขั้นตอนถัดไป
            int titleX = (w - titleFm.stringWidth(title)) / 2;
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(DARK);
            // วาดข้อความลงบนพื้นที่ Graphics ตามพิกัดที่กำหนด
            g2.drawString(title, titleX + 4, 84 + 5);

            // นกเล็ก ๆ ลอยทางขวาของหัวข้อ
            // ใช้ฟังก์ชันคณิตศาสตร์ของ Java เพื่อคำนวณค่าที่ใช้กับตำแหน่ง/Animation
            int birdY = 84 + (int) (Math.sin(floatTick * 0.18) * 5);
            // เรียก `drawBird(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
            drawBird(g2, 625, birdY - 34, 64, 54);

            // เมฆแบบเดียวกับหน้า Start
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(new Color(235, 255, 218));
            // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
            for (int x = -40; x < w + 80; x += 105) {
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x, 600, 130, 95);
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x + 45, 580, 120, 110);
            }

            // พื้นด้านล่าง
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(GRASS);
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 640, w, 30);
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(DARK);
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 638, w, 4);
            // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
            g2.setColor(DIRT);
            // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
            g2.fillRect(0, 670, w, Math.max(0, h - 670));

            // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
            g2.dispose();
        }

        // ประกาศเมธอด `drawBird()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        private void drawBird(Graphics2D g2, int x, int y, int width, int height) {
            // ตรวจเงื่อนไข `birdSprite == null`; ถ้าเป็นจริงจึงทำคำสั่งภายในบล็อกถัดไป
            if (birdSprite == null) {
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(255, 221, 64));
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x, y, width, height - 10);
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(Color.BLACK);
                // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
                g2.fillOval(x + width - 20, y + 12, 6, 6);
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
