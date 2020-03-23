import DataBase.ListsOfUsers;

public interface funTaskPlanner {
    void hello();
    void logIn();
    void signOut();

    void mainMenu();

    void tasks();

    void lists();
    void makeList();
    void editList(ListsOfUsers list);
    void searchTask();
}
