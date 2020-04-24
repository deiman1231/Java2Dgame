package Tile;

import Functional.HitBox;
import Graphics.SpriteSheet;

import java.awt.Graphics2D;

public abstract class TileMap{

    protected SpriteSheet sprite;
    protected int width;
    protected int height;
    protected int tileWidth;
    protected int tileHeight;
    protected Tile[] tiles;

    public TileMap(String tileMap, int width, int height, int tileWidth, int tileHeight, SpriteSheet sprite){
        this.sprite = sprite;
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        tiles = new Tile[height*width];

    }

    public Tile getTile(int i){
        return tiles[i];
    }

    public Tile getTile(int x, int y, int width, int height){
        for(int i = 0; i < this.width*this.height; i++){
            if(tiles[i] != null && tiles[i].getHitBox().collide(x, y, width, height)){
                return tiles[i];
            }
        }
        return null;
    }

    public Tile[] getTiles(){
        return tiles;
    }

    public abstract void update(HitBox c);

    public abstract void render(Graphics2D g);
}