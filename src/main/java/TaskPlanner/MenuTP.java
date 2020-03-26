package TaskPlanner;

import DataBase.DataBaseAble;
import Enity.ListsOfUsers;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuTP implements TaskPlannerAble {

    private Scanner in;
    private PrintStream out;

    public MenuTP(){
        in = new Scanner(System.in);
        out = System.out;
    }

    @Override
    public int start() {
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

        return number;
    }
}
