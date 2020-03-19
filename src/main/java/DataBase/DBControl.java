package DataBase;

import java.sql.*;


public class DBControl {

    private Connection conn = null;
    private String userDB = "postgres";
    private String passwordDB = "";

    public DBControl(){
        String url = "jdbc:postgresql://localhost:5432/taskPlanner";
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, userDB, passwordDB);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void close (){
        try{
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
