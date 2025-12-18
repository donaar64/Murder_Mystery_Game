import java.util.*;
import java.util.stream.Collectors;
public class Player {


HashSet<Habit> knownHabits;
HashSet<Relationship> knownRelationships;
HashSet<Class1Knowledge> allUsefulKnowledge;
HashSet<ExistenceOfPerson> knownPeople;
HashSet<ExistenceOfLocation> knownLocations;


public Player(){
    knownRelationships = new HashSet<>();
    allUsefulKnowledge = new HashSet<>();
    knownPeople = new HashSet<>();
    knownLocations = new HashSet<>();
    knownHabits = new HashSet<>();
}


/**
 * This method allows the user to remember information
 */
public void rememberInformation(){
    Scanner reader = new Scanner(System.in);
    boolean selected = false;
    boolean keepRemembering = true;
    String input;
    //There is a while loop that allows for the player to keep remembering without needing to go all the way back
    while(keepRemembering){
    Manager.clear();
    //The method then asks the player what they would like to remember about or if they would like to leave
    System.out.println("What would you like to do?");
    System.out.println("1:Remember about people");
    System.out.println("2:Remember about locations");
    System.out.println("3:Return to previous screen");
    do{
        //Input validation is performed and if 3 was chosen then it goes back to make decision but other wise it remembers certain things
         input = reader.next();
        if(input.equals("1") | input.equals("2")| input.equals("3")){
            if(input.equals("3")){
                keepRemembering= false;
                selected = true;
            }
            if(Integer.parseInt(input)==1 |Integer.parseInt(input)==2 ){
                selected = true;
            }

        }
        else{
            System.out.println("Please enter a valid input");

        }


    }
    while(!selected);


    if(input.contains("1")){
        //If it tries to remember about people and there is no knowledge of people then it returns
        List<Object> personNamed;
        boolean named = false;
        Manager.clear();
        if(knownPeople.isEmpty()){
            System.out.println("You have no knowledge of people");
            Manager.dramaticPause();
            return;
        }
        Scanner newReader = new Scanner(System.in);
        
        //It then prints a list of every person that the person knows exists
            HashSet<Student> thoseDeceased = new HashSet<>();
            for(ExistenceOfPerson person : knownPeople){
                if(person.getPerson().isDead()){
                    thoseDeceased.add(person.getPerson());
                }
                else{
                System.out.println(person.getPerson());
                }
            }
            for(Student person :thoseDeceased){
                System.out.println( person + " Deceased");
            }
        do{
            //It then determines which they would like to remember about by checking if the name that they typed in mathches any of a person that they have knowledge of
            System.out.print("Who would you like to remember about? (Please type full name): ");
            input = newReader.nextLine();
            String temp = input;
             personNamed = knownPeople.stream().map(n -> n.getPerson()).filter(n-> n.getName().toLowerCase().equals(temp.toLowerCase())).collect(Collectors.toList());
                 if(!personNamed.isEmpty()){
                    named = true;
                 }
             else{
                System.out.println("Please enter a valid input");
            
            }

        }   
        while(!named);

        rememberPerson((Student)personNamed.get(0));
        
    }

    if(input.contains("2")){
        //This works the same as the one about people bur for locations instead
        List<Object> placeNamed;
        boolean named = false;
        Manager.clear();
        if(knownLocations.isEmpty()){
            System.out.println("You have no knowledge of events");
            Manager.dramaticPause();
            return;
        }
        Scanner newReader = new Scanner(System.in);

        do{

            for(ExistenceOfLocation place : knownLocations){
                System.out.println(place.getPlace());
            }
            System.out.print("What would you like to remember about? (Please type full name): ");
            input = newReader.nextLine();
            String temp = input;
             placeNamed = knownLocations.stream().map(n -> n.getPlace()).filter(n-> n.getName().equals(temp)).collect(Collectors.toList());
             if(!placeNamed.isEmpty()){
                named = true;
             }
             else{
                System.out.println("Please enter a valid input");
            
            }

        }   
        while(!named);

        rememberLocation((Location) placeNamed.get(0));
    }
}


}
/**
 * This method prints everything that the player remembers regarding a specific person
 * @param person The person that this is concerning
 */
public void rememberPerson(Student person){
    //This goes through all of the habits and relationships that the player knows about and if the person is in them then it prints them
    Manager.clear();
    System.out.println(person);
    System.out.println("Known Habits:");
   knownHabits.stream().filter(n ->n.getPerson().equals(person)).forEach(n-> System.out.println("   "+n.getPlace()));
   System.out.println("Known Relationships:");
   knownRelationships.stream().filter(n ->n.getPeople().contains(person)).forEach(n-> System.out.println("  "+n));
    Manager.dramaticPause();
}
/**
 * This method prints everything that the player remembers regarding a specific location
 * @param place The location that this is concerning
 */
public void rememberLocation(Location place){
    Manager.clear();
    System.out.println(place);
    System.out.println("Building: "+place.getBuilding());
    System.out.println(place.isClass()? "Class":"Club");
    System.out.println((place.isEarly() ? "Early":"Late"));
    System.out.println("Known attendants");
    knownHabits.stream().filter(n ->n.getPlace().equals(place)).forEach(n-> System.out.println("    "+n.getPerson()));
     Manager.dramaticPause();
}
/**
 * This method returns the set of all of the ExistenceOfPerson for the player
 * @return the set of all ExistenceOfPerson for the player
 */
public HashSet<ExistenceOfPerson> getKnownPeople(){
    return knownPeople;
}
/**
 * This method returns the set of all of the ExistenceOfLocatin for the player
 * @return The set of all ExistenceOfLocation for the player
 */
public HashSet<ExistenceOfLocation> getKnownLocations(){
    return knownLocations;
}
/**
 * This method allows the player to learn new class 1 knowledge and prints out something if they do
 * @param thingToKnow The class 1 knowledge that this function is regarding
 * @param pause If this is true then there will be a dramatic pause when the player learns something new
 * @return This will return true if something new was learned
 */
public boolean  learnKnowledge(Class1Knowledge thingToKnow,boolean pause){
    //It tries to add the knowledge to the set and if it fails then it returns false
    if(!allUsefulKnowledge.add(thingToKnow)){
        return false;
    }
    //It then tries to learn all of the underlying knowledge for this piece of knowledge
    for(ExistenceOf toKnow : thingToKnow.underlyingKnowledge()){
        learnKnowledge(toKnow,pause);
    }
//It then prints out if it learned something new what it was
    if(thingToKnow instanceof Habit){
        if(knownHabits.add((Habit)thingToKnow)){
            System.out.println("You learned that "+thingToKnow);
            if(pause){
                Manager.dramaticPause();
            }
        }
    }
    else if(thingToKnow instanceof Relationship){
        if(knownRelationships.add((Relationship) thingToKnow)){
            System.out.println("You learned that "+thingToKnow);
            if(pause){
                Manager.dramaticPause();
            }
        }
    }
    return true;

}
/**
 * This method allows the player to learn new existenceOf knowledge and prints something out if they do
 * @param toKnow The existenceOf knowledge that this is regarding
 * @param pause If this is true there will be a dramatic pause when the player learns something new
 * @return This will return true if something new was learned
 */
public boolean  learnKnowledge(ExistenceOf toKnow,boolean pause){
    //This tries to add it to the sets of the existence of knowledge and if it succeeds thens it prints something
    if(toKnow instanceof ExistenceOfPerson){
        ExistenceOfPerson personToKnow = (ExistenceOfPerson) toKnow;
        if(knownPeople.add(personToKnow)){
            System.out.println("You learned about a new person: "+personToKnow.getPerson());
            if(pause){
                Manager.dramaticPause();
            }
            return true;
        }
    }
    else{
        ExistenceOfLocation placeToKnow = (ExistenceOfLocation) toKnow;
        if(knownLocations.add(placeToKnow)){
            System.out.println("You learned about a new event: "+placeToKnow.getPlace());
            if(pause){
                Manager.dramaticPause();
            } 
            return true;       
        }
    }
    return false;
}





}
