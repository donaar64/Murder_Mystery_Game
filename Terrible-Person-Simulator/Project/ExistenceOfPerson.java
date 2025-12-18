

public class ExistenceOfPerson extends ExistenceOf{
    Student person;

    /**
     * This method creates an ExistenceOfPerson object with the student provided
     * @param person The student that this knowledge is about
     */
    public ExistenceOfPerson(Student person)
{
    this.person = person;

}
    /**
     * This method returns the student that this knowledge is regarding
     * @return The student that this knowledge is regarding
     */
    public Student getPerson(){
        return person;
    }

    /**
     * This method converts the contents of this knowledge into a string
     * @return This will return the name of the place + exists
     */
    @Override
    public String toString(){
        return person + " exists.";
    }

    /**
     * This method checks if another object is equivalent to this piece of knowledge. It is considered to be if it is the same class and talks about the same student
     * @param other The object this is being checked against
     * @return This will return true if the object is equivalent
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof ExistenceOfPerson)){
            return false;
        }

        if(((ExistenceOfPerson) other).getPerson().equals(person)){
            return true;
        }
        return false;

    }
     /**
     * This method comes up with a hashvalue for this object. Because the students all have unique id's that is what is being used for the hash value. The hash values are then negative so that they don't conflict with the ExistenceOfLocation values
     * @return This is the hash value for this knowledge
     */
    @Override
   public int hashCode(){
    return -1*(person.getId());
   }
}
