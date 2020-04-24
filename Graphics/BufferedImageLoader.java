package Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageLoader{

    public BufferedImage loadImage(String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            System.out.println(path);
            e.printStackTrace();
        }
        return image;
    }
}