package birdgame.ui;

import birdgame.model.PlayerRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultFrame extends JFrame {

    private static final Color SKY = new Color(110, 205, 235);
    private static final Color SKY_DARK = new Color(67, 179, 214);
    private static final Color GRASS = new Color(86, 205, 95);
    private static final Color GRASS_DARK = new Color(52, 165, 70);
    private static final Color PANEL = new Color(255, 252, 239);
    private static final Color BORDER = new Color(74, 63, 78);
    private static final Color YELLOW = new Color(255, 190, 45);
    private static final Color GREEN = new Color(45, 180, 89);
    private static final Color RED = new Color(244, 111, 86);

    public ResultFrame(List<PlayerRecord> records, String currentPlayerName) {
        setTitle("Bird Game - Player History");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI(records, currentPlayerName);
    }

    private void buildUI(List<PlayerRecord> records, String currentPlayerName) {

        JPanel background = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int w = getWidth();
                int h = getHeight();

                // ท้องฟ้า
                g2.setColor(SKY);
                g2.fillRect(0, 0, w, h);

                // เมฆ
                drawCloud(g2, 90, 85, 1.0);
                drawCloud(g2, 620, 115, 0.8);
                drawCloud(g2, 730, 50, 0.55);

                // หญ้า
                int grassY = h - 95;
                g2.setColor(GRASS);
                g2.fillRect(0, grassY, w, 95);

                g2.setColor(GRASS_DARK);
                for (int x = 0; x < w; x += 32) {
                    int[] xs = {x, x + 12, x + 24};
                    int[] ys = {grassY + 18, grassY, grassY + 18};
                    g2.fillPolygon(xs, ys, 3);
                }

                // เส้นพื้น
                g2.setColor(new Color(213, 198, 100));
                g2.fillRect(0, h - 28, w, 28);

                g2.dispose();
            }
        };

        background.setBorder(
                BorderFactory.createEmptyBorder(22, 28, 22, 28)
        );

        // ===== Header =====
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel mainTitle = new JLabel("PLAYER HISTORY");
        mainTitle.setFont(AppFont.bold(34));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(
                "บันทึกผลของ " + currentPlayerName + " เรียบร้อยแล้ว"
        );
        title.setFont(AppFont.bold(22));
        title.setForeground(BORDER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titleCard = new JPanel();
        titleCard.setBackground(PANEL);
        titleCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 4),
                        BorderFactory.createEmptyBorder(10, 22, 10, 22)
                )
        );
        titleCard.add(title);
        titleCard.setMaximumSize(new Dimension(620, 58));
        titleCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(mainTitle);
        header.add(Box.createVerticalStrut(12));
        header.add(titleCard);
        header.add(Box.createVerticalStrut(16));

        // ===== Table =====
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

        for (int i = records.size() - 1; i >= 0; i--) {
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

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(AppFont.plain(15));
        table.setForeground(BORDER);
        table.setBackground(new Color(255, 255, 250));
        table.setGridColor(new Color(190, 205, 205));
        table.setSelectionBackground(new Color(210, 243, 220));
        table.setSelectionForeground(BORDER);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(AppFont.bold(15));
        table.getTableHeader().setBackground(SKY_DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(
                new Dimension(table.getTableHeader().getWidth(), 38)
        );

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER, 4)
        );
        scrollPane.getViewport().setBackground(
                new Color(255, 255, 250)
        );

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(PANEL);
        tableCard.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons =====
        JButton nextPlayer = createGameButton(
                "NEXT PLAYER",
                GREEN
        );
        nextPlayer.addActionListener(e -> {
            dispose();
            new PlayerSetupFrame().setVisible(true);
        });

        JButton exit = createGameButton(
                "EXIT GAME",
                RED
        );
        exit.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                28,
                10
        ));
        buttons.setOpaque(false);
        buttons.add(nextPlayer);
        buttons.add(exit);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(header, BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        background.add(content, BorderLayout.CENTER);

        setContentPane(background);
    }

    private JButton createGameButton(String text, Color color) {
        JButton button = new JButton(text);

        button.setFont(AppFont.bold(17));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        button.setPreferredSize(new Dimension(190, 48));

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 4),
                        BorderFactory.createEmptyBorder(4, 12, 4, 12)
                )
        );

        return button;
    }

    private void drawCloud(
            Graphics2D g2,
            int x,
            int y,
            double scale
    ) {
        int s = (int) (60 * scale);

        g2.setColor(new Color(246, 255, 242, 230));
        g2.fillOval(x, y + s / 3, s, s / 2);
        g2.fillOval(x + s / 3, y, s, s);
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
