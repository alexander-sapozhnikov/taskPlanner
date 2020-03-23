import DataBase.ListsOfUsers;
import DataBase.User;
import DataBase.funDataBase;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class TaskPlanner implements funTaskPlanner {

    private Scanner in;
    private funDataBase dataBase;
    private PrintStream out;
    private User user = null;

    TaskPlanner(funDataBase dataBase){
        in = new Scanner(System.in);
        this.dataBase = dataBase;
        out = System.out;
    }

    @Override
    public void hello() {
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
    }

    @Override
    public void logIn() {
        out.println("Write username: ");
        String username = in.next();

        out.println("Write password: ");
        String password = in.next();

        user = dataBase.getUser(username, password);

        if(user == null){
            out.println("Incorrect username or password." +
                    "\nWould you like to make new account? (y/n) ");

            if(in.next().equals("y")){
                signOut();
            } else {
                logIn();
            }
        } else {
            out.println("Good day, " + user.getFirstname() + "!");
            mainMenu();
        }
    }

    @Override
    public void signOut() {
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

        int id = dataBase.setUser(user);

        while (id == 0){
            out.println("User with this username exist. Think up another name.");
            out.println("Write username: ");
            username = in.next();
            while(username.equals("")){
                out.println("Write not empty username: ");
                username = in.next();
            }
            user.setUsername(username);
            id = dataBase.setUser(user);
        }

        if(id > 0){
            user.setId(id);
            mainMenu();
        } else {
            out.println("Oops, something went wrong!");
        }
    }

    @Override
    public void mainMenu() {

        out.println("Main menu:");
        out.println("1 - Tasks");
        out.println("2 - Lists");
        out.println("3 - Search in tasks");
        out.println("Write integer: ");

        int number = in.nextInt();

        while(number < 0 || number > 3){
            out.println("Write integer: ");
            number = in.nextInt();
        }

        switch (number){
            case 1: tasks(); break;
            case 2: lists(); break;
            case 3: searchTask(); break;
        }

    }

    @Override
    public void tasks() {

    }

    @Override
    public void lists() {
        out.println("Menu lists");
        out.println("0 - create new list");
        out.println("Your lists:");
        List<ListsOfUsers> lists = dataBase.getListsOfUsers(user.getId());
        if(lists.isEmpty()){
            out.println("You don't have any lists.");
        } else{
            for (int i = 0; i < lists.size(); i++) {
                out.println((i+1) +" - " + lists.get(i).getName());
            }
        }
        out.println("-1 - back to main menu");

        out.println("Write integer: ");
        int number = in.nextInt();

        while(number < -1 || lists.size() < number){
            out.println("Write integer: ");
            number = in.nextInt();
        }

        switch (number){
            case 0: mainMenu(); break;
            case 1: makeList(); break;
            default: editList(lists.get(number-1)); break;
        }
    }

    @Override
    public void makeList() {

    }

    @Override
    public void editList(ListsOfUsers list) {

    }

    @Override
    public void searchTask() {

    }
}
