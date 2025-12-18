
import java.util.*;
public class Student {
    final private String fName;
    final private String lName;
    final private boolean female;
    final private HashSet<String> attributes;
    final private HashSet<Class1Knowledge> knowledge;
    final private int id;
    final private int startingAttributes =2;
    final private ArrayList<Location> placesToGo;
 private boolean dead = false;
    final private HashMap<Student,Link> socialLinks;
    final private HashSet<Student> friends;
    final private HashSet<Location> placesGoneTo;
    private boolean underArrest = false;


    /**
     * This method creates a new student object with the following attributes, creates some relevant constants and then assigns the student two random attributes
     * @param firstName The students first name
     * @param lastName The students last name
     * @param female This should be true if the student is female
     * @param id The students unique identifying number
     */
    public Student(String firstName, String lastName, boolean female,int id){
        fName = firstName;
        lName = lastName;
        this.female = female;
        this.id = id;
        attributes = new HashSet<>();
        knowledge = new HashSet<>();
        placesToGo = new ArrayList<>();
        socialLinks = new HashMap<>();
        friends = new HashSet<>();
        placesGoneTo = new HashSet<>();
        Random randomizer = new Random();

        //This code assigns two random attributes to a student
        for(int i=0;i<startingAttributes;i++){
            String toAdd =(Manager.getAttributes()[randomizer.nextInt(Manager.getAttributes().length)]);
            while(!attributes.add(toAdd)){
                toAdd =(Manager.getAttributes()[randomizer.nextInt(Manager.getAttributes().length)]);
            }
            
        }
    }

    /**
     * This method returns the first name of the student
     * @return The first name of the student
     */
    public String getFirst(){
        return fName;
    }

    /**
     * This method returns the last name of the student
     * @return The last name of the student
     */
    public String getLast(){
        return lName;
    }

    /**
     * This method returns the full name of the student
     * @return The name in the form "First Last"
     */
    public String getName(){
        return fName+" "+lName;

    }

    /**
     * This method returns the full name of the student as a string
     * @return The full name of the student
     */
    @Override
    public String toString(){
        return getName();
    }
    /**
     * This method returns if the student is female
     * @return This method will return true if the student is and false if they are not
     */
    public boolean isFemale(){
        return female;
    }

    /**
     * This method returns the id number of the student
     * @return The id number of the student
     */
    public int getId(){
        return id;
    }

    /**
     * This method checks if two students are the same student
     * It uses the id number to determine this
     * @param other The student that is being checked if it is equal
     * @return This returns true if the studed ids are the same
     */
    public boolean equals(Student other){
        return other.getId()==id;
    }
    
    /**
    * This method will return the student's list of attributes
    * @return The list of attributes
    */
    public HashSet<String> getAttributes(){
        return attributes;
    }
   /**
    * This method will return the student's list of knowledge
    * @return The list of knowledge
    */
   public HashSet<Class1Knowledge> getKnowledge(){
    return knowledge;
    }

    /**
     * This method takes knowledge and then adds it and its underlying knowledge to this persons knowledge pool
     * @param thing The knowledge being shared
     */
    public void AcquireKnowledge(Class1Knowledge thing){
        if(dead|underArrest){
            return;
        }
        //This grabs the underlying knowledge
        ArrayList<ExistenceOf> moreKnowledge = thing.underlyingKnowledge();

        for(Knowledge thingToKnow : moreKnowledge){
            //This checks if it is an existence of location knowledge
            if(thingToKnow instanceof ExistenceOfLocation){
                Location place =( (ExistenceOfLocation) thingToKnow).getPlace();
                if((!place.isClass() && !placesGoneTo.contains(place))){
                placesToGo.add(place);
                }

            }
        }
        knowledge.add(thing);
    }
    /**
     * This method removes a place from the students lists of new locations to go to and then adds the location to the students list of locations they go to
     * @param place This is the place that will be added
     * @return This method returns true if the student succesfully had the place added to their set and false if they did not
     */
    public boolean addPlace(Location place){
        if(dead|underArrest){
            return false;
        }
        //Remove the place from the places to go so it doesn't try to pick somewhere it has already gone
        while(placesToGo.contains(place)){
            placesToGo.remove(place);
        }
        //It then adds the place to the places that the student has gone
        return placesGoneTo.add(place);


    }
    /**
     * This method attempts to share knowledge between two people
     * The first thing it does is randomly decide if the people should share knowledge if it passes then knowledge will try to be shared
     * It then gives the person a piece of knowledge that they do not already know. If they already know all of this student's knowledge then nothing is shared
     * @param person The Student who the knowledge is being shared with
     * @param chance The base chance before modifiers that a person will share knowledge
     * @return This will return true if knowledge was successfully shared and false if it was not
     */
    public boolean shareKnowledge(Student person,int chance){
        if(dead|underArrest)
        {
            return false;
        }
        //This part applies modifiers to the chance and then rolls the dice to see if knowledge is shared
        Random randomizer = new Random();
        if(attributes.contains("Gossip")){chance*=2;}
        if(attributes.contains("Private")){chance/=2;}
        if(person.getAttributes().contains("Observant")){chance*=2;}
        if(person.getAttributes().contains("Unobservant")){chance/=2;}
         if(!(randomizer.nextInt(100)<chance)){
            return false;
        }
        
        //This section actually tries to share knowledge
        HashSet<Class1Knowledge> thingsToKnow = person.getKnowledge();
        ArrayList<Object> difference = Manager.listDifference( knowledge,thingsToKnow);
        if(difference != null){
            Class1Knowledge newKnowledge = (Class1Knowledge) difference.get(randomizer.nextInt(difference.size()));
            person.AcquireKnowledge(newKnowledge);
            return true;
        }

        
        

        return false;
    }

