package DataBase;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
