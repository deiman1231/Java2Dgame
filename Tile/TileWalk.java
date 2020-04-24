package Tile;

import Functional.HitBox;
import Functional.Vec2;
import Graphics.SpriteSheet;

public class TileWalk extends Tile{

    TileWalk(Vec2 v, int id, SpriteSheet sprite, int tileWidth, int tileHeight){
        super(v, id, sprite, tileWidth, tileHeight);
    }

    @Override
    public boolean update(HitBox c) {
        return false;
    }
}