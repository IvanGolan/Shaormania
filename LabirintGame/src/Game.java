import Map.MapHandler;
import Player.Player;

import java.util.*;

public class Game {

    private boolean running;
    MapHandler currentMap;
    MapHandler gameMap;
    Player player;

    HashMap<Integer,Integer> prevTiles = new HashMap<Integer, Integer>();
    private int startX;
    private int startY;
    private final int up = 5;
    private final int down = 2;
    private final int left = 1;
    private final int right = 3;

    Game(){
        //starting game
        System.out.println("<----Maze Game---->");
        System.out.println("Starting game...");
        System.out.println("Game controls:");
        System.out.println("w-->Move Up");
        System.out.println("s-->Move Down");
        System.out.println("a-->Move Left");
        System.out.println("d-->Move Right");
        System.out.println("o-->Interact with things");

        running = true;
        //map stuff
        gameMap = new MapHandler();
        currentMap = new MapHandler();
        gameMap.loadMap("/Map/Map.map");
        initCurrMap();

        //player stuff
        spawnPlayer();
        loadCurrMap();
        currentMap.printMap();
    }

    public void setPlayerPos(int x,int y){
        currentMap.setTile(player.playerIcon,x,y);
    }

    public void run(){
        while(running){
            update();
        }
    }
    public void update(){

        showCurrTile();

        playerMovement();

    }

    public void playerMovement(){

        Scanner in = new Scanner(System.in);
        System.out.println("Next move:");
        String move = in.nextLine();

        //last tile
        currentMap.setTile(gameMap.getTile(player.getPosx(), player.getPosy()), player.getPosx(), player.getPosy());

        switch (move){
            case "w":
                player.moveUp(checkCollision(up, Directions.UP));
                player.updateMoves();
                break;
            case "s":
                player.moveDown(checkCollision(down, Directions.DOWN));
                player.updateMoves();
                break;
            case "d":
                player.moveRight(checkCollision(right, Directions.RIGHT));
                player.updateMoves();
                break;
            case "a":
                player.moveLeft(checkCollision(left, Directions.LEFT));
                player.updateMoves();
                break;
            case "o":
                if(gameMap.getTile(player.getPosx(), player.getPosy()).equals("C")){
                    player.foundTreasure();
                    gameMap.setTile("O", player.getPosx(), player.getPosy());
                    System.out.println(player.getNrTreasure());
                }
                if(gameMap.getTile(player.getPosx(),player.getPosy()).equals("D") && startX != player.getPosx() && startY != player.getPosy()){
                    System.out.println("You Won");
                    System.out.println("Number of moves made is: " + player.getNrMoves());
                    System.out.println("Number of treasures collected is: " + player.getNrTreasure());
                    if(player.getNrTreasure() == 5){
                        System.out.println("Congratulations,you got the max amount of treasures!!");
                    }
                    System.exit(1);
                }
                break;
            default:
                System.out.println("Wrong key entered");
                System.exit(1);
                break;
        }
        setPlayerPos(player.getPosx(), player.getPosy());
        loadCurrMap();
        currentMap.printMap();
    }

    public int checkCollision(int move, Directions direction) {
        int tilesToMove = 0;

        switch (direction) {
            case UP:
                for (int i = player.getPosx(); i > player.getPosx() - up ; i--) {
                    if (gameMap.getTile(i - 1, player.getPosy()).equals("W")) {
                        break;
                    }
                    else {
                        tilesToMove++;
                    }
                }
                break;
            case DOWN:
                for (int i = player.getPosx(); i < player.getPosx() + down; i++) {
                    if (gameMap.getTile(i + 1, player.getPosy()).equals("W")) {
                        break;
                    }
                    else {
                        tilesToMove++;
                    }
                }
                break;
            case LEFT:
                for(int i = player.getPosy(); i > player.getPosy() - left; i--){
                    if(gameMap.getTile(player.getPosx(), i - 1).equals("W")){
                        break;
                    }
                    else {
                        tilesToMove++;
                    }
                }
                break;
            case RIGHT:
                for(int i = player.getPosy(); i < player.getPosy() + right; i++){
                    if(gameMap.getTile(player.getPosx(), i + 1).equals("W")){
                        break;
                    }
                    else {
                        tilesToMove++;
                    }
                }
                break;
        }
        return tilesToMove;
    }
    public void showCurrTile(){

        String tile = gameMap.getTile(player.getPosx(), player.getPosy());

        switch (tile){
            case "O":
                System.out.println("You're currently on an empty tile");
                break;
            case "D":
                System.out.println("You're currently on a DOOR,\n" +
                        "            __________\n" +
                        "           |  __  __  |\n" +
                        "           | |  ||  | |\n" +
                        "           | |  ||  | |\n" +
                        "           | |__||__| |\n" +
                        "           |  __  __()|\n" +
                        "           | |  ||  | |\n" +
                        "           | |  ||  | |\n" +
                        "           | |  ||  | |\n" +
                        "           | |  ||  | |\n" +
                        "           | |__||__| |\n" +
                        "           |__________|\n " +
                        "press o if it's not the one you spawned in");
                break;
            case "C":
                System.out.println("You're currently on a TREASURE,\n" +
                        "         __________\n" +
                        "        /\\____;;___\\\n" +
                        "       | /         /\n" +
                        "       `. ())oo() .\n" +
                        "        |\\(%()*^^()^\\\n" +
                        "       %| |-%-------|\n" +
                        "      % \\ | %  ))   |\n" +
                        "      %  \\|%________|\n" +
                        "      press o to claim it");
                break;
        }
    }

    public void spawnPlayer(){

        Scanner in = new Scanner(System.in);
        System.out.println("Choose the starting door (1-2): ");
        int door = in.nextInt();

        switch(door){
            case 1:
                startX = 1;
                startY = 1;
                player = new Player(1,1);
                currentMap.setTile(player.playerIcon, 1,1);
                break;
            case 2:
                startX = 8;
                startY = 8;
                player = new Player(8,8);
                currentMap.setTile(player.playerIcon, 8,8);
        }
    }

    public void loadCurrMap(){

        int x = player.getPosx();
        int y = player.getPosy();

        for(int i = 0; i < currentMap.getN(); i++){
            for(int j = 0; j < currentMap.getN(); j++){
                if((x-1 == i && y == j) || (x + 1 == i && y == j) || (x == i && y-1 == j) || (x == i && y+1==j) || (x-1 == i && y + 1==j)
                        || (x+1==i && y-1==j) || (x+1==i && y+1==j) || (x-1 == i && y - 1 ==j)){
                    currentMap.setTile(gameMap.getTile(i,j),i,j);
                }
                if((x == i && y == j)){
                    currentMap.setTile(player.playerIcon,i,j);
                }
            }
        }

    }

    public void initCurrMap(){
        for(int i = 0;i < currentMap.getN(); i++){
            for(int j = 0;j < currentMap.getN(); j++){
                currentMap.setTile("X",i,j);
            }
        }
    }

}