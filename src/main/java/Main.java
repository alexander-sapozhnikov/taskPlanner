import DataBase.DBControl;
import DataBase.ListsOfUsers;
import DataBase.Task;
import DataBase.User;
import org.apache.log4j.Logger;


public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        DBControl dbControl = new DBControl();


        User user = new User();
        user.setUsername("Lola");
        user.setPassword("2d4");
        int id = dbControl.setUser(user);
        logger.debug(id);
//
//        user.setUsername("Vova");
//        user.setPassword("fsaf");
//        logger.debug(dbControl.setUser(user));

        ListsOfUsers lists = new ListsOfUsers();
        lists.setName("Work");
        lists.setUserId(id);
        int listId = dbControl.setListsOfUsers(lists);
        logger.debug(listId);

//        lists.setName("Net");
//        lists.setUserId(5);
//        logger.debug(dbControl.setListsOfUsers(lists));

        Task task = new Task();
        task.setName("to do smt");
        task.setDescription("im sitting here");
        logger.debug(dbControl.setTask(task, id, listId));


        task.setName("to do homeWork");
        task.setDescription("im happy so pipez");
        logger.debug(dbControl.setTask(task, id, listId));

        task.setName("reading a interesting book");
        task.setDescription("just relax");
        logger.debug(dbControl.setTask(task, id, listId));

        logger.debug(dbControl.deleteUser(id));

        dbControl.close();
    }
}
