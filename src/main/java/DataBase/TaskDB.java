package DataBase;

import Enity.Task;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDB implements DataBaseAble<Task> {
    private Connection conn = null;
    private static Logger logger = Logger.getLogger(TaskDB.class);
    private int userId;

    public TaskDB (int userId){
        conn = ConnectionDataBase.getConnection();
        this.userId = userId;
    }

    @Override
    public int set(@NonNull Task task) {
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
            prepared.setInt(1,task.getListId());
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
    public List<Task> get(@NonNull Task task) {
        PreparedStatement prepared;
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            StringBuilder stringBuilder = new StringBuilder("SELECT t.id, name, description, alert_time, alert_received " +
                    " FROM watcherfortasks as w ")
                    .append("LEFT JOIN tasks as t on w.contactid = t.id ")
                    .append("WHERE w.userid = ? ");

            if(task.getId() != -1){
                stringBuilder.append("AND id = " + task.getId());
            }
            prepared = conn.prepareStatement(stringBuilder + " AND name Like ? AND  description Like ? ");
            prepared.setInt(1, userId);
            prepared.setString(2, "%" + task.getName() + "%");
            prepared.setString(3, "%" + task.getDescription() + "%");

            ResultSet result = prepared.executeQuery();
            while (result.next()){
                task = new Task();
                task.setId(result.getInt("id"));
                task.setName(result.getString("name"));
                task.setDescription(result.getString("description"));
                task.setAlert_time(((Timestamp)result.getObject("alert_time")).toLocalDateTime());
                task.setAlert_received(result.getBoolean("alert_received"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            logger.warn(e);
        }
        return tasks;
    }

    @Override
    public boolean update(@NonNull Task task) {
        PreparedStatement prepared;

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
    public boolean delete(int id) {
        PreparedStatement prepared;
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
}
