package Tile;

import Functional.HitBox;
import Functional.Vec2;
import Graphics.SpriteSheet;

import java.awt.Graphics2D;

public class TileMapObj extends TileMap{

    TileMapObj(String tileMap, int width, int height, int tileWidth, int tileHeight, SpriteSheet sprite){
        super(tileMap, width, height, tileWidth, tileHeight, sprite);

        String map[] = tileMap.split(",");
        for(int i = 0; i < width*height; i++){
            int id = Integer.parseInt(map[i].replaceAll("\\s+", ""));
            if(id != 0){
                tiles[i] = new TileObj(new Vec2((i%height)*tileWidth, (i/height)*tileHeight), id, sprite, tileWidth, tileHeight);
            }
        }
    }

    public void update(HitBox c){
        for(int i = 0; i < width*height; i++){
            if(tiles[i] != null){
                tiles[i].update(c);
            }
        }
    }

    public void render(Graphics2D g){
        for(int i = 0; i < width*height; i++){
            if(tiles[i] != null){
                tiles[i].render(g);
            }
        }
    }
}