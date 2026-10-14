/*
 * หน้ากรอกชื่อก่อนเริ่มเกม
 * แสดงประวัติผู้เล่นเก่า รับชื่อ และสร้าง Player ใหม่ก่อนส่งไป GameFrame
 * ถ้ามีเงื่อนไขเกี่ยวกับชื่อ เช่น ห้ามว่าง/ห้ามซ้ำ จุดแก้หลักจะอยู่ใน startGame()
 */

package birdgame.ui;

import birdgame.model.Player;

import birdgame.model.PlayerRecord;

import birdgame.service.FileStorageService;

import javax.imageio.ImageIO;

import javax.swing.*;

import javax.swing.border.LineBorder;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;

import javax.swing.table.JTableHeader;

import java.awt.*;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import java.net.URL;

import java.util.List;

public class PlayerSetupFrame extends JFrame {

    private final JTextField playerNameField = new JTextField();

    private final FileStorageService storageService = new FileStorageService();

    private BufferedImage birdSprite;

    private int birdFrame = 0;

    private int animationTick = 0;

    private int floatTick = 0;

    private PlayerThemePanel backgroundPanel;

    private static final Color SKY = new Color(86, 191, 204);

    private static final Color DARK = new Color(78, 57, 78);

    private static final Color CREAM = new Color(250, 250, 246);

    private static final Color GRASS = new Color(84, 215, 108);

    private static final Color DIRT = new Color(228, 219, 151);

    private static final Color HOVER = new Color(255, 245, 188);

    // สร้างหน้า Player Setup โหลดรูปนก สร้าง UI และให้ช่องชื่อพร้อมพิมพ์ทันที
    public PlayerSetupFrame() {

        setTitle("Bird Game - Player");

        setSize(760, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        loadBirdSprite();

        buildUI();

        startAnimation();

        SwingUtilities.invokeLater(playerNameField::requestFocusInWindow);
    }

    // โหลด bird.png สำหรับ Animation ในหน้าเมนู ถ้าโหลดไม่ได้จะใช้ภาพ fallback แทน
    private void loadBirdSprite() {

        try {

            URL url = PlayerSetupFrame.class.getResource("/birdgame/assets/bird.png");

            if (url != null) {

                birdSprite = ImageIO.read(url);
            }
        } catch (Exception ignored) {

            birdSprite = null;
        }
    }

    // จัดวางหัวข้อ ช่องชื่อ ตารางประวัติ และปุ่ม START GAME
    private void buildUI() {

        backgroundPanel = new PlayerThemePanel();

        backgroundPanel.setLayout(null);

        JLabel title = new JLabel("PLAYER SETUP", SwingConstants.CENTER);

        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 42));

        title.setForeground(CREAM);

        title.setBounds(160, 42, 440, 56);

        backgroundPanel.add(title);

        JLabel subtitle = new JLabel(
                "ใส่ชื่อผู้เล่น แล้วกด START GAME",
                SwingConstants.CENTER
        );

        subtitle.setFont(AppFont.bold(18));

        subtitle.setForeground(DARK);

        subtitle.setBounds(145, 105, 470, 32);

        backgroundPanel.add(subtitle);

        JPanel nameCard = new JPanel(null);

        nameCard.setBackground(new Color(250, 250, 246));

        nameCard.setBorder(new LineBorder(DARK, 5));

        nameCard.setBounds(125, 160, 510, 120);

        JLabel nameLabel = new JLabel("PLAYER NAME");

        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        nameLabel.setForeground(DARK);

        nameLabel.setBounds(25, 18, 200, 30);

        nameCard.add(nameLabel);

        playerNameField.setFont(AppFont.bold(20));

        playerNameField.setForeground(DARK);

        playerNameField.setBackground(Color.WHITE);

        playerNameField.setCaretColor(DARK);

        playerNameField.setBorder(new LineBorder(DARK, 3));

        playerNameField.setBounds(25, 55, 460, 42);

        // กด Enter ในช่องชื่อให้ใช้เส้นทางเดียวกับการกดปุ่ม START จึงตรวจเงื่อนไขชุดเดียวกัน
        playerNameField.addActionListener(e -> startGame());

        nameCard.add(playerNameField);

        backgroundPanel.add(nameCard);

        JLabel historyTitle = new JLabel("PLAYER HISTORY", SwingConstants.CENTER);

        historyTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));

        historyTitle.setForeground(DARK);

        historyTitle.setBounds(215, 300, 330, 34);

        backgroundPanel.add(historyTitle);

        JTable historyTable = createHistoryTable();

        JScrollPane scrollPane = new JScrollPane(historyTable);

        scrollPane.setBorder(new LineBorder(DARK, 5));

        scrollPane.getViewport().setBackground(new Color(250, 250, 246));

        scrollPane.setBounds(60, 340, 640, 205);

        backgroundPanel.add(scrollPane);

        JButton startButton = createGameButton("START GAME", new Color(238, 238, 238));

        startButton.setBounds(210, 575, 340, 64);

        startButton.addActionListener(e -> startGame());

        backgroundPanel.add(startButton);

        setContentPane(backgroundPanel);
    }

    private JButton createGameButton(String text, Color baseColor) {

        JButton button = new JButton(text);

        button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));

        button.setForeground(DARK);

        button.setBackground(baseColor);

        button.setFocusPainted(false);

        button.setBorder(new LineBorder(DARK, 5));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override

            public void mouseEntered(MouseEvent e) {

                button.setBackground(HOVER);
            }

            @Override

            public void mouseExited(MouseEvent e) {

                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // โหลดประวัติจาก CSV แล้วสร้าง JTable โดยแสดงรายการล่าสุดไว้ด้านบน
    private JTable createHistoryTable() {

        String[] columns = {
                "ลำดับ",
                "ชื่อผู้เล่น",
                "ผล",
                "ด่านสูงสุด",
                "ท่อสะสม",
                "เวลาเล่น"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override

            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        // ประวัติในตารางโหลดจากไฟล์จริงทุกครั้งที่เปิดหน้า ไม่ได้เก็บไว้แค่ในหน่วยความจำ
        List<PlayerRecord> records = storageService.loadRecords();

        for (int i = records.size() - 1; i >= 0; i--) {

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

        JTable table = new JTable(model);

        table.setFont(AppFont.plain(14));

        table.setForeground(DARK);

        table.setBackground(new Color(250, 250, 246));

        table.setGridColor(new Color(180, 170, 180));

        table.setSelectionBackground(new Color(255, 245, 188));

        table.setSelectionForeground(DARK);

        table.setRowHeight(29);

        table.setShowVerticalLines(true);

        table.setShowHorizontalLines(true);

        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();

        header.setFont(AppFont.bold(14));

        header.setForeground(CREAM);

        header.setBackground(DARK);

        header.setReorderingAllowed(false);

        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {

            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(45);

        table.getColumnModel().getColumn(1).setPreferredWidth(105);

        table.getColumnModel().getColumn(2).setPreferredWidth(105);

        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        table.getColumnModel().getColumn(4).setPreferredWidth(75);

        table.getColumnModel().getColumn(5).setPreferredWidth(145);

        return table;
    }

    // Timer นี้มีไว้ขยับ Animation ของนกในหน้า Setup เท่านั้น ไม่เกี่ยวกับ Game Loop ตอนเล่น
    private void startAnimation() {

        new Timer(80, e -> {

            animationTick++;

            floatTick++;

            if (animationTick >= 2) {

                animationTick = 0;

                birdFrame = (birdFrame + 1) % 3;
            }

            if (backgroundPanel != null) {

                backgroundPanel.repaint();
            }

        }).start();
    }

    // ตรวจชื่อเบื้องต้นก่อนสร้าง Player ใหม่ ถ้าชื่อว่างจะหยุดอยู่หน้าเดิม
    private void startGame() {

        String name = playerNameField.getText().trim();

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "กรุณาใส่ชื่อผู้เล่นก่อนเริ่มเกม"
            );

            playerNameField.requestFocusInWindow();

            return;
        }

        Player player = new Player(1, name);

        dispose();

        new GameFrame(player).setVisible(true);
    }

    private class PlayerThemePanel extends JPanel {

        PlayerThemePanel() {

            setBackground(SKY);
        }

        @Override

        // วาดธีมพื้นหลังของหน้า Setup เช่น ท้องฟ้า เมฆ หญ้า และนกตกแต่ง
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();

            int h = getHeight();

            g2.setColor(SKY);

            g2.fillRect(0, 0, w, h);

            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 42));

            FontMetrics titleFm = g2.getFontMetrics();

            String title = "PLAYER SETUP";

            int titleX = (w - titleFm.stringWidth(title)) / 2;

            g2.setColor(DARK);

            g2.drawString(title, titleX + 4, 84 + 5);

            int birdY = 84 + (int) (Math.sin(floatTick * 0.18) * 5);

            drawBird(g2, 625, birdY - 34, 64, 54);

            g2.setColor(new Color(235, 255, 218));

            for (int x = -40; x < w + 80; x += 105) {

                g2.fillOval(x, 600, 130, 95);

                g2.fillOval(x + 45, 580, 120, 110);
            }

            g2.setColor(GRASS);

            g2.fillRect(0, 640, w, 30);

            g2.setColor(DARK);

            g2.fillRect(0, 638, w, 4);

            g2.setColor(DIRT);

            g2.fillRect(0, 670, w, Math.max(0, h - 670));

            g2.dispose();
        }

        private void drawBird(Graphics2D g2, int x, int y, int width, int height) {

            if (birdSprite == null) {

                g2.setColor(new Color(255, 221, 64));

                g2.fillOval(x, y, width, height - 10);

                g2.setColor(Color.BLACK);

                g2.fillOval(x + width - 20, y + 12, 6, 6);

                return;
            }

            int[] frameX = {81, 480, 1028};

            int[] frameWidth = {312, 462, 313};

            int sx = frameX[birdFrame];

            int sw = frameWidth[birdFrame];

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
