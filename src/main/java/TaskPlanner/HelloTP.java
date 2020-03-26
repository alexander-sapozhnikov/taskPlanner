package TaskPlanner;

import DataBase.DataBaseAble;
import Enity.User;

import java.io.PrintStream;
import java.util.Scanner;

public class HelloTP implements TaskPlannerAble {

    private Scanner in;
    private DataBaseAble<User>  dataBase;
    private PrintStream out;
    private User user = null;

    public HelloTP (DataBaseAble<User> dataBase){
        in = new Scanner(System.in);
        this.dataBase = dataBase;
        out = System.out;
    }

    @Override
    public int start() {
        out.println("1 - Log In");
        out.println("2 - Sign Out");
        out.println("Write 1 ore 2:");

        int number = in.nextInt();
        while(number < 1 || 2 < number){
            out.println("Please. Write 1 or 2.");
            number = in.nextInt();
        }

        if(number == 1){
            logIn();
        } else {
            signOut();
        }
        return 0;
    }

    private void logIn() {
        out.println("Write username: ");
        String username = in.next();

        out.println("Write password: ");
        String password = in.next();

        user = new User();
        user.setUsername(username);
        user.setPassword(password);

        user = dataBase.get(user).get(0);

        if(user == null){
            out.println("Incorrect username or password." +
                    "\nWould you like to make new account? (y/n) ");

            if(in.next().equals("y")){
                signOut();
            } else {
                logIn();
            }
        }
    }

    private void signOut() {
        user = new User();
        out.println("Creating new user.");

        out.println("Write username: ");
        String username = in.next();
        while(username.equals("")){
            out.println("Write not empty username: ");
            username = in.next();
        }
        user.setUsername(username);

        out.println("Write password: ");
        String password = in.next();
        while(password.equals("")){
            out.println("Write not empty password: ");
            password = in.next();
        }
        user.setPassword(password);

        out.println("Write firstname: ");
        String firstname = in.next();
        user.setFirstname(firstname);

        out.println("Write lastname: ");
        String lastname = in.next();
        user.setLastname(lastname);

        out.println("Write phone: ");
        String phone = in.next();
        user.setPhone(phone);

        int id = dataBase.set(user);

        while (id == 0){
            out.println("User with this username exist. Think up another name.");
            out.println("Write username: ");
            username = in.next();
            while(username.equals("")){
                out.println("Write not empty username: ");
                username = in.next();
            }
            user.setUsername(username);
            id = dataBase.set(user);
        }

        user.setId(id);
    }


    public User getUser() {
        return user;
    }
}
