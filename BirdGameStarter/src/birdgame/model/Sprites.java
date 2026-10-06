package birdgame.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/** โหลดภาพจาก src/birdgame/assets ครั้งเดียว (โหลดไม่ได้ = null → แต่ละคลาสวาดรูปทรงสำรองแทน) */
final class Sprites {
    static final BufferedImage PIPE_BODY = load("pipe_body.png");
    static final BufferedImage PIPE_CAP = load("pipe_cap.png");
    static final BufferedImage HEART = load("heart.png");
    static final BufferedImage NEST = load("nest.png");

    private Sprites() {}

    private static BufferedImage load(String name) {
        try {
            URL url = Sprites.class.getResource("/birdgame/assets/" + name);
            return url == null ? null : ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }
}
