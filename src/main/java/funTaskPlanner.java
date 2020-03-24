import DataBase.ListsOfUsers;
import DataBase.Task;

public interface funTaskPlanner {
    void hello();
    void logIn();
    void signOut();

    void mainMenu();

    void tasks();
    void makeTask();
    void menuEditTask(Task task);
    void editTask(Task task);

    void lists();
    void makeList();
    void editList(ListsOfUsers list);

    void searchTask();
}
