public class Game{

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        Player player = new Player();
        Manager manager = new Manager(false,100,player);
        Manager.clear();
        manager.startGame();
        while (true) { 
            manager.makeDecision(player);
        }}
        



    }


    



