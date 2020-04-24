package Characters;

import Functional.GamePanel;
import Functional.HitBox;
import Functional.Vec2;
import Graphics.SpriteSheet;
import Functional.Bullet;
import Functional.Body;
import Sound.Sound;


import java.awt.Graphics2D;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static java.lang.Math.abs;

public class Enemy extends Entity {

    private Vec2 enemyPos;
    private int moveX;
    private int moveY;
    private int cornersReached = 0;
    private Vec2[] patrolRoute;
    private HitBox hitBox;
    private int shootFreq = 20;
    private int k = shootFreq;
    private double timeLastSeen;
    private Body body;
    private GunType gun;
    private Player player;
    private int radiusDetection = 250;
    private int chasingRadius = 150;
    private int spotsFromEveryWhereRadius = 20;
    private int delay = 50;
    private int runAway = 0;
    private boolean chasing = false;
    private BufferedImage grenadeL;


    public Enemy(GamePanel gp, Vec2 pos, SpriteSheet sprite, int size, int moveX, int moveY, SpriteSheet bullet, SpriteSheet dying, int[] Ammo, GunType gun, SpriteSheet explosion) {
        super(gp, pos, sprite, size, 6, explosion);
        enemyPos = pos;
        this.moveX = moveX;
        this.moveY = moveY;
        patrolRoute = createPatrolRoute();
        maxSpeed = 1;
        this.grenadeL = bullet.takeImage(3, 0, 11);
        this.player = gp.getPlayer();
        hitBox = new HitBox(pos, size, size);
        body = new Body(gp, pos, dying, size, 0, Ammo, explosion);
        this.gun = gun;
    }

    /**
     * Turns the X and Y distances in the constructor into 4 vector points to patrol between.
     *
     * @return an array of points to patrol between.
     */
    private Vec2[] createPatrolRoute() {
        Vec2 topLeft = new Vec2(enemyPos);
        Vec2 topRight = new Vec2(enemyPos.getX() + moveX, enemyPos.getY());
        Vec2 bottomRight = new Vec2(enemyPos.getX() + moveX, enemyPos.getY() + moveY);
        Vec2 bottomLeft = new Vec2(enemyPos.getX(), enemyPos.getY() + moveY);
        return new Vec2[]{topLeft, topRight, bottomRight, bottomLeft};
    }

    /**
     * Checks if the enemy has reached a vector destination and returns that
     *
     * @param destination the location to compare the enemy's position to
     * @return whether the enemy has reached the destination
     */
    private boolean checkArrival(Vec2 destination) {
        return (enemyPos.getX() == destination.getX() && enemyPos.getY() == destination.getY());
    }

    /**
     * Governs the general behaviour of the enemy.
     * If the player has not been spotted, i.e. the alertLevel is 0,
     * patrols a route specified by the values in the constructor
     * If player is spotted, i.e. the alertLevel is above 1, but not exceeding ALERT_THRESHOLD,
     * stands still and starts a timer to check if the player is still visible in ALERT_DELAY
     * If the player is spotted for a long enough time to raise the alertLevel above ALERT_THRESHOLD,
     * chases the player until the reset by walkToPlayer
     */
    private void movement() {

        //if (alertLevel < ALERT_THRESHOLD)

            switch (cornersReached) {
                case 0:
                    walkTo(patrolRoute[1]);
                    if (checkArrival(patrolRoute[1])) {
                        cornersReached = 1;
                    }
                    break;
                case 1:
                    walkTo(patrolRoute[2]);
                    if (checkArrival(patrolRoute[2])) {
                        cornersReached = 2;
                    }
                    break;
                case 2:
                    //System.out.println("x");
                    walkTo(patrolRoute[3]);
                    if (checkArrival(patrolRoute[3])) {
                        cornersReached = 3;
                    }
                    break;
                case 3:
                    walkTo(patrolRoute[0]);
                    if (checkArrival(patrolRoute[0])) {
                        cornersReached = 0;
                    }
                    break;
            }
        //}
    }