    /**
     * This method attempts to share knowledge between two people
     * The first thing it does is randomly decide if the people should share knowledge with a base chance of 30% if it passes then knowledge will try to be shared
     * It then gives the person a piece of knowledge that they do not already know. If they already know all of this student's knowledge then nothing is shared
     * @param person The Student who the knowledge is being shared with
     * @return This will return true if knowledge was successfully shared and false if it was not
     */
    public boolean shareKnowledge(Student person){
        return shareKnowledge(person,30);
    }

    /**
     * This method checks if the student is dead
     * @return This will return true if the student is dead
     */
    public boolean isDead(){
        return dead;
    }

    /**
     * This method kills the student and sets dead to true
     */
    public void kill(){
        dead=true;
    }

    /**
     * This method adds the person to the students friends
     * @param person The student that this student is becoming friends with
     * @return This will return false if the student is already a member of this students friends
     */
    public boolean makeFriend(Student person){
        if(dead| underArrest){
            return false;
        }
        // This attempts to add the student to the friends set and if it cannot then it returns false

        return friends.add(person);

    }
    /**
     * This method returns the map containing all of the students social links
     * @return The hasmap of the students links with the other students as keys
     */
    public HashMap<Student,Link> getRelationShips(){
        return socialLinks;
    }

    /**
     * This method gets the student to try to join a new club
     * @param chance The base percent chance that the student chooses to join a new club
     * @return This will return true if a new club was successfully joined
     */
    public boolean joinNewClub(int chance){
        if(dead| underArrest){
            return false;
        }
        //If there are no clubs on the list then it returns fals3e
        if(placesToGo.isEmpty()){
            return false;
        }
        
        //This modifies the percentage chance based on the students attributes
        if(attributes.contains("Social")){chance*=2;}
        if(attributes.contains("Anti-Social")){chance/=2;}

        //This part performs the randomization. If it fails then false is returned
        Random randomizer = new Random();
        int randint = randomizer.nextInt(100);
        if(randint>chance){
            return false;
        }

        Location clubToJoin;
        boolean joiningSuccess;
        //It will continue to try to join a club until one is successfully joined or it runs out of clubs to attempt to join
        do {
             //This part selects a random location from the list of locations to join and then attempts to join it
            clubToJoin = (Location) Manager.randomObject(placesToGo);
            joiningSuccess =clubToJoin.addStudent(this);
            
            //It then removes all instances of the chosen club from the list of potential clubs to join
            while(placesToGo.contains(clubToJoin)){
                placesToGo.remove(clubToJoin);
            }
        }while(!joiningSuccess && !placesToGo.isEmpty());


        return joiningSuccess;

    }

    /**
     * This method returns useful information about the state of the student. It is mainly for debugging
     * @param attributes If this is true then the students will also be returnd
     * @return A string containing useful information about the state of the student
     */
    public String getInfo(boolean attributes){
        String attr = attributes? this.attributes.toString():"";

        return toString()+":"+attr+" Friends:"+friends.size()+" Knowledge:"+knowledge.size()+ " Locations:"+placesGoneTo.size() + " Considering:"+ placesToGo.size();
    }

    /**
     * This method returns the locations that the student goes to
     * @return This is the set of locations that the student goes to
     */
    public HashSet<Location> getLocations(){
        return placesGoneTo;

    }

    /**
     * This method returns the set of friends that the student has
     * @return The friends that the student has
     */
    public HashSet<Student> getFriends(){
        return friends;
    }
    /**
     * This method sets the under arrest variable to true which prevents various things from happening
     */
    public void arrest(){
        underArrest = true;
    }
/**
 * This method releases the student from arrest but gives them the anti social attribute which makes them less likely to join clubs
 */
    public void releaseFromArrest(){
        underArrest = false;
        attributes.add("Anti-Social");
    }

    
}
