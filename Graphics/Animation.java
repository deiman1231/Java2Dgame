package Graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation{
    private int delay;
    private int currentFrame;
    private int lastFrame;
    private int count;
    
    public void init(ArrayList<BufferedImage> img, int i){
        delay = i;
        lastFrame = img.size();
        currentFrame = 0;
    }

    public void update(){
        if(delay == -1) return;

        count++;
        if(delay == count){
            currentFrame++;
            count = 0;
        }
        if(currentFrame == lastFrame){
            currentFrame = 0;
        }
    }
    public void setDelay(int delay) {this.delay = delay;} 
    public int getCurrentFrame() { return currentFrame; }
}