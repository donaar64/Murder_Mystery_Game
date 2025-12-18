public class BuildingNode {
private int weight;
private Building object;

/**
 * This method creates a building node withe the given weight and building
 * @param weight The time it takes to get to this node
 * @param object The building for this node
 */
public BuildingNode(int weight, Building object){
    this.weight = weight;
    this.object = object;
}
/**
 * This method returns the weight of the node
 * @return the weight of the node
 */
public int getWeight(){
    return weight;
}
/**
 * This method returns the building for this node
 * @return The building for this node
 */
public Building getBuilding(){return  object;}


}
