package Functional;

import Characters.Player;
import Tile.TileManager;
import Graphics.*;
import Characters.Enemy;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int gameW;
    private final int gameH;

    public enum GameState{
        Menu, Play, SkillTree;
    }

    GameState state = GameState.Menu;
    private boolean running;
    private Graphics2D g;
    private BufferedImage img;
    private ArrayList<BufferedImage> spriteSheet;
    private Thread thread;
    private ArrayList<SpriteSheet> sheet;
    private int spriteSize = 32;
    private Menu menu;
    public static Vec2 map;
    private HUD hud;
    private LevelManager levelManager;
    private SkillTree skilltree;
    private SpriteSheet explosion;
    private Player player;
    private ArrayList<Enemy> enemies;
    
    public Vec2 getPlayerPosition(){
        return player.getPos();
    }



    public GamePanel(int gameW, int gameH) {
        this.gameW = gameW;
        this.gameH = gameH;
        setPreferredSize(new Dimension(gameW, gameH));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
    }

    private void init() {
        map = new Vec2(gameW / 2, gameH / 2);
        Vec2.setWorldVar(map.getX(), map.getY());
        BufferedImageLoader loader = new BufferedImageLoader();
        try {
            spriteSheet = new ArrayList<BufferedImage>();
            for (int i = 0; i < 4; i++) {
                spriteSheet.add(loader.loadImage("../res/sprite" + String.valueOf(i) + ".png")); ///home/lancs/queeneyp/IdeaProjects/Game2D/res/sprite0.png
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            e.fillInStackTrace();
        }
        sheet = new ArrayList<SpriteSheet>();
        for(int i = 0; i < 4; i++){
            sheet.add(new SpriteSheet(spriteSheet.get(i), spriteSize));
        }

        hud = new HUD(this);
        levelManager = new LevelManager(this, sheet, hud);
        player = levelManager.getPlayer();
        menu = new Menu(this);
        levelManager.levelOne();
        skilltree = levelManager.getSkillTree();
        img = new BufferedImage(gameW, gameH, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        enemies = levelManager.getEnemies();
    }

    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void run() {
        init();
        running = true;


        final double GAME_HERTZ = 64.0;
        final double TBU = 1000000000 / GAME_HERTZ; // Time Before Update

        final double TARGET_FPS = 1000;
        final double TTBR = 1000000000 / TARGET_FPS; // Total time before render

        final int MUBR = 3; // Must Update before render

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;


        while (running) {
            int updateCount = 0;
            double thisTime = System.nanoTime();
            while ((thisTime - lastUpdateTime) > TBU && (updateCount < MUBR)) {
                update();
                updateCount++;
                lastUpdateTime += TBU;
            }
            draw();
            render(g);
            lastRenderTime = thisTime;

            while (thisTime - lastRenderTime < TTBR && thisTime - lastUpdateTime < TBU) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println("ERROR: yielding thread");
                }
                thisTime = System.nanoTime();
            }
        }
    }

    public ArrayList<Bullet> getAllEnemyBullets() {
        ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
        for (Enemy enemy : enemies) {
            enemyBullets.addAll(enemy.getBullets());
        }
        return enemyBullets;
    }

    public void update() {
        if(state == GameState.Menu){
            menu.update();
        }
        if(state == GameState.Play){
            player.update(getAllEnemyBullets());
            //System.out.println(player.getPos());
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).update(player.getBullets());
                //if(enemies.get(i).getDead()){ enemies.remove(i); }
            }
            hud.update();
            levelManager.update();
        }
        if(state == GameState.SkillTree){
            skilltree.update();
        }
    }

    private void render(Graphics2D g) {
        if(state == GameState.Menu){
            menu.render(g);
        }
        if(state == GameState.Play){
            g.translate(-player.getPos().getWorldVar().getX(), -player.getPos().getWorldVar().getY());
            //System.out.println(-player.getPos().getWorldVar().getX() + " " + -player.getPos().getWorldVar().getY());
            //tm.render(g);
            levelManager.render(g);
            player.render(g);
            hud.render(g);
            Vec2.setWorldVar(map.getX(), map.getY());
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).render(g);
            }
        }
        if(state == GameState.SkillTree){
            skilltree.render(g);
        }
    }

    private void draw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(img, 0, 0, gameW, gameH, null);
        g2.dispose();
    }

    public SpriteSheet getSpriteSheet(int i) {
        return sheet.get(i);
    }

    public TileManager getTileManager() {
        return levelManager.getTileManager();
    }

    public Vec2 getMap() {
        return map;
    }

    public HUD getHud(){
        return hud;
    }

    public int getGameW(){
        return gameW;
    }

    public int getGameH(){
        return gameH;
    }

    public GameState getGameState(){
        return state;
    }

    public void setGameState(GameState x){
        state = x;
    }

    public Player getPlayer(){
        return player;
    }

    public SpriteSheet getExplosion() {
        return explosion;
    }
}