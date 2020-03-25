package TaskPlanner;

import DataBase.DataBaseAble;
import DataBase.TaskDB;
import DataBase.UserDB;
import Enity.Task;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SchedulerControl {
    private final static ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void  setScheduler(Task oldTask, DataBaseAble<Task> taskDB) {
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        Runnable runnable =  ()->{
            oldTask.setName("");
            oldTask.setDescription("");

            Task task = taskDB.get(oldTask).get(0);

            long timeNow = LocalDateTime.now().toEpochSecond(offset);

            if(task.getAlert_time().toEpochSecond(offset) - timeNow == 0){
                StringBuilder stringBuilder = new StringBuilder()
                        .append("Time for doing task \"")
                        .append(task.getName())
                        .append("\"!");

                System.out.println(stringBuilder);

                task.setAlert_received(true);
                taskDB.update(task);
            }
        };

        long time = oldTask.getAlert_time().toEpochSecond(offset) -
                LocalDateTime.now().toEpochSecond(offset);

        scheduler.schedule(runnable,time, SECONDS);


    }
}
