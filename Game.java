
import java.util.ArrayList;
import java.util.Scanner;


public class Game {
    //scanner for user input
    private Scanner userInput;
    //list of people playing
    private ArrayList<Player> table;
    //dealer for the table
    private Player dealer;
    //deck the dealer is using
    private Deck currDeck;
    //flag to check if game has ended or not
    private boolean flag;
    //create user input String
    private String input;
    //create array to contain valid bets
    private static int[] validBets = {5,10,15,20,50,100};


    //constructor for BlackJack
    public Game(){
        //init table dealer
        this.dealer = new Player();

        //init table
        this.table = new ArrayList<>();

        //init deck
        this.currDeck = new Deck();
        //init all cards in deck
        this.currDeck.createDeck();
        //shuffle deck
        this.currDeck.shuffle();

        //init input scanner
        this.userInput = new Scanner(System.in);

        //init flag to true to start game
        this.flag = true;


    }


    public void startGame(){
        table.add(dealer);
        //draw 2 cards for everyone in the game
        dealCards();
        dealCards();

        //System.out.println("Your current Cards are:" + table.get(0).getHand().toString());

        while(flag == true){
            // for loop to go through each person in the table
            for( int i = 0; i < table.size(); i ++) {
                //if statement to check if dealers turn
                if(table.get(i).equals(dealer)){
                    System.out.print("-------------Dealers Turn-------------");
                    dealerAI();
                    break;
                }

                //Prints out statement to acquire a bet amount
                System.out.println("\n"+"Current Player : "+ table.get(i).toString());
                System.out.print("\n"+"Valid bets : $5 , $10, $20, $50, $100" + "\n");
                System.out.print(" Please enter a bet amount: ");


                input = userInput.nextLine();

                //if statement to check whether or not statement is a valid number
                if((isValidNumeric(input))){

                    // /changed input String to a double
                    Double bet = Double.parseDouble(input);

                    //Check whether amount inputted is greater than given funds
                    //Checks Whether amount is less than or equal to 0
                    //loops until a correct amount is given
                    while((bet > table.get(i).getBank()) || (bet <= 0) ){
                        System.out.println("Insufficient Funds");
                        System.out.println("Please input a valid amount");
                        input = userInput.nextLine();
                        bet = Double.parseDouble(input);
                        table.get(i).setBet(bet);
                    }

                    table.get(i).setBet(bet);
                    //loop to begin game continues current player until 2 is found for stand
                    while(Integer.parseInt(input) != 2){

                        //check if current hand contains black jack
                        //increments players bank and then moves on to next player
                        if((table.get(i).getHand().deckValue(table.get(i).getHand()) == 21) && (table.get(i).getHand().deckSize() == 2)) {
                            table.get(i).setBet(table.get(i).getBet()*1.5);
                            input = "2";
                            System.out.println("Congrats, You've Got BlackJack!!!");
                            break;
                        }

                        System.out.println("\n"+"Your current bet is: $"+ bet);
                        //shows dealers first card
                        System.out.println("\n"+"Dealer's First Card is: " + "\n -" + dealer.getHand().getCard(0));

                        //shows players cards and hand value
                        System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
                        System.out.println("Which Equal : " + table.get(i).getHand().deckValue(table.get(i).getHand()));

                        //prints our statement to check for player action
                        System.out.println(" -------------What would you like to do?-------------");
                        System.out.println(" Please input the number for an action");
                        System.out.println("\n"+"(1)Hit | (2)Stand |(3)Double Down | (4)Split | quit");


                        //get user input
                        input = userInput.nextLine();
                        checkAction(bet,i,input);
                    }
                }
                else if (input.equals("quit"))
                    System.exit(0);
            }

            compareHandToDealer();
            reset();
            dealCards();
            dealCards();

        }
    }



