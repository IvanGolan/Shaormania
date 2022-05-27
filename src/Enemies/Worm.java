package Enemies;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Worm extends Enemy {

    private BufferedImage[] sprites;

    public Worm(TileMap tm){

        super(tm);
        moveSpeed = 0.8;
        maxSpeed = 0.8;
        fallSpeed = 0.2;

        maxFallSpeed = 10;
        width = 64;
        height = 64;

        cwidth = 40;
        cheight = 40;

        health = maxHealth = 10;
        damage = 1;

        //load sprites
        try{

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Enemies/Worm.png"));

            sprites = new BufferedImage[6];
            for(int i = 0;i < sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i * width,0,width,height);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(200);

        right = true;
        facingRight = true;
    }

    private void getNextPosition(){

        if(left){
            dx -= moveSpeed;
            if(dx < -maxSpeed){
                dx = -maxSpeed;
            }
        }
        else if(right){
            dx += moveSpeed;
            if(dx > maxSpeed){
                dx = maxSpeed;
            }
        }

        if(falling){
            dy += fallSpeed;
        }
    }

    public void update(){

        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400){
                flinching = false;
            }
        }

        if(right && dx == 0){
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0){
            right = true;
            left = false;
            facingRight = true;
        }
        animation.update();
    }

    public void draw(Graphics2D g){

        //if(notOnScreen())
        // return;
        setMapPosition();

        if(facingRight){
            g.drawImage(animation.getImage(),(int)(x + xmap -width /2),(int)(y + ymap -height / 2),null);
        }
        else {
            g.drawImage(animation.getImage(),(int)(x + xmap - width / 2 + width ), (int)(y + ymap -height / 2),-width,height,null);
        }

    }
}

