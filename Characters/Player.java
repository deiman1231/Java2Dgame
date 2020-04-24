package Characters;

import Functional.*;
import Graphics.SpriteSheet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {


    private HUD hud;
    private int shootFre = 20;
    private int k = shootFre;
    private int shootFreM4 = 5;
    private BufferedImage currGun;
    private int[] gunsAmmo = new int[4];
    private int[] maxGunAmmo = new int[4];
    private int currGunAmmo;
    private GunType gun;
    private SpriteSheet bullet;
    private BufferedImage grenadeL;
    private BufferedImage pistol;
    private BufferedImage shotGun;
    private BufferedImage M4;
    private Body body;
    AlphaComposite composite;
    private int type = AlphaComposite.SRC_OVER;
    private double stealthStartTime;
    private double stealthEndTime;
    boolean stealth = false;
    private double stealthTimeLeft = STEALTH_TIME;
    private static final int STEALTH_TIME = 3000;
    private static final float stealthAlpha = 0.4f;
    float alpha = stealthAlpha;
    double lastUpdateTime = System.currentTimeMillis();
    private boolean tabPressed = false;

    public Player(GamePanel gp, Vec2 pos, SpriteSheet sprite, int size, SpriteSheet bullet, HUD hud, SpriteSheet explosion, SpriteSheet dying) {
        super(gp, pos, sprite, size, 300, explosion);
        this.bullet = bullet;
        this.hud = hud;
        this.body = body;
        this.M4 = bullet.takeImage(12, 0, 10);
        this.pistol = bullet.takeImage(12, 0, 10);
        this.grenadeL = bullet.takeImage(0, 0, 10);
        this.shotGun = bullet.takeImage(12, 0, 10);
        body = new Body(gp, pos, dying, size, 0, new int[] {0}, explosion);
        gunsAmmo[0] = 20;
        gunsAmmo[1] = 10;
        gunsAmmo[2] = 100;
        gunsAmmo[3] = 1;
        maxGunAmmo[0] = 20;
        maxGunAmmo[1] = 10;
        maxGunAmmo[2] = 100;
        maxGunAmmo[3] = 1;
        currGunAmmo = gunsAmmo[0];
        composite = AlphaComposite.getInstance(type, 1f);
    }

    public void movement() {
        if (input.keyDown(38) || input.keyDown(40)) {
            if (input.keyDown(38)) {
                ya = -maxSpeed;
            }
            if (input.keyDown(40)) {
                ya = maxSpeed;
            }
        } else {
            ya = 0;
        }
        if (input.keyDown(37) || input.keyDown(39)) {
            if (input.keyDown(37)) {
                xa = -maxSpeed;
            }
            if (input.keyDown(39)) {
                xa = maxSpeed;
            }
        } else {
            xa = 0;
        }
    }

    public void move() {
        if (gp.getTileManager().getTileMap().getTile(v.getX() + (int) xa, v.getY(), sizeX, sizeY) == null) {
            v.setX(v.getX() + (int) xa);
            gp.getMap().setX(gp.getMap().getX() + (int) xa);
        }
        if (gp.getTileManager().getTileMap().getTile(v.getX(), v.getY() + (int) ya, sizeX, sizeY) == null) {
            v.setY(v.getY() + (int) ya);
            gp.getMap().setY(gp.getMap().getY() + (int) ya);
        }
    }

    public void shoot() {
        if (input.keyDown(KeyEvent.VK_TAB) && !tabPressed) {
            toggleStealth();
            tabPressed = true;
        }else if(!input.keyDown(KeyEvent.VK_TAB)){
            tabPressed = false;
        }

        if (input.keyDown(8)) {
            System.out.println(v);
        }
        if (input.keyDown(KeyEvent.VK_SPACE) && gun == GunType.grenadeL && gunsAmmo[3] > 0) {
            if (k == shootFre) {
                bullets.add(new Bullet(Bullet.BulletType.GrenadeL, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, 0, 20, 20));
                k = 0;
                gunsAmmo[3]--;
            } else {
                k++;
            }
        } else if (input.keyDown(KeyEvent.VK_SPACE) && gun != GunType.grenadeL) {
            if (gun == GunType.pistol && gunsAmmo[0] > 0) {
                if (k == shootFre) {
                    bullets.add(new Bullet(Bullet.BulletType.Pistol, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, 0, 3, 3));
                    k = 0;
                    gunsAmmo[0]--;
                }
            }
            if (gun == GunType.shotGun && gunsAmmo[1] > 0) {
                if (k == shootFre) {
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, 0, 3, 3));
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, 1, 3, 3));
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, -1, 3, 3));
                    k = 0;
                    gunsAmmo[1]--;
                } else {
                    k++;
                }
            }
            if (gun == GunType.M4 && gunsAmmo[2] > 0) {
                if (k > shootFre) {
                    k = 0;
                }
                if (k == shootFreM4) {
                    bullets.add(new Bullet(Bullet.BulletType.M4, new Vec2(v.getX() + sizeX / 2, v.getY() + sizeY / 2), facing, 10, 10, 0, 3, 3));
                    k = 0;
                    gunsAmmo[2]--;
                } else {
                    k++;
                }
            }
        } else {
            k = shootFre;
        }
    }

