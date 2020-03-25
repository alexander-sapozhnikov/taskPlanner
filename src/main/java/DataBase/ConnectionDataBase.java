package DataBase;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataBase {

    private static Connection conn = null;
    static Logger logger = Logger.getLogger(ConnectionDataBase.class);


    public static Connection getConnection(){
        if(conn == null){
            String url = "jdbc:postgresql://localhost:5432/taskPlanner";
            try {
                Class.forName("org.postgresql.Driver");
                String userDB = "postgres";
                String passwordDB = "";
                conn = DriverManager.getConnection(url, userDB, passwordDB);
            } catch (SQLException e) {
                logger.error(e);
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        return conn;
    }

    public static void Close (){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
}