    /**
     * Walks to a vector destination specified by the parameter.
     * Moves first in the X axis, then the Y.
     * Checks for collision and will swap movement axis if collision occurs.
     *
     * @param destination the vector destination to walk to.
     */
    private void walkTo(Vec2 destination) {
        if (player.stealth){
            radiusDetection = 0;
        } else {
            radiusDetection = 250;
        }
        int xDiff = v.getX() - destination.getX();
        int yDiff = v.getY() - destination.getY();
        //System.out.println(xDiff + " " + yDiff);
        int xDiffC = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int yDiffC = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        boolean stuckLeft = collide(LEFT);
        boolean stuckRight = collide(RIGHT);
        boolean stuckUp = collide(UP);
        boolean stuckDown = collide(DOWN);
        if(!isCloserThanRadius(radiusDetection)){
            chasing = false;
        }
        if(isCloserThanRadius(spotsFromEveryWhereRadius)){
            chasing = true;
            shoot();
            setFacingForPlayer();
        }
        else if(!spot(player) && !chasing){
            runAway = 0;
            chasing = false;
            //System.out.println(1);
            if (xDiff > 0 && !stuckLeft) { //walk left
                //System.out.println(2);
                xa = -maxSpeed;  //should be -ve
                ya = 0;
            }
            if (xDiff < 0 && !stuckRight) { //walk right
                xa = maxSpeed;   //should be +ve
                ya = 0;
            }
            if (yDiff > 0 && !stuckUp) { //walk up
                ya = -maxSpeed;
                xa = 0;
            }
            if (yDiff < 0 && !stuckDown) { //walk down
                ya = maxSpeed;
                xa = 0;
            }
        }else{
            if(runAway < delay && !chasing){
                xa = 0;
                ya = 0;
                runAway++;
            }
            if(runAway == delay){
                chasing = true;
            }
            if(chasing && !isCloserThanRadius(chasingRadius)){
                if(!stuckUp && !stuckDown && !stuckLeft && !stuckRight){
                    Vec2 chaseSpeed = chaseSpeed();
                    xa = (float) chaseSpeed.getXd();
                    ya = (float) chaseSpeed.getYd();
                }else{
                    revertingIfStuck(xDiffC, yDiffC);
                }
            }else if(chasing && isCloserThanRadius(radiusDetection)){
                xa = 0;
                ya = 0;
                setFacingForPlayer();
                shoot();
            }
        }
    }

    public void revertingIfStuck(int xDiff, int yDiff){
        boolean stuckLeft = collide(LEFT);
        boolean stuckRight = collide(RIGHT);
        boolean stuckUp = collide(UP);
        boolean stuckDown = collide(DOWN);
        if(stuckDown && xDiff < 0){
            xa = -maxSpeed*3;
            ya = 0;
        }else if(stuckDown && xDiff > 0){
            xa = maxSpeed*3;
            ya = 0;
        }else if(stuckUp && xDiff > 0){
            xa = maxSpeed*3;
            ya = 0;
        }else if(stuckUp && xDiff < 0){
            xa = -maxSpeed*3;
            ya = 0;
        }else if(stuckRight && yDiff > 0){
            xa = 0;
            ya = maxSpeed*3;
        }else if(stuckRight && yDiff < 0){
            xa = 0;
            ya = -maxSpeed*3;
        }else if(stuckLeft && yDiff > 0){
            xa = 0;
            ya = maxSpeed*3;
        }else if(stuckLeft && yDiff < 0){
            xa = 0;
            ya = -maxSpeed*3;
        }
    }

    /**
     * Checks if the next tile to be moved onto is walkable by checking if the hitbox of that tile is null.
     * Then moves by incrementing the x or y position of the enemy by xa or ya respectively.
     */
    public void move() {
        if (gp.getTileManager().getTileMap().getTile(v.getX() + (int) xa, v.getY(), sizeX, sizeY) == null) { //walkable tiles have hitbox of null
            v.setX(v.getX() + (int) xa);
        }
        if (gp.getTileManager().getTileMap().getTile(v.getX(), v.getY() + (int) ya, sizeX, sizeY) == null) {
            v.setY(v.getY() + (int) ya);
        }
    }

    private boolean collide(int direction) {
        switch (direction) {
            case DOWN:
                return !(gp.getTileManager().getTileMap().getTile(v.getX(), v.getY() + (int) maxSpeed*3, sizeX, sizeY) == null);

            case LEFT:
                return !(gp.getTileManager().getTileMap().getTile(v.getX() - (int) maxSpeed*3, v.getY(), sizeX, sizeY) == null);

            case RIGHT:
                return !(gp.getTileManager().getTileMap().getTile(v.getX() + (int) maxSpeed*3, v.getY(), sizeX, sizeY) == null);

            case UP:
                return !(gp.getTileManager().getTileMap().getTile(v.getX(), v.getY() - (int) maxSpeed*3, sizeX, sizeY) == null);

        }
        return false;
    }



