package Functional;

import Graphics.BufferedImageLoader;
import Characters.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {
    private GamePanel gp;
    private Rectangle healthBar;
    private BufferedImageLoader loader;
    private BufferedImage[] gunIcons = new BufferedImage[4];
    private BufferedImage healthBarImage;
    private Vec2 iconPosition;
    private BufferedImage currGun;
    private Integer ammo;

    public HUD(GamePanel gp){
        this.gp = gp;
        healthBar = new Rectangle(gp.getMap().getX()-gp.getGameW()/2, gp.getMap().getY()-gp.getGameH()/2, gp.getGameW(), 10);
        loader = new BufferedImageLoader();
        gunIcons[0] = loader.loadImage("../res/hud/Pistol.png");
        gunIcons[1] = loader.loadImage("../res/hud/Shotgun.png");
        gunIcons[2] = loader.loadImage("../res/hud/AR.png");
        gunIcons[3] = loader.loadImage("../res/hud/GL.png");
        healthBarImage = loader.loadImage("../res/healthBar.png");
        iconPosition = new Vec2(gp.getMap().getX()-gp.getGameW()/2, gp.getMap().getY()-gp.getGameH()/2+20);
        currGun = gunIcons[0];
    }

    public void decreaseHealth(int size){
        healthBar.setSize((int) healthBar.getWidth() - size, 10);
    }

    public void setCurrGun(int i) {
        currGun = gunIcons[i];
    }

    public void setCurrAmmo(int i){
        ammo = i;
    }

    public void update(){
        healthBar.setRect(gp.getMap().getX()-gp.getGameW()/2, gp.getMap().getY()-gp.getGameH()/2, gp.getGameW(), 10);
        iconPosition.setV(new Vec2(gp.getMap().getX()-gp.getGameW()/2-10, gp.getMap().getY()-gp.getGameH()/2-20));
    }

    public void render(Graphics2D g){
        g.setColor(Color.RED);
        g.fill(healthBar);
        //g.drawImage(healthBarImage, gp.getMap().getX()-gp.getGameW()/2, gp.getMap().getY()-gp.getGameH()/2+100, 200, 20, null);
        g.drawImage(currGun, iconPosition.getX(), iconPosition.getY(), 100, 100, null);
        Font f = new Font("Arial", Font.BOLD, 20);
        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(ammo.toString(), iconPosition.getX()+90, iconPosition.getY()+60);
        //g.drawImage(healthBarImage, gp.getMap().getX()-gp.getGameW()/2+200, gp.getMap().getY()-gp.getGameH()/2+100, 50, 50, null);

    }
}
