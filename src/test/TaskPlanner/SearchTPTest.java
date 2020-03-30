package TaskPlanner;

import DataBase.DataBaseAble;
import DataBase.TaskDB;
import Enity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SearchTPTest {
    private SearchTP searchTP;
    @Before
    public void initialization(){
        TaskDB dataBase = mock(TaskDB.class);
        searchTP = new SearchTP(dataBase);
        searchTP.setStatus(1);
    }

    @Test
    public void checkBackMenu(){
        Scanner scanner = new Scanner("-1\n-1\n-1\n");
        searchTP.setIn(scanner);
        Assert.assertEquals(searchTP.start(),0);

    }

    @Test
    public void checkAgainSearch(){
        Scanner scanner = new Scanner("-1\n-1\n1\n");
        searchTP.setIn(scanner);
        Assert.assertEquals(searchTP.start(),1);

    }

}