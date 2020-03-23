package DataBase;

import lombok.Data;

public @Data class ListsOfUsers {
    private int id;
    private int userId;
    private String name;

    @Override
    public String toString() {
        return "ListsOfUsers{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
