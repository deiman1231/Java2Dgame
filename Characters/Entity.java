package Characters;

import Functional.*;
import Graphics.Animation;
import Graphics.SpriteSheet;
import Sound.Sound;
import Tile.TileManager;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Entity {

    public enum GunType{
        grenadeL, pistol, shotGun, M4
    }

    protected GamePanel gp;
    protected Vec2 v;
    protected SpriteSheet sprite;
    protected Animation ani;
    protected ArrayList<BufferedImage> img;
    protected Input input;
    protected HitBox hitBox;
    protected int delay = 5;
    protected int sizeX;
    protected int sizeY;
    protected int currFrame;
    protected float maxSpeed = 4;
    protected float xa;
    protected float ya;
    protected TileManager tm;
    protected int facing = 3;
    protected ArrayList<Bullet> bullets = new ArrayList<>();
    protected int health;
    protected int maxHealth;
    protected boolean dead = false;
    protected boolean gotShot = false;
    Sound sound = new Sound();
    ArrayList<Explosion> explosions = new ArrayList<>();
    SpriteSheet explosion;

    static final int UP = 4;
    static final int DOWN = 0;
    static final int LEFT = 2;
    static final int RIGHT = 6;
    static final int LEFT_UP = 3;
    static final int LEFT_DOWN = 1;
    static final int RIGHT_DOWN = 7;
    static final int RIGHT_UP = 5;

    public Entity(GamePanel gp, Vec2 v, SpriteSheet sprite, int size, int health, SpriteSheet explosion) {
        this(gp, v, sprite, size, size, health, explosion);
    }

    public Entity(GamePanel gp, Vec2 v, SpriteSheet sprite, int size){
        this.gp = gp;
        this.v = v;
        this.sprite = sprite;
        this.sizeX = size;
        this.sizeY = size;
        hitBox = new HitBox(v, size, size);
        ani = new Animation();
        img = new ArrayList<>();
        ani.init(sprite.getArray(0), -1);
        setAnimation(-1, 0);
    }

    private Entity(GamePanel gp, Vec2 v, SpriteSheet sprite, int sizeX, int sizeY, int health, SpriteSheet explosion) {
        this.gp = gp;
        this.v = v;
        this.sprite = sprite;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.health = health;
        this.maxHealth = health;
        this.explosion = explosion;
        hitBox = new HitBox(v, sizeX, sizeY);
        ani = new Animation();
        input = new Input(gp);
        img = new ArrayList<>();
        ani.init(sprite.getArray(0), 1);
        setAnimation(-1, 0);
    }


    private void setAnimation(int delay, int currAnim) {
        ani.setDelay(delay);
        img = sprite.getArray(currAnim);
        currFrame = ani.getCurrentFrame();
    }

    private void animate() {
        if (ya < 0 && xa == 0) {
            setAnimation(delay, UP);
            facing = UP;
        } else if (ya > 0 && xa == 0) {
            setAnimation(delay, DOWN);
            facing = DOWN;
        } else if (xa < 0 && ya == 0) {
            setAnimation(delay, LEFT);
            facing = LEFT;
        } else if (xa > 0 && ya == 0) {
            setAnimation(delay, RIGHT);
            facing = RIGHT;
        } else if (ya < 0 && xa < 0) {
            setAnimation(delay, LEFT_UP);
            facing = LEFT_UP;
        } else if (ya > 0 && xa < 0) {
            setAnimation(delay, LEFT_DOWN);
            facing = LEFT_DOWN;
        } else if (xa > 0 && ya > 0) {
            setAnimation(delay, RIGHT_DOWN);
            facing = RIGHT_DOWN;
        } else if (xa > 0 && ya < 0) {
            setAnimation(delay, RIGHT_UP);
            facing = RIGHT_UP;
        } else if (xa == 0 && ya == 0) {
            setAnimation(-1, facing);
            ani.init(sprite.getArray(facing), -1);
        }
    }

    public void updateOther(){
        setAnimation(delay, 0);
        ani.update();
    }

    public void update() {
        animate();
        ani.update();
    }

    public void getShot(Bullet b) {
        if (hitBox.collide(b.getHitBox())) {
            health--;
            gotShot = true;
        }
        if (health == 0) {
            dead = true;
        }
    }


    public void updateBullets(ArrayList<Bullet> b) {
        for (Iterator<Bullet> iterator = b.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            getShot(bullet);
            if (gotShot) {
                if (bullet.getBulletType() == Bullet.BulletType.GrenadeL){
                    explosions.add(new Explosion(gp, new Vec2(bullet.getPos()), explosion, 32));
                }
                iterator.remove();
                gotShot = false;
            }
        }
        for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (getBulletCollision(bullet)) {
                iterator.remove();
                if (bullet.getBulletType() == Bullet.BulletType.GrenadeL){
                    explosions.add(new Explosion(gp, new Vec2(bullet.getPos()), explosion, 32));
                }
            }
            else if (outOfMap(bullet)) {
                iterator.remove();
                if (bullet.getBulletType() == Bullet.BulletType.GrenadeL){
                    explosions.add(new Explosion(gp, new Vec2(bullet.getPos()), explosion, 32));
                }
            }
        }
    }


    boolean outOfMap(Bullet b) {
        Vec2 map = gp.getMap();
        if (b.getPos().getX() > map.getX() + gp.getWidth() / 2 || b.getPos().getX() < map.getX() - gp.getWidth() / 2 ||
                b.getPos().getY() > map.getY() + gp.getHeight() / 2 || b.getPos().getY() < map.getY() - gp.getHeight() / 2) {
            return true;
        }
        return false;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    boolean getBulletCollision(Bullet b) {
        return (gp.getTileManager().getTileMap().getTile(b.getPos().getX(), b.getPos().getY(), b.getWidth(), b.getHeight()) != null);
    }

    void removeBullet(int i) {
        bullets.remove(i);
    }


    public void incrementHealth(int i){
        maxHealth += i;
    }

    public void decreaseHealth(int i) {
        if(health - i <= 0){
            health = 0;
            dead = true;
        }else{
            health -= i;
        }
    }

    public void resetHealth(){
        health = maxHealth;
    }

    public abstract void render(Graphics2D g);

    public Vec2 getPos() {
        return v;
    }

    public HitBox getHitBox(){
        return hitBox;
    }

    public void setPos(Vec2 v){
        this.v = v;
    }

    public SpriteSheet getSprite() {
        return sprite;
    }

    public void setSprite(SpriteSheet sprite) {
        this.sprite = sprite;
    }

    public int getSizeX(){
        return sizeX;
    }

    public int getSizeY(){
        return sizeY;
    }

}