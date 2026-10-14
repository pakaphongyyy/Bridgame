/*
 * ============================================================
 * ไฟล์: ResultFrame.java
 * หน้าที่หลัก: หน้าสรุปผล/ประวัติ
 *
 * ภาพรวม:
 * แสดงประวัติการเล่นใน JTable พร้อมปุ่มเล่นคนถัดไปและออกจากเกม โดยใช้ธีมเดียวกับหน้าอื่น
 *
 * หมายเหตุสำหรับคนอ่านโค้ด:
 * - คอมเมนต์ภาษาไทยในไฟล์นี้มีไว้เพื่ออธิบายว่าแต่ละส่วนทำอะไรและทำไมต้องมี
 * - คอมเมนต์ไม่ได้เปลี่ยน Game Logic เดิมของโปรเจกต์
 * - ถ้าจะปรับโค้ด ให้ดูความสัมพันธ์กับ Model / Service / UI ตามที่อธิบายไว้ก่อนแก้
 * ============================================================
 */

// กำหนดว่าไฟล์นี้อยู่ในแพ็กเกจ `birdgame.ui` เพื่อจัดโครงสร้างคลาสให้เป็นหมวดหมู่
package birdgame.ui;

// นำเข้า PlayerRecord เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import birdgame.model.PlayerRecord;

// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.*;
// นำเข้า DefaultTableCellRenderer เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.table.DefaultTableCellRenderer;
// นำเข้า DefaultTableModel เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import javax.swing.table.DefaultTableModel;
// นำเข้า คลาสทั้งหมดในแพ็กเกจนี้ เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.awt.*;
// นำเข้า List เพื่อให้ไฟล์นี้เรียกใช้คลาสดังกล่าวได้โดยไม่ต้องเขียนชื่อแพ็กเกจเต็ม
import java.util.List;

// ประกาศคลาส `ResultFrame` ซึ่งเป็นหน่วยหลักที่รวมข้อมูล (fields) และพฤติกรรม (methods) ของส่วนนี้
public class ResultFrame extends JFrame {

    // ประกาศค่าคงที่ `SKY` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color SKY = new Color(110, 205, 235);
    // ประกาศค่าคงที่ `SKY_DARK` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color SKY_DARK = new Color(67, 179, 214);
    // ประกาศค่าคงที่ `GRASS` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color GRASS = new Color(86, 205, 95);
    // ประกาศค่าคงที่ `GRASS_DARK` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color GRASS_DARK = new Color(52, 165, 70);
    // ประกาศค่าคงที่ `PANEL` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color PANEL = new Color(255, 252, 239);
    // ประกาศค่าคงที่ `BORDER` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color BORDER = new Color(74, 63, 78);
    // ประกาศค่าคงที่ `YELLOW` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color YELLOW = new Color(255, 190, 45);
    // ประกาศค่าคงที่ `GREEN` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color GREEN = new Color(45, 180, 89);
    // ประกาศค่าคงที่ `RED` ใช้ร่วมกันในคลาส และไม่ควรถูกเปลี่ยนค่าหลังประกาศ
    private static final Color RED = new Color(244, 111, 86);

    // Constructor ของ `ResultFrame` ทำงานเมื่อสร้างอ็อบเจ็กต์ใหม่ ใช้กำหนดค่าเริ่มต้นและเตรียมส่วนที่จำเป็น
    public ResultFrame(List<PlayerRecord> records, String currentPlayerName) {
        // เรียก `setTitle(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setTitle("Bird Game - Player History");
        // เรียก `setSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setSize(900, 650);
        // เรียก `setDefaultCloseOperation(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // เรียก `setLocationRelativeTo(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setLocationRelativeTo(null);
        // เรียก `setResizable(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setResizable(false);

        // เรียก `buildUI(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        buildUI(records, currentPlayerName);
    }

