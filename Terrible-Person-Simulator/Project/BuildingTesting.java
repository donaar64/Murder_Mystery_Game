import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
public class BuildingTesting {
    

    @Test
    public void testDjikstrasAlgorithm(){
        Building main = new Building("main",0);
        Building number2 = new Building("number2",1);
        Building number3 = new Building("number3",2);
        Building number4 = new Building("number4", 3);
        Building number5 = new Building("number5",4);
        main.addPath(number2,2);
        main.addPath(number4,3);
        main.addPath(number5,7);
        number2.addPath(number3,3);
        number3.addPath(number4,1);
        number4.addPath(number5,1);

        HashMap[] allMaps = main.allPaths();
        HashMap<Building,Integer> times = allMaps[0];
        System.out.println(times);
        assertTrue(2==times.get(number2));
        assertTrue(4==times.get(number3));
        assertTrue(3==times.get(number4));
        assertTrue(4==times.get(number5));
        System.out.println(allMaps[2]);
        
    }
}
