import DataBase.DBControl;
import DataBase.Task;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SchedulerControl {
    private final static ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void  setScheduler(Task oldTask) {
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        Runnable runnable =  ()->{
            long timeNow = LocalDateTime.now().toEpochSecond(offset);
            DBControl dbControl = new DBControl();
            Task task = dbControl.getFromIdTask(oldTask.getId());

            if(task.getAlert_time().toEpochSecond(offset) - timeNow == 0){
                StringBuilder stringBuilder = new StringBuilder()
                        .append("Time for doing task \"")
                        .append(task.getName())
                        .append("\"!");

                System.out.println(stringBuilder);

                task.setAlert_received(true);
                dbControl.updateTask(task);
            }
            dbControl.close();
        };

        long time = oldTask.getAlert_time().toEpochSecond(offset) -
                LocalDateTime.now().toEpochSecond(offset);

        scheduler.schedule(runnable,time, SECONDS);


    }
}
