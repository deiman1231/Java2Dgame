package Functional;

import Characters.Entity;
import Graphics.SpriteSheet;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelGoal extends Entity {

    private boolean onGoal = false;
    private Input input;
    private boolean levelCompleted = false;
    private SpriteSheet fCloud;
    private BufferedImage img2;


    public LevelGoal(GamePanel gp, Vec2 v, SpriteSheet sprite, int size, SpriteSheet fCloud) {
        super(gp, v, sprite, size);
        input = new Input(gp);
        this.fCloud = fCloud;
        img2 = fCloud.getImage();
    }

    public void update(HitBox b){
        super.updateOther();
        if(hitBox.collide(b)){
            onGoal = true;
            if(input.keyDown(KeyEvent.VK_F)){
                levelCompleted = true;
            }
        } else {
            onGoal = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        //System.out.println(currFrame);
        g.drawImage(img.get(currFrame), v.getX(), v.getY(), sizeX, sizeY, null);
        if (onGoal){
            g.drawImage(img2, v.getX()+sizeX/2, v.getY()+sizeY/2 + 100, 80, 40, null);
        }
    }

    public boolean isLevelCompleted(){
        return levelCompleted;
    }

    public void setLevelCompleted(boolean b) {
        levelCompleted = b;
    }
}
