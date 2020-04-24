package Graphics;

import Functional.Vec2;
import Functional.Input;
import Functional.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import Characters.Player;

import javax.swing.*;

public class SkillTree{
    private int templateX;
    private int templateY;
    private int templateW;
    private int templateH;
    private int totalPoints = 5;
    private Integer speedPoints = 0;
    private Integer healthPoints = 0;
    private Integer ammoPoints = 0;
    private BufferedImage template;
    private BufferedImage speedIcon;
    private BufferedImage healthIcon;
    private BufferedImage ammoIcon;
    private BufferedImage emptyBox;
    private BufferedImageLoader loader;
    private boolean pressed = false;
    private Font font;
    private Vec2 map;
    private Input input;
    private Player player;
    private GamePanel gp;

    public SkillTree(GamePanel gp, Player player, Vec2 map){
        loader = new BufferedImageLoader();
        template = loader.loadImage("../res/template.png");
        speedIcon = loader.loadImage("../res/run.jpg");
        healthIcon = loader.loadImage("../res/health.png");
        ammoIcon = loader.loadImage("../res/ammo.png");
        emptyBox = loader.loadImage("../res/emptyBox.png");
        font = new Font("arial", 1, 50);
        input = new Input(gp);
        this.gp = gp;
        this.player = player;
        this.map = map;
        templateW = 600;
        templateH = 450;
    }

    public void buttonFunctions(){
        if(input.keyDown(KeyEvent.VK_Q) && !pressed){
            speedPoints++;
            totalPoints--;
            player.incrementMaxSpeed(0.2f);
            pressed = true;
        }
        if(input.keyDown(KeyEvent.VK_W) && !pressed){
            totalPoints--;
            healthPoints++;
            player.incrementHealth(5);
            pressed = true;
        }
        if(input.keyDown(KeyEvent.VK_E) && !pressed){
            totalPoints--;
            ammoPoints++;
            player.incrementMaxGunAmmo(Player.GunType.pistol, 5);
            player.incrementMaxGunAmmo(Player.GunType.shotGun, 5);
            player.incrementMaxGunAmmo(Player.GunType.M4, 10);
            player.incrementMaxGunAmmo(Player.GunType.grenadeL, 1);
            pressed = true;
        }
        if(!input.keyDown(KeyEvent.VK_Q) && !input.keyDown(KeyEvent.VK_W) && !input.keyDown(KeyEvent.VK_E)){
            pressed = false;
        }
    }

    public void setPoints(int i){
        totalPoints = i;
    }

    public int getPoints(){
        return totalPoints;
    }

    public void update(){
        buttonFunctions();
        templateX = map.getX() - 300;
        templateY = map.getY() - 250;
        System.out.println(totalPoints);
        if(totalPoints == 0){
            gp.setGameState(GamePanel.GameState.Play);
        }
    }

    public void render(Graphics2D g){
        g.drawImage(template, templateX, templateY, templateW, templateH, null);
        g.drawImage(emptyBox, templateX + templateW/3-155, templateY + 45, 110, 110, null);
        g.drawImage(emptyBox, templateX + 2*templateW/3-155, templateY + 45, 110, 110, null);
        g.drawImage(emptyBox, templateX + 3*templateW/3-155, templateY + 45, 110, 110, null);
        g.drawImage(speedIcon, templateX + templateW/3-150, templateY + 50, 100, 100, null);
        g.drawImage(healthIcon, templateX + 2*templateW/3-150, templateY + 50, 100, 100, null);
        g.drawImage(ammoIcon, templateX + 3*templateW/3-150, templateY + 50, 100, 100, null);
        g.drawImage(emptyBox, templateX + templateW/3-137, templateY + 185, 75, 75, null);
        g.drawImage(emptyBox, templateX + 2*templateW/3-137, templateY + 185, 75, 75, null);
        g.drawImage(emptyBox, templateX + 3*templateW/3-137, templateY + 185, 75, 75, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        if(speedPoints > 9){
            g.drawString(speedPoints.toString(), templateX + templateW/3-127, templateY + 240);
        }else{
            g.drawString(speedPoints.toString(), templateX + templateW/3-112, templateY + 240);
        }
        if(healthPoints > 9){
            g.drawString(healthPoints.toString(), templateX + 2*templateW/3-127, templateY + 240);
        }else{
            g.drawString(healthPoints.toString(), templateX + 2*templateW/3-112, templateY + 240);
        }
        if(ammoPoints > 9){
            g.drawString(ammoPoints.toString(), templateX + 3*templateW/3-127, templateY + 240);
        }else{
            g.drawString(ammoPoints.toString(), templateX + 3*templateW/3-112, templateY + 240);
        }
        if(input.keyDown(KeyEvent.VK_Q) ){
            g.setColor(Color.RED);
            g.drawString("Q", templateX + templateW/3-120, templateY + 350);
        }else{
            g.setColor(Color.WHITE);
            g.drawString("Q", templateX + templateW/3-120, templateY + 350);
        }
        if(input.keyDown(KeyEvent.VK_W) ){
            g.setColor(Color.RED);
            g.drawString("W", templateX + 2*templateW/3-125, templateY + 350);
        }else{
            g.setColor(Color.WHITE);
            g.drawString("W", templateX + 2*templateW/3-125, templateY + 350);
        }
        if(input.keyDown(KeyEvent.VK_E) ){
            g.setColor(Color.RED);
            g.drawString("E", templateX + 3*templateW/3-115, templateY + 350);
        }else{
            g.setColor(Color.WHITE);
            g.drawString("E", templateX + 3*templateW/3-115, templateY + 350);
        }
    }
}