    //method to execute current action from user
    public void checkAction(Double bet,Integer i,String in){
        //checks if input is valid
        if(isValidNumeric(in) ){
            int action = Integer.parseInt(input);
            switch(action){
                //Player has chosen to pick a new card
                case 1 :
                    //current action is HIT
                    checkHand(i,bet,"1");
                    break;
                case 2 :
                    //current action is STAND
                    System.out.println("You've Stayed with: "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                    input= "2";;
                    break;
                case 3:
                    //current action is DOUBLE DOWN
                    bet = bet + bet;
                    checkHand(i,bet,"2");
                    System.out.println("Next players turn" );
                    break;
                case 4:
                    if(table.get(i).getHand().getCard(0).getValue() == table.get(i).getHand().getCard(1).getValue()){

                        table.get(i).getHand().setSplit(true);
                        table.get(i).getHand().removeCard(1);

                        System.out.println(table.get(i).getHand().deckValue(table.get(i).getHand()));
                        System.out.println(table.get(i).getHand().toString());

                        //prints our statement to check for player action
                        System.out.println(" -------------What would you like to do?-------------");
                        System.out.println(" Please input the number for an action");
                        System.out.println("\n"+"(1)Hit | (2)Stand |(3)Double Down | (4)Split | quit");

                        input = userInput.nextLine();
                        checkAction(bet,i,input);
                        checkHand(i,bet,input);
                        table.get(i).setSplitDeck(table.get(i).getHand());

                        for(int j = 1; j < table.get(i).getHand().deckSize(); j++){
                            table.get(j).getHand().removeCard(j);
                        }

                        //prints our statement to check for player action
                        System.out.println(" -------------What would you like to do?-------------");
                        System.out.println(" Please input the number for an action");
                        System.out.println("\n"+"(1)Hit | (2)Stand |(3)Double Down | (4)Split | quit");

                        input = userInput.nextLine();
                        checkAction(bet,i,input);
                        checkHand(i,bet,input);



                    }
                    break;
                default :
                    //break out of switch statement to report error
                    System.out.println("ERROR ---- Please Input A Valid Action ---- ERROR  ");
                    break;

            }
        }
        else if (input.equals("quit"))
            System.exit(0);
    }

    //error check player hand values
    public void checkHand(int i, Double bet,String currAction){
        table.get(i).getHand().draw(currDeck);
        //auto win at 21 add current bet to player bank
        if(table.get(i).getHand().deckValue(table.get(i).getHand()) == 21){
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println("Congrats, You've got 21");
            table.get(i).addToBank(bet);
            input = "2";
        }
        //bus if hand value is > 21
        else if (table.get(i).getHand().deckValue(table.get(i).getHand()) > 21){
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println(" You've busted with " + table.get(i).getHand().deckValue(table.get(i).getHand()));
            table.get(i).subToBank(bet);
            input = "2";
            System.out.println("Current funds remaining: " + table.get(i).getBank());
        }
        else{
            //set current action to what was inputted and output cards and values
            input = currAction;
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println("You ended with: " + table.get(i).getHand().deckValue(table.get(i).getHand()));
        }
    }

    //method dto check hands for dealer
    public void checkHand(String currAction){
        dealer.getHand().draw(currDeck);
        //auto win at 21 add current bet to player bank
        if(dealer.getHand().deckValue(dealer.getHand()) == 21){
            System.out.println("Your current Cards are:" + dealer.getHand().toString());
            System.out.println("Congrats, You've got 21");
        }
        //bus if hand value is > 21
        else if (dealer.getHand().deckValue(dealer.getHand()) > 21){
            System.out.println("Your current Cards are:" + dealer.getHand().toString());
            System.out.println(" You've busted with " + dealer.getHand().deckValue(dealer.getHand()));
            input = "2";
        }
        else{
            //set current action to what was inputted and output cards and values
            input = currAction;
            System.out.println("Your current Cards are:" + dealer.getHand().toString());
            System.out.println("You ended with: " + dealer.getHand().deckValue(dealer.getHand()));
        }
    }

    //adds a player to the table
    public void addPlayerToTable(Player p){
        this.table.add(p);
    }

    //method to get all current players in game
    public void currentPlayers(){
        for(int i = 0; i<table.size(); i++){
            System.out.println(table.get(i).toString());
        }
    }


    //method to deal everyone in the table a card
    public void dealCards(){
        for (int i = 0; i < table.size(); i++){
            table.get(i).getHand().draw(currDeck);
        }
    }

    //checks if input is valid number
    public boolean isValidNumeric(String str)
    {
        str = str.trim(); // trims the white spaces.

        if (str.length() == 0)
            return false;

        // if string is of length 1 and the only
        // character is not a digit
        if (str.length() == 1 &&
                !Character.isDigit(str.charAt(0)))
            return false;

        // If the 1st char is not '+', '-', '.' or digit
        if (str.charAt(0)!='+' && str.charAt(0)!='-' &&
                !Character.isDigit(str.charAt(0)) &&
                str.charAt(0)!='.')
            return false;

        // To check if a '.' or 'e' is found in given
        // string. We use this flag to make sure that
        // either of them appear only once.
        boolean flagDotOrE = false;

        for (int i=1; i<str.length(); i++)
        {
            // If any of the char does not belong to
            // {digit, +, -, ., e}
            if (!Character.isDigit(str.charAt(i)) &&
                    str.charAt(i) != 'e' && str.charAt(i)!='.' &&
                    str.charAt(i) != '+' && str.charAt(i)!='-')
                return false;

            if (str.charAt(i) == '.')
            {
                // checks if the char 'e' has already
                // occurred before '.' If yes, return 0.
                if (flagDotOrE == true)
                    return false;

                // If '.' is the last character.
                if (i+1 >= str.length())
                    return false;

                // if '.' is not followed by a digit.
                if (!Character.isDigit(str.charAt(i+1)))
                    return false;
            }

            else if (str.charAt(i) == 'e')
            {
                // set flagDotOrE = 1 when e is encountered.
                flagDotOrE = true;

                // if there is no digit before 'e'.
                if (!Character.isDigit(str.charAt(i-1)))
                    return false;

                // If 'e' is the last Character
                if (i+1>=str.length())
                    return false;

                // if e is not followed either by '.',
                // '+', '-' or a digit
                if (!Character.isDigit(str.charAt(i+1)) &&
                        str.charAt(i+1) != '+' &&
                        str.charAt(i+1) != '-')
                    return false;
            }
        }

        /* If the string skips all above cases, then
           it is numeric*/
        return true;
    }

    //compare everyone's hands to dealers to see if theyve won
    public void compareHandToDealer(){
        System.out.println("Dealer has: " +dealer.getHand().deckValue(dealer.getHand()));
        for (int i = 0; i < table.size(); i++) {
            //if statement to check if dealers hand is greater then player and is less than or equal to 21
            if ((dealer.getHand().deckValue(dealer.getHand()) > table.get(i).getHand().deckValue(table.get(i).getHand()) && (dealer.getHand().deckValue(dealer.getHand()) <= 21))){
                System.out.println(" You've lost this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).subToBank(table.get(i).getBet());
                System.out.print("Remaining Bank "+ table.get(i).getBank());
                dealer.addToBank(table.get(i).getBet());
            }
            // else if to check if player hand is greater then dealer and is less than to 21
            else if((dealer.getHand().deckValue(dealer.getHand()) < table.get(i).getHand().deckValue(table.get(i).getHand())) && (table.get(i).getHand().deckValue(table.get(i).getHand()) <= 21) ){
                System.out.println(" You've Won this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).addToBank(table.get(i).getBet());
                System.out.print("Remaining Bank: "+ table.get(i).getBank());
            }
            //do nothing if dealer is the player
            else if(table.get(i).equals(dealer))
                break;
            //checks if ur hand value is equal to the dealer
            else if(dealer.getHand().deckValue(dealer.getHand()) == table.get(i).getHand().deckValue(table.get(i).getHand()))
                System.out.println("Push, Your equal with the dealer with: "+ table.get(i).getHand().deckValue(table.get(i).getHand()));

            //dealer hand value is greater then 21, player auto wins if under 21
            else if((dealer.getHand().deckValue(dealer.getHand()) > 21) && (table.get(i).getHand().deckValue(table.get(i).getHand()) < 21)){
                System.out.println(" You've Won this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).addToBank(table.get(i).getBet());
                System.out.print("Remaining Bank: "+ table.get(i).getBank());
            }
            //check if player hand is 21 and dealer hand is greater then 21
            else if((table.get(i).getHand().deckValue(table.get(i).getHand()) == 21) && (dealer.getHand().deckValue(dealer.getHand()) < 21)){
                System.out.println("You've won with a hand of 21");
                table.get(i).addToBank(table.get(i).getBet());
                table.get(i).toString();
            }
        }
    }

    //reset table everyone gets new cards
    public void reset(){
        for (int i = 0; i < table.size(); i++){
            table.get(i).getHand().clearDeck();
        }
    }

    // dealer AI action
    public void dealerAI(){

        System.out.println(dealer.getHand());
        System.out.println(dealer.getHand().deckValue(dealer.getHand()));

        while(dealer.getHand().deckValue(dealer.getHand()) < 16)
            checkHand("1");
    }
}
