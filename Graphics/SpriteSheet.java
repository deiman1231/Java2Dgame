package Graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet{

    private BufferedImage image;
    private ArrayList<BufferedImage> images;
    private int colms;
    private int rows;
    private int size;
    private int xOffSet = 0;
    private int yOffSet = 0;

    public SpriteSheet(BufferedImage image, int size){
        this.image = image;
        this.size = size;
        colms = image.getWidth() / size;
        rows = image.getHeight() / size;
    }

    public BufferedImage takeImage(int col, int row){
        return image.getSubimage(col*size, row*size, size, size);
    }

    public BufferedImage takeImage(int col, int row, int size){
        return image.getSubimage(col*size, row*size, size, size);
    }

    public BufferedImage takeImageEntity(int col, int row){
        return image.getSubimage(col*size + xOffSet, row*size + yOffSet, size-xOffSet, size-yOffSet);
    }

    public ArrayList<BufferedImage> getArray(int row){
        images = new ArrayList<>();
        for(int i = 0; i < colms; i++){
            images.add(takeImageEntity(i, row));
        }
        return images;
    }

    public int getColms(){
        return colms;
    }

    public int getRows(){
        return rows;
    }

    public BufferedImage getImage() {
        return image;
    }
}