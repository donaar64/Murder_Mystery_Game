import static org.junit.Assert.*;
import org.junit.*;


import java.io.*;
import java.util.*;
public class LinkTesting {
   Student geoff;
   Student mary;
   Link thing;
    @Before
    public void setup(){
         geoff = new Student("geoff","fred",false,94);
         mary = new Student("Mary","Hudson",true,87);
    }
    @Test
    public void testLinkTraveling(){
        Link thing = new Link(geoff,mary);

        assertEquals(geoff,thing.getOther(mary));
        assertEquals(mary,thing.getOther(geoff));
        
    }

    @Test
    public void testLinkTravelingNull(){
        Link thing = new Link(geoff,mary);
        Student susan  = new Student("susan","",false,97);
        assertNull(thing.getOther(susan));
    }

    @Test
    public void testLinksAreEqual(){
        Link thing1 = new Link(geoff,mary);
        Link thing2 = new Link(mary,geoff);
        assertTrue(thing1.equals(thing2));

    }
    @Test
    public void testLinksAreNotEquals(){
        Student susan  = new Student("susan","",false,97);
        Link thing1 = new Link(geoff,mary);
        Link thing2 = new Link(geoff,susan);
        Link thing3 = new Link(susan,mary);
        assertFalse(thing1.equals(thing2));
        assertFalse(thing1.equals(thing3));

    }

    @Test
    public void testIfASetContainsLinks(){
        Link thing1 = new Link(geoff,mary);
        Link thing2 = new Link(mary,geoff);
        HashSet<Link> fred = new HashSet<Link>();
        System.out.println(thing1.hashCode()+" "+thing2.hashCode());
        fred.add(thing1);
        assertFalse(fred.add(thing2));

    }

    



}
