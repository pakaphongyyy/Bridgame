/*
 * โหลดรูปภาพหลักของเกมจากโฟลเดอร์ assets
 * โหลดไว้ครั้งเดียวแล้วให้ Bird / Pipe / Heart / Nest นำไปวาดต่อ
 * ถ้ารูปไม่ขึ้นทุกที่ ให้เช็ก path ในไฟล์นี้ก่อน
 */

package birdgame.model;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.net.URL;

final class Sprites {

    static final BufferedImage PIPE_BODY = load("pipe_body.png");

    static final BufferedImage PIPE_CAP = load("pipe_cap.png");

    static final BufferedImage HEART = load("heart.png");

    static final BufferedImage NEST = load("nest.png");

    static final BufferedImage BIRD = load("bird.png");

    private Sprites() {}

    // พยายามอ่านรูปจาก classpath ถ้าหาไม่เจอจะคืน null เพื่อให้เกมยังมี fallback แทนการ crash
    private static BufferedImage load(String name) {

        try {

            URL url = Sprites.class.getResource("/birdgame/assets/" + name);

            return url == null ? null : ImageIO.read(url);
        } catch (Exception e) {

            return null;
        }
    }
}
