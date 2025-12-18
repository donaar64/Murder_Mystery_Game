import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;
public class ManagerTesting{
 Manager manager;
 Player player;

  @Before
 public void setup(){
    player = new Player();
     manager = new Manager(false, 15,player);
 }

 @Test
 public void testAllConnections(){
  for(int i=0;i<15;i++){
    Student person = manager.getStudentAt(i);
    for(int j=0;j<15;j++){
      Student person2 = manager.getStudentAt(j);
      if(!person2.equals(person)){
        assertTrue(person2.getRelationShips().keySet().contains(person));
        assertTrue(person.getRelationShips().keySet().contains(person2));
      }
    }
  }
 }

 
  @Test
  public void testNewClubs(){
    Student person = new Student("Geoff","Keighly", false,15);
    Student fred = new Student("Fred","George",false,13);
    Location library = new Location("Library",false,1,false,new Building("Library",0));
    person.AcquireKnowledge(new Habit(fred,library));
    assertTrue(person.joinNewClub(200));
    assertTrue(person.getLocations().contains(library));
  }

  @Test
  public void testJoiningNewClubWhenInIt(){
    Student person = new Student("Geoff","Keighly", false,15);
    Student fred = new Student("Fred","George",false,13);
    Location library = new Location("Library",false,1,false,new Building("Library",0));
    person.AcquireKnowledge(new Habit(fred,library));
    assertTrue(person.joinNewClub(200));
    assertFalse(person.joinNewClub(200));
    assertTrue(person.getLocations().contains(library));

  }

  @Test
  public void testListDifference(){
    HashSet<Location> places = new HashSet<>();
    HashSet<Location> otherPlaces = new HashSet<>();
    Building imaginary = new Building("Library", 2);
    Location place1 = new Location("Library",false,0,false,imaginary);
    Location place2 = new Location("Bookstore", true,1,false,imaginary);
    Location place3 = new Location("Subway",true,2,false,imaginary);
    Location place4 = new Location("Jimmy Johns",false,3,false,imaginary);
    places.add(place1);
    places.add(place2);
    places.add(place3);
    otherPlaces.add(place1);
    otherPlaces.add(place2);
    otherPlaces.add(place4);
    ArrayList<Object> difference = Manager.listDifference(places, otherPlaces);
    assertTrue(difference.contains(place3));
    assertFalse(difference.contains(place1));
    assertFalse(difference.contains(place2));
    assertFalse(difference.contains(place4));
    difference = Manager.listDifference(otherPlaces,places);
    assertTrue(difference.contains(place4));
    assertFalse(difference.contains(place1));
    assertFalse(difference.contains(place2));
    assertFalse(difference.contains(place3));


  }

  @Test
  public void testDFS(){

    Location place = new Location("Library", false,0,false,new Building("Library",2));
    Student person = new Student("Geoff","Keighly", false,15);
    Student fred = new Student("Fred","George",false,13);
    Student geoff = new Student("geoff","fred",false,94);
    Student mary = new Student("Mary","Hudson",true,87);
    Student[] people = {person,fred,geoff,mary};
    HashSet<Link> links = new HashSet<Link>();
    for(Student person1 :people){
      for(Student person2:people){
        if(!person1.equals(person2)){
          if(!person1.getRelationShips().keySet().contains(person2)){
            //If there isn't then it creates one and adds it to the linkpartner's and the person's relationship maps
            Link relationship = new Link(person1,person2);
            person2.getRelationShips().put(person1,relationship);
            person1.getRelationShips().put(person2,relationship);
            links.add(relationship);
        }
        }

      }


    }

    place.addStudent(fred);
    place.addStudent(person);
    place.addStudent(geoff);
    place.addStudent(mary);
    HashSet<Link> doneLinks = new HashSet<>();
    for(int i=0;i<6;i++){
      Link sL = place.createFriendship(500);
      assertFalse(links.add(sL));
      assertTrue(doneLinks.add(sL));
      assertTrue(sL.areFriends());
    }
    assertNull(place.createFriendship(500));
    for(Student person1:people){
      for(Student person2 :people){
        if(!person1.equals(person2)){
          assertTrue(person1.getRelationShips().get(person2).areFriends());
        }
      }
    }
    

  }


  @Test
  public void testBFS(){
    Student murderer = manager.getMurderer();
    murderer.getRelationShips().keySet().clear();
    assertTrue(murderer.getRelationShips().keySet().isEmpty());
    for(int i=0;i<15;i++){
      manager.addStudent(null, i);
    }
    Student person = new Student("Geoff","Keighly", false,15);
    Student fred = new Student("Fred","George",false,13);
    Student geoff = new Student("geoff","fred",false,94);
    Student mary = new Student("Mary","Hudson",true,87);
    Student george = new Student("Georder","Hudson",false,44);
    Student[] people = {murderer,person,fred,geoff,mary,george};
    int i=1;
    for(Student person1:people){
      manager.addStudent(person1,i);
      i++;
    }

    for(Student person1 :people){
      for(Student person2:people){
        if(!person1.equals(person2)){
          if(!person1.getRelationShips().keySet().contains(person2)){
            //If there isn't then it creates one and adds it to the linkpartner's and the person's relationship maps
            Link relationship = new Link(person1,person2);
            person2.getRelationShips().put(person1,relationship);
            person1.getRelationShips().put(person2,relationship);
        }
        }

      }


    }
    murderer.getRelationShips().get(mary).becomeFriends();
    murderer.getRelationShips().get(fred).becomeFriends();
    fred.getRelationShips().get(geoff).becomeFriends();
    mary.getRelationShips().get(george).becomeFriends();
    george.getRelationShips().get(person).becomeFriends();
    player.learnKnowledge(new ExistenceOfPerson(person), false);
    player.learnKnowledge(new ExistenceOfPerson(george), false);
    manager.murder(true);
    System.out.println(mary.isDead());
    for(Student person2:people){
      if(person2.isDead()){
        System.out.println(person2);
      }
    }
    assertTrue(mary.isDead());





  }


}
