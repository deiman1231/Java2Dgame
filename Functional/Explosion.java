package Functional;

import java.awt.*;

import Characters.Entity;
import Characters.Player;
import Graphics.*;

public class Explosion extends Entity {

    boolean done;

    public Explosion(GamePanel gp, Vec2 v, SpriteSheet sprite, int size) {
        super(gp, v, sprite, size);
        this.gp = gp;
        this.v = v;
        this.sprite = sprite;
        done = false;
    }

    public void update(Player player) {
        super.updateOther();

    }

    @Override
    public void render(Graphics2D g) {
        if (currFrame == img.size() - 1) {
            done = true;
        }
        if (!done) {
            g.drawImage(img.get(currFrame), v.getX(), v.getY(), 32, 32, null);
        }
    }


}





//TODO f cloud on goal
//check walls?
//design level