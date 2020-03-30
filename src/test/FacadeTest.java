import TaskPlanner.MenuTP;
import TaskPlanner.SearchTP;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FacadeTest {
    private Facade facade;
    private MenuTP menuTP = mock(MenuTP.class);
    private SearchTP somethingTP = mock(SearchTP.class);
    @Before
    public void initialization(){
        facade = new Facade();
        facade.add(menuTP);
        facade.add(somethingTP);
    }

    @Test
    public void getMainMenu(){
        facade.get(0);
        verify(menuTP).start();
    }

    @Test
    public void getSomething(){
        facade.get(1);
        verify(somethingTP).start();
    }
}