package birdgame.ui;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {

    public StartFrame() {
        setTitle("Bird Game");
        setSize(560, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("BIRD GAME");
        title.setFont(AppFont.bold(36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Java Swing");
        subtitle.setFont(AppFont.plain(18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("เริ่มเกม");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(220, 44));
        startButton.addActionListener(e -> startGame());

        JButton exitButton = new JButton("ออกจากเกม");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(220, 44));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);
        panel.add(Box.createVerticalGlue());
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(14));
        panel.add(exitButton);

        add(panel);
    }

    private void startGame() {
        dispose();
        new PlayerSetupFrame().setVisible(true);
    }
}
