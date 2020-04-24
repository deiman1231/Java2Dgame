package Functional;

public class HitBox{
    private Vec2 v;
    private int width;
    private int height;

    public HitBox(Vec2 v, int width, int height){
        this.v = v;
        this.width = width;
        this.height = height;
    }

    public boolean collide(HitBox B){
        return (v.getX() < B.getWidth() + B.getVector().getX() &&
        v.getX() + width > B.getVector().getX() &&
        v.getY() < B.getHeight() + B.getVector().getY() &&
        v.getY() + height > B.getVector().getY());

    }

    public boolean collide(int x, int y, int width, int height){
        return (v.getX() < width + x &&
        v.getX() + this.width > x &&
        v.getY() < height + y &&
        v.getY() + this.height > y);
    }

    private Vec2 getVector(){
        return v;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}