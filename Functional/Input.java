package Functional;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener{

    private boolean keyPressed[] = new boolean[256];
    private int counter = 0;
    private int counterMax = 4;

    public Input(GamePanel game){
        game.addKeyListener(this);
    }

    public boolean keyDown(int keyCode){
        return keyPressed[keyCode];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
        if(e.getKeyCode() == 90){
            if(counter < counterMax){
                counter++;
            }
            if(counter == counterMax){
                counter = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }

    public int getCounter(){
        return counter;
    }
}