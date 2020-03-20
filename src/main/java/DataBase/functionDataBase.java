package DataBase;

import lombok.NonNull;

import java.util.List;

public interface functionDataBase {
    int setUser(@NonNull User user);
    User getUser(String name, String password);
    User getFromIdUser(int id);
    boolean updateUser(@NonNull User user);

    int setTask(@NonNull Task task, int listId, int userId);
    List<Task> getTasks( String name, String description);
    Task getFromIdTask(int id);
    boolean updateTask(@NonNull Task task);

    int setlistsOfUsers(ListsOfUsers listsOfUsers);
    List<ListsOfUsers> getListsOfUsers(int userId);
    boolean updateListsOfUsers(@NonNull ListsOfUsers listsOfUsers);

    User getUserFromWathcer(int taskId);
}
