package Functional;

import Tile.TileManager;
import Characters.Player;
import Characters.Enemy;
import Characters.Entity;
import Graphics.SpriteSheet;
import Graphics.BufferedImageLoader;
import Graphics.SkillTree;

import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class LevelManager {

    private GamePanel gp;
    private TileManager tm;
    private Player player;
    private SpriteSheet explosion;
    private SpriteSheet bullet;
    private SpriteSheet dyingEnemy;
    private ArrayList<Enemy> enemies;
    private SpriteSheet coinSprite;
    private LevelGoal levelGoal;
    private final static int RESPAWN_DELAY = 200;
    private int start = 0;
    private SkillTree skillTree;
    private int size = 50;
    private final static int SPRITE_SIZE = 32;
    private String nextLvl;
    private String repeatLvl;
    private ArrayList<SpriteSheet> enemySheet;
    private SpriteSheet goalPrompt;

    LevelManager(GamePanel gp, ArrayList<SpriteSheet> sprite, HUD hud) {
        int playerX = gp.getGameW() / 2;
        int playerY = gp.getGameH() / 2;
        this.gp = gp;
        enemies = new ArrayList<>();
        enemySheet = new ArrayList<>();
        BufferedImageLoader loader = new BufferedImageLoader();
        for (Integer i = 0; i < 4; i++) {
            enemySheet.add(new SpriteSheet(loader.loadImage("../res/enemy" + i.toString() + ".png"), 32));
        }
        explosion = new SpriteSheet(loader.loadImage("../res/BigExplosionSheet.png"), 32);
        goalPrompt = new SpriteSheet(loader.loadImage("../res/hud/PressFCloud.png"), 80);
        bullet = new SpriteSheet(loader.loadImage("../res/bullets.png"), SPRITE_SIZE);
        coinSprite = new SpriteSheet(loader.loadImage("../res/coinAni.png"), 97);
        dyingEnemy = new SpriteSheet(loader.loadImage("../res/Enemy Death Sheet.png"), 32);
        SpriteSheet dyingPlayer = new SpriteSheet(loader.loadImage("../res/corpses/Player Death Sheet.png"), 32);
        player = new Player(gp, new Vec2(playerX, playerY), sprite.get(0), size, bullet, hud, explosion, dyingPlayer);
    }

    public void levelFour(){
        int playerX = 400;
        int playerY = 300;
        player.resetHealth();
        player.resetAmmo();
        player.getPos().setV(new Vec2(playerX, playerY));
        gp.getMap().setV(new Vec2(playerX, playerY));
        tm = new TileManager("res/Office1F.xml");
        enemies.add(makeEnemy(1052, 1176, Entity.GunType.M4, 0, 0));
        enemies.add(makeEnemy(864, 1128, Entity.GunType.shotGun, 30, 30));
        enemies.add(makeEnemy(664, 1210, Entity.GunType.pistol, 90, 80));
        enemies.add(makeEnemy(754, 1290, Entity.GunType.pistol, -90, -80));
        enemies.add(makeEnemy(864, 716, Entity.GunType.M4, 30,-500));
        levelGoal = new LevelGoal(gp, new Vec2(1020, 1130), coinSprite, size, goalPrompt);
        enemies.add(new Enemy(gp, new Vec2(gp.getGameW() / 2 + 350, gp.getGameH() / 2), enemySheet.get(0), size, 25, 25, bullet, dyingEnemy, new int[] {5, 5, 5, 5}, Entity.GunType.shotGun, explosion));
        repeatLvl = "levelFour";

}

    public void levelTwo(){
        int playerX = 400;
        int playerY = 300;
        player.resetHealth();
        player.resetAmmo();
        tm = new TileManager("res/Office3F.xml");
        player.getPos().setV(new Vec2(playerX, playerY));
        gp.getMap().setV(new Vec2(playerX, playerY));
        enemies.add(makeEnemy(240, 1152, Entity.GunType.pistol, 0,0));
        enemies.add(makeEnemy(316,1484, Entity.GunType.pistol, 0, 0));
        enemies.add(makeEnemy(1600, 250, Entity.GunType.M4, 390, 1090));
        enemies.add(makeEnemy(1990, 1340, Entity.GunType.M4, -390, -1090));
        enemies.add(makeEnemy(1204, 240, Entity.GunType.shotGun, 0, 380));
        enemies.add(makeEnemy(336, 612, Entity.GunType.shotGun,420, 300 ));
        enemies.add(makeEnemy(764, 612, Entity.GunType.shotGun, -420, 300));
        enemies.add(makeEnemy(764, 920, Entity.GunType.shotGun, -420, -300));
        enemies.add(makeEnemy(336, 920, Entity.GunType.shotGun, 420, -300));
        levelGoal = new LevelGoal(gp, new Vec2(2132, 1484), coinSprite, size, goalPrompt);
        repeatLvl = "levelTwo";
        nextLvl = "levelThree";
    }

    public void levelThree() {
        int playerX = gp.getGameW()/2;
        int playerY = gp.getGameH()/2;
        player.resetHealth();
        player.resetAmmo();
        tm = new TileManager("res/Office2F.xml");
        player.getPos().setV(new Vec2(playerX, playerY));
        gp.getMap().setV(new Vec2(playerX, playerY));
        enemies.add(makeEnemy(1916, 236, Entity.GunType.shotGun, -600, 0));
        enemies.add(makeEnemy(960, 284, Entity.GunType.shotGun, 90, 90));
        enemies.add(makeEnemy(1388, 788, Entity.GunType.M4, -950, 0));
        enemies.add(makeEnemy(432, 788, Entity.GunType.M4, 950, 0));
        enemies.add(makeEnemy(1050, 1344, Entity.GunType.pistol, 90, -228));
        enemies.add(makeEnemy(960, 1572, Entity.GunType.M4, 0, 300));
        enemies.add(makeEnemy(296, 1560, Entity.GunType.M4, 0, 300));
        levelGoal = new LevelGoal(gp, new Vec2(288,1944), coinSprite, size, goalPrompt);
        repeatLvl = "levelThree";
        nextLvl = "levelFour";
    }

    public void levelOne(){
        int playerX = gp.getGameW()/2;
        int playerY = gp.getGameH()/2;
        player.resetHealth();
        player.resetAmmo();
        tm = new TileManager("res/Office4F.xml");
        player.getPos().setV(new Vec2(playerX, playerY));
        gp.getMap().setV(new Vec2(playerX, playerY));
        enemies.add(makeEnemy(824, 384, Entity.GunType.pistol, 0, -92));
        enemies.add(makeEnemy(768, 96, Entity.GunType.pistol, 130, 190));
        levelGoal = new LevelGoal(gp, new Vec2(1360, 400), coinSprite, size, goalPrompt);
        skillTree = new SkillTree(gp, player, gp.getMap());
        repeatLvl = "levelOne";
        nextLvl = "levelTwo";
    }

    TileManager getTileManager() {
        return tm;
    }

    ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    SkillTree getSkillTree() {
        return skillTree;
    }

    public void update() {
        levelGoal.update(player.getHitBox());
        if (levelGoal.isLevelCompleted()) {
            skillTree.setPoints(5);
            gp.setGameState(GamePanel.GameState.SkillTree);
            levelGoal.setLevelCompleted(false);
        }
        if(skillTree.getPoints() == 0){
            skillTree.setPoints(5);
            nextLevel();
        }
        if(player.isDead()){
            if(start < RESPAWN_DELAY){
                start++;
            }else{
                start = 0;
                player.resurrectPlayer();
                repeatLevel();
            }
        }
    }

    public void render(Graphics2D g) {
        tm.render(g);
        levelGoal.render(g);
    }

    private void nextLevel() {
        try {
            tm = null;
            enemies.clear();
            levelGoal = null;
            Method method = this.getClass().getMethod(nextLvl);
            method.invoke(this);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            System.out.println(e);
        }
    }

    private Enemy makeEnemy(int x, int y, Entity.GunType gun, int patrolDistanceX, int patrolDistanceY){
        return new Enemy(gp, new Vec2(x, y), enemySheet.get(0), size, patrolDistanceX, patrolDistanceY, bullet, dyingEnemy, new int[]{5, 5, 5, 5}, gun, explosion);
    }




    private void repeatLevel(){
        try{
            tm = null;
            enemies.clear();
            levelGoal = null;
            Method method = this.getClass().getMethod(repeatLvl);
            method.invoke(this);
        }catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException e){
            System.out.println(e);
        }
    }
}
