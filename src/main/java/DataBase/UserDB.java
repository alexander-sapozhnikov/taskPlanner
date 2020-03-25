package DataBase;

import Enity.User;
import com.google.common.hash.Hashing;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class UserDB implements DataBaseAble<User> {

    private Connection conn = null;
    static Logger logger = Logger.getLogger(UserDB.class);

    public UserDB(){
       conn = ConnectionDataBase.getConnection();
    }

    @Override
    public int set(@NonNull User user) {
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
                prepared = conn.prepareStatement("INSERT INTO listsOfUsers " +
                        "(id, userId, name)" +
                        "VALUES (DEFAULT, ?, ?)");
                prepared.setInt(1, resultSet.getInt("id"));
                prepared.setString(2, "None list");
                prepared.execute();

                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.warn(e);
            return  e.getSQLState().equals("23505") ? 0 : -1;
        }
        return -1;
    }

    @Override
    public List<User> get(@NonNull User user) {
        PreparedStatement prepared = null;
        ResultSet result = null;

        try {
            prepared = conn.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            prepared.setString(1, user.getUsername());
            String hash = Hashing.sha256()
                    .hashString(user.getPassword(), StandardCharsets.UTF_8)
                    .toString();
            prepared.setString(2, hash);

            result = prepared.executeQuery();

            if(result.next()){
                user = new User();
                user.setId(result.getInt("id"));
                user.setPassword(result.getString("password"));
                user.setFirstname(result.getString("firstname"));
                user.setLastname(result.getString("lastname"));
                user.setPhone(result.getString("phone"));
            } else {
                user = null;
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Arrays.asList(user);
    }

    @Override
    public boolean update(@NonNull User user) {
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
    public boolean delete(int id){
        PreparedStatement prepared = null;
        try {
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
}
