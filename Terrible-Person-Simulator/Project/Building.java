import java.util.*;
public class Building {

    int id;
    String name;
    HashSet<Location> thingsThatHappenHere;

    HashSet<Path> paths;

    /**
     * This method creates a new building object with the following attributes and creates some relevant constants
     * @param name The buildings name
     * @param id The buildings unique identifying number
     */
    public Building(String name, int id){
        this.name = name;
        this.id = id;
         thingsThatHappenHere = new HashSet<>();
         paths = new HashSet<>();
    }
    /**
     * This method creates a path between this building and another one
     * @param toGo The building to create the path to
     * @param travelTime The time it takes to travel that pass
     * @return This returns true if a path was created
     */
    public boolean addPath(Building toGo, int travelTime){
        Path newPath = new Path(this,toGo,travelTime);
        toGo.addPath(newPath);
        return addPath(newPath);

    }

    /**
     * This method gets the path between this building and another one
     * @param place The building that the path being asked about is to
     * @return The path being asked about, this will be null if one does not exist
     */
    public Path getPathTo(Building place){
        for(Path linkTo: paths){
            if(linkTo.getOther(this).equals(place)){
                return linkTo;
            }
        }
        return null;

    }
    /**
     * This method adds a new path to the set of the buildings paths
     * @param travelOver The path being added
     * @return This will return true if the path was successfully added
     */
    public boolean addPath(Path travelOver){
        return paths.add(travelOver);

    }
    /**
     * This method gets all of this buildings paths
     * @return A set containing all of this buildings paths
     */
    public HashSet<Path> getPaths(){
        return paths;
    }
    /**
     * This method returns the buildings id number
     * @return The id of the building
     */
    public int getId(){
        return id;
    }
    /**
     * This method converts the building into a string. This is just the buildings name
     * @return The buildings name
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * This method prints various useful pieces of information about the building, useful for testing
     */
    public void getInfo(){
        System.out.println(name);
        for(Path path : paths){
            System.out.print(path+",");
        }
        System.out.println();
    }

    /**
     * This method generates a hashcode for a Building and since they all have unique ids those are also used as hasvalues
     * @return The hash value for the Building
     */
    @Override
    public int hashCode(){
        return id;
    }

    /**
     * This method checks whether this Building is the same as a different Building
     * It uses the Building Ids to determine this
     * @param other The object being checked against
     * @return This will return true if the building ids are the same
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Building)){
            return false;
        }
        Building place = (Building) other;
        return (place.getId() == id);
    }
    /**
     * This method finds the optimal path from this building to every other building and then returns that in various forms
     * @return This is returned as an array of hashmaps. Position 0 holds the optimal travel time from this building to each other one. Position one holds pre processed information regarding the last path that was traveled on to get to each building
     * Position 2 holds the optimal path to each building from this one as a string 
     */
    public HashMap[] allPaths(){
        //This is the djikstras implementation

        PriorityQueue<BuildingNode> queue = new PriorityQueue<>((v1,v2)->v1.getWeight()-v2.getWeight());
        HashSet<Building> explored = new HashSet<>();
        HashMap<Building,Integer> times = new HashMap<>();
        HashMap<Building,Path> pathsTo = new HashMap<>();
        queue.add(new BuildingNode(0,this));
        times.put(this,0);

        while(!queue.isEmpty()){
            
            BuildingNode current = queue.poll();
            Building currentBuilding = current.getBuilding();

            if(!(explored.contains(current.getBuilding()))){
                explored.add(current.getBuilding());
                 HashSet<Path> buildingsPaths = currentBuilding.getPaths(); 
                for(Path toGo: buildingsPaths){
                    Building place = toGo.getOther(currentBuilding);
                    if(!(times.keySet().contains(toGo.getOther(currentBuilding)))){
                            times.put(place,Integer.MAX_VALUE);
                            pathsTo.put(place,toGo);

                    }
                    if(times.get(place)> current.getWeight()+toGo.getTravelTime()){
                        times.put(place, current.getWeight()+toGo.getTravelTime()); 
                        pathsTo.put(place,toGo);
                    }
                    queue.add(new BuildingNode(times.get(place),place));


                }
            
        
        }
        
            

        }

        HashMap<Building,String> pathStrings = new HashMap<>();
        for(Building place :pathsTo.keySet()){
            pathStrings.put(place,getPathString(pathsTo,place));
        }
        HashMap[] maps = new HashMap[3];
        maps[0]=times;maps[1]=pathsTo;maps[2]=pathStrings;

        return maps;
    }

    /**
     * This method returns the string for a path to it given a map of paths
     * @param paths The map of buildings and paths 
     * @param place if place is this building then this function will return the base case
     * @return The string containg the way to this building given the paths
     */
    private String getPathString(HashMap<Building,Path> paths,Building place){
        if(paths.get(place).getOther(place).equals(this)){
            return this.name+ " -> "+place;
        }
        return getPathString(paths,paths.get(place).getOther(place))+" -> "+place;

    }
    /**
     * This method returns all of the Locations at the building
     * @return The set of locations for this building
     */
    public HashSet<Location> getEvents(){
        return thingsThatHappenHere;
    }
    /**
     * This method adds a new Location to the set of events that happen here
     * @param place this is the location to be added to this building
     */
    public void addEvent(Location place){
        thingsThatHappenHere.add(place);
    }



}
