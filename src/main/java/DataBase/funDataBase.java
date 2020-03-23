package DataBase;

import lombok.NonNull;

import java.util.List;

public interface funDataBase {
    int setUser(@NonNull User user);
    User getUser(String name, String password);
    User getFromIdUser(int id);
    boolean updateUser(@NonNull User user);
    boolean deleteUser(int id);

    int setTask(@NonNull Task task, int userId, int listId);
    List<Task> getTasks( String name, String description);
    Task getFromIdTask(int id);
    boolean updateTask(@NonNull Task task);
    boolean deleteTask(int id);

    int setListsOfUsers(ListsOfUsers listsOfUsers);
    List<ListsOfUsers> getListsOfUsers(int userId);
    boolean updateListsOfUsers(@NonNull ListsOfUsers listsOfUsers);
    boolean deleteListsOfUsers(int id);

    User getWatcherForTasks(int taskId);
}
