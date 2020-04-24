package Graphics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.security.Key;

import Functional.GamePanel;
import Functional.Input;

import javax.imageio.ImageIO;

public class Menu {

    public enum ChosenButton{
        play, instructions, quit;
    }
    public enum ChosenWindow{
        menu, instructions
    }
    private String instructions = "Press arrow keys to move in a specific direction";
    private String intructions1 = "Press letter 'z' to change your gun";
    private String instructions2 = "Press space bar to shoot";
    private String instructions3 = "Your goal is to find the money bag which is at the end";
    private String instructions4 = "of the level and it will teleport you to the next level";
    private String instructions5 = "ESC to exit";
    private String instructions6 = "'ENTER' to enter";
    private boolean pressedDown = false;
    private boolean pressedUp = false;
    private ChosenButton button;
    private ChosenWindow window;
    private GamePanel gp;
    private Rectangle playButton;
    private Rectangle quit;
    private Input input;
    private Image img;

    public Menu(GamePanel gp){
        this.gp = gp;
        input = new Input(gp);
        playButton = new Rectangle(gp.getGameW() / 2 - 50, 250, 150, 50);
        quit = new Rectangle(gp.getGameW() / 2 - 50, 450, 150, 50);
        try{
            img = ImageIO.read(new File("res/Background.png"));
        }catch (IOException e){
            System.out.println(e);
        }

        button = ChosenButton.play;
        window = ChosenWindow.menu;
    }

    public ChosenButton nextButton(ChosenButton button){
        if(button == ChosenButton.play){
            return ChosenButton.instructions;
        }else if(button == ChosenButton.instructions){
            return ChosenButton.quit;
        }else if(button == ChosenButton.quit){
            return ChosenButton.play;
        }
        return null;
    }

    public ChosenButton previousButton(ChosenButton button){
        if(button == ChosenButton.play){
            return ChosenButton.quit;
        }else if(button == ChosenButton.instructions){
            return ChosenButton.play;
        }else if(button == ChosenButton.quit){
            return ChosenButton.instructions;
        }
        return null;
    }

    public void buttonFunctions(){
        if (input.keyDown(KeyEvent.VK_ENTER) && button == ChosenButton.play) {
            gp.setGameState(GamePanel.GameState.Play);
        }
        if (input.keyDown(KeyEvent.VK_ENTER) && button == ChosenButton.quit) {
            System.exit(0);
        }
        if(input.keyDown(KeyEvent.VK_ENTER) && button == ChosenButton.instructions){
            window = ChosenWindow.instructions;
        }
        if(input.keyDown(27) && window == ChosenWindow.instructions){
            window = ChosenWindow.menu;
        }
    }

    public void update(){
        buttonFunctions();
        if(input.keyDown(KeyEvent.VK_DOWN) && !pressedDown){
            button = nextButton(button);
            pressedDown = true;
        }else if(!input.keyDown(KeyEvent.VK_DOWN) && pressedDown){
            pressedDown = false;
        }
        if(input.keyDown(KeyEvent.VK_UP) && !pressedUp){
            button = previousButton(button);
            pressedUp = true;
        }else if(!input.keyDown(KeyEvent.VK_UP) && pressedUp){
            pressedUp = false;
        }
    }

    public void render(Graphics2D g){
        g.drawImage(img, 0, 0, null);
        if(window == ChosenWindow.menu){
            Font font = new Font("arial", 1, 30);
            Font font1 = new Font("arial", 1, 50);
            Font font2 = new Font("arial", 1, 20);

            g.setFont(font1);
            g.setColor(Color.ORANGE);
            g.drawString("HUNGRY HEIST", gp.getGameW()/2 - 180, 100);
            g.setFont(font);
            if(button == ChosenButton.play){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.WHITE);
            }
            g.drawString("PLAY", playButton.x, playButton.y);
            if(button == ChosenButton.instructions){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.WHITE);
            }
            g.drawString("INSTRUCTIONS", gp.getGameW() / 2 - 120, 350);
            if(button == ChosenButton.quit){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.WHITE);
            }
            g.drawString("QUIT", quit.x, quit.y);
            g.setColor(Color.WHITE);
            g.setFont(font2);
            g.drawString(instructions6, 0, gp.getGameH()-20);
        }
        if(window == ChosenWindow.instructions){
            g.drawImage(img, 0, 0, null);
            Font font = new Font("arial", 1, 30);
            Font font1 = new Font("arial", 1, 20);
            g.setFont(font);
            g.drawString("INSTRUCTIONS", gp.getGameW()/2-120, 50);
            g.drawString(instructions, 20, 150);
            g.drawString(intructions1, 20, 200);
            g.drawString(instructions2, 20, 250);
            g.drawString(instructions3, 20, 300);
            g.drawString(instructions4, 20, 330);
            g.setFont(font1);
            g.drawString(instructions5, 0, gp.getGameH()-20);
        }
    }

    public ChosenButton getButton(){
        return button;
    }
}
