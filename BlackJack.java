import java.util.Scanner;

public class BlackJack {

    //main to execute the blackjack program
    public static void main(String[] args){
        //Welcome message
        System.out.println("Welcome to BlackJack AI version!! ");
        System.out.print("To quit the game simply enter the word 'quit' " + "\n");

        //message for user name input
        System.out.println("Please input your name  ");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();

        //message for user bank amount
        System.out.print("Please enter how much funds you wish to deposit to start the game"+"\n");
        Integer bank = scan.nextInt();
        //create player
        Player p1 = new Player(bank, name);

        //create new blackjack game
        Game blackJack = new Game();

        //adds the first player to the game
        blackJack.addPlayerToTable(p1);

        System.out.println("\n"+"There are two modes in this game");
        System.out.println("\n"+"Do you wish to play the game or watch the AI play? ");
        System.out.print("To Watch the Ai play please enter 'ai' otherwise input play"+"\n");
        String input = scan.nextLine();
        input = scan.nextLine();


        //check for userinput to determine if the ai plays or the user plays
        if (input.toLowerCase().equals("ai")){
            System.out.println("Please input an amount you wish the AI to leave the table with if reached ");
            Double goal = scan.nextDouble();
            blackJack.aiGame(goal);
        }
        else if (input.toLowerCase().equals("play"))
            blackJack.startGame();
    }
}
