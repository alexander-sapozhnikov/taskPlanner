import TaskPlanner.TaskPlannerAble;

import java.util.ArrayList;
import java.util.List;

public class Facade {

    private List<TaskPlannerAble> list;

    public Facade(){
        list = new ArrayList<>();
    }

    public void add (TaskPlannerAble taskPlanner){
        taskPlanner.setStatus(list.size());
        list.add(taskPlanner);
    }

    public int get (int id){
        return list.get(id).start();
    }
}
