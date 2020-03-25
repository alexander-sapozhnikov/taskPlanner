package TaskPlanner;

import DataBase.DataBaseAble;
import Enity.Task;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class SearchTP implements TaskPlannerAble {

    private Scanner in;
    private DataBaseAble<Task> dataBase;
    private PrintStream out;
    private int status;

    public SearchTP (DataBaseAble<Task> dataBase, int status){
        in = new Scanner(System.in);
        out = System.out;
        this.dataBase = dataBase;
        this.status = status;
    }

    @Override
    public int start() {
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

        Task task = new Task();
        task.setName(name);
        task.setDescription(description);

        List<Task> tasks = dataBase.get(task);

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
            return  0;
        }

        return status;
    }
}
