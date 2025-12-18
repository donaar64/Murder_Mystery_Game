public class ExistenceOfLocation extends ExistenceOf {
    Location place;
    /**
     * This is the constructor for this class. This classes only job is to store knowledge about location existence
     * @param place The location it is being held about
     */
    public ExistenceOfLocation(Location place){
        this.place = place;
    }
    
    /**
     * This method converts the contents of this knowledge into a string
     * @return This will return the name of the place + exists
     */
    
    /** 
     * @return String
     */
    @Override
    public String toString(){
        return place.getName() +" exists";
    }

    /**
     * This method grabs the place that the knowledge is about
     * @return The location that the knowledge is storing
     */
    public Location getPlace(){
        return place;
    }

    /**
     * This method checks if another object is equivalent to this piece of knowledge. It is considered to be if it is the same class and talks about the same place
     * @param other THe object that this is being checked against
     * @return This will return true if the object is equivalent
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof ExistenceOfLocation)){
            return false;
        }

        if(((ExistenceOfLocation) other).getPlace().equals(place)){
            return true;
        }
        return false;
    }

     /**
     * This method comes up with a hashvalue for this object. Because the locations all have unique id's that is what is being used for the hash value
     * @return This is the hash value for this knowledge
     */
    @Override
    public int hashCode(){
        return place.getId();
    }

}
