package birdgame.ui;

import birdgame.model.PlayerRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultFrame extends JFrame {

    public ResultFrame(List<PlayerRecord> records, String currentPlayerName) {
        setTitle("Bird Game - Player History");
        setSize(800, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI(records, currentPlayerName);
    }

    private void buildUI(List<PlayerRecord> records, String currentPlayerName) {
        JLabel title = new JLabel(
            "บันทึกผลของ " + currentPlayerName + " เรียบร้อยแล้ว",
            SwingConstants.CENTER
        );
        title.setFont(AppFont.bold(25));
        title.setBorder(BorderFactory.createEmptyBorder(16, 8, 12, 8));

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
        table.setRowHeight(30);

        JButton nextPlayer = new JButton("ผู้เล่นคนถัดไป");
        nextPlayer.addActionListener(e -> {
            dispose();
            new PlayerSetupFrame().setVisible(true);
        });

        JButton exit = new JButton("ออกจากเกม");
        exit.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel();
        buttons.add(nextPlayer);
        buttons.add(exit);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}
