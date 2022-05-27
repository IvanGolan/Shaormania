package Miscellanious;


import java.sql.*;

public class  Database {
    private volatile static Database instance = null;

    private Connection c = null;
    private Statement stmt = null;



    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:GameDatabase.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();


            this.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private void createTable(){
        if(c != null){
            try{
                String Sql = "CREATE TABLE TabelJoc " +
                        "(id INT NOT NULL, " +
                        "Score INT NOT NULL );";

                stmt.executeUpdate(Sql);
                c.commit();

                System.out.println("Database tables if didn't exist before, have been created successfully!");


            }catch (Exception e){
                System.out.println( e.getMessage() );
            }
        }
    }


    public void addScore(int score) {
        int id = 1;

        try {

            String sql = "INSERT INTO TabelJoc (id ,Score) VALUES ("+id+",'"+ score +"');";
            stmt.executeUpdate(sql);
            c.commit();

        } catch ( Exception e ) {
            System.out.println(e.getMessage());
        }

    }
    public void Close(){
        if(c != null){
            try {
                stmt.close();
                c.close();
                System.out.println("The connection has successfully been closed!");
            }
            catch(Exception e){
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
        }
    }
}