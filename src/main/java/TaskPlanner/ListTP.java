package TaskPlanner;

import DataBase.DataBaseAble;
import DataBase.ListsOfUsersDB;
import Enity.ListsOfUsers;
import Enity.Task;
import Enity.User;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class ListTP implements TaskPlannerAble {

    private Scanner in;
    private DataBaseAble<ListsOfUsers> dataBase;
    private PrintStream out;
    private int status;

    public ListTP (DataBaseAble<ListsOfUsers> dataBase){
        in = new Scanner(System.in);
        out = System.out;
        this.dataBase = dataBase;
    }

    @Override
    public int start() {
        out.println("Menu lists:");
        out.println("0 - create new list");
        out.println("Your lists:");

        List<ListsOfUsers> lists = dataBase.get(new ListsOfUsers());

        if(lists.isEmpty()){
            out.println("You don't have any lists.");
        } else{
            for (int i = 1; i < lists.size(); i++) {
                out.println(i +" - " + lists.get(i).getName());
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
            case -1: return  0;
            case 0: makeList(); break;
            default: menuList(lists.get(number-1)); break;
        }

        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public void makeList() {
        out.println("Write name for list: ");
        String name = in.next();

        ListsOfUsers list = new ListsOfUsers();
        list.setName(name);

        dataBase.set(list);
        out.println("Successful! List with name \""+name+"\" created.\n");

    }

    public void menuList(ListsOfUsers list) {
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
                dataBase.delete(list.getId());
            }
        } else if (number == 1){
            out.println("Write new name for list: ");
            String name = in.next();

            list.setName(name);

            dataBase.update(list);
            out.println("Successful! List with name \""+name+"\" changed.\n " + list);
        }
    }
}
