package Miscellanious;

import Entity.MapObject;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal extends MapObject {
    private int xmap;
    private int ymap;


    private BufferedImage image;
    public Portal(TileMap tm, int x, int y){
        super(tm);
        this.x = x;
        this.y = y;

        width = 32;
        height = 32;

        cwidth = 45;
        cheight = 45;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/Portal.png"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setMapPosition(int x,int y){
        this.xmap= x;
        this.ymap = y;
    }
    public void draw(Graphics2D g){

        setMapPosition();
        g.drawImage(image,(int)x + xmap - width / 2,
                (int)y + ymap - height / 2,
                null);
    }
}
