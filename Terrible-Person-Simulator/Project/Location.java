
import java.util.*;
public class Location {
    final private int id;
    final private boolean isClass;
    final private String name;
    final private Building takesPlaceIn;
    final private boolean early;
    //This variable stores each student in this location as a key to a map containing each other student as a key 
    //to the relationship they share with the original student. Basically a relationship is stored behind the two students in it
    final private HashMap<Student, HashMap<Student,Link>> studentLinks;
    final private HashSet<Student> attendingStudents;

    /**
     * This method creates a location with the following traits
     * @param name The name of the location
     * @param isClass This will be true if the location is a class and false if it is a club
     * @param id The id number of the location, these should be unique
     * @param early This will be true if the location takes place early and false if it takes place late
     * @param takesPlaceIn This is the building that the location takes place in
     */
    public Location(String name, boolean isClass, int id,boolean early,Building takesPlaceIn){
        this.id=id;
        this.isClass = isClass;
        this.name = name;
        this.early = early;
        this.takesPlaceIn = takesPlaceIn;
        attendingStudents = new HashSet<>();
        studentLinks = new HashMap<>();
    }

    /**
     * This method returns the name of the location object
     * @return The name of the location object
     */
    public String getName(){
        return name;
    }
    /**
     * This method is the default to string method for a location returns the name
     * @return The name of the location
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * This method is for determining if a given location is a class
     * @return This will return true if the location is a class
     */
    public boolean isClass(){
        return isClass;
    }
    /**
     * This method returns the id of a location
     * @return This will return the id of a location which should be unique
     */
    public int getId(){
        return id;
    }

    /**
     * This method adds a student to the locations list of students and then adds their relationships to
     * other students the locations internal relationship map
     * @param person The student being added
     * @return This will return true if the student was not already going to this location
     */
    public boolean addStudent(Student person){
        //This checks to see if the student already goes to this location and if they do then it returns false
        if(attendingStudents.contains(person)){
            return false;
        }

        //adding a new map to represent the persons relationships to the other students in this location
        HashMap<Student,Link> links = new HashMap<>();
        //adds a key value pair to this map for each student that goes to this location and their relationship
        for(Student linkPartner : studentLinks.keySet()){
            Link relationship = person.getRelationShips().get(linkPartner);
            studentLinks.get(linkPartner).put(person,relationship);
            links.put(linkPartner,relationship);
            //This part gives each student knowledge that the student goes to this location
            linkPartner.AcquireKnowledge(new Habit(person,this));
            person.AcquireKnowledge(new Habit(linkPartner,this));
        }
        //adds the just built map to the internal relationship map
        studentLinks.put(person,links);
        //adds the student to the set of students that go to this location
        attendingStudents.add(person);
        person.addPlace(this);
        

        return true;
    }

    /**
     * This method checks whether this location is the same as a different location
     * It uses the location Ids to determine this
     * @param other The location being checked against
     * @return This will return true if the location ids are the same
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Location)){
            return false;
        }
        Location place = (Location) other;
        return (place.getId() == id);
    }
    /**
     * This method generates a hashcode for a location and since they all have unique ids those are also used as hasvalues
     * @return The hash value for the Location
     */
    @Override
    public int hashCode(){
        return id;
    }

    /**
     * This method first randomly chooses based on the chance variable if a friendship will be created
     * If it is then it will grab the links between every student who attends the location, take the ones that are not friends
     * and then randomly decide two to make friends
     * @param chance The chance out of 100 that a friendship will be created
     * @return This will return null if no relationship was created and it will return the link that was changed if one is
     */
    public Link createFriendship(int chance){
        //This part randomly decides if a friendship will be created
        Random randomizer = new Random();
        if(randomizer.nextInt(100)>chance){
            return null;
        }
        // these are sets for the links of people that are not friends and the explored students
        HashSet<Link> notFriends = new HashSet<>();
        HashSet<Student> explored = new HashSet<>();
        ArrayDeque<Student> stack = new ArrayDeque<>();

        //This part pushes a random student on the stack first
        stack.push((Student)Manager.randomObject(attendingStudents));
        
        while(!stack.isEmpty()){
            //This part pops the next student off of the stack and if they have not yet been explored then they explore their links
            Student current = stack.pop();
            if(!explored.contains(current)){
                explored.add(current);

                //This grabs all of the other students in the location
                Set<Student> knownStudents = studentLinks.get(current).keySet();
                //For each other student in the location if that person hasn't been explored then it throws them on the stack to explore late
                for(Student person : knownStudents){
                    if(!explored.contains(person)){
                        stack.add(person);
                        //If the connection between two students says they are not friends then it adds that link to the set of non friend links
                        Link connection = studentLinks.get(current).get(person);
                        if(!connection.areFriends()){
                            notFriends.add(connection);
                        }
                    }
                }
                
            }
            
        }
        //If there are no unfriended links then it will return null
        if(notFriends.isEmpty()){
            return null;
        }
        //If there are then it picks one at random and calls the become friends method of the link
       Link thisOne = ((Link) Manager.randomObject(notFriends));
       thisOne.becomeFriends();
        return thisOne;
    }

    /**
     * This method creates a string full of useful information about the location. Mainly for debugging
     * @return This returns a string with useful information about the location
     */
    public String getInfo(){
        return toString()+":"+attendingStudents.size();
    }
    /**
     * This method returns the building that this location takes place int
     * @return the building that this location takes place in
     */
    public Building getBuilding(){
        return takesPlaceIn;
    }
    /**
     * This method returns if this is an early location
     * @return This will return true if the location is early and false if it is late
     */
    public boolean isEarly(){
        return early;

    }
    /**
     * This method returns all of the students that attend this event
     * @return This returns the set of students that attend here
     */
    public HashSet<Student> getStudents(){
        return attendingStudents;
    }


}
