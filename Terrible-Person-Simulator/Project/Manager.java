import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Manager  {
    private static String[]  attributes ={"Gossip","Observant","Unobservant","Private","Social","Anti-Social"};

    private ArrayList<String> lastNames;
    private ArrayList<String> mFirstNames;
    private ArrayList<String> fFirstNames;
    private ArrayList<String> classNames;
    private ArrayList<String> clubNames;
    private ArrayList<String> deathMessages;

    final private Random randomizer;

    private Student[] students;
    final private Location[] classes;
    final private Location[] clubs;
    final private Building[] buildings;

    final private HashSet<Link> allFriendships;

    final private static int numberOfClassesPerStudent =4;
    final private static int startingNumberOfClubsPerStudent =2;
    private static int studentPopulation = 100;
    final private static int numberOfClasses = 20;
    final private static int numberOfClubs =15;
    final private static int numberOfBuildings = 9;
    
    private int day = 1;
    private int time=0;
    final private int deadline = 56;
    final private int hoursInADay = 10;
    final private int earlyChangeover = 4;
    final private int lateChangeover =8;
    final private int clubChangeOver = 5;
    private int arrestsLeft = 4;
    private boolean murderedYet = false;
    private Building currentBuilding;
    private Building dorms;
    final private Student murderer;
    final private Player player;
    boolean started = false;
    
/**
 * This method creates a manager object that then initializes many things that are neccessary for the game to start
 * @param testing If this is true then various testing mode features are enabled for debugging purposes
 * @param studentPopulation This is the size that student population will be
 * @param user The player object that needs to referred to for various things
 */
    public Manager(boolean testing, int studentPopulation, Player user){
        loadNames();
         randomizer = new Random();
        students = createStudents(studentPopulation);
        classes= new Location[numberOfClasses];
        clubs = new Location[numberOfClubs];
        buildings = createBuildings(numberOfBuildings);
        allFriendships = new HashSet<>();
        this.studentPopulation = studentPopulation;
        player = user;
        //Sets the current building to the dorms and also sets the dorm constant to the dorm
        for(Building structure :buildings){
            if(structure.name.equals("Dorm")){
                currentBuilding = structure;
                dorms = structure;
            }
        }
       
        //randomly chooses a murderer from the students
        murderer = students[randomizer.nextInt(studentPopulation)];
        
        createLocations();
        
         
        // Giving each student a relationship with each other student
         for(Student person: students){
            for(Student linkPartner:students){
                if(!(linkPartner.equals(person))){
                    //This checks to see if there is already a relationship with this person
                    if(!linkPartner.getRelationShips().keySet().contains(person)){
                        //If there isn't then it creates one and adds it to the linkpartner's and the person's relationship maps
                        Link relationship = new Link(person,linkPartner);
                        person.getRelationShips().put(linkPartner,relationship);
                        linkPartner.getRelationShips().put(person,relationship);
                    }
                    
                    
                }
            }
            
            
            //assigning each student 4 random classes and two random clubs
            for(int i=0;i<numberOfClassesPerStudent;i++){
                //picks a random class
                Location place = classes[randomizer.nextInt(numberOfClasses)];
                //attempts to add the student to the location, if they are already there it tries a different one
                while(!place.addStudent(person)){
                    place = classes[randomizer.nextInt(10)];
                }

            }
            for(int i=0;i<startingNumberOfClubsPerStudent;i++){
                //picks a random club
                Location place = clubs[randomizer.nextInt(numberOfClubs)];
                //attempts to add the student to the club, if they are already there it tries a different one
                while(!place.addStudent(person)){
                    place = clubs[randomizer.nextInt(10)];
                }

            }
         }

         if(testing){
            System.out.println("Students");
            for(Student person :students){
                System.out.println(person.getInfo(true));
            }
            System.out.println("Locations");
            for(Location place :classes){
                System.out.println(place.getInfo());
            }
            for(Location place :clubs){
                System.out.println(place.getInfo());
            }

         }
    }
/**
 * This method creates a number of students equal to the number given and then assigns them random names, genders, and ids
 * @param numberToCreate The number of students to create
 * @return an array containing all of the created students
 */
private Student[] createStudents(int numberToCreate){
students = new Student[numberToCreate];

 // Creates 100 students with random names and then places them into the student array
 for(int i=0;i<numberToCreate;i++){
    boolean gender = randomizer.nextInt(2)==0;
   String fName;
    if(gender){
        fName = fFirstNames.get(randomizer.nextInt(fFirstNames.size()));
    }
    else{
        fName = mFirstNames.get(randomizer.nextInt(mFirstNames.size()));
    }
    String lName = lastNames.get(randomizer.nextInt(lastNames.size()));
    students[i] = new Student(fName, lName, gender, i);
}
return students;

}
/**
 * This method creates a number of classes and clubs
 */
private void createLocations(){
// Creates 10 classes and adds them to the class array and then creates 10 clubs and adds them to it
        // After a name is chosen it is removed from the list of possible names so that there are no duplicate names
        //It then randomly assigns that activity as either early or late and then picks a random building to stick it in
        for(int i=0;i<numberOfClasses;i++){
            String name;
             name = classNames.get(randomizer.nextInt(classNames.size()));
             classNames.remove(name);
             Building place = buildings[randomizer.nextInt(1,numberOfBuildings)];
             classes[i] = new Location(name,true,i,randomizer.nextBoolean(),place);
             place.addEvent(classes[i]);
         }
         for(int i=0;i<numberOfClubs;i++){
             String name;
             name = clubNames.get(randomizer.nextInt(clubNames.size()));
             clubNames.remove(name);
             Building place = buildings[randomizer.nextInt(numberOfBuildings)];
             clubs[i] = new Location(name,false,i+numberOfClasses,randomizer.nextBoolean(),place);//sets id as i plus number of classes because classes will terminate at that number
             place.addEvent(clubs[i]);
          }
}
/**
 * This method creates the buildings for the game based on an input file. It uses said file to set names ids and connections between the buildings
 * @param buildingsToMake This asks for the number of buildings that are to be made
 * @return This returns an array containing all of the created buildings
 */
private Building[] createBuildings( int buildingsToMake){
    Building[] buildings = new Building[buildingsToMake];
try {
    Scanner fileReader = new Scanner(new File(getPath()+"/Names/Buildings.txt"));
    int i = 0;
    //This part creates all of the buildings and then assigns them to the array
    while(fileReader.hasNext()){
        String buildingName = fileReader.next().replace("_", " ");
        buildings[i]= new Building(buildingName,i);
        fileReader.nextLine();
        i++;
    }

    // This part gives the building their paths and other important information from reading the file
    i=0;
    fileReader = new Scanner(new File(getPath()+"/Names/Buildings.txt"));
    while(fileReader.hasNext()){
        Scanner reader= new Scanner(fileReader.nextLine());

        while (reader.hasNext()){
        String input = reader.next();
        if("0123456789".contains(input)){
            int pathTo = Integer.parseInt(input);
            buildings[i].addPath(buildings[pathTo],randomizer.nextInt(1,3));
        }
        }
        i++;
    }
        

    
} catch (Exception e) {
}

return buildings;

}
/**
 * This method creates a scanner object for each of our name files and then reads those files into array lists of strings
 *  for us to use later in generating names for certain objects
 */
    private void loadNames(){
        try{
        //makes array list then creates a scanner and reads through all of the names in the file putting those into the list
        fFirstNames = new ArrayList<>();
        Scanner reader= new Scanner(new File(getPath()+"/Names/FemaleFirstNames.txt"));
        while(reader.hasNext()){
            fFirstNames.add(reader.nextLine());
        }
        reader.close();

        lastNames = new ArrayList<>();
        reader = new Scanner(new File(getPath()+"/Names/LastNames.txt"));
        while(reader.hasNext()){
            String nextLine = reader.nextLine();
            lastNames.add(nextLine);
        }
        reader.close();

        mFirstNames = new ArrayList<>();
        reader = new Scanner(new File(getPath() +"/Names/MaleFirstNames.txt"));
        while(reader.hasNextLine()){
            String nextLine = reader.next();
            mFirstNames.add(nextLine);
        }
        reader.close();

        classNames = new ArrayList<>();
        reader = new Scanner(new File(getPath()+"/Names/Classes.txt"));
        while(reader.hasNext()){
            String nextLine = reader.nextLine();
            classNames.add(nextLine);
        }
        reader.close();

        clubNames  = new ArrayList<>();
        reader = new Scanner(new File(getPath() +"/Names/Clubs.txt"));
        while(reader.hasNext()){
            clubNames.add(reader.nextLine());
        }
        reader.close();

        deathMessages = new ArrayList<>();
        reader = new Scanner(new File(getPath() +"/Names/DeathMessages.txt"));
        while(reader.hasNext()){
            deathMessages.add(reader.nextLine());
        }
        reader.close();



    }
    catch(Exception e){
        System.out.println("One of the name files was not found");
    }

}

/**
 * This method returns the array of possible attributes for a student to have
 * @return
 */
public static String[] getAttributes(){
    return attributes;
}
/**
 * This method returns the number of students on the campus
 * @return The number of students on the campus
 */
public static int getStudentPopulation(){
    return studentPopulation;
}

/**
 * This method returns the number of classes on the campus
 * @return The number of classes on the campus
 */
public static int getClassNumber(){
    return numberOfClasses;
}

/**
 * This method returns the number of clubs on the campus
 * @return The number of clubs on the campus
 */
public static int getClubNumber(){
    return numberOfClubs;
}
/**
 * This method returns the student at a specified position in the student array
 * @param id The position in the array of the student
 * @return The student in the array
 */
public Student getStudentAt(int id){
    return students[id];
}



/** 
 * This method does all neccessary things to progress to the next day. This includs printing tests, telling people to make new friends, sharing knowledge and other various tasks
 * @param testing If this is true then various information is printed so that debugging can occur
 */
public void nextDay(boolean testing){
    //This part determines if the player has breached their deadline and if they have then they lose the game
    if(day >deadline){
        System.out.println("You ran out of time and got pulled from the case. The murderer was "+ murderer+ " and they were never caught. You failed.");
        System.exit(0);
    }
    //Sets chance to between 30 and 70 percent for the chances of people becoming friends
    int friendChance = randomizer.nextInt(30,71);
    //resets it so the murderer can murder again every weeks
    if(day%7==0| day == 1){
        murderedYet = false;
    }
    //Checks if it is a weekday.
    boolean weekday = IntStream.of(0,1,2,3,4).anyMatch(n-> day%7==n);
    //If it is a weekday then students go to one class and one club
    if(weekday){
        makeFriends(friendChance,true);
        makeFriends(friendChance,false);

    }
    //If it is not a weekday then students go to 2 clubs and have increased chances of making friends
    else{
        makeFriends(friendChance+10,false);
        makeFriends(friendChance+5,false);
    }
    //All of the students then share knowledge with each other
    knowledgeSharing();

    //5 Students try to join new clubs

    for(int i=0;i<5;i++){
        students[randomizer.nextInt(studentPopulation)].joinNewClub(friendChance);

    }
    //This gives a 1 in 7 chance for the murderer to murder and if it is the last day of the week then they definitely will
    if(!murderedYet&&(randomizer.nextInt(7)==1 || day%7==6)){
        murder(false);
    }

    //If testing then new info is printed
    if (testing){
        getInfo();
    }
    if(!started){
        return;
    }

    //This part prints out various useful information to the player and then pauses  so they can understand the state of the game
    String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    System.out.println(days[day%7]+" has passed");
    System.out.println("You have gone home.");
    System.out.println("You have " +(deadline-day)+ " days remaining to solve the case.");
    //This part changes the day and resets the time and building
    time=0;
    day++;
    currentBuilding = dorms;
    dramaticPause();

    
}
/**
 * This method returns the student that is the murderer
 * @return The murderer
 */
public Student getMurderer(){
    return murderer;
}

/**
 * This part prints various information and mainly exists for debugging
 */
private void getInfo(){
    
        System.out.println("Students");
        for(Student person :students){
            System.out.println(""+person.getInfo(false));
        }
        System.out.println("Locations");
        for(Location place :classes){
            System.out.println(place.getInfo());
        }
        for(Location place :clubs){
            System.out.println(place.getInfo());
        }

     
}
/**
 * This method goes through either the classes or the clubs and calls all of those location to make friendships
 * @param chance The chance that friendships are made for each location
 * @param isClass This should be true if the classes are being checked and false if it is the clubs
 */
private void makeFriends(int chance,boolean isClass){
    //This part will select the places array as either the classes or the clubs depending on if the isClass paramater is true
    Location[] places = isClass ? classes :clubs;
    //It then calls the createFriendship method one each Location in the place array.
    for(Location place : places){
        Link friendship =place.createFriendship(chance);
        //If a friendship is created it adds it the the set of links for all friendships
        if(friendship!=null){
            allFriendships.add(friendship);
        }
    }

}

/**
 * This method goes through every friendship and gets them to share knowledge
 */
private void knowledgeSharing(){
    //This part checks every friendship
    for(Link relationship:allFriendships){
        //For each student in the relationship it tries to share knowledge with the other person
        for(Student person:relationship.getStudents()){
            person.shareKnowledge(relationship.getOther(person));
        }
}
    
}
/**
 * This method gets the current path to the files so that they can be read. To do this it gets the current directory and then appends some files onto it if they don't exist so that the correct path is there
 * @return A string containg the path to the files
 */
public static String getPath(){
String currentPath = System.getProperty("user.dir");
if(!currentPath.contains("Terrible-Person-Simulator")){
    currentPath+=("/Terrible-Person-Simulator");
}
if(!currentPath.contains("Project")){
    currentPath += "/Project";
}
return currentPath;

}
/**
 * This Method takes the first collection and then gets rid of every element in it that is in the second collection. Essentially A-B
 * @param <E> This must extend the collections class. This is the collection type
 * @param subtractFrom The collection that everything is being subtracted from. This is A
 * @param subtractor The collection that is being subtracted. This is B
 * @return This returns every element in subtractFrom that is also in subtractor. This will return null if every element in subtractFrom is in subtractor
 */
public static <E extends Collection> ArrayList<Object> listDifference(E subtractFrom, E subtractor){
    ArrayList<Object> difference = new ArrayList<>();
    for(Object object : subtractFrom){
        if(!subtractor.contains(object)){
            difference.add(object);
        }
    }
    if(difference.isEmpty()){
        return null;
    }
    return difference;
    
}

/**
 * This method murders someone using breadth first searching.
 * @param testing This is so that this function can easily be tested
 */
public void murder(boolean testing){
    //This part makes sure that there are no murders before the game starts
    if(!started && !testing ){
        return;
    }
    //This part checks to make sure that the murder has friends that are alive
    boolean livingFriend = false;
    for(Student person : murderer.getFriends()){
        if(!person.isDead()){
            livingFriend = true;
        }
    }

    //If the murderer has no living friend then a random location is chosen from those the murderer attends and then a random person is chosen from there to become friends with the murderer
    if(!livingFriend){
        Location placeToMakeFriend = (Location)randomObject(murderer.getLocations());
        Student quickFriend;
        do{
         quickFriend =(Student) randomObject(placeToMakeFriend.getStudents());
        }
        while(quickFriend.equals(murderer));
        murderer.getRelationShips().get(quickFriend).becomeFriends();
    }

    //This part is setting stuff up for the breadth first search

    Student furthestFromDetective = null;
    int furthestDistance =-1;

    //Then for each person that the murderer is friends with a breadth first search takes place
    for(Student person :murderer.getFriends()){
        HashMap<Student,Student> connections = new HashMap<>();

        if(!person.isDead()){
            //It starts by setting up the queue and the explored 
            int distance = 0;
            ArrayDeque<Student> queue = new ArrayDeque<>();
            HashSet<Student> explored = new HashSet<>();
            //It then throws the first person onto the queue
            queue.add(person);
            boolean found = false;
            while(!queue.isEmpty() && !found){

                //It then takes the first person from the queue
                Student word = queue.remove();
                //It checks to see if they have been checked already and that they aren't the murder and if those are true then it passes
                if (!explored.contains(word)&& !word.equals(murderer)) {
                    //If it passes it gets thrown onto the explored pile
                    explored.add(word);

                    //It then goes through ever one of the persons friends
                    for(Student toCheck: person.getFriends()){

                        //It then creates a link between these two people in reverse order. This way if one of these people has a connection to the detective the distance can be determined through back tracing
                        if(!explored.contains(toCheck))connections.put(toCheck,word);

                        //It then checks to see if the detective knows about the persons friend
                        if(player.getKnownPeople().stream().map(n-> n.getPerson()).anyMatch(n -> n.equals(toCheck))){
                            // If they are then they have been found and the back trace starts
                            found = true;
                            Student oneBack = toCheck;
                            Student last = person;
                            
                            Student current = word;


                            //It then goes through the map of connections backwards to determine how far down was gone
                            while (!current.equals(person)) {
                                //This is pulling on the connection
                                current = connections.get(current);
                                //If the curren person that it is on is friends with the murderers friend it just decides to save time and add two to the distance and be done
                                if(current.getFriends().contains(person)){
                                    distance+= 2;
                                    break;
                                }
                                //If one of the friends contains the previous one then that means that map took an unneccesary connection and this is then rectified by taking a shorter path
                                else if(current.getFriends().contains(oneBack)){
                                    oneBack = last;
                                    last = current;
                                }
                                //otherwise it shifts everything up and increases the distance
                                else{
                                    oneBack = last;
                                    last = current;
                                    distance++;
                                }
                            }
                                break;
                        }
                        else{
                            //If the detective doesn't know about the person then the person is thrown on the queue
                            if(!explored.contains(toCheck) && !toCheck.isDead()){
                                queue.add(toCheck);
                            }
                        }
                }


            }

        }



            // It then checks to see if this person was the furthest from the detective and if they were then they are the new target
            if(furthestFromDetective==null){
                furthestFromDetective = person;
                furthestDistance = distance;
            }
            else{
                if(furthestDistance <distance){
                    furthestFromDetective = person;
                }
            }


        
    }
}
//This part prints a death message and then the person gets killed
    Student personToKill = furthestFromDetective;
    System.out.println("A Murder has occured");
    System.out.println(personToKill+ " "+(String)Manager.randomObject(deathMessages));
    player.learnKnowledge( new ExistenceOfPerson(personToKill), !testing);
    personToKill.kill();
    murderedYet = true;
    
    
   

}

/**
 * This part starts the game by simulating ten days, murdering someone and then printing some useful information
 */
public void startGame(){
    for(int i =0;i<10;i++){
        nextDay(false);
    }
    started = true;
    murder(false);
    System.out.println("You are a detective who's job it is to find this murderer.");
    System.out.println("This crime has been commited on a college campus that you have never been to and know nothing about.");
    System.out.println("The murderer has sent a note saying that they will only kill those they are friends with and they will only kill once a week");
    System.out.println("You must talk to students and determine who was friends with the current and possibly future murder victims to determine this killer");
    System.out.println("You have been give 8 weeks to solve this murder. Good Luck!");
    dramaticPause();


}
/**
 * This method puts a student into the array at a specific location, Meant as a debugging/testing method
 * @param person The student being inserted
 * @param id The position in the array to insert them
 */
public void addStudent(Student person,int id){
    students[id] = person;
}

public void addLocation(Location place, int id){
    
}
/**
 * This method asks the player what they would like to do through the scanner and then does that. It also calls next day
 * @param player The player object that this should be referencing
 */
public void makeDecision(Player player){
        boolean selected=false;
        Manager.clear(); 
        String input;
        do {
            getSchedule(false);
            System.out.println("Current Building: "+currentBuilding);
            System.out.println("\n1:Change Building");
            System.out.println("2:Attend Event in Building to talk to Students");
            System.out.println("3:Remember Information");
            System.out.println("4:Check Schedule");
            System.out.println("5:Pass time");
            System.out.println("6:Interrogate Student");
            System.out.println("7:Guess Murderer");
            System.out.print("What would you like to do?: ");
            Scanner reader = new Scanner(System.in);
             input = reader.next();

            if(("1234567").contains(input)){
                selected = true;
            }
            else{
                Manager.clear();
                System.out.println("Please enter a valid input");
    
            }
    
    
        }
        while(!selected);
        
        switch(input){

            case"1":
                changeBuilding();
            break;

            case"2":
                goToLocation();
            break;
            case"3":
            player.rememberInformation();
            break;
            case"4":
            getSchedule(true);
            break;
            case"5":
            passTime();
            break;
            case "6":
            arrestStudent();
            break;
            case"7":
            guessMurderer();
            break;

        }
        if(time>= 10){
            nextDay(false);
        }
    }
    /**
     * This method allows the player to arrest a student and gain all of their knowledge. The arrested student also cannot make friends or share knowledge while they are under arrest
     */
    public void arrestStudent(){
        if(arrestsLeft == 0){
            System.out.println("You have no interrogations left!");
            dramaticPause();
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Are you sure that you would like to interrogate a student? You have "+arrestsLeft +" interrogations left.");
        System.out.println("If you are then type in 'Yes I am'");
        String input = reader.nextLine();
        if(!input.toLowerCase().equals("yes i am")){
            return;
        }
        clear();
        List<Object> personNamed;
        boolean named = false;
        HashSet<ExistenceOfPerson> knownPeople =player.getKnownPeople();
        for(ExistenceOfPerson newPerson : knownPeople){
            if(!newPerson.getPerson().isDead())
            System.out.println(newPerson.getPerson());
        }
        do{
        System.out.print("Who would you like to interrogate? (Please type full name): ");
        input = reader.nextLine();
        String temp = input;
        //This part checks everyone the player knows of to find the person that their input is referencing
         personNamed = knownPeople.stream().map(n -> n.getPerson()).filter(n-> n.getName().toLowerCase().equals(temp.toLowerCase())).collect(Collectors.toList());
         if(!personNamed.isEmpty()){
            named = true;
         }
         else{
            System.out.println("Please enter a valid input");
        
        }

    }
    while(!named);
    Student person = (Student)personNamed.get(0);
    System.out.println(person+" was taken in for questioning. You will start interrogating them tomorrow");
    person.arrest();
    if(person.equals(murderer)){
        murderedYet = true;
    }
    nextDay(false);
    System.out.println("You start your interrogation");
    for(Class1Knowledge thingToKnow : person.getKnowledge()){
        player.learnKnowledge(thingToKnow, true);
    }
    clear();
    System.out.println("You finally finish interrogating "+person+ "and learned all that you could. They will be released tomorrow");
    nextDay(false);
    person.releaseFromArrest();
    if(person.equals(murderer)){
        murderedYet = false;
    }



    }

/**
 * This method allows the player to guess the murder. If they are correct then they win the game and if they are not then they lose the game
 */
    public void guessMurderer(){
        System.out.println("Are you absolutely sure that you want to guess the murderer (you only get 1 shot)?");
        System.out.println("If you are then type in 'Yes I am'");
        Scanner reader = new Scanner(System.in);
        String input = reader.nextLine();
        if(!input.toLowerCase().equals("yes i am")){
            return;
        }
        clear();
        List<Object> personNamed;
        boolean named = false;
        HashSet<ExistenceOfPerson> knownPeople =player.getKnownPeople();
        for(ExistenceOfPerson newPerson : knownPeople){
            System.out.println(newPerson.getPerson()+"  "+((newPerson.getPerson().isDead()? "   Deceased":"")));
        }
        do{
        System.out.print("Who do you think is the murderer? (Please type full name): ");
        input = reader.nextLine();
        String temp = input;
        //This part checks everyone the player knows of to find the person that their input is referencing
         personNamed = knownPeople.stream().map(n -> n.getPerson()).filter(n-> n.getName().toLowerCase().equals(temp.toLowerCase())).collect(Collectors.toList());
         if(!personNamed.isEmpty()){
            named = true;
         }
         else{
            System.out.println("Please enter a valid input");
        
        }

    }
    while(!named);
    if(((Student)personNamed.get(0)).equals(murderer)){
        System.out.println("Congratulations, you have succesfully solved the murders and cracked the case");
    }
    else{
        System.out.println("You are wrong. The murderer was "+ murderer+" and they have gotten away scott free.");
    }
    dramaticPause();
    System.exit(0);

    }
    /**
     * This method adds time to the time variable that represents the hour in the day
     */
    public void passTime(){
        clear();
        getSchedule(false);
        System.out.println("\nHow much time would you like to pass:");
        Scanner reader = new Scanner(System.in);
        time += reader.nextInt();
    }

    /**
     * This method allows the player to change the building that they are in. It first though displays some relevant information about timing before allowing the player to change their building 
     */
    public void changeBuilding(){
        clear();
        //It first gets very useful information about pathing from djikstras algorithm
        Map[] paths = currentBuilding.allPaths();
        
        //It then makes a new array of buildings so that it can understand references that the user makes to buildings
        Building[] buildingArray = new Building[currentBuilding.getPaths().size()];
        boolean selected  =false;
        String input;
        
        //It then prints out the optimal travel route to each building
        int i=0;
        getSchedule(false);
        System.out.println("Buildings");
        for(Object thing :paths[0].keySet()){
            Building place = (Building) thing;
            if(place != currentBuilding){
            System.out.print(place);
            System.out.print(":"+" ".repeat(18 -place.toString().length())+" Travel Time:"+paths[0].get(place));
            System.out.println("        Optimal path: " + paths[2].get(place));
            }
        }
        //It then prints out the buildings that can be traveled to
        System.out.println("\nBuildings that can be traveled to:");
        for(Path path:currentBuilding.getPaths()){
            Building place = path.getOther(currentBuilding);
            if(path.getTravelTime()+time>hoursInADay){
                System.out.println("Not Enough time"+":"+place+" "+path.getTravelTime()+" hour"+(path.getTravelTime()>1 ? "s":""));
            }
            else{
                System.out.println(i+":"+place+" "+path.getTravelTime()+" hour"+(path.getTravelTime()>1 ? "s":""));
                buildingArray[i] = place;
                i++;
            }
        }
        do{
            //It asks the user for the building that it would like to travel to and does input validation on their response
            System.out.print("\nWhere would you like to go to? (Please enter the number of the building you would like to travel to or enter to return to previous screen): ");
            Scanner reader = new Scanner(System.in);
            input = reader.nextLine();
            if("01234".contains(input)| input.equals("")){
                if(input.equals("")){
                    return;
                }
                if(Integer.parseInt(input)<buildingArray.length && buildingArray[Integer.parseInt(input)]!=null){
                    selected = true;
                }
            }
            else{
                
                System.out.println("Please enter valid input");
            }
        
    }   
    while(!selected);

    //It then switches to that building and passes the appropriate amount of time
    int selection = Integer.parseInt(input);
    Building place = buildingArray[selection];
    time += place.getPathTo(currentBuilding).getTravelTime();
    currentBuilding = place;
    }

    /**
     * This method determines the events taking place inside of a building and allows the user to attend them
     */
    public void goToLocation(){
        Location[] events = new Location[10];
        String input;
        boolean selected = false;
        clear();
        //If there is something going on that the player has not learned about then they learn about it
        boolean somethingIsHappening = false;
        boolean learnedSomething = false;
        for(Location place: currentBuilding.getEvents()){
            if(isGoingOn(place)){
                if(player.learnKnowledge(new ExistenceOfLocation(place), false)){
                    learnedSomething = true;
                }
            }
        }
        if(learnedSomething){
            dramaticPause();
            clear();
        }

        //It then lists all events happenieng at the moment and puts them into an array so that it can better understand player input
            int i =0;
            for(Location place :currentBuilding.getEvents()){
                if(isGoingOn(place)){
                    somethingIsHappening = true;
                    System.out.println(""+i+": "+place);
                    events[i] = place;
                    i++;
                }

            }
                    //If no events are happening then the player is informed
            if(!somethingIsHappening){
                if(currentBuilding.equals(dorms)){
                    System.out.println("No classes take place in the Dorms");
                    dramaticPause();
                    return;
                }
                else{
                    System.out.println("There are no events in this building at this time");
                    dramaticPause();
                    return;
                }
                
            }
            do{
            System.out.print("Which event would you like to attend (press enter to return to previous screen): ");

            Scanner reader = new Scanner(System.in);
            input = reader.nextLine();
                //Input validation is then performed
            if("012345678910".contains(input) || input.equals("")){
                if(input.equals("")){
                    return;
                }
                if(Integer.parseInt(input) < events.length && events[Integer.parseInt(input)]!=null){
                    selected = true;
                }
                else{
                    clear();
                    System.out.println("Please enter valid input");
                }
            }
            else{
                clear();
                System.out.println("Please enter valid input");
            }
        }
        while (!selected);
        //That location is then gone to
        doThingAtLocation(events[Integer.parseInt(input)]);



    }

    /**
     * This method allows the player to talk to a student at a location
     * @param place The location to talk to a student at
     */
    public void doThingAtLocation(Location place){
        Student[] students = new Student[60];
        String input;
        boolean selected = false;
        clear(); 
         
        //The player learns about all of the students who are in this class that they didn't know about before
        int i =0;
        boolean learnedSomething = false;
        for(Student person:place.getStudents()){
            if(player.learnKnowledge(new Habit(person,place), false)){
                learnedSomething = true;
            }
        }
        if(learnedSomething){
            dramaticPause();
            clear();
        }

        //It then prints out all the student that attend this event that are not dead
        for(Student person: place.getStudents()){
            if(!person.isDead()){
                System.out.println(i+": "+person);
                students[i] = person;
                i++;
            }
         }
        //It asks the player who they would they would like to talk to and does input validation
         do{
            System.out.print("Which student would you like to talk to (please enter number): ");
               Scanner reader= new Scanner(System.in);
               input = reader.nextLine();

               if("0123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960".contains(input) && !input.equals("")){
                   if(Integer.parseInt(input)<students.length&&students[Integer.parseInt(input)]!=null){
                       selected = true;
                   }
                   else{
                       
                       System.out.println("Please enter valid input");
                   }
               }
    }
    while(!selected);

        talkTo(students[Integer.parseInt(input)]);
        



    }
    /**
     * This method allows the player to talk to and gain information from students
     * @param person The person student that the player is talking to
     */
    public void talkTo(Student person){

        boolean selected = false;
        String input;
        clear();
        //It then asks the player what they would like to talk to the student about and does input validation on the response
        System.out.println("1:Ask about person (Gives all knowledge related to person)");
        System.out.println("2:Ask about location (Gives all knowledge related to location)");
        System.out.println("3:Have a conversation (Gives random knowledge that the student knows)");
        do{
            System.out.print("What would you like to do: ");
            Scanner reader= new Scanner(System.in);
            input = reader.nextLine();

            if(("1".equals(input)|"2".equals(input)|"3".equals(input))&& !input.equals("")){
                selected = true;
        
            }
            else{
                
                System.out.println("Please enter valid input");
            }
     } while (!selected);
        clear();
        switch(input){
            case "1":{
                //If they want to talk about a student then the first all of the people that the player knows about are listed so that they have reference for their names
                Scanner reader = new Scanner(System.in);
                List<Object> personNamed;
                boolean named = false;
                HashSet<ExistenceOfPerson> knownPeople =player.getKnownPeople();
                HashSet<Student> thoseDeceased = new HashSet<>();
                for(ExistenceOfPerson regarding : knownPeople){
                    if(regarding.getPerson().isDead()){
                        thoseDeceased.add(regarding.getPerson());
                    }
                    else{
                    System.out.println(regarding.getPerson());
                    }
                }
                for(Student regarding :thoseDeceased){
                    System.out.println( regarding + " Deceased");
                }
                do{
                    //It then asks the player who they would like to talk to
                System.out.print("Who would you like to talk about? (Please type full name): ");
                input = reader.nextLine();
                String temp = input;
                //It then checks that name against every person that the player knows, and finds the student that they were referring to
                 personNamed = knownPeople.stream().map(n -> n.getPerson()).filter(n-> n.getName().toLowerCase().equals(temp.toLowerCase())).collect(Collectors.toList());
                 if(!personNamed.isEmpty()){
                    named = true;
                 }
                 else{
                    System.out.println("Please enter a valid input");
                
                }
    
            }
            while(!named);
            // it then finds the person they were talking to and gives the player everything piece of knowledge that they have regarding the person that they were asking about
            HashSet<Class1Knowledge> personsKnowledge =person.getKnowledge();
            boolean learned = false;
            for(Class1Knowledge thing :personsKnowledge){
                if(thing.underlyingKnowledge().contains(new ExistenceOfPerson((Student) personNamed.get(0)))){
                if(player.learnKnowledge(thing,true)){
                    learned = true;
                }

                }
            }
            if(!learned){
                System.out.println("This person had no new information on this person");
                dramaticPause();
            }

            break;
            }
            case "2":
                //This one works the same as the person asking except for locations
               { Scanner reader = new Scanner(System.in);
                List<Object> locationNamed;
                boolean named = false;
                HashSet<ExistenceOfLocation> knownPlaces =player.getKnownLocations();
                for(ExistenceOfLocation newPlace : knownPlaces){
                    System.out.println(newPlace.getPlace());
                }
                do{
                System.out.print("What event would you like to talk about? (Please type full name): ");
                input = reader.nextLine();
                String temp = input;
                 locationNamed = knownPlaces.stream().map(n -> n.getPlace()).filter(n-> n.getName().toLowerCase().equals(temp.toLowerCase())).collect(Collectors.toList());
                 if(!locationNamed.isEmpty()){
                    named = true;
                 }
                 else{
                    System.out.println("Please enter a valid input");
                
                }

            }
            while(!named);
            HashSet<Class1Knowledge> personsKnowledge =person.getKnowledge();
            boolean learned = false;
            for(Class1Knowledge thing :personsKnowledge){
                if(thing.underlyingKnowledge().contains(new ExistenceOfLocation((Location) locationNamed.get(0)))){
                    if(player.learnKnowledge(thing,true)){
                        learned = true;
                    }
            }
        }
            if(!learned){
                System.out.println("This person had no new information on this event");
                dramaticPause();
            }
            
                break;
            }

            case "3":{
                //In this case the person gives the player 10 random things they known but that value can be modified by attributes
                HashSet<Class1Knowledge> knowledge = person.getKnowledge();
                int thingsToTalkAbout = 10;
                boolean learnedSomething = false;
                if(person.getAttributes().contains("Gossip")){thingsToTalkAbout*=2;}
                if(person.getAttributes().contains("Private")){thingsToTalkAbout/=2;}
                for(int i=0;i<thingsToTalkAbout; i++){
                   if( player.learnKnowledge((Class1Knowledge) Manager.randomObject(knowledge),false)){
                    learnedSomething = true;
                   }
                    
                }
                if(learnedSomething){
                    dramaticPause();
                }
                else{
                    System.out.println("You talked with " + person + " and learned nothing new");
                    dramaticPause();
                }

            }
            }
            time+=2;
    }

    /**
     * This method displays various information about the current time 
     */
    public void getSchedule(boolean pause){
        clear();
        String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        System.out.println("Day: "+days[day%7]);
        System.out.println("Time: "+time);
        if(pause){
            System.out.println("Known Locations Schedules");
            for(Building structure : buildings){
                if(structure.getEvents().stream().anyMatch(n-> player.getKnownLocations().contains(new ExistenceOfLocation(n)))){

                
                System.out.println(structure);
                System.out.println("    Early Classes M-F 0-4:");
                structure.getEvents().stream().filter(n->n.isClass()&& n.isEarly()).filter(n-> player.getKnownLocations().contains(new ExistenceOfLocation(n))).forEach(n->System.out.println("         "+n));
                System.out.println("    Late Classes M-F 4-8:"); 
                structure.getEvents().stream().filter(n->n.isClass()&& !n.isEarly()).filter(n-> player.getKnownLocations().contains(new ExistenceOfLocation(n))).forEach(n->System.out.println("        "+n));
                System.out.println("    Early Clubs M-F 8-10, Weekends 0-5:");
                structure.getEvents().stream().filter(n->!n.isClass()&& n.isEarly()).filter(n-> player.getKnownLocations().contains(new ExistenceOfLocation(n))).forEach(n->System.out.println("        "+n));
                System.out.println("    Late Clubs M-F 8-10, Weekends 5-10:");
                structure.getEvents().stream().filter(n->!n.isClass()&& !n.isEarly()).filter(n-> player.getKnownLocations().contains(new ExistenceOfLocation(n))).forEach(n->System.out.println("       "+n));
                }
                System.out.println();
            }


        dramaticPause();
        }
    }

    /**
     * This method checks to see if a specific event is currently occuring
     * @param place The event that is being checked to see if it is happening rightnow
     * @return This will return true if the event is happening right now
     */
    public boolean isGoingOn(Location place){
         if(!place.isClass()){
            //Checking if it is a weekday
            if(IntStream.of(1,2,3,4,5).anyMatch(n-> n==day%7)){
                return lateChangeover<=time;
            }
            // If it is not a weekday
            if(place.isEarly()){
                return 0<=time && time<clubChangeOver;

            }
            return time>= clubChangeOver;

         }

         if(IntStream.of(1,2,3,4,5).anyMatch(n-> n==day%7)){
            if(place.isEarly()){
                return 0<= time && time <earlyChangeover;

            }
            return earlyChangeover <= time && time<lateChangeover;
         }

         return false;
    }


/**
 * This method takes a collection and the pulls a random element from it. It does by randomly selecting a number less than the size of the list and then iterates through it that many times to return that object
 * @param <T> The type of collection that is being checked
 * @param collection The collection that is having a random element pulled from it
 * @return This will return an object that was randomly selected from the collection or it will return null if the collection is empty
 */
public static <T extends Collection> Object randomObject(T collection){
    Random randomizer = new Random();
    int number = randomizer.nextInt(collection.size());
    int i=0;
    for(Object thing : collection){
        if(i==number){
            return thing;
        }
        i++;
    }
    return null;   
}
/**
 * This method clears the terminal
 */
public static void clear(){
    String osName = System.getProperty("os.name");
    try{
        // Code taken from https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
        if(osName.contains("Mac")){
            System.out.println("this didn't work");
            System.out.print("\033\143");

        }
        else{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }
    catch(Exception e){
        System.out.println("The clear command broke");
    }

}
/**
 * This method gives a blank scanner which forces the player to press enter to continue
 */
public static void dramaticPause(){
    Scanner reader = new Scanner(System.in);
    System.out.println("Press enter to continue");
    reader.nextLine();

}



}