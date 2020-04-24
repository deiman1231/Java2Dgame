package Functional;

import Characters.Entity;
import Characters.Player;
import Graphics.SpriteSheet;

import java.awt.*;

public class Body extends Entity {
    private int[] gunAmmo = new int[4];
    private boolean down = false;

    public Body(GamePanel gp, Vec2 v, SpriteSheet sprite, int size, int health, int[] gunAmmo, SpriteSheet explosion){
        super(gp, v, sprite, size, health, explosion);
        for(int i = 0; i < gunAmmo.length; i++){
            this.gunAmmo[i] = gunAmmo[i];
        }
    }



    public void update(Player player){
        super.updateOther();
        //System.out.println(gunAmmo[1]);
        if(hitBox.collide(player.getHitBox())){
            gunAmmo = player.getLeftGunsAmmo(gunAmmo);
        }
    }

    @Override
    public void render(Graphics2D g){
        //System.out.println(currFrame);
        if(currFrame == img.size()-1){
            down = true;
        }
        if(!down){
            g.drawImage(img.get(currFrame), v.getX(), v.getY(), sizeX, sizeY, null);
        }else{
            g.drawImage(img.get(img.size()-1), v.getX(), v.getY(), sizeX, sizeY, null);
        }
    }
}
