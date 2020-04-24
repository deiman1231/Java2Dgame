package Functional;

public class Vec2{
    private int x;
    private int y;
    private double xd;
    private double yd;

    private static int worldX;
    private static int worldY;
    
    public Vec2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vec2(double x, double y){
        this.xd = x;
        this.yd = y;
    }

    public Vec2(Vec2 v){
        this.x = v.getX();
        this.y = v.getY();
    }

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public double getXd() { return xd; }
    public double getYd() { return yd; }
    public void setXd(double Xd){
        this.xd = Xd;
    }
    public void setYd(double Yd){
        this.yd = Yd;
    }
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }
    public void setV(Vec2 v){
        this.setX(v.getX());
        this.setY(v.getY());
    }

    static void setWorldVar(int x, int y) {
        worldX = x;
        worldY = y;
    }

    public static int getWorldVarX() {
        return worldX;
    }

    public static int getWorldVarY() {
        return worldY;
    }

    Vec2 getWorldVar() {
        return new Vec2(x-worldX, y-worldY);
    }

    @Override
    public String toString(){
        return ("(" + x + "," + y + ")");
    }
    
}