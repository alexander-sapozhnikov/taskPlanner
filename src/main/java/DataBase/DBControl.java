package DataBase;

import com.google.common.hash.Hashing;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DBControl implements funDataBase {

    private Connection conn = null;
    private String userDB = "postgres";
    private String passwordDB = "";
    static Logger logger = Logger.getLogger(DBControl.class);
    public DBControl(){
        String url = "jdbc:postgresql://localhost:5432/taskPlanner";
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, userDB, passwordDB);
        } catch (SQLException e) {
            logger.error(e);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    /**
     * 1 >  - successful and it's id user
     * 0 - need change username
     * -1 - error
     * @param user
     * @return
     */
    @Override
    public int setUser(@NonNull User user){
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("INSERT INTO users " +
                    "(id, username, password, firstname, lastname, phone)" +
                    "VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING id");
            prepared.setString(1, user.getUsername());
            String hash = Hashing.sha256()
                    .hashString(user.getPassword(), StandardCharsets.UTF_8)
                    .toString();
            prepared.setString(2, hash);
            prepared.setString(3, user.getFirstname());
            prepared.setString(4, user.getLastname());
            prepared.setString(5, user.getPhone());

            ResultSet resultSet = prepared.executeQuery();
            if(resultSet.next()){

                ListsOfUsers listsOfUsers = new ListsOfUsers();
                listsOfUsers.setUserId(resultSet.getInt("id"));
                listsOfUsers.setName("None list");
                setListsOfUsers(listsOfUsers);

                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.warn(e);
            return  e.getSQLState().equals("23505") ? 0 : -1;
        }
        return -1;
    }

    @Override
    public User getUser(String name, String password){
        User user = null;
        PreparedStatement prepared = null;
        ResultSet result = null;

        try {
            prepared = conn.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            prepared.setString(1, name);
            String hash = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();
            prepared.setString(2, hash);

            result = prepared.executeQuery();

            if(result.next()){
                user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setPassword(result.getString("password"));
                user.setFirstname(result.getString("firstname"));
                user.setLastname(result.getString("lastname"));
                user.setPhone(result.getString("phone"));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return user;
    }

    @Override
    public User getFromIdUser(int id) {
        User user = null;
        PreparedStatement prepared = null;
        ResultSet result = null;

        try {
            prepared = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            prepared.setInt(1, id);

            result = prepared.executeQuery();

            if(result.next()){
                user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setPassword(result.getString("password"));
                user.setFirstname(result.getString("firstname"));
                user.setLastname(result.getString("lastname"));
                user.setPhone(result.getString("phone"));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return user;
    }

    @Override
    public boolean updateUser(@NonNull User user) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("UPDATE users SET " +
                    "username = ?, " +
                    "password = ?, " +
                    "firstname = ?, " +
                    "lastname = ?, " +
                    "phone = ? " +
                    "where id = ?");
            prepared.setString(1, user.getUsername());
            prepared.setString(2, user.getPassword());
            prepared.setString(3, user.getFirstname());
            prepared.setString(4, user.getLastname());
            prepared.setString(5, user.getPhone());
            prepared.setInt(6, user.getId());

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        PreparedStatement prepared = null;
        try {

            prepared = conn.prepareStatement("SELECT * FROM watcherForTasks " +
                    "WHERE userId = ?");
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();

            List<Integer> tasksId = new ArrayList<>();
            while (resultSet.next()){
                tasksId.add(resultSet.getInt("contactId"));
            }

            prepared = conn.prepareStatement("DELETE FROM tasks " +
                    "WHERE id = ?");

            for(int taskId : tasksId){
                prepared.setInt(1, taskId);
                prepared.execute();
            }
            prepared = conn.prepareStatement("DELETE FROM users " +
                    "WHERE id = ?");
            prepared.setInt(1, id);

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public int setTask(@NonNull Task task, int userId, int listId) {
        PreparedStatement prepared = null;
        int taskId = -1;

        try {
            prepared = conn.prepareStatement("INSERT INTO tasks " +
                    "(id, name, description, alert_time, alert_received)" +
                    "VALUES (DEFAULT, ?, ?, ?, ?) RETURNING id");
            prepared.setString(1, task.getName());
            prepared.setString(2, task.getDescription());
            prepared.setObject(3, task.getAlert_time());
            prepared.setBoolean(4, task.isAlert_received());

            ResultSet resultSet = prepared.executeQuery();

            if(resultSet.next()){
                taskId = resultSet.getInt("id");
            }

            prepared = conn.prepareStatement("INSERT INTO tasksInLists " +
                    "(id, listId, taskId)" +
                    "VALUES (DEFAULT, ?, ?) ");
            prepared.setInt(1,listId);
            prepared.setInt(2, taskId);
            prepared.execute();

            prepared = conn.prepareStatement("INSERT INTO watcherForTasks " +
                    "(id, userId, contactId)" +
                    "VALUES (DEFAULT, ?, ?) ");
            prepared.setInt(1,userId);
            prepared.setInt(2, taskId);
            prepared.execute();


        } catch (SQLException e) {
            logger.warn(e);
        }

        return taskId;
    }

    @Override
    public List<Task> getTasks( String name, String description) {
        PreparedStatement prepared;
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if(name.equals("")){
                prepared = conn.prepareStatement("SELECT * FROM tasks WHERE description Like ?");
                prepared.setString(1, "%"+description+"%");
            } else if (description.equals("")){
                prepared = conn.prepareStatement("SELECT * FROM tasks WHERE name Like ?");
                prepared.setString(1, "%"+name+"%");
            } else {
                prepared = conn.prepareStatement("SELECT * FROM tasks WHERE name Like ? AND  description Like ? ");
                prepared.setString(1, "%"+name+"%");
                prepared.setString(2, "%"+description+"%");
            }

            ResultSet result = prepared.executeQuery();
            while (result.next()){
                Task task = new Task();
                task.setId(result.getInt("id"));
                task.setName(result.getString("name"));
                task.setDescription(result.getString("description"));
                task.setAlert_time((LocalDate)result.getObject("alert_time"));
                task.setAlert_received(result.getBoolean("alert_received"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            logger.warn(e);
        }
        return tasks;
    }

    @Override
    public Task getFromIdTask(int id) {
        PreparedStatement prepared;
        Task task = null;
        try {
            prepared = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?");
            prepared.setInt(1, id);

            ResultSet result = prepared.executeQuery();

            if (result.next()){
                task = new Task();
                task.setId(result.getInt("id"));
                task.setName(result.getString("name"));
                task.setDescription(result.getString("description"));
                task.setAlert_time((LocalDate)result.getObject("alert_time"));
                task.setAlert_received(result.getBoolean("alert_received"));
            }
        } catch (SQLException e) {
            logger.warn(e);
        }
        return task;
    }

    @Override
    public boolean updateTask(@NonNull Task task) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("UPDATE tasks SET " +
                    "name = ?, " +
                    "description = ?, " +
                    "alert_time = ?, " +
                    "alert_received = ? " +
                    "where id = ?");
            prepared.setString(1, task.getName());
            prepared.setString(2, task.getDescription());
            prepared.setObject(3, task.getAlert_time());
            prepared.setBoolean(4, task.isAlert_received());
            prepared.setInt(5, task.getId());

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public boolean deleteTask(int id) {
        PreparedStatement prepared = null;
        try {
            prepared = conn.prepareStatement("DELETE FROM tasks " +
                    "WHERE id = ?");
            prepared.setInt(1, id);

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public int setListsOfUsers(ListsOfUsers listsOfUsers) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("INSERT INTO listsOfUsers " +
                    "(id, userId, name)" +
                    "VALUES (DEFAULT, ?, ?) RETURNING id");
            prepared.setInt(1, listsOfUsers.getUserId());
            prepared.setString(2, listsOfUsers.getName());

            ResultSet resultSet = prepared.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException e) {
            logger.warn(e);
        }

        return -1;
    }

    @Override
    public List<ListsOfUsers> getListsOfUsers(int userId) {
        PreparedStatement prepared;
        ArrayList<ListsOfUsers> list = new ArrayList<>();
        try {
            prepared = conn.prepareStatement("SELECT * FROM listsOfUsers WHERE userId = ? ORDER BY id ASC");
            prepared.setInt(1, userId);

            ResultSet result = prepared.executeQuery();
            result.next();
            while (result.next()){
                ListsOfUsers listsOfUsers = new ListsOfUsers();
                listsOfUsers.setId(result.getInt("id"));
                listsOfUsers.setUserId(result.getInt("userId"));
                listsOfUsers.setName(result.getString("name"));

                list.add(listsOfUsers);
            }
        } catch (SQLException e) {
            logger.warn(e);
        }
        return list;
    }

    @Override
    public boolean updateListsOfUsers(@NonNull ListsOfUsers listsOfUsers) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("UPDATE tasks SET " +
                    "name = ? " +
                    "where id = ?");
            prepared.setString(1, listsOfUsers.getName());

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public boolean deleteListsOfUsers(int id) {
        PreparedStatement prepared = null;
        try {
            prepared = conn.prepareStatement("DELETE FROM listsOfUsers " +
                    "WHERE id = ?");
            prepared.setInt(1, id);

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public User getWatcherForTasks(int taskId) {
        User user = null;
        PreparedStatement prepared = null;
        ResultSet result = null;

        try {
            prepared = conn.prepareStatement("SELECT * FROM watcherForTasks WHERE contactId = ?");
            prepared.setInt(1, taskId);

            result = prepared.executeQuery();
            result.next();

            user = getFromIdUser(result.getInt("userId"));
        } catch (SQLException e) {
            logger.error(e);
        }

        return user;
    }

    public void close (){
        try{
            conn.close();
        } catch (SQLException e) {
            logger.warn(e);
        }
    }
}
