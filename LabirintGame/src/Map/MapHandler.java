package Map;

import java.io.*;

public class MapHandler {

    private final int n = 10;
    private String [][] map;

    public MapHandler(){
        map = new String[10][10];
    }

    public void loadMap(String s){

        try{
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String delims = "\\s+";
            for(int row = 0;row < n;row++){
                String line = br.readLine();
                String []tokens = line.split(delims);
                for(int col = 0; col < n ; col++){
                    map[row][col] = tokens[col];
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTile(String ch, int i, int j){
        map[i][j] = ch;
    }

    public int getN() {
        return n;
    }

    public void printMap(){

        for(int i = 0;i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(map[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public String getTile(int i,int j){
        return map[i][j];
    }
}