//    public boolean checkDead(){
//
//    }

    public void update(ArrayList<Bullet> b){
        if(!dead){
            super.update();
            movement();
            move();
            shoot();
            updateStealth();
            updateBullets(b);
            updateExplosions();
            setSprite(gp.getSpriteSheet(input.getCounter()));
            changeGun();
        }else{
            body.update(this);
        }
        //System.out.println(health);
    }

    private void updateExplosions() {
        for (Explosion e : explosions) {
            e.update(this);
        }
    }

//    @Override
//    public void getShot(Bullet b) {
//        if (hitBox.collide(b.getHitBox())) {
//            health--;
//            gotShot = true;
//        }
//        if (health == 0) {
//            System.out.println("he ded");
//        }
//    }

    @Override
    public void render(Graphics2D g){
        if(!dead){
            g.setComposite(composite);
            g.drawImage(img.get(currFrame), v.getX(), v.getY(), sizeX, sizeY, null);
            for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).render(g, currGun);
            }
            for (Explosion e : explosions){
                e.render(g);
            }
        }else{
            body.render(g);
        }
    }

    public void changeGun() {
        hud.setCurrAmmo(currGunAmmo);
        if (input.getCounter() == 0) {
            hud.setCurrGun(0);
            gun = GunType.pistol;
            currGun = pistol;
            currGunAmmo = gunsAmmo[0];
        } else if (input.getCounter() == 1) {
            hud.setCurrGun(1);
            gun = GunType.shotGun;
            currGun = shotGun;
            currGunAmmo = gunsAmmo[1];
        } else if (input.getCounter() == 2) {
            hud.setCurrGun(2);
            gun = GunType.M4;
            currGun = M4;
            currGunAmmo = gunsAmmo[2];
        } else if (input.getCounter() == 3) {
            hud.setCurrGun(3);
            gun = GunType.grenadeL;
            currGun = grenadeL;
            currGunAmmo = gunsAmmo[3];
        }
    }

    public int getCurrGunAmmoAmmo() {
        return currGunAmmo;
    }

    public int getMaxGunAmmo(int i) {
        return maxGunAmmo[i % 4];
    }

    public void incrementMaxGunAmmo(GunType gun, int ammo) {
        int i = 0;
        if (gun == GunType.pistol) {
            i = 0;
        }
        if (gun == GunType.shotGun) {
            i = 1;
        }
        if (gun == GunType.M4) {
            i = 2;
        }
        if (gun == GunType.grenadeL) {
            i = 3;
        }
        maxGunAmmo[i] += ammo;
    }

    public void incrementHealth(int i) {
        maxHealth += i;
    }

    public void resetHealth() {
        health = maxHealth;
    }

    public int getHealth(){
        return health;
    }

    public void incrementMaxSpeed(float i) {
        maxSpeed += i;
    }

    public void resetAmmo(){
        for(int i = 0; i < gunsAmmo.length; i++){
            gunsAmmo[i] = maxGunAmmo[i];
        }
    }

    public int[] getGunsAmmo(){
        return gunsAmmo;
    }

    public boolean isDead(){
        return dead;
    }

    public void resurrectPlayer(){
        dead = false;
    }

    public int[] getLeftGunsAmmo(int[] ammo){
        for(int i = 0; i < gunsAmmo.length; i++){
            if(gunsAmmo[i] + ammo[i] < maxGunAmmo[i]){
                //System.out.println();
                gunsAmmo[i] += ammo[i];
                ammo[i] = 0;
            } else {
                ammo[i] = ammo[i] - (maxGunAmmo[i] - gunsAmmo[i]);
                gunsAmmo[i] = maxGunAmmo[i];
            }
        }
        return ammo;
    }

    private void stealthOff() {
        stealth = false;
        alpha = 1f;
        composite = AlphaComposite.getInstance(type, alpha);
        stealthEndTime = System.currentTimeMillis();
    }

    public HUD getHud(){
        return hud;
    }


    private void updateStealth() {

        if (stealth) {
            if (System.currentTimeMillis() - stealthStartTime > stealthTimeLeft) {
                stealthTimeLeft = 0;
                stealthOff();
            }
        }
        if (!stealth && stealthTimeLeft < STEALTH_TIME) {
            stealthTimeLeft = stealthTimeLeft + (System.currentTimeMillis() - lastUpdateTime) / 2;
        }


        if (stealthTimeLeft > STEALTH_TIME) {
            stealthTimeLeft = STEALTH_TIME;
        }
        lastUpdateTime = System.currentTimeMillis();
    }


    private void toggleStealth() {
        boolean toggledOff = false;
        if (stealthTimeLeft > 0) {
            stealth = !stealth;
            if (!stealth) {
                toggledOff = true;
            }
        }
        if (stealth) {
            stealthStartTime = System.currentTimeMillis();
            alpha = stealthAlpha;
        } else if (toggledOff) {
            stealthEndTime = System.currentTimeMillis();
            alpha = 1f;
            stealthTimeLeft = stealthTimeLeft - (stealthEndTime - stealthStartTime);
        }

        composite = AlphaComposite.getInstance(type, alpha);
    }

}