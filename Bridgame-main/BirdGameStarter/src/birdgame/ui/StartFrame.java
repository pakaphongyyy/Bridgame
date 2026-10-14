/*
 * หน้าแรกของเกม
 * มีหน้าที่แสดงธีมเมนู Animation นก และปุ่ม START / EXIT
 * ไม่มี Game Logic อยู่ตรงนี้ เป็นเพียงหน้าทางเข้าก่อน PlayerSetupFrame
 */

package birdgame.ui;

import javax.imageio.ImageIO;

import javax.swing.*;

import javax.swing.border.LineBorder;

import java.awt.*;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import java.net.URL;

public class StartFrame extends JFrame {

    private BufferedImage birdSprite;

    private int birdFrame = 0;

    private int animationTick = 0;

    private int floatTick = 0;

    private GameMenuPanel menuPanel;

    // ตั้งค่าหน้าต่างหน้าแรก โหลด sprite สร้างเมนู และเริ่ม Animation นก
    public StartFrame() {

        setTitle("Bird Game");

        setSize(760, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        loadBirdSprite();

        buildUI();

        startMenuAnimation();
    }

    // โหลด sprite นกไว้ใช้ตกแต่งหน้าแรก ถ้าหาไฟล์ไม่เจอจะใช้รูป fallback
    private void loadBirdSprite() {

        try {

            URL url = StartFrame.class.getResource("/birdgame/assets/bird.png");

            if (url != null) {

                birdSprite = ImageIO.read(url);
            }
        } catch (Exception ignored) {

            birdSprite = null;
        }
    }

    // วางโลโก้ ปุ่ม START ปุ่ม EXIT และ component หลักของหน้า Start
    private void buildUI() {

        menuPanel = new GameMenuPanel();

        menuPanel.setLayout(null);

        JButton startButton = createGameButton("START", new Color(238, 238, 238));

        startButton.setBounds(215, 440, 330, 72);

        startButton.addActionListener(e -> startGame());

        JButton exitButton = createGameButton("EXIT", new Color(238, 238, 238));

        exitButton.setBounds(215, 525, 330, 72);

        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(startButton);

        menuPanel.add(exitButton);

        setContentPane(menuPanel);
    }

    // สร้างปุ่มสไตล์เดียวกันพร้อม hover เพื่อลดโค้ดตกแต่งซ้ำ
    private JButton createGameButton(String text, Color baseColor) {

        JButton button = new JButton(text);

        button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));

        button.setForeground(new Color(68, 49, 68));

        button.setBackground(baseColor);

        button.setFocusPainted(false);

        button.setBorder(new LineBorder(new Color(68, 49, 68), 5, false));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override

            public void mouseEntered(MouseEvent e) {

                button.setBackground(new Color(255, 245, 188));
            }

            @Override

            public void mouseExited(MouseEvent e) {

                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // Timer สำหรับสลับเฟรมนกและทำให้นกลอยขึ้นลงเบา ๆ บนหน้าเมนู
    private void startMenuAnimation() {

        new Timer(80, e -> {

            animationTick++;

            floatTick++;

            if (animationTick >= 2) {

                animationTick = 0;

                birdFrame = (birdFrame + 1) % 3;
            }

            if (menuPanel != null) {

                menuPanel.repaint();
            }

        }).start();
    }

    // ปิดหน้า Start แล้วเปิด PlayerSetupFrame
    private void startGame() {

        dispose();

        new PlayerSetupFrame().setVisible(true);
    }

    private class GameMenuPanel extends JPanel {

        GameMenuPanel() {

            setBackground(new Color(86, 191, 204));
        }

        @Override

        // วาดฉากหน้าเมนู เช่น ท้องฟ้า เมฆ หญ้า และตำแหน่งนก
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();

            int h = getHeight();

            g2.setColor(new Color(86, 191, 204));

            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(235, 255, 218));

            for (int x = -40; x < w + 80; x += 105) {

                g2.fillOval(x, 555, 130, 95);

                g2.fillOval(x + 45, 535, 120, 110);
            }

            g2.setColor(new Color(84, 215, 108));

            g2.fillRect(0, 600, w, 48);

            g2.setColor(new Color(228, 219, 151));

            g2.fillRect(0, 648, w, h - 648);

            g2.setColor(new Color(83, 65, 83));

            g2.fillRect(0, 598, w, 5);

            Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 66);

            g2.setFont(titleFont);

            String title = "BIRD GAME";

            FontMetrics fm = g2.getFontMetrics();

            int titleX = (w - fm.stringWidth(title)) / 2;

            int titleY = 125;

            g2.setColor(new Color(78, 57, 78));

            g2.drawString(title, titleX + 6, titleY + 7);

            g2.setColor(new Color(250, 250, 246));

            g2.drawString(title, titleX, titleY);

            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

            g2.setColor(new Color(78, 57, 78));

            String sub = "FLY • COLLECT HEARTS • REACH THE NEST";

            FontMetrics sfm = g2.getFontMetrics();

            g2.drawString(sub, (w - sfm.stringWidth(sub)) / 2, 165);

            int birdY = 255 + (int) (Math.sin(floatTick * 0.18) * 8);

            drawMenuBird(g2, w / 2 - 45, birdY, 90, 90);

            g2.setFont(AppFont.bold(18));

            g2.setColor(new Color(78, 57, 78));

            String hint = "Choose START to enter the game";

            FontMetrics hfm = g2.getFontMetrics();

            g2.drawString(hint, (w - hfm.stringWidth(hint)) / 2, 390);

            g2.dispose();
        }

        private void drawMenuBird(Graphics2D g2, int x, int y, int width, int height) {

            if (birdSprite == null) {

                g2.setColor(new Color(255, 221, 64));

                g2.fillOval(x, y, width, height - 15);

                g2.setColor(Color.BLACK);

                g2.fillOval(x + width - 28, y + 20, 8, 8);

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
