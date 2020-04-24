package Functional;

import Characters.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class Bullet {

    public enum BulletType{
        Pistol, ShotGun, M4, GrenadeL;
    }
    private Vec2 pos;
    private int width;
    private int height;
    private double bulletSpeedY;
    private double bulletSpeedX;
    private int misDirection;
    private int facing;
    private BulletType bullet;
    private HitBox hitBox;
    private Vec2 target = null;

    public Bullet(BulletType bullet, Vec2 pos, int facing, int bulletSpeedX, int bulletSpeedY, int misDirection, int width, int height) {
        this.bullet = bullet;
        this.pos = pos;
        this.facing = facing;
        hitBox = new HitBox(pos, width, height);
        this.bulletSpeedX = bulletSpeedX;
        this.bulletSpeedY = bulletSpeedY;
        this.misDirection = misDirection;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor for bullet class if you want to assign the target where bullet has to shoot.
     * @param pos position from where we shoot the bullet
     * @param target target where we shoot the bullet.
     * @param misDirection miss direction of the bullets if we shoot with a shotgun.
     * @param width bullet width
     * @param height bullet height
     */
    public Bullet(BulletType bullet, Vec2 pos, Vec2 target, int misDirection, int width, int height){
        this.bullet = bullet;
        this.pos = pos;
        this.target = target;
        this.misDirection = misDirection;
        this.width = width;
        this.height = height;
        hitBox = new HitBox(pos, width, height);
        bulletSpeedX = getTrajectory().getXd();
        bulletSpeedY = getTrajectory().getYd();
    }

    public void move(){
        if(target == null){
            if(facing == 4){
                pos.setY(pos.getY() - (int) bulletSpeedY + misDirection);
                pos.setX(pos.getX() + misDirection);
            }
            if(facing == 0){
                pos.setY(pos.getY() + (int) bulletSpeedY + misDirection);
                pos.setX(pos.getX() + misDirection);
            }
            if(facing == 2){
                pos.setX(pos.getX() - (int) bulletSpeedX + misDirection);
                pos.setY(pos.getY() + misDirection);
            }
            if(facing == 6){
                pos.setX(pos.getX() + (int) bulletSpeedX + misDirection);
                pos.setY(pos.getY() + misDirection);
            }
            if(facing == 3){
                pos.setY(pos.getY() - (int) bulletSpeedY + misDirection);
                pos.setX(pos.getX() - (int) bulletSpeedX - misDirection);
            }
            if(facing == 1){
                pos.setY(pos.getY() + (int) bulletSpeedY + misDirection);
                pos.setX(pos.getX() - (int) bulletSpeedX + misDirection);
            }
            if(facing == 7){
                pos.setX(pos.getX() + (int) bulletSpeedX + misDirection);
                pos.setY(pos.getY() + (int) bulletSpeedY - misDirection);
            }
            if(facing == 5){
                pos.setX(pos.getX() + (int) bulletSpeedX + misDirection);
                pos.setY(pos.getY() - (int) bulletSpeedY + misDirection);
            }
        }else{
            pos.setX((int) (pos.getX() + bulletSpeedX) + misDirection);
            pos.setY((int) (pos.getY() + bulletSpeedY) + misDirection);
        }
    }

    /**
     *
     * @return A vector class with x and y coordinates where x is speed X axis and y is speed Y axis
     */
    public Vec2 getTrajectory(){
        double ax = 0;
        double ay = 0;
        double ratio;
        int diffX = target.getX() - pos.getX();
        int diffY = target.getY() - pos.getY();
        if(diffX == 0){
            ax = 0;
            if(diffY <= 0){
                ay = -10;
            }
            if(diffY > 0){
                ay = 10;
            }
            return new Vec2(ax, ay);
        }
        if(diffY == 0){
            ay = 0;
            if(diffX < 0){
                ax = -10;
            }
            if(diffX > 0){
                ax = 10;
            }
            return new Vec2(ax, ay);
        }
        ratio = Math.abs((double) diffX / diffY);
        if(diffX > 0 && diffY > 0){
            ay = 10;
            ax = ratio*10;
            return reScale(ax, ay);
        }else if(diffX < 0 && diffY > 0) {
            ay = 10;
            ax = -ratio * 10;
            return reScale(ax, ay);
        }else if(diffX < 0 && diffY < 0){
            ay = -10;
            ax = -10*ratio;
            return reScale(ax, ay);
        }else{
            ay = -10;
            ax = 10*ratio;
            return reScale(ax, ay);
        }
    }

    /**
     * rescales it that x*x + y*y would be less than 100
     * @param x
     * @param y
     * @return returns rescaled vector;
     */
    public Vec2 reScale(double x, double y){
        while(Math.sqrt(x*x + y*y) > 10){
            x = x / 1.1;
            y = y / 1.1;
        }
        return new Vec2(x, y);
    }

    public void update(){
        move();
    }

    public void render(Graphics2D g, BufferedImage image){
        g.drawImage(image, pos.getX(), pos.getY(), width, height, null);
    }

    public Vec2 getPos(){
        return pos;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public HitBox getHitBox(){
        return hitBox;
    }

    public BulletType getBulletType(){
        return bullet;
    }
}