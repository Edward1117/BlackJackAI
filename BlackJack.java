public class BlackJack {

    //main to execute the blackjack program
    public static void main(String[] args){
        //Welcome message
        System.out.println("Welcome to BlackJack AI version!! ");
        System.out.print("To quit the game simply enter the word 'quit' " + "\n");

        //create player
        Player p1 = new Player(50, "Edward");

        //create new blackjack game
        Game blackJack = new Game();
        //adds the first player to the game
        blackJack.addPlayerToTable(p1);
        //starts the game
        blackJack.startGame();
    }
}
