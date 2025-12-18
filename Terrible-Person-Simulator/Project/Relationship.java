import java.util.*;
public class Relationship extends Class1Knowledge {
    Student person1;
    Student person2;
    ExistenceOfPerson person1Knowledge;
    ExistenceOfPerson person2Knowledge;

    /**
     * This method creates a new Relationship object regarding the friendship of two students
     * @param person The first person
     * @param other The second person
     */
    public Relationship(Student person, Student other){
        person1 = person;
        person2 = other;
        person1Knowledge = new ExistenceOfPerson(person);
        person2Knowledge = new ExistenceOfPerson(other);
    }

    
    /** 
     * This method converts this object into a string of the form "person1 is friends with person2."
     * @return The string representation of this object
     */
    @Override
    public String toString(){
        return person1 + " is friends with "+ person2+".";
    }

    /**
     * This method returns both of the students from this object in the form of an array list
     * @return An array list containg the students in this object
     */
    public ArrayList<Student> getPeople(){
        ArrayList<Student> people = new ArrayList<Student>();
        people.add(person1); people.add(person2);
        return people;
    }

    /** 
     * This method returns the ExistenceOf knowledge that exists due to the 2 people contained in this knowledge
     * @return An array list that contains the underlying knowledge for this object
     */
    @Override
    public ArrayList<ExistenceOf> underlyingKnowledge(){
        ArrayList<ExistenceOf> things = new ArrayList<ExistenceOf>();
        things.add(person1Knowledge); things.add(person2Knowledge);
        return things;
    }
    
    /**
     * This method determines if this object is equivalent to another object. It determines that it is if the other object is of the same type and concerns the same two students
     * @param other The object being checked against
     * @return This will return true if the objects are equal
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Relationship)){
            return false;
        }
        Relationship unknown = (Relationship) other;
        if(unknown.getPeople().contains(person1)&& unknown.getPeople().contains(person2)){
            return true;
        }
        return false;

    }

     /**
     * This method generates a hashcode using cantor's pairing function to generate a unique integer for every set of integers. Because the id's are unique it uses them as the input integers. Two hundred is then subtracted so that these won't clash with the ExistenceOfPerson hash value
     * @return The hash value for this object
     */
    @Override
    public int hashCode(){
        double student1 = Math.max(person1.getId(),person2.getId());
        double student2 = Math.min(person1.getId(),person2.getId());
        return -1*(int)((student1+student2)*((student1+student2+1.0))/2.0+student2) -200;
    }


}
