import Functional.GamePanel;
import Functional.Input;
import Graphics.Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Launcher {

    private JFrame obj;
    private GamePanel gp;

    private void game() {
        obj = new JFrame();
        gp = new GamePanel(800, 600);

        obj.add(gp);
        obj.pack();
        obj.setTitle("game");
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public static void main(String[] args) {
        Launcher l = new Launcher();
        l.game();
    }
}