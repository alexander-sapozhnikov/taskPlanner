import DataBase.DBControl;
import DataBase.User;
import org.apache.log4j.Logger;


public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {

        DBControl dataBase = new DBControl();


        TaskPlanner taskPlanner = new TaskPlanner(dataBase);

        taskPlanner.hello();

        dataBase.close();
    }
}
