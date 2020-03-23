import DataBase.DBControl;
import org.apache.log4j.Logger;


public class Main {
    static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {

        DBControl dataBase = new DBControl();

        TaskPlanner taskPlanner = new TaskPlanner(dataBase);

        //taskPlanner.hello();

        taskPlanner.setUser(dataBase.getUser("alex", "123"));
        taskPlanner.setStatus(0);

        while(taskPlanner.getStatus() != -1){
            switch (taskPlanner.getStatus()){
                case 0: taskPlanner.mainMenu(); break;
                case 1: taskPlanner.tasks(); break;
                case 2: taskPlanner.lists(); break;
                case 3: taskPlanner.searchTask(); break;
            }
        }

        dataBase.close();
    }
}
