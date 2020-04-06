package TaskPlanner;

import DataBase.DataBaseAble;
import DataBase.UserDB;
import Enity.ListsOfUsers;
import Enity.Task;
import Enity.User;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

public class TaskTP implements TaskPlannerAble {

    private Scanner in;
    private DataBaseAble<Task> dataBase;
    private DataBaseAble<ListsOfUsers> listDB;
    private PrintStream out;
    private int status;
    static Logger logger = Logger.getLogger(TaskTP.class);

    public TaskTP (DataBaseAble<Task> dataBase, DataBaseAble<ListsOfUsers> listDB){
        in = new Scanner(System.in);
        out = System.out;
        this.dataBase = dataBase;
        this.listDB = listDB;
    }

    @Override
    public int start() {
        out.println("Menu tasks:");
        out.println("0 - create new task");

        List<Task> tasks = dataBase.get(new Task());

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
            case -1: return 0;
            case 0: makeTask(); break;
            default: menuTask(tasks.get(number-1)); break;
        }
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

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
        List<ListsOfUsers> lists = listDB.get(new ListsOfUsers());
        for (int i = 0; i < lists.size(); i++) {
            out.println((i) + " - " + lists.get(i).getName());
        }

        int listId = in.nextInt();
        Task task = new Task();
        try {
            task.setName(name);
            task.setDescription(description);
            LocalDateTime alert_time = LocalDateTime.of(year, Month.of(month), day, hour, min, 0);
            task.setAlert_time(alert_time);
            task.setAlert_received(false);
            task.setListId(lists.get(listId).getId());
        } catch (Exception e){
            logger.error(e);
            out.println("Error! Please, try another time.");
            return;
        }

        SchedulerControl.setScheduler(task, dataBase);

        dataBase.set(task);
        out.println("Successful!");
    }

    public void menuTask(Task task) {
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
                dataBase.delete(task.getId());
            }
        } else if (number == 1){
            editTask(task);
        }
    }


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

        out.println("Do you want to edit data? (y/n)");
        String accept = in.next();
        LocalDateTime alert_time = null;

        if(accept.equals("y")){
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
        try {
            newTask.setId(task.getId());
            newTask.setName(name);
            newTask.setDescription(description);

            if (alert_time == null) {
                newTask.setAlert_time(task.getAlert_time());
                newTask.setAlert_received(false);
            } else {
                newTask.setAlert_time(alert_time);
                newTask.setAlert_received(true);
                SchedulerControl.setScheduler(task, dataBase);
            }
        } catch (Exception e){
            logger.error(e);
            out.println("Error! Please, try another time.");
            return;
        }
        dataBase.update(newTask);
        out.println("Successful!");
    }
}
