import DataBase.*;
import org.apache.log4j.Logger;
import TaskPlanner.*;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {

        int status = 0;

        HelloTP helloTP = new HelloTP(new UserDB());

        status = helloTP.start();

        int userId = helloTP.getUser().getId();

        TaskPlannerAble taskTP = new TaskTP(new TaskDB(userId), new ListsOfUsersDB(userId), 1);
        TaskPlannerAble listTP = new ListTP(new ListsOfUsersDB(userId), 2);
        TaskPlannerAble searchTP = new SearchTP(new TaskDB(userId), 3);


        while(true){
            switch (status){
                case 0: status = helloTP.mainMenu(); break;
                case 1: status = taskTP.start(); break;
                case 2: status = listTP.start(); break;
                case 3: status = searchTP.start(); break;
            }
        }

    }
}