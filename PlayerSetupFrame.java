package birdgame.ui;

import birdgame.model.Player;
import birdgame.model.PlayerRecord;
import birdgame.service.FileStorageService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlayerSetupFrame extends JFrame {

    private final JTextField playerNameField = new JTextField();
    private final FileStorageService storageService = new FileStorageService();

    public PlayerSetupFrame() {
        setTitle("Bird Game - Player");
        setSize(720, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("เริ่มผู้เล่นคนใหม่");
        title.setFont(AppFont.bold(28));

        JLabel description = new JLabel(
            "เกมเล่นได้ทีละ 1 คน และจะเก็บประวัติผู้เล่นก่อนหน้าไว้"
        );

        JPanel namePanel = new JPanel(new BorderLayout(10, 10));
        namePanel.add(new JLabel("ชื่อผู้เล่น:"), BorderLayout.WEST);
        namePanel.add(playerNameField, BorderLayout.CENTER);

        JButton startButton = new JButton("เริ่มเกม");
        startButton.addActionListener(e -> startGame());

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(title);
        top.add(Box.createVerticalStrut(6));
        top.add(description);
        top.add(Box.createVerticalStrut(16));
        top.add(namePanel);
        top.add(Box.createVerticalStrut(10));
        top.add(startButton);

        JTable historyTable = createHistoryTable();

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        add(root);
    }

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
        table.setRowHeight(28);
        return table;
    }

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
}