    private void shoot() {
        int shootFreq = 20;
        int shootFreqM4 = 5;


            if(gun == GunType.pistol){
                if(k == shootFreq){
                    bullets.add(new Bullet(Bullet.BulletType.Pistol, new Vec2(v.getX()+sizeX/2, v.getY()+sizeY/2), new Vec2(player.getPos().getX() + player.getSizeX()/2, player.getPos().getY() + player.getSizeY()/2), 0, 3, 3));
                    //sound.gunSound(GunType.pistol);
                    k = 0;
                }else{
                    k++;
                }
            }
            if(gun == GunType.shotGun){
                if(k == shootFreq){
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX()+sizeX/2, v.getY()+sizeY/2), new Vec2(player.getPos().getX() + player.getSizeX()/2, player.getPos().getY() + player.getSizeY()/2), 0, 3, 3));
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX()+sizeX/2, v.getY()+sizeY/2), new Vec2(player.getPos().getX() + player.getSizeX()/2, player.getPos().getY() + player.getSizeY()/2), -1, 3, 3));
                    bullets.add(new Bullet(Bullet.BulletType.ShotGun, new Vec2(v.getX()+sizeX/2, v.getY()+sizeY/2), new Vec2(player.getPos().getX() + player.getSizeX()/2, player.getPos().getY() + player.getSizeY()/2), 1, 3, 3));
                    //sound.gunSound(GunType.shotGun);
                    k = 0;
                }else{ k++; }
            }
            if(gun == GunType.M4){
                if(k > shootFreq){
                    k = 0;
                }
                if(k == shootFreqM4){
                    bullets.add(new Bullet(Bullet.BulletType.M4, new Vec2(v.getX()+sizeX/2, v.getY()+sizeY/2), new Vec2(player.getPos().getX() + player.getSizeX()/2, player.getPos().getY() + player.getSizeY()/2), 0, 3, 3));
                    //sound.gunSound(GunType.M4);
                    k = 0;
                }else{ k++; }
            }
        }
    //}



    public boolean spot(Player player){
        int xDiff = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int yDiff = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        //System.out.println(xDiff + " " + yDiff);
        double dist = Math.sqrt((xDiff*xDiff + yDiff*yDiff));
        if((facing == 6 || facing == 5 || facing == 7) && (xDiff > 0 && Math.abs(yDiff) <= Math.abs(xDiff)) && dist < radiusDetection){
            return true;
        }else if((facing == 2 || facing == 1 || facing == 3) && (xDiff < 0 && Math.abs(yDiff) <= Math.abs(xDiff)) && dist < radiusDetection){
            return true;
        }else if((facing == 4 || facing == 3 || facing == 5) && (yDiff < 0 && Math.abs(yDiff) > Math.abs(xDiff)) && dist < radiusDetection){
            return true;
        }else if((facing == 0 || facing == 1 || facing == 7) && (yDiff > 0 && Math.abs(yDiff) > Math.abs(xDiff)) && dist < radiusDetection){
            return true;
        }else if((facing == 7 || facing == 0 || facing == 6) && xDiff > 0 && yDiff > 0 && dist < radiusDetection){
            return true;
        }else if((facing == 1 || facing == 0 || facing == 2) && xDiff < 0 && yDiff > 0 && dist < radiusDetection){
            return true;
        }else if((facing == 3 || facing == 2 || facing == 4) && yDiff < 0 && xDiff < 0 && dist < radiusDetection){
            return true;
        }else if((facing == 5 || facing == 4 || facing == 6) && yDiff < 0 && xDiff > 0 && dist < radiusDetection){
            return true;
        }
        return false;
    }

    public void setFacingForPlayer(){
        int xDiff = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int yDiff = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        double dist = Math.sqrt((xDiff*xDiff + yDiff*yDiff));
        if(xDiff > 0 && Math.abs(yDiff) <= Math.abs(xDiff)){ facing = 6; }
        else if(xDiff < 0 && Math.abs(yDiff) <= Math.abs(xDiff)){ facing = 2; }
        else if(yDiff < 0 && Math.abs(yDiff) > Math.abs(xDiff)){ facing = 4; }
        else if(yDiff > 0 && Math.abs(yDiff) > Math.abs(xDiff)){ facing = 0; }
        else if(xDiff > 0 && yDiff > 0){ facing = 7; }
        else if(xDiff < 0 && yDiff > 0){ facing = 1; }
        else if(yDiff < 0 && xDiff < 0){ facing = 3; }
        else if(yDiff < 0 && xDiff > 0){ facing = 5; }
    }

    public Vec2 chaseSpeed(){
        double ax = 0;
        double ay = 0;
        double ratio;
        int diffX = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int diffY = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        if(diffX == 0){
            ax = 0;
            if(diffY <= 0){
                ay = -maxSpeed*3;
            }
            if(diffY > 0){
                ay = maxSpeed*3;
            }
            return reScale(ax, ay);
        }
        if(diffY == 0){
            ay = 0;
            if(diffX <= 0){
                ax = -maxSpeed*3;
            }
            if(diffX > 0){
                ax = maxSpeed*3;
            }
            return reScale(ax, ay);
        }
        ratio = Math.abs((double) diffX / diffY);
        if(diffX > 0 && diffY > 0){
            ay = maxSpeed*3;
            ax = ratio*maxSpeed*3;
            return reScale(ax, ay);
        }else if(diffX < 0 && diffY > 0) {
            ay = maxSpeed*3;
            ax = -ratio * maxSpeed*3;
            return reScale(ax, ay);
        }else if(diffX < 0 && diffY < 0){
            ay = -maxSpeed*3;
            ax = -maxSpeed*ratio*3;
            return reScale(ax, ay);
        }else{
            ay = -maxSpeed*3;
            ax = maxSpeed*ratio*3;
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
        while(Math.sqrt(x*x + y*y) > 3){
            x = (double) x / 1.01;
            y = (double) y / 1.01;
        }
        return new Vec2(x, y);
    }

    public boolean isCloserThanRadius(int radius){
        int xDiff = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int yDiff = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        double dist = Math.sqrt((xDiff * xDiff + yDiff*yDiff));
        return (dist < radius);
    }

    public boolean isFurtherThanRadius(int radius){
        int xDiff = player.getPos().getX() + player.getSizeX()/2 - (v.getX() + getSizeX()/2);
        int yDiff = player.getPos().getY() + player.getSizeY()/2 - (v.getY() + getSizeY()/2);
        double dist = Math.sqrt((xDiff * xDiff + yDiff*yDiff));
        return (dist > radius);
    }

    public void updateBulletsPlayer(){
        for(int i = 0; i < bullets.size(); i++){
            if(player.getHitBox().collide(bullets.get(i).getHitBox())){
                if(bullets.get(i).getBulletType() == Bullet.BulletType.Pistol){
                    player.decreaseHealth(30);
                    player.getHud().decreaseHealth(gp.getGameW()/30);
                }
                if(bullets.get(i).getBulletType() == Bullet.BulletType.ShotGun){
                    System.out.println(1);
                    player.decreaseHealth(20);
                    player.getHud().decreaseHealth(100);

                }
                if(bullets.get(i).getBulletType() == Bullet.BulletType.M4){
                    player.decreaseHealth(15);
                }
                if(bullets.get(i).getBulletType() == Bullet.BulletType.GrenadeL){
                    player.decreaseHealth(200);
                }
                bullets.remove(i);
            }
        }
    }

    public void update(ArrayList<Bullet> b) {
        if (health != 0 && !player.isDead()) {
            updateBullets(b);
            movement();
            move();
            super.update();
            updateBulletsPlayer();
        } else{
            if(bullets.size() == 0){
                bullets.clear();
            }
            body.update(player);
        }
    }

    @Override
    public void getShot(Bullet b) {
        if (hitBox.collide(b.getHitBox())) {
            System.out.println("OW");
            health--;
            gotShot = true;
            movement();
        }
        if (health == 0) {
            dead = true;
        }
    }

    public void render(Graphics2D g) {
        if (health != 0) {
            g.drawImage(img.get(currFrame), v.getX(), v.getY(), sizeX, sizeY, null);
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).render(g, grenadeL);
            }
        }
        if (health == 0) {
            body.render(g);
        }
    }

    public boolean getDead() {
        return dead;
    }
}