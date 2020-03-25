package DataBase;

import Enity.ListsOfUsers;
import Enity.Task;
import Enity.User;
import lombok.NonNull;

import java.util.List;

public interface DataBaseAble <T> {

    int set(@NonNull T object);
    List<T> get(@NonNull T object);
    boolean update (@NonNull T object);
    boolean delete (int id);
}
