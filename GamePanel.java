package birdgame.ui;

import birdgame.model.Bird;
import birdgame.model.Player;
import birdgame.service.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {

    private final Player player;
    private final GameEngine gameEngine;
    private final Runnable onLose;
    private final Runnable onWin;

    private final JLabel playerLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JLabel heartLabel = new JLabel();
    private final JLabel pipeLabel = new JLabel();

    private Timer gameTimer;
    private boolean turnEnded = false;

    private static final int BIRD_WIDTH = 50;
    private static final int BIRD_HEIGHT = 40;
    private static final int GROUND_HEIGHT = 70;

    public GamePanel(Player player, Runnable onLose, Runnable onWin) {
        this.player = player;
        this.gameEngine = new GameEngine(player);
        this.onLose = onLose;
        this.onWin = onWin;

        setFocusable(true);
        setLayout(new BorderLayout());
        setBackground(new Color(232, 247, 255));

        buildUI();
        bindSpaceBar();

        gameEngine.startGame();
        updateStatus();
        startGameLoop();
    }

    private void buildUI() {
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
        hud.add(playerLabel);
        hud.add(levelLabel);
        hud.add(heartLabel);
        hud.add(pipeLabel);

        JPanel gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int width = getWidth();
                int height = getHeight();

                // Background
                g2.setColor(new Color(160, 220, 255));
                g2.fillRect(0, 0, width, height);

                // Ground
                g2.setColor(new Color(80, 180, 90));
                g2.fillRect(0, height - GROUND_HEIGHT, width, GROUND_HEIGHT);

                // Bird
                Bird bird = gameEngine.getGameState().getBird();
                int birdX = bird.getX();
                int birdY = bird.getY();

                g2.setColor(Color.YELLOW);
                g2.fillOval(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);

                g2.setColor(Color.BLACK);
                g2.fillOval(birdX + 34, birdY + 9, 6, 6);

                g2.setColor(Color.ORANGE);
                int[] xPoints = { birdX + 47, birdX + 62, birdX + 47 };
                int[] yPoints = { birdY + 17, birdY + 22, birdY + 27 };
                g2.fillPolygon(xPoints, yPoints, 3);

                g2.setColor(Color.DARK_GRAY);
                g2.setFont(AppFont.bold(20));
                g2.drawString("กด SPACEBAR เพื่อให้นกบิน", 280, 55);

                g2.dispose();
            }
        };

        putClientProperty("gameArea", gameArea);

        JLabel help = new JLabel("SPACE = บินขึ้น", SwingConstants.CENTER);
        help.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        add(hud, BorderLayout.NORTH);
        add(gameArea, BorderLayout.CENTER);
        add(help, BorderLayout.SOUTH);
    }

    private void bindSpaceBar() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "birdJump");

        actionMap.put("birdJump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (turnEnded) return;

                Bird bird = gameEngine.getGameState().getBird();
                bird.jump();
                repaintGameArea();
            }
        });
    }

    private void startGameLoop() {
        gameTimer = new Timer(16, e -> updateGame());
        gameTimer.start();
    }

    private void updateGame() {
        if (turnEnded) return;

        Bird bird = gameEngine.getGameState().getBird();

        // Gravity + movement
        bird.update();

        // ไม่ให้นกทะลุขอบบน
        if (bird.getY() < 0) {
            bird.setY(0);
        }

        // ตรวจว่าตกถึงพื้นหรือยัง
        JPanel gameArea = getGameArea();

        if (gameArea != null) {
            int groundY = gameArea.getHeight() - GROUND_HEIGHT - BIRD_HEIGHT;

            if (bird.getY() >= groundY) {
                bird.setY(Math.max(0, groundY));
                gameOver();
            }
        }

        updateStatus();
        repaintGameArea();
    }

    private void gameOver() {
        if (turnEnded) return;

        turnEnded = true;

        if (gameTimer != null) {
            gameTimer.stop();
        }

        JOptionPane.showMessageDialog(
                this,
                player.getName() + " ตกถึงพื้น! จบรอบ"
        );

        onLose.run();
    }

    private void updateStatus() {
        playerLabel.setText("ผู้เล่น: " + player.getName());
        levelLabel.setText("ด่าน: " + player.getCurrentLevel());
        heartLabel.setText("หัวใจ: " + gameEngine.getHeartDisplay());
        pipeLabel.setText("ท่อสะสม: " + player.getTotalPipes());
    }

    private JPanel getGameArea() {
        Object value = getClientProperty("gameArea");

        if (value instanceof JPanel) {
            return (JPanel) value;
        }

        return null;
    }

    private void repaintGameArea() {
        JPanel gameArea = getGameArea();

        if (gameArea != null) {
            gameArea.repaint();
        } else {
            repaint();
        }
    }

    public void finishAtNest() {
        if (turnEnded) return;

        turnEnded = true;

        if (gameTimer != null) {
            gameTimer.stop();
        }

        onWin.run();
    }
}
