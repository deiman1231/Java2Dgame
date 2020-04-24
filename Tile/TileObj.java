package Tile;

import Functional.HitBox;
import Functional.Vec2;
import Graphics.SpriteSheet;

public class TileObj extends Tile{

    TileObj(Vec2 v, int id, SpriteSheet sprite, int tileWidth, int tileHeight){
        super(v, id, sprite, tileWidth, tileHeight);
        tile = new HitBox(v, tileWidth, tileHeight);
    }

    public HitBox getHitBox(){
        return tile;
    }

    @Override
    public boolean update(HitBox c) {
        return tile.collide(c);
    }
}