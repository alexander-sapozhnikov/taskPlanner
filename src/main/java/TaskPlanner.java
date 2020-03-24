import DataBase.ListsOfUsers;
import DataBase.Task;
import DataBase.User;
import DataBase.funDataBase;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

public class TaskPlanner implements funTaskPlanner {

    private Scanner in;
    private funDataBase dataBase;
    private PrintStream out;
    private User user = null;
    private int status;

    public TaskPlanner(funDataBase dataBase){
        in = new Scanner(System.in);
        this.dataBase = dataBase;
        out = System.out;
        status = -1;
    }

    public void setUser(User user) {
        this.user = user;
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
            status = 0;
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
            status = 0;
        } else {
            out.println("Oops, something went wrong!");
            status = -1;
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

        status = number;

    }

    @Override
    public void tasks() {
        out.println("Menu tasks:");
        out.println("0 - create new list");
        out.println("Your tasks:");
        List<Task> tasks = dataBase.getTasks(user.getId());
        if(tasks.isEmpty()){
            out.println("You don't have any tasks.");
        } else{
            for (int i = 0; i < tasks.size(); i++) {
                out.println((i+1) +" - " + tasks.get(i).getName());
            }
        }
        out.println("-1 - back to main menu");

        out.println("Write integer: ");
        int number = in.nextInt();

        while(number < -1 || tasks.size() < number){
            out.println("Write integer: ");
            number = in.nextInt();
        }

        switch (number){
            case -1: status = 0; break;
            case 0: makeTask(); break;
            default: menuEditTask(tasks.get(number-1)); break;
        }
    }

    @Override
    public void makeTask() {
        out.println("Write name for task: ");
        String name = in.next();

        out.println("Write description: ");
        String description = in.next();

        out.println("Write month: (1 - january...)");
        int month = in.nextInt();

        out.println("Write day: ");
        int day = in.nextInt();

        out.println("Write hour: ");
        int hour = in.nextInt();

        out.println("Write minutes: ");
        int min = in.nextInt();

        int year = LocalDateTime.now().getYear();
        if(LocalDateTime.now().getMonth().getValue() > month){
            year++;
        }

        out.println("Write number list for task: ");
        List<ListsOfUsers> lists = dataBase.getListsOfUsers(user.getId());
        for (int i = 0; i < lists.size(); i++) {
            out.println((i) +" - " + lists.get(i).getName());
        }
        int listId = in.nextInt();

        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        LocalDateTime alert_time = LocalDateTime.of(year, Month.of(month), day, hour, min, 0);
        task.setAlert_time(alert_time);
        task.setAlert_received(false);

        SchedulerControl.setScheduler(task);

        dataBase.setTask(task, user.getId(), lists.get(listId).getId());
        out.println("Successful!");
    }

    @Override
    public void menuEditTask(Task task) {
        out.println("Task with name \""+task.getName()+"\"");
        out.println("Menu: " +
                "\n-1 - back" +
                "\n0 - delete" +
                "\n1 - edit ");
        int number = in.nextInt();
        if(number == 0){
            out.println("Do you want to delete this list? (y/n)");
            String delete = in.next();
            if(delete.equals("y")){
                dataBase.deleteTask(task.getId());
            }
        } else if (number == 1){
           editTask(task);
        }
    }

    @Override
    public void editTask(Task task) {
        out.println("Edit task.");
        out.println("-1 - skip this item");
        out.println("Write name for task: ");
        String name = in.next();
        if(name.equals("-1")){
            name = task.getName();
        }

        out.println("Write description: ");
        String description = in.next();
        if(description.equals("-1")){
            description = task.getDescription();
        }

        out.println("Do you want to skip editing data? (y/n)");
        String accept = in.next();
        LocalDateTime alert_time = null;

        if(accept.equals("n")){
            out.println("Write month: (1 - january...)");
            int month = in.nextInt();

            out.println("Write day: ");
            int day = in.nextInt();

            out.println("Write hour: ");
            int hour = in.nextInt();

            out.println("Write minutes: ");
            int min = in.nextInt();

            int year = LocalDateTime.now().getYear();
            if(LocalDateTime.now().getMonth().getValue() > month){
                year++;
            }

            alert_time = LocalDateTime.of(year, Month.of(month), day, hour, min, 0);
        }

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        if(alert_time == null){
            newTask.setAlert_time(task.getAlert_time());
            newTask.setAlert_received(false);
        } else {
            newTask.setAlert_time(alert_time);
            newTask.setAlert_received(true);
            SchedulerControl.setScheduler(newTask);
        }

        dataBase.updateTask(newTask);
        out.println("Successful!");
    }

    @Override
    public void lists() {
        out.println("Menu lists:");
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
            case -1: status = 0; break;
            case 0: makeList(); break;
            default: editList(lists.get(number-1)); break;
        }
    }

    @Override
    public void makeList() {
        out.println("Write name for list: ");
        String name = in.next();

        ListsOfUsers list = new ListsOfUsers();
        list.setUserId(user.getId());
        list.setName(name);

        dataBase.setListsOfUsers(list);
        out.println("Successful! List with name \""+name+"\" created.\n");

    }

    @Override
    public void editList(ListsOfUsers list) {
        out.println("List with name \""+list.getName()+"\"");
        out.println("Menu: " +
                "\n-1 - back" +
                "\n0 - delete" +
                "\n1 - edit ");
        int number = in.nextInt();
        if(number == 0){
            out.println("Do you want to delete this list? (y/n)");
            String delete = in.next();
            if(delete.equals("y")){
                dataBase.deleteListsOfUsers(list.getId());
            }
        } else if (number == 1){
            out.println("Write new name for list: ");
            String name = in.next();

            list.setUserId(user.getId());
            list.setName(name);

            dataBase.updateListsOfUsers(list);
            out.println("Successful! List with name \""+name+"\" changed.\n " + list);
        }
    }

    @Override
    public void searchTask() {
        out.println("Write name task (-1 - skip)");
        String name = in.next();
        if(name.equals("-1")){
            name = "";
        }

        out.println("Write description task (-1 - skip)");
        String description = in.next();
        if(description.equals("-1")){
            description = "";
        }

        List<Task> tasks = dataBase.getTasks(name, description, user.getId());

        if(tasks.isEmpty()){
            out.println("You don't have any lists.");
        } else{
            for (int i = 0; i < tasks.size(); i++) {
                out.println((i+1) +" - " + tasks.get(i).getName());
                out.println(tasks.get(i).getDescription());
            }
        }
        out.println("0 - new search");
        out.println("-1 - back to main menu");

        out.println("Write integer: ");
        int number = in.nextInt();

        if(number == -1){
            status = 0;
        }


    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
