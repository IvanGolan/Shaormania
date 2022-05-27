package Miscellanious;

import Entity.MapObject;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Shaorma extends MapObject {

    private int xmap;
    private int ymap;


    private BufferedImage image;
    public Shaorma(TileMap tm,int x,int y){
        super(tm);
        this.x = x;
        this.y = y;

        width = 16;
        height = 16;

        cwidth = 16;
        cheight = 16;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/Shaorma.png"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setMapPosition(int x,int y){
        this.xmap = x;
        this.ymap = y;
    }
    public void draw(Graphics2D g){

        g.drawImage(image,(int)x + xmap - width / 2,
                (int)y + ymap - height / 2,
                null);
    }

}
