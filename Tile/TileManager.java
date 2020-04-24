package Tile;

import Functional.HitBox;
import Graphics.*;


import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TileManager {

    private static ArrayList<TileMap> tm;
    private SpriteSheet sprite;
    private int width = 0;
    private int height = 0;
    private int tileWidth;
    private int tileHeight;
    private int spriteSize = 16;
    private boolean rendered = false;

    private TileManager() {
        tm = new ArrayList<TileMap>();
    }

    public TileManager(String path) {
        new TileManager();
        addTileMap(path);
    }

    private void addTileMap(String path) {
        BufferedImageLoader loader = new BufferedImageLoader();

        String imagePath;

        int layers = 0;
        BufferedImage image;

        String[] data = new String[10];

        try {
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = bf.newDocumentBuilder();
            File f = new File(path);


            Document doc = builder.parse(f);
            doc.getDocumentElement().normalize();


            NodeList list = doc.getElementsByTagName("tileset");
            Node node = list.item(0);
            Element element = (Element) node;

            imagePath = element.getAttribute("name");
            try {
                tileWidth = Integer.parseInt(element.getAttribute("tilewidth"));
                tileHeight = Integer.parseInt(element.getAttribute("tileheight"));
            } catch (NumberFormatException e) {
                System.out.println("sorry");
            }
            image = loader.loadImage("../res/" + imagePath + ".png");
            sprite = new SpriteSheet(image, spriteSize);

            list = doc.getElementsByTagName("layer");
            layers = list.getLength();

            for (int i = 0; i < layers; i++) {
                node = list.item(i);
                element = (Element) node;
                data[i] = element.getElementsByTagName("data").item(0).getTextContent();
                if (i == 0) {
                    width = Integer.parseInt(element.getAttribute("width"));
                    height = Integer.parseInt(element.getAttribute("height"));
                    tm.add(new TileMapWalk(data[i], width, height, tileWidth * 3, tileHeight * 3, sprite));
                } else {
                    tm.add(new TileMapObj(data[i], width, height, tileWidth * 3, tileHeight * 3, sprite));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SpriteSheet getSpriteSheet() {
        return sprite;
    }

    public int getMapWidth() {
        return width;
    }

    public int getMapHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeigth() {
        return tileHeight;
    }

    public void update(HitBox c) {
        tm.get(1).update(c);
    }

    public void render(Graphics2D g) {
        //tm.get(0).render(g);
        //if(!rendered){
        for (int i = 0; i < tm.size(); i++) {
            tm.get(i).render(g);
        }
        //rendered = true;
        //}

    }

    public TileMap getTileMap() {
        return tm.get(1);
    }
}