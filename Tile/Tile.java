package Tile;



import Functional.HitBox;
import Functional.Vec2;
import Graphics.SpriteSheet;

import java.awt.Graphics2D;

public abstract class Tile{

    private Vec2 v;
    private int id;
    protected SpriteSheet sprite;
    private int tileWidth;
    private int tileHeight;
    protected HitBox tile;

    public Tile(Vec2 v, int id, SpriteSheet sprite, int tileWidth, int tileHeight){
        this.v = v;
        this.id = id;
        this.sprite = sprite;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Vec2 getPos(){
        return v;
    }

    public abstract boolean update(HitBox c);
    
    public int getId(){
        return id;
    }

    public HitBox getHitBox(){
        return tile;
    }
    
    void render(Graphics2D g){
        g.drawImage(sprite.takeImage(((id-1) % sprite.getColms()), ((id-1) / sprite.getColms())),
        v.getX(), v.getY() - 1, tileWidth, tileHeight, null);
    }
}