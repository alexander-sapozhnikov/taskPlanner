package DataBase;

import Enity.ListsOfUsers;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListsOfUsersDB implements DataBaseAble<ListsOfUsers>{
    private Connection conn = null;
    private static Logger logger = Logger.getLogger(ListsOfUsersDB.class);
    private int userId;

    public ListsOfUsersDB(int userId){
        conn = ConnectionDataBase.getConnection();
        this.userId = userId;
    }

    @Override
    public int set(@NonNull ListsOfUsers listsOfUsers) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("INSERT INTO listsOfUsers " +
                    "(id, userId, name)" +
                    "VALUES (DEFAULT, ?, ?) RETURNING id");
            prepared.setInt(1, userId);
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
    public List<ListsOfUsers> get(@NonNull ListsOfUsers object) {
        PreparedStatement prepared;
        ArrayList<ListsOfUsers> list = new ArrayList<>();
        try {
            prepared = conn.prepareStatement("SELECT * FROM listsOfUsers WHERE userId = ? ORDER BY id ASC");
            prepared.setInt(1, userId);
            ResultSet result = prepared.executeQuery();
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
    public boolean update(@NonNull ListsOfUsers listsOfUsers) {
        PreparedStatement prepared = null;

        try {
            prepared = conn.prepareStatement("UPDATE listsOfUsers SET " +
                    "name = ? " +
                    "where id = ?");
            prepared.setString(1, listsOfUsers.getName());
            prepared.setInt(2, listsOfUsers.getId());

            prepared.execute();
            return true;
        } catch (SQLException e) {
            logger.warn(e);
            return  false;
        }
    }

    @Override
    public boolean delete(int id) {
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
}
