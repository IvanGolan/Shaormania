package GameState;

import Enemies.Bat;
import Enemies.ToxicEnemy;
import Entity.Enemy;
import Miscellanious.*;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Level1State extends GameState{

    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private Background bg5;

    private Player player;
    private HUD hud;
    private ArrayList<Enemy> enemies;
    private ArrayList<Shaorma> shaormas;
    private ArrayList<Explosion> explosions;
    private TileMap tileMap;
    private Sounds level1Music;
    private Portal portal;
    private int contor = 0;
    public Level1State(GameStateManager gsm){

        this.gsm = gsm;
        bg1 = new Background("/Backgrounds/plx-1.png",3);
        bg2 = new Background("/Backgrounds/plx-2.png",3);
        bg3 = new Background("/Backgrounds/plx-3.png",3);
        bg4 = new Background("/Backgrounds/plx-4.png",3);
        bg5 = new Background("/Backgrounds/plx-5.png",3);
        init();

    }

    public void init(){

        tileMap = new TileMap(32);
        tileMap.loadTiles("/TileSets/TilesetLvl1.png");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0,0);
        tileMap.setTween(0.2);

        player = new Player(tileMap);
        player.setPosition(80,160);

        populateEnemies();

        explosions = new ArrayList<Explosion>();
        shaormas = new ArrayList<Shaorma>();
        hud = new HUD(player);
        portal = new Portal(tileMap,2000,90);
       /* level1Music = new Sounds();
        level1Music.setFile(1);
        level1Music.play();
        level1Music.loop();*/

    }

    private void populateEnemies(){

        enemies = new ArrayList<Enemy>();
        Bat s;
        ToxicEnemy t;
        Point[] batPoints = new Point[]{
                new Point(190,160),
                new Point(620,160),
                new Point(1984,70)
        };
        for(int i = 0;i < batPoints.length;i++){
            s = new Bat(tileMap);
            s.setPosition(batPoints[i].x,batPoints[i].y);
            enemies.add(s);
        }

        Point[] toxicPoints = new Point[]{new Point(1696,160),
                                          new Point(620,20)
                                          };
        for (int i = 0; i < toxicPoints.length;i++){
            t = new ToxicEnemy(tileMap);
            t.setPosition(toxicPoints[i].x,toxicPoints[i].y);
            enemies.add(t);
        }
    }
    public void update(){

        bg1.update();
        bg2.update();
        bg3.update();
        bg4.update();
        bg5.update();

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

        boolean touchPortal = false ;
        boolean isDead = false;
        //bg draw
        bg1.draw(g);
        bg2.draw(g);
        bg3.draw(g);
        bg4.draw(g);
        bg5.draw(g);

        // draw tilemap
        tileMap.draw(g);

        //player draw

        player.draw(g);
        //enemies draw

        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }

        portal.draw(g);

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
        if(player.getDead()) {
            /*isDead = true;
            if(isDead){
                contor++;
            }
            if(contor <= 100){
                Font font = new Font("Arial",Font.PLAIN,30);
                g.setColor(Color.BLACK);
                g.setFont(font);
                g.drawString("You Died",100,80);
            }
            else {*/
            gsm.setState(GameStateManager.MENUSTATE);
            //}
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
                        g.setColor(Color.BLACK);
                        g.setFont(font);
                        g.drawString("You Escaped the Jungle",30,80);
                    }
                }
                else{
                    gsm.setState(GameStateManager.LEVEL2STATE);
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
