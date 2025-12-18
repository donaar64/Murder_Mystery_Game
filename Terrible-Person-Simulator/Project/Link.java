import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class stores the status of the relationship between two students
 */
public class Link {
    Student person1;
    Student person2;
    boolean friends;

    /**
     * This method creates a link between two people and then sets the relationship status to the friends parameter
     * @param person1 The first student
     * @param person2 The second student
     * @param friends If the two students are friends
     */
    public Link(Student person1,Student person2, boolean friends){
        this.person1 = person1;
        this.person2 =person2;
        this.friends = friends;
    }
    
    /**
     * This method creates a link between two people and then says they are not friends
     * @param person1 The first student
     * @param person2 The second student
     */
    public Link(Student person1, Student person2){
        this.person1 = person1;
        this.person2 =person2;
        friends = false;
    }

    /**
     * This method returns the first student
     * @return The first Student
     */
    public Student getFirstStudent(){
        return person1;
    }
    /**
     * This method returns the second student
     * @return The second student
     */
    public Student getSecondStudent(){
        return person2;
    }

    /**
     * This method checks to see if a student is a part of this link
     * @param person The student being checked
     * @return This method returns true if the student is a part of this link and false if they are not
     */
    public boolean isIn(Student person){
        return person.equals(person1)||person.equals(person2);
    }

    /**
     * This method returns both of the students in an arraylist
     * @return The students as an array list
     */
    public HashSet<Student> getStudents(){
        Stream<Student> students =Stream.of(person1,person2);
        return (HashSet<Student>)(students.collect(Collectors.toSet()));
    }
    /**
     * This method returns if two students are friends
     * @return This method returns true if the students are friends and false if they are not
     */
    public boolean areFriends(){
        return friends;
    }

    /**
     * This method makes the relationship read as friends. It then calls each students make friends method.
     */
    public void becomeFriends(){
        //If friends is already true then it does nothing
        if(friends){
            return;
        }
        //If friends is not true then it sets it to true
        friends = true;
        person1.makeFriend(person2);
        person2.makeFriend(person1);

        //This adds the knowledge of the relationship to each students knowledge pool
        Relationship interpersonal = new Relationship(person2, person1);
        person1.AcquireKnowledge(interpersonal);
        person2.AcquireKnowledge(interpersonal);
    }
    
    /**
     * This method takes a Student who is a member of the link and returns the other
     * @param person The starting end of the link
     * @return The other end of the link. if the student inputted is not a member of the link then it will return null
     */
    public Student getOther(Student person){
        if(!(person.equals(person1)|person.equals(person2))){
            return null;
        }

        return person.equals(person1) ? person2 :person1;
    }
    /**
     * This method determines whether two links are equal to each other. It says that they are if they have the same students and the friendship status is the same
     * @param other The link being checked against
     * @return This will return true if the links are equal
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof  Link)){
            return false;
        }
        Link other2 = (Link)other;
       boolean firstPerson = other2.isIn(person1);
       boolean secondPerson = other2.isIn(person2);
       return firstPerson && secondPerson && other2.areFriends()==friends;


    }




    
    /** 
     * This method uses cantor's pairing function to generate a unique integer for a set of students. The max is taken from each to ensure that the students are in a consistent order
     * @return The hash code that is generated
     */
    @Override
    public int hashCode(){
        double student1 = Math.max(person1.getId(),person2.getId());
        double student2 = Math.min(person1.getId(),person2.getId());
        return (int)((student1+student2)*((student1+student2+1.0))/2.0+student2);
    }


}