    // สร้างและจัดวางองค์ประกอบ GUI ของหน้าจอนี้
    private void buildUI(List<PlayerRecord> records, String currentPlayerName) {

        // ประกาศเมธอด `JPanel()` เพื่อรวมขั้นตอนการทำงานส่วนนี้ไว้เรียกใช้ซ้ำ
        JPanel background = new JPanel(new BorderLayout()) {
            @Override
            // เมธอดที่ Swing เรียกทุกครั้งที่ต้องวาด JPanel ใหม่
            protected void paintComponent(Graphics g) {
                // เรียก `super.paintComponent(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                super.paintComponent(g);

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `Graphics2D g2` เพื่อใช้ต่อในขั้นตอนถัดไป
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int w` เพื่อใช้ต่อในขั้นตอนถัดไป
                int w = getWidth();
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int h` เพื่อใช้ต่อในขั้นตอนถัดไป
                int h = getHeight();

                // ท้องฟ้า
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(SKY);
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, 0, w, h);

                // เมฆ
                // เรียก `drawCloud(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawCloud(g2, 90, 85, 1.0);
                // เรียก `drawCloud(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawCloud(g2, 620, 115, 0.8);
                // เรียก `drawCloud(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                drawCloud(g2, 730, 50, 0.55);

                // หญ้า
                // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int grassY` เพื่อใช้ต่อในขั้นตอนถัดไป
                int grassY = h - 95;
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(GRASS);
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, grassY, w, 95);

                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(GRASS_DARK);
                // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
                for (int x = 0; x < w; x += 32) {
                    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int[] xs` เพื่อใช้ต่อในขั้นตอนถัดไป
                    int[] xs = {x, x + 12, x + 24};
                    // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int[] ys` เพื่อใช้ต่อในขั้นตอนถัดไป
                    int[] ys = {grassY + 18, grassY, grassY + 18};
                    // เรียก `g2.fillPolygon(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    g2.fillPolygon(xs, ys, 3);
                }

                // เส้นพื้น
                // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
                g2.setColor(new Color(213, 198, 100));
                // วาดสี่เหลี่ยมทึบ ใช้ทำพื้นหลัง/พื้น/แถบสี
                g2.fillRect(0, h - 28, w, 28);

                // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
                g2.dispose();
            }
        };

        // กำหนดกรอบและระยะขอบของ Swing component
        background.setBorder(
                BorderFactory.createEmptyBorder(22, 28, 22, 28)
        );

        // ===== Header =====
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel header` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel header = new JPanel();
        // เรียก `header.setOpaque(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        header.setOpaque(false);
        // กำหนด Layout Manager เพื่อควบคุมวิธีจัดวาง component ภายใน container
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel mainTitle` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel mainTitle = new JLabel("PLAYER HISTORY");
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        mainTitle.setFont(AppFont.bold(34));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        mainTitle.setForeground(Color.WHITE);
        // เรียก `mainTitle.setAlignmentX(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JLabel title` เพื่อใช้ต่อในขั้นตอนถัดไป
        JLabel title = new JLabel(
                "บันทึกผลของ " + currentPlayerName + " เรียบร้อยแล้ว"
        );
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        title.setFont(AppFont.bold(22));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        title.setForeground(BORDER);
        // เรียก `title.setAlignmentX(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel titleCard` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel titleCard = new JPanel();
        // กำหนดสีพื้นหลังของ Swing component
        titleCard.setBackground(PANEL);
        // กำหนดกรอบและระยะขอบของ Swing component
        titleCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 4),
                        BorderFactory.createEmptyBorder(10, 22, 10, 22)
                )
        );
        // เรียก `titleCard.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        titleCard.add(title);
        // เรียก `titleCard.setMaximumSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        titleCard.setMaximumSize(new Dimension(620, 58));
        // เรียก `titleCard.setAlignmentX(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        titleCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        header.add(mainTitle);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        header.add(Box.createVerticalStrut(12));
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        header.add(titleCard);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        header.add(Box.createVerticalStrut(16));

        // ===== Table =====
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

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int i = records.size() - 1; i >= 0; i--) {
            // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `PlayerRecord entry` เพื่อใช้ต่อในขั้นตอนถัดไป
            PlayerRecord entry = records.get(i);

            model.addRow(new Object[] {
                    i + 1,
                    entry.getPlayerName(),
                    entry.isFinished() ? "ถึงรัง" : "ยังไม่ถึงรัง",
                    entry.getHighestLevel(),
                    entry.getTotalPipes(),
                    entry.getPlayedAt()
            });
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JTable table` เพื่อใช้ต่อในขั้นตอนถัดไป
        JTable table = new JTable(model);
        // เรียก `table.setRowHeight(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setRowHeight(34);
        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        table.setFont(AppFont.plain(15));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        table.setForeground(BORDER);
        // กำหนดสีพื้นหลังของ Swing component
        table.setBackground(new Color(255, 255, 250));
        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        table.setGridColor(new Color(190, 205, 205));
        // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
        table.setSelectionBackground(new Color(210, 243, 220));
        // เรียก `table.setSelectionForeground(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setSelectionForeground(BORDER);
        // เรียก `table.setFillsViewportHeight(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        table.setFillsViewportHeight(true);

        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        table.getTableHeader().setFont(AppFont.bold(15));
        // กำหนดสีพื้นหลังของ Swing component
        table.getTableHeader().setBackground(SKY_DARK);
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(
                new Dimension(table.getTableHeader().getWidth(), 38)
        );

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `DefaultTableCellRenderer centerRenderer` เพื่อใช้ต่อในขั้นตอนถัดไป
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        // เรียก `centerRenderer.setHorizontalAlignment(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // วนลูปผ่านข้อมูลตามเงื่อนไขใน for เพื่อประมวลผลสมาชิกทีละตัว
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    // เรียก `setCellRenderer(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
                    .setCellRenderer(centerRenderer);
        }

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JScrollPane scrollPane` เพื่อใช้ต่อในขั้นตอนถัดไป
        JScrollPane scrollPane = new JScrollPane(table);
        // กำหนดกรอบและระยะขอบของ Swing component
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER, 4)
        );
        // กำหนดสีพื้นหลังของ Swing component
        scrollPane.getViewport().setBackground(
                // สร้างค่าสี RGB/RGBA ใหม่สำหรับใช้กับ GUI หรือกราฟิก
                new Color(255, 255, 250)
        );

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel tableCard` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel tableCard = new JPanel(new BorderLayout());
        // กำหนดสีพื้นหลังของ Swing component
        tableCard.setBackground(PANEL);
        // กำหนดกรอบและระยะขอบของ Swing component
        tableCard.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
        // เรียก `tableCard.add(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons =====
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JButton nextPlayer` เพื่อใช้ต่อในขั้นตอนถัดไป
        JButton nextPlayer = createGameButton(
                "NEXT PLAYER",
                GREEN
        );
        // ผูกเหตุการณ์คลิกปุ่มกับโค้ดที่จะทำเมื่อผู้ใช้กดปุ่ม
        nextPlayer.addActionListener(e -> {
            // ปิดและปล่อย resource ของหน้าต่างปัจจุบัน ก่อนเปิดหน้าถัดไป
            dispose();
            // ทำให้หน้าต่างหรือ component นี้แสดงบนหน้าจอ
            new PlayerSetupFrame().setVisible(true);
        });

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JButton exit` เพื่อใช้ต่อในขั้นตอนถัดไป
        JButton exit = createGameButton(
                "EXIT GAME",
                RED
        );
        // จบโปรแกรม Java ทั้งหมดด้วยสถานะปกติ (exit code 0)
        exit.addActionListener(e -> System.exit(0));

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel buttons` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel buttons = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                28,
                10
        // คำสั่งนี้เปลี่ยน/กำหนดสถานะบางอย่างของโปรแกรมตามค่าที่ระบุ
        ));
        // เรียก `buttons.setOpaque(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        buttons.setOpaque(false);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        buttons.add(nextPlayer);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        buttons.add(exit);

        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JPanel content` เพื่อใช้ต่อในขั้นตอนถัดไป
        JPanel content = new JPanel(new BorderLayout(0, 12));
        // เรียก `content.setOpaque(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        content.setOpaque(false);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        content.add(header, BorderLayout.NORTH);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        content.add(tableCard, BorderLayout.CENTER);
        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        content.add(buttons, BorderLayout.SOUTH);

        // เพิ่มอ็อบเจ็กต์/Component นี้เข้า collection หรือ container เพื่อให้ถูกจัดเก็บหรือแสดงผล
        background.add(content, BorderLayout.CENTER);

        // เรียก `setContentPane(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        setContentPane(background);
    }

    // สร้าง JButton ที่ตั้งค่าสี ฟอนต์ กรอบ และพฤติกรรม hover ให้เหมือนกัน
    private JButton createGameButton(String text, Color color) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `JButton button` เพื่อใช้ต่อในขั้นตอนถัดไป
        JButton button = new JButton(text);

        // กำหนดฟอนต์และขนาดตัวอักษรสำหรับข้อความที่จะวาดหรือ component นี้
        button.setFont(AppFont.bold(17));
        // กำหนดสีตัวอักษร/ส่วนหน้าของ Swing component
        button.setForeground(Color.WHITE);
        // กำหนดสีพื้นหลังของ Swing component
        button.setBackground(color);
        // เรียก `button.setFocusPainted(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setFocusPainted(false);
        // เรียก `button.setBorderPainted(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setBorderPainted(true);
        // เรียก `button.setOpaque(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setOpaque(true);
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        // เรียก `button.setPreferredSize(...)` เพื่อให้ขั้นตอนย่อยนี้ทำงาน
        button.setPreferredSize(new Dimension(190, 48));

        // กำหนดกรอบและระยะขอบของ Swing component
        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 4),
                        BorderFactory.createEmptyBorder(4, 12, 4, 12)
                )
        );

        // ส่งค่า `button` กลับไปยังผู้เรียกเมธอด แล้วจบการทำงานของเมธอดนี้
        return button;
    }

    private void drawCloud(
            Graphics2D g2,
            int x,
            int y,
            double scale
    ) {
        // คำนวณหรือสร้างค่าทางขวา แล้วเก็บผลไว้ใน `int s` เพื่อใช้ต่อในขั้นตอนถัดไป
        int s = (int) (60 * scale);

        // กำหนดสีที่จะใช้วาดคำสั่งกราฟิกถัดไป
        g2.setColor(new Color(246, 255, 242, 230));
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x, y + s / 3, s, s / 2);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x + s / 3, y, s, s);
        // วาดวงรีทึบ ใช้ประกอบฉาก เช่น เมฆ ดวงอาทิตย์ หรือกราฟิกสำรอง
        g2.fillOval(x + s, y + s / 4, s, (int) (s * 0.7));
        g2.fillRoundRect(
                x + s / 4,
                y + s / 2,
                (int) (s * 1.6),
                s / 2,
                s / 3,
                s / 3
        );
    }
}
