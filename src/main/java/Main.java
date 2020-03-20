import DataBase.DBControl;
import DataBase.ListsOfUsers;
import DataBase.Task;
import DataBase.User;
import org.apache.log4j.Logger;


public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        DBControl dbControl = new DBControl();

        /*User user = new User();
        user.setId(15);
        user.setUsername("nsssssa");
        user.setPassword("123");
        logger.debug( dbControl.updateUser(user));*/

        /*ask task = new Task();
        task.setId(2);
        task.setName("to do univer");
        task.setDescription("im very very  tired");
        logger.debug(dbControl.setTask(task, 1, 1));*/

        /*ListsOfUsers lists = new ListsOfUsers();
        lists.setName("Netcracker");
        lists.setUserId(1);
        logger.debug(dbControl.getListsOfUsers(1).size());*/

        logger.debug(dbControl.getUserFromWathcer(4));

        dbControl.close();
    }
}
