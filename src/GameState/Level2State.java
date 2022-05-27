package GameState;

import Enemies.Bat;
import Enemies.ToxicEnemy;
import Enemies.Villager;
import Enemies.Worm;
import Entity.Enemy;
import Miscellanious.*;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level2State extends GameState{

    private Background bg1;
    private Background bg2;

    private Player player;
    private HUD hud;
    private ArrayList<Enemy> enemies;
    private ArrayList<Shaorma> shaormas;
    private ArrayList<Explosion> explosions;
    private TileMap tileMap;
    private Sounds level2Music;
    private Portal portal;
    private int contor = 0;
    public Level2State(GameStateManager gsm){

        this.gsm = gsm;
        bg1 = new Background("/Backgrounds/Level2BG1.png",3);
        bg2 = new Background("/Backgrounds/Level2BG2.png",3);
        init();

    }

    public void init(){

        tileMap = new TileMap(32);
        tileMap.loadTiles("/TileSets/TilesetLvl1.png");
        tileMap.loadMap("/Maps/level2.map");
        tileMap.setPosition(0,0);
        tileMap.setTween(0.2);

        player = new Player(tileMap);
        player.setPosition(80,160);

        populateEnemies();

        explosions = new ArrayList<Explosion>();
        shaormas = new ArrayList<Shaorma>();
        hud = new HUD(player);
        level2Music = new Sounds();
        portal = new Portal(tileMap,2464,90);
        /*level2Music = new Sounds();
        level2Music.setFile(2);
        level2Music.play();
        level2Music.loop();*/
    }

    private void populateEnemies(){

        enemies = new ArrayList<Enemy>();
        Villager s;
        Worm t;
        Point[] batPoints = new Point[]{
                new Point(190,160),
                new Point(416,70),
                new Point(1536,160),
                new Point(1400,160)

        };
        for(int i = 0;i < batPoints.length;i++){
            s = new Villager(tileMap);
            s.setPosition(batPoints[i].x,batPoints[i].y);
            enemies.add(s);
        }

        Point[] wormPoints = new Point[]{new Point(992,70),
                new Point(1532,60),
                new Point(2112,140)
        };
        for (int i = 0; i < wormPoints.length;i++){
            t = new Worm(tileMap);
            t.setPosition(wormPoints[i].x,wormPoints[i].y);
            enemies.add(t);
        }
    }
    public void update(){

        bg1.update();
        bg2.update();

        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(),GamePanel.HEIGHT - player.gety());

        //player check attack
        player.checkAttack(enemies);

        //update all enemies
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()){
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
                shaormas.add(new Shaorma(tileMap, e.getx(),e.gety()));
            }
        }

        for(int i = 0;i < explosions.size(); i++){
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()){
                explosions.remove(i);
                i--;
            }
        }
        for(int i = 0;i < shaormas.size();i++){
            if(player.intersects(shaormas.get(i))){
                player.setScore(15);
                shaormas.remove(i);
                i--;
            }
        }

    }

    public void draw(Graphics2D g){

        boolean touchPortal = false;
        boolean isDead = false;
        //bg draw
        bg1.draw(g);
        bg2.draw(g);

        // draw tilemap
        tileMap.draw(g);

        //player draw

        player.draw(g);
        //enemies draw

        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }

        //draw explosions
        for(int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }
        //draw shaormas
        for(int i = 0; i < shaormas.size(); i++){
            shaormas.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            shaormas.get(i).draw(g);
        }
        //draw hud
        hud.draw(g);

        //draw game over message
        if(player.getDead()){
            isDead = true;
            if(isDead){
                Font font = new Font("Arial",Font.PLAIN,30);
                g.setColor(Color.BLACK);
                g.setFont(font);
                g.drawString("You Died",100,80);
            }
            else {
                gsm.setState(GameStateManager.MENUSTATE);
            }
        }
        //draw win message
        if(player.intersects(portal)){
            touchPortal = true;
            if(touchPortal){
                contor++;
            }
            if(contor <= 300){
                if(player.intersects(portal)){
                    Font font = new Font("Arial",Font.PLAIN,30);
                    g.setColor(Color.WHITE);
                    g.setFont(font);
                    g.drawString("You Found Your Family",30,80);
                }
            }
            else{
                gsm.setState(GameStateManager.MENUSTATE);
            }
        }

    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_A) player.setLeft(true);
        if(k == KeyEvent.VK_D) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_SPACE) player.setAttacking();
    }

    public void keyReleased(int  k){
        if(k == KeyEvent.VK_A) player.setLeft(false);
        if(k == KeyEvent.VK_D) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);
    }

}
