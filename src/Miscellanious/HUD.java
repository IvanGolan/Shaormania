package Miscellanious;

import Entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;

    private BufferedImage image;
    private Font font;

    public HUD(Player p){

        player = p;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/hearts_hud.png"));
            font = new Font("Arial",Font.PLAIN,14);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){

        g.drawImage(image, 10, 10, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealth() + "/" +player.getMaxHealth(),30,23);
        g.drawString("Score:" + player.getScore(), 10, 40);
    }
}
