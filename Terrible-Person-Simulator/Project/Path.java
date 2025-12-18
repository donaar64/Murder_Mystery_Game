public class Path {
Building placeA;
Building placeB;
int travelTime;
/**
 * This method creates a path object with the following variables
 * @param a One of the buildings that it connects
 * @param b The other building that it connects
 * @param travelTime The time it takes to get between them
 */
public Path(Building a, Building b, int travelTime){
    placeA =a;
    placeB= b;
    this.travelTime = travelTime;
}
/**
 * This method returns the travel tiem for the path
 * @return The travel time for the path
 */
public int getTravelTime(){
    return travelTime;
}

/**
 * This method returns the building on the other end of the path from a specific building
 * @param place The place that you are checking the other side of the path from
 * @return The building on the other end of the path
 */
public Building getOther(Building place){
    if(!place.equals(placeA) && !place.equals(placeB)){
        return null;
    }

    return place.equals(placeA) ? placeB:placeA;
}

/**
 * This method checks if this path is equal to some other object. It says that it is if it is also a path and it has the same buildings
 * @param other The object being checked against to see if it is equal
 * @return This will return true if the objects are deemed to be equal to each other
 */
@Override
public boolean equals(Object other){
    if(!(other instanceof Path)){
        return false;
    }
    Path otherPlace = (Path) other;
    return otherPlace.getOther(placeA).equals(placeB);
}

/** 
     * This method uses cantor's pairing function to generate a unique integer for a set of Buildings. The max is taken from each to ensure that the Buildings are in a consistent order
     * @return The hash code that is generated
     */
@Override
public int hashCode(){
    double student1 = Math.max(placeA.getId(),placeB.getId());
    double student2 = Math.min(placeA.getId(),placeB.getId());
    return (int)((student1+student2)*((student1+student2+1.0))/2.0+student2);
}
/**
 * This method returns the string object for the path 
 * @return This will be placeA +  " -> "+ placeB+ " "+ travelTime+ " hours"
 */
@Override
public String toString(){
return placeA +  " -> "+ placeB+ " "+ travelTime+ " hours";


}

}
