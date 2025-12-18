import java.util.*;
public class Habit extends Class1Knowledge {
    Location goesTo;
    Student person;
    ExistenceOfPerson personKnowledge;
    ExistenceOfLocation locationKnowledge;

    /**
     * This method creates a habit knowledge object about the student going to the location. It then creates ExistenceOf knowledge objects about the student and the location
     * @param person The person that this habit is regarding
     * @param goesTo the location that this habit is regarding
     */
    public Habit(Student person, Location goesTo){
        this.goesTo = goesTo;
        this.person = person;
        personKnowledge = new ExistenceOfPerson(person);
        locationKnowledge = new ExistenceOfLocation(goesTo);
    }
    /**
     * This method converts this knowledge into a string of the form "Student attends Location."
     * @return The string form of this knowledge object
     */
    @Override
    public String toString(){
        return person+ " attends "+goesTo+".";
    }
    /**
     * This method returns the student this knowledge is regarding
     * @return The student
     */
    public Student getPerson(){
        return person;
    }
    /**
     * This method returns the location that this knowledge is regarding
     * @return The location
     */
    public Location getPlace(){
        return goesTo;
    }
    /**
     * This method determines if this object is equivalent to another object. It determines that it is if the other object is of the same type and concerns the same student and location
     * @param other The object being checked against
     * @return This will return true if the objects are equal
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Habit)){
            return false;
        }
        Habit unknown = (Habit) other;
        if(unknown.getPlace().equals(goesTo)&& unknown.getPerson().equals(person)){
            return true;
        }
        return false;
    }
    
    
    /** 
     * This method returns the ExistenceOf knowledge that exists due to the person and location contained in this knowledge
     * @return An array list that contains the underlying knowledge for this object
     */
    @Override
    public ArrayList<ExistenceOf> underlyingKnowledge(){
        ArrayList<ExistenceOf> things = new ArrayList<ExistenceOf>();
        things.add(personKnowledge);
        things.add(locationKnowledge);
        return things;
    }
    /**
     * This method generates a hashcode using cantor's pairing function to generate a unique integer for every set of integers. Because the id's are unique it uses them as the input integers. Two hundred is then added so that these won't clash with the ExistenceOfLocation hash value
     * @return The hash value for this object
     */
    @Override
    public int hashCode(){
        double student1 = person.getId();
        double place = goesTo.getId();
        return (int)((student1+place)*((student1+place+1.0))/2.0+place)+200;

    }

}
