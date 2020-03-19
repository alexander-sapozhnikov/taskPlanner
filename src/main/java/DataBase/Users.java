package DataBase;

import lombok.Data;

public @Data class Users {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;

}
