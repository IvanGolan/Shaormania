package Entity;
import GameState.GameStateManager;
import GameState.GameState;
import Miscellanious.Database;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends MapObject{

    //player stuff
    private int health;
    private int maxHealth;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    private Database database = Database.getInstance();
    private int score;
    //attack
    private boolean attacking;
    private int attackDamage;
    private int attackRange;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {4,6,4,2,5};

    //animation actions
    private static final int IDLE = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int ATTACKING = 4;

    public Player(TileMap tm){
        super(tm);

        width = 50;
        height = 37;

        cwidth = 20;
        cheight = 30;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        score = 0;
        attackDamage = 8;
        attackRange = 45;

        //load sprites

        try {

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Player/Player.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0;i < 5; i++){
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for(int j = 0; j < numFrames[i]; j++){
                        bi[j] = spritesheet.getSubimage(j * width, i * height,width,height);

                }
                sprites.add(bi);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }

    public int getHealth(){
        return health;
    }
    public int getMaxHealth(){
        return maxHealth;
    }

    public void setAttacking(){
        attacking = true;
    }

    private void getNextPosition(){

        //movement
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
        else {
            if(dx > 0){
                dx -= stopSpeed;
                if(dx < 0){
                    dx = 0;
                }
            }
            else if(dx < 0){
                dx += stopSpeed;
                if(dx > 0){
                    dx = 0;
                }
            }
        }

        //cannot attack while moving
        if( (currentAction == ATTACKING) && !(jumping || falling) ){
            dx = 0;
        }

        //jumping
        if(jumping && !falling){
            dy = jumpStart;
            falling = true;
        }

        //falling
        if(falling){

            dy += fallSpeed;

            if(dy > 0){
                jumping = false;
            }

            if(dy < 0 && !jumping){
                dy += stopJumpSpeed;
            }

            if(dy > maxFallSpeed){
                dy = maxFallSpeed;
            }
        }

    }

    public void checkAttack(ArrayList<Enemy> enemies){
        for(int i = 0; i <enemies.size(); i++){
            Enemy e = enemies.get(i);
            if(attacking){
                if(facingRight){
                    if(e.getx() > x && e.getx() < x + attackRange && e.gety() > y - height && e.gety() < y + height / 2){
                        e.hit(attackDamage);

                    }
                }
                else {
                    if(e.getx() < x && e.getx() > x - attackRange && e.gety() > y - height && e.gety() < y + height / 2){
                        e.hit(attackDamage);
                    }
                }
            }
            if(intersects(e)){
                hit(e.getDamage());
            }
        }

    }

    public void hit(int damage){
        if(flinching){
            return;
        }
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public boolean getDead(){
        return dead;
    }
    public void setScore(int score){
        this.score += score;
    }
    public int getScore(){
        return score;
    }
    public void update() {

        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        //check attack has stopped
        if(currentAction == ATTACKING){
            if(animation.hasPlayedOnce()) attacking = false;
        }
        if(attacking){
           if(currentAction != ATTACKING){

               currentAction = ATTACKING;
               animation.setFrames(sprites.get(ATTACKING));
               animation.setDelay(35);
               width = 60;
           }
        }
        else if(dy > 0){
            if(currentAction != FALLING){

                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 32;
            }
        }
        else if(dy < 0){
            if(currentAction != JUMPING){

                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 32;

            }
        }
        else if(left || right){
            if(currentAction != RUNNING ){
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(40);
                width = 32;
            }
        }
        else {
            if(currentAction != IDLE){

                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 32;

            }
        }
        animation.update();

        //set direction
        if(currentAction != ATTACKING){
            if(right) facingRight = true;
            if(left) facingRight = false;
        }
        //check done flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000){
                flinching = false;
            }
        }
        try{
            if(dead){
                database.addScore(score);
                database.Close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g){

        setMapPosition();

        //draw player

        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000;
            if(elapsed / 100 % 2 == 0){
                return;
            }
        }
        if(facingRight){
            g.drawImage(animation.getImage(),(int)(x + xmap -width /2),(int)(y + ymap -height / 2),null);
        }
        else{
            if(currentAction == ATTACKING){
                g.drawImage(animation.getImage(),(int)(x + xmap - width / 2 + width ), (int)(y + ymap -height / 2),-width + 10,height,null);
            }
            else {
                g.drawImage(animation.getImage(),(int)(x + xmap - width / 2 + width ), (int)(y + ymap -height / 2),-width - 20,height,null);
            }
        }

    }


}
