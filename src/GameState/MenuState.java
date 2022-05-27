package GameState;

import Miscellanious.Sounds;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState{

    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private Background bg5;

    private int currentChoice = 0;
    private String[] options = {"Start","Help","Quit"};

    private Color titleColor;
    private Font titleFont;

    private Font font;
    private Sounds menuMusic;
    public MenuState(GameStateManager gsm){

        this.gsm = gsm;

        try{
            bg1 = new Background("/Backgrounds/parallax-mountain-bg.png",0);
            bg1.setVector(-0.5,0);
            bg2 = new Background("/Backgrounds/parallax-mountain-montain-far.png",0);
            bg2.setVector(-0.5,0);
            bg3 = new Background("/Backgrounds/parallax-mountain-mountains.png",0);
            bg3.setVector(-0.5,0);
            bg4 = new Background("/Backgrounds/parallax-mountain-trees.png",0);
            bg4.setVector(-0.5,0);
            bg5 = new Background("/Backgrounds/parallax-mountain-foreground-trees.png",0);
            bg5.setVector(-0.5,0);

            titleColor = new Color(0,0,128);
            titleFont = new Font("Century Gothic",Font.PLAIN,30);

            font = new Font("Arial",Font.PLAIN,15);

/*
            menuMusic = new Sounds();
            menuMusic.setFile(0);
            menuMusic.play();
            menuMusic.loop();*/

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void init(){}
    public void update(){
        bg1.update();
        bg2.update();
        bg3.update();
        bg4.update();
        bg5.update();
    }
    public void draw(Graphics2D g){
        //bg draw
        bg1.draw(g);
        bg2.draw(g);
        bg3.draw(g);
        bg4.draw(g);
        bg5.draw(g);
        //title draw

        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Shaormania",105,55);

        //draw menu opts

        g.setFont(font);
        for(int i = 0;i<options.length;i++){
            if(i == currentChoice){
                g.setColor(Color.BLACK);
            }
            else{
                g.setColor(Color.RED);
            }
            g.drawString(options[i],170,80 + i * 20);
        }
    }

    private void select(){
        if(currentChoice == 0){
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1){
            //help
        }
        if(currentChoice == 2){
            System.exit(0);
        }
    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_ENTER){
            select();
        }

        if(k == KeyEvent.VK_UP){
            currentChoice--;
            if(currentChoice == -1){
                currentChoice = options.length - 1;
            }
        }

        if(k == KeyEvent.VK_DOWN){
            currentChoice++;
            if(currentChoice == options.length){
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k){}
}
