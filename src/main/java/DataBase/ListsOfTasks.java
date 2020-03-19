package DataBase;

import lombok.Data;

public @Data class ListsOfTasks {
    private int id;
    private int userId;
    private int taskId;
    private String name;
}
