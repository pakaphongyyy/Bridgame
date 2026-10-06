package birdgame.service;

import birdgame.model.Bird;
import birdgame.model.Heart;
import birdgame.model.Nest;
import birdgame.model.Pipe;

public class CollisionService {
    public boolean checkBirdGround(Bird bird, int panelHeight) {
        return bird.getY() < 0 || bird.getY() > panelHeight - 50;
    }

    // จะเติม hitbox ของ Pipe / Heart / Nest ในขั้นถัดไป
    public boolean checkBirdPipe(Bird bird, Pipe pipe) {
        return false;
    }

    public boolean checkBirdHeart(Bird bird, Heart heart) {
        return false;
    }

    public boolean checkBirdNest(Bird bird, Nest nest) {
        return false;
    }
}
