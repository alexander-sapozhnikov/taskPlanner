import DataBase.*;
import TaskPlanner.*;

public class Main {
    public static void main(String[] args) {

        int status = 0;

//        HelloTP helloTP = new HelloTP(new UserDB());
//        status = helloTP.start();
//        int userId = helloTP.getUser().getId();

        int userId = 19;
        Facade facade = new Facade();
        facade.add(new MenuTP());
        facade.add(new TaskTP(new TaskDB(userId), new ListsOfUsersDB(userId)));
        facade.add(new ListTP(new ListsOfUsersDB(userId)));
        facade.add(new SearchTP(new TaskDB(userId)));

        while(true){
            status = facade.get(status);
        }

    }
}