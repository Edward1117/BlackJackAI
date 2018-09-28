import java.util.ArrayList;
import java.util.Random;
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
    private static int[] validBets = {10,20,50,100};
    //create double to contain wong counter
    private Double wongHalvesCounter;
    //create counter for current round
    private int currRound;

    //constructor for BlackJack
    public Game(){
        wongHalvesCounter = 0.0;
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
        //set current round to 1
        this.currRound = 1;
    }
    //method to start the ai game
    public void aiGame(Double leaveAmount){
        double bet;
        table.add(dealer);
        //draw 2 cards for everyone in the game
        dealCards();
        dealCards();
        //while look to continue to game until next person turn or bust
        while(flag){
            // for loop to go through each person in the table
            for( int i = 0; i < table.size(); i ++) {
                //if statement to check if dealers turn
                if(table.get(i).equals(dealer)){
                    //calls dealer ai function to execute actions based on hand
                    dealerAI();
                    break;
                }
                else{
                    //statement to end game if you've achieved your amount you wanted to leave with
                    if (table.get(i).getBank() >= leaveAmount ){
                        System.out.print("----------------------Total turns Taken: "+ currRound+"----------------------");
                        System.out.println("\n"+"You've Achieved Your Goal Of: $"+ leaveAmount);
                        System.out.println("You've left with  $"+ table.get(i).getBank());
                        printWinsLoses(table.get(i));
                        System.exit(0);
                    }
                    //if statement to determine bet amount based on the wong counter
                    if(wongHalvesCounter > 0){
                        Random rand = new Random();
                        Integer temp = rand.nextInt(3);
                        bet = validBets[temp];
                        System.out.print(table.get(i).toString());
                        System.out.print("remaining balance: "+table.get(i).getBank());
                        while(bet > table.get(i).getBank()){
                            temp --;

                            if(temp >= 0){
                                System.out.print(temp);
                                bet = validBets[temp];
                            }
                            else if (temp < 0 && (table.get(i) != dealer)) {
                                System.out.print("Error on bank");
                                System.exit(0);
                            }
                            else
                                bet = table.get(i).getBank();
                        }
                    }
                    else if (wongHalvesCounter == 0){
                        bet = 10.0;
                    }
                    else{
                        bet = 5.0;
                    }
                    table.get(i).setBet(bet);
                    System.out.println("\n"+"current AI Bet: "+ bet);


                    //check if ai has a hand value of 21 and only has 2 cards in the hand
                    if (table.get(i).getHand().deckValue(table.get(i).getHand()) == 21 && table.get(i).getHand().deckSize() == 2){
                        input = "2";
                        table.get(i).addToBank(bet*1.5);
                        System.out.println("Congrats you've got blackjack");
                        System.out.print("\n"+table.get(i).getHand().toString());
                    }
                    else{
                        //calls player ai to determine new action
                        playerAI(table.get(i),bet);
                    }
                    //Check whether amount inputted is greater than given funds
                    //Checks Whether amount is less than or equal to 0
                    //loops until a correct amount is given
                    while((bet > table.get(i).getBank()) || (bet <= 0) ){
                        if (table.get(i).getBank()<5){
                            printWinsLoses(table.get(i));
                            System.out.println("Your Broke GAME OVER!!! ");
                            System.exit(0);
                        }
                        else{
                            //reset bet amount to 10 if current bank does not contain the bigger amount
                            bet = 5.0;
                            //sets the ai's bet to the lower bet amount
                            table.get(i).setBet(bet);
                        }
                    }
                    table.get(i).setBet(bet);
                    //loop to begin game continues current player until 2 is found for stand
                    while(Integer.parseInt(input) != 2){
                        //prints our your current bank remaining at end of each decision
                        System.out.println("\n"+"Your current remaining Balance is : $"+ table.get(i).getBank());
                        System.out.print("Current Bet: "+table.get(i).getBet());
                        //make new decision if possible
                        playerAI(table.get(i),bet);
                    }
                    if (table.get(i).getBank()<5)
                        System.exit(0);
                }
            }
            //rounds over input new bank amounts to all players
            compareHandToDealer();
            //resets every players hands to empty
            reset();
            //check if remaining deck size is <= 5 if so create new deck and populate and shuffle current deck
            if(currDeck.deckSize()<=10){
                currDeck.createDeck();
                currDeck.shuffle();
                //System.out.print("\n"+"Remaining Cards:                 "+currDeck.deckSize());
            }
            //prints out current wong value of remaining deck
            System.out.println("\n"+"Current wong Value: " + wongHalvesCounter);
            //increment round
            currRound++;
            //deal 2 new cards to every player
            dealCards();
            dealCards();
        }
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
                    dealerAI();
                    break;
                }
                else{
                    //Prints out statement to acquire a bet amount
                    System.out.println("\n"+"Current Player : "+ table.get(i).toString());
                    System.out.print("\n"+"Valid bets : $5 , $10, $20, $50, $100" + "\n");
                    System.out.print(" Please enter a bet amount: ");
                    //get user input
                    input = userInput.nextLine();
                    //if statement to check whether or not statement is a valid number
                    if((isValidNumeric(input))){
                        //changed input String to a double
                        Double bet = Double.parseDouble(input);
                        //Check whether amount inputted is greater than given funds
                        //Checks Whether amount is less than or equal to 0
                        //loops until a correct amount is given
                        while((bet > table.get(i).getBank()) || (bet <= 0) ){
                            if (table.get(i).getBank()==0){
                                System.out.println("Your Broke GAME OVER!!! ");
                                System.exit(0);
                            }
                            else{
                                //player has insufficient funds remaining prompt for correct amount
                                System.out.println("Insufficient Funds");
                                System.out.println("Please input a valid amount");
                                input = userInput.nextLine();
                                bet = Double.parseDouble(input);
                                table.get(i).setBet(bet);
                            }
                        }
                        //set players bet
                        table.get(i).setBet(bet);
                        //loop to begin game continues current player until 2 is found for stand
                        while(Integer.parseInt(input) != 2){
                            //check if current hand contains black jack
                            //increments players bank and then moves on to next player
                            if((table.get(i).getHand().deckValue(table.get(i).getHand()) == 21) && (table.get(i).getHand().deckSize() == 2)) {
                                table.get(i).setBet(table.get(i).getBet()*1.5);
                                input = "2";
                                System.out.println("Congrats, You've Got BlackJack!!!");
                                System.out.println(table.get(i).getHand().toString());
                                table.get(i).addToBank(bet);
                                break;
                            }
                            System.out.println("\n"+"Your current bet is: $"+ bet);
                            //shows dealers first card
                            System.out.println("\n"+"Dealer's First Card is: " + "\n -" + dealer.getHand().getCard(0));
                            //shows players cards and hand value
                            System.out.println("\n"+"\n"+"Your current Cards are:" + table.get(i).getHand().toString());
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
                    //quits the game if input ever equals quit
                    else if (input.equals("quit"))
                        System.exit(0);

                }
            }
            //round ended give everyone the money they've won or take away money
            compareHandToDealer();
            //resets all hands
            reset();
            //populate deck with new cards if threshold is found
            if(currDeck.deckSize()<=10){
                currDeck.createDeck();
                currDeck.shuffle();
            }
            //print out the meaning of the wong value
            System.out.println("\n"+"Wong value represents the odds you have against the house");
            System.out.println("If the number is positive you have a higher chance of winning");
            System.out.println("Current wong Value: " + wongHalvesCounter);

            //increment round
            currRound++;
            //deal 2 cards to everyone in the table
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
                    System.out.println("\n"+ " Current Action: HIT");
                    checkHand(i,bet,"1");
                    break;
                case 2 :
                    //current action is STAND
                    System.out.println("You've Stayed with: "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                    input= "2";
                    break;
                case 3:
                    //current action is DOUBLE DOWN
                    bet = bet + bet;
                    if(table.get(i).getBank() >= bet){
                        System.out.println("\n"+ " Current Action: DOUBLE DOWN");
                        table.get(i).setBet(bet);
                        checkHand(i,bet,"2");
                        System.out.println("\n"+"Next players turn" );
                    }else{
                        System.out.println("Insufficient funds");
                        System.exit(0);
                        System.out.println("Please Choose another Action");
                        System.out.println("\n"+"(1)Hit | (2)Stand |(3)Double Down | (4)Split | quit");
                        input = userInput.nextLine();
                        checkAction(table.get(i).getBet(),i,input);
                    }
                    break;
                case 4:
                    if(table.get(i).getHand().getCard(0).getValue() == table.get(i).getHand().getCard(1).getValue()){
                        //removes the second card of the hand
                        table.get(i).getHand().removeCard(1);

                        System.out.println(table.get(i).getHand().deckValue(table.get(i).getHand()));
                        System.out.println(table.get(i).getHand().toString());

                        //prints our statement to check for player action
                        printActionMenu();

                        input = userInput.nextLine();
                        checkAction(bet,i,input);
                        checkHand(i,bet,input);
                        table.get(i).setSplitDeck(table.get(i).getHand());


                        Card tempCard = new Card(table.get(i).getHand().getCard(0).getSuit(),table.get(i).getHand().getCard(0).getValue());
                        table.get(i).getHand().clearDeck();
                        table.get(i).getHand().addCard(tempCard);

                        //prints our statement to check for player action
                        printActionMenu();

                        System.out.println(table.get(i).getHand().deckValue(table.get(i).getHand()));
                        System.out.println(table.get(i).getHand().toString());

                        input = userInput.nextLine();
                        checkAction(bet,i,input);
                        checkHand(i,bet,input);

                    }
                    else{
                        System.out.print("Error Card Values are invalid and can't be split");
                        System.out.print("\n"+"Please input another Action ");
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

    //error check player hand values using position for player
    public void checkHand(int i, Double bet,String currAction){
        table.get(i).getHand().draw(currDeck);
        //auto win at 21 add current bet to player bank
        if(table.get(i).getHand().deckValue(table.get(i).getHand()) == 21){
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println("Congrats, You've got 21");
            input = "2";
        }
        //bust if hand value is > 21
        else if (table.get(i).getHand().deckValue(table.get(i).getHand()) > 21){
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println(" You've busted with " + table.get(i).getHand().deckValue(table.get(i).getHand()));
            input = "2";
            //System.out.println("Current funds remaining: " + table.get(i).getBank());
        }
        else{
            //set current action to what was inputted and output cards and values
            input = currAction;
            System.out.println("Your current Cards are:" + table.get(i).getHand().toString());
            System.out.println("\n"+"You ended with: " + table.get(i).getHand().deckValue(table.get(i).getHand()));
        }
    }

    //method to check hands for dealer
    public void checkHand(String currAction){
        dealer.getHand().draw(currDeck);
        //auto win at 21 add current bet to player bank
        if(dealer.getHand().deckValue(dealer.getHand()) == 21){
            System.out.println("Your current Cards are:" + dealer.getHand().toString());
            System.out.println("Congrats, You've got 21");
            input = "2";
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
            System.out.println("\n"+"You ended with: " + dealer.getHand().deckValue(dealer.getHand()));
        }
    }

    //error check player hands and commit action using player
    public void checkHand(Player p, Double bet,String currAction){
        if(currAction.equals("1") || currAction.equals("3"))
            p.getHand().draw(currDeck);
        //auto win at 21 add current bet to player bank
        if(p.getHand().deckValue(p.getHand()) == 21){
            System.out.println("Your current Cards are:" + p.getHand().toString());
            System.out.println("Congrats, You've got 21");
            input = "2";
        }
        //bus if hand value is > 21
        else if (p.getHand().deckValue(p.getHand()) > 21){
            System.out.println("Your current Cards are:" + p.getHand().toString());
            System.out.println(" You've busted with " + p.getHand().deckValue(p.getHand()));
            input = "2";
            //System.out.println("Current funds remaining: " + p.getBank());
        }
        else{
            //set current action to what was inputted and output cards and values
            input = currAction;
            System.out.println("Your current Cards are:" + p.getHand().toString());
            System.out.println("\n"+"You ended with: " + p.getHand().deckValue(p.getHand()));
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
        for (int i = 0; i < table.size(); i++) {
            //if statement to check if dealers hand is greater then player and is less than or equal to 21
            if ((dealer.getHand().deckValue(dealer.getHand()) > table.get(i).getHand().deckValue(table.get(i).getHand()) && (dealer.getHand().deckValue(dealer.getHand()) <= 21))){
                System.out.println(" You've lost this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).subToBank(table.get(i).getBet());
                System.out.print("\n"+"Remaining Bank "+ table.get(i).getBank()+"\n");
                table.get(i).incrementLoses();
                break;
            }
            // else if to check if player hand is greater then dealer and is less than to 21
            else if((dealer.getHand().deckValue(dealer.getHand()) < table.get(i).getHand().deckValue(table.get(i).getHand())) && (table.get(i).getHand().deckValue(table.get(i).getHand()) <= 21) ){
                System.out.println(" You've Won this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).addToBank(table.get(i).getBet());
                System.out.print("\n"+"Remaining Bank: "+ table.get(i).getBank()+"\n");
                table.get(i).incrementWins();
                break;
            }
            //checks if ur hand value is equal to the dealer
            else if(dealer.getHand().deckValue(dealer.getHand()) == table.get(i).getHand().deckValue(table.get(i).getHand())){
                System.out.println("Push, Your equal with the dealer with: "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                break;
            }

            //dealer hand value is greater then 21, player auto wins if under 21
            else if((dealer.getHand().deckValue(dealer.getHand()) > 21) && (table.get(i).getHand().deckValue(table.get(i).getHand()) <= 21)){
                System.out.println(" You've Won this round! "+ table.get(i).getName()+ " with "+ table.get(i).getHand().deckValue(table.get(i).getHand()));
                table.get(i).addToBank(table.get(i).getBet());
                System.out.print("\n"+"Remaining Bank: "+ table.get(i).getBank()+"\n");
                table.get(i).incrementWins();
                break;
            }
            //check if player hand is 21 and dealer hand is greater then 21
            else if((table.get(i).getHand().deckValue(table.get(i).getHand()) == 21) && (dealer.getHand().deckValue(dealer.getHand()) < 21)){
                System.out.println("You've won with a hand of 21");
                table.get(i).addToBank(table.get(i).getBet());
                table.get(i).toString();
                table.get(i).incrementWins();
                break;
            }
        }
        countWongValue();
    }

    //reset table everyone gets new cards
    public void reset(){
        for (int i = 0; i < table.size(); i++){
            table.get(i).getHand().clearDeck();
        }
    }

    // dealer AI action
    public void dealerAI(){
        System.out.print("-------------Dealers Turn-------------");
        System.out.println(dealer.getHand());
        System.out.println(dealer.getHand().deckValue(dealer.getHand()));

        while(dealer.getHand().deckValue(dealer.getHand()) < 17)
            checkHand("1");
    }

    //player ai
    public void playerAI(Player newAI, Double bet){
        if(newAI.getBank()< newAI.getBet()){
            System.out.print(" Bet Amount: "+ newAI.getBet());
            System.out.println("\n"+"Not enough funds to continue the game SORRY. Funds Remaining : " + newAI.getBank());
            printWinsLoses(newAI);
            System.exit(0);
        }

        System.out.println("\n"+"---------------------Player Turn---------------------");
        System.out.println("---------------------"+"Current Round "+currRound+"----------------------"+"\n");
        //print statement to get dealers first card on every turn
        System.out.println("Dealers First Card is: " + dealer.getHand().getCard(0));
        //checks if dealers first card is 2 and your hand value is less than 9
        if((dealer.getHand().getCard(0).getValue() == Value.TWO) && (newAI.getHand().deckValue(newAI.getHand())<=9)){
            //hit if hand is <= 9
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            input = "1";
            checkHand(newAI,bet,"1");
        }
        //checks if dealers first card is 2 and your hand value is 13 or greater
        else if((dealer.getHand().getCard(0).getValue() == Value.TWO) && (newAI.getHand().deckValue(newAI.getHand())>12)){
            //stand because the chances of the dealer getting 2 cards totaling 20 is roughly 10 percent
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            input = "2";
            checkHand(newAI,bet,"2");
        }
        //checks dealers hand if first card is 2 and your hand is equal to 10
        else if((dealer.getHand().getCard(0).getValue() == Value.TWO) && (newAI.getHand().deckValue(newAI.getHand())==10)){
            //double down if hand = 10
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
        //checks dealers hand if first card is 2 and your hand is equal to 11
        else if((dealer.getHand().getCard(0).getValue() == Value.TWO) && (newAI.getHand().deckValue(newAI.getHand())==11)){
            //double down if hand is equal to 11
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
        //checks dealer hand if first card is a 3 and your hand is less than 8
        else if((dealer.getHand().getCard(0).getValue() == Value.THREE) && (newAI.getHand().deckValue(newAI.getHand())<=8)){
            //hit if hand less than 8
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }
        //checks dealers hand if first card is 3 and if your hand is greater then or equal to 12
        else if((dealer.getHand().getCard(0).getValue() == Value.THREE) && (newAI.getHand().deckValue(newAI.getHand())>=12)){
            //stand if so
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
            //checks dealers hand if first card is 3 and if your hand is equal to 9
        else if((dealer.getHand().getCard(0).getValue() == Value.THREE) && (newAI.getHand().deckValue(newAI.getHand())==9)){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
            //checks dealers hand if first card is 3 and if your hand is equal to 10
        else if((dealer.getHand().getCard(0).getValue() == Value.THREE) && (newAI.getHand().deckValue(newAI.getHand())==10)){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
            //checks dealers hand if first card is 3 and if your hand is equal to 11
        else if((dealer.getHand().getCard(0).getValue() == Value.THREE) && (newAI.getHand().deckValue(newAI.getHand())==11)){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
            //checks dealers hand if first card is 4 and if your hand is less than or equal to 8
        else if((dealer.getHand().getCard(0).getValue() == Value.FOUR) && (newAI.getHand().deckValue(newAI.getHand())<=8)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }
            //checks dealers hand if first card is 4 and if your hand is greater than 11
        else if((dealer.getHand().getCard(0).getValue() == Value.FOUR) && (newAI.getHand().deckValue(newAI.getHand())>=12)){
            //stand
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
            //checks dealers hand if first card is 4 and if your hand is equal to 9,10,11
        else if((dealer.getHand().getCard(0).getValue() == Value.FOUR) && ((newAI.getHand().deckValue(newAI.getHand())==9) || (newAI.getHand().deckValue(newAI.getHand())==10) || (newAI.getHand().deckValue(newAI.getHand())>=11))){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }
            //checks dealers hand if first card is 5 and if your hand is less than or equal to 8

        else if(dealer.getHand().getCard(0).getValue() == Value.FIVE && (newAI.getHand().deckValue(newAI.getHand())<=8)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }
            //checks dealers hand if first card is 5 and if your hand is greater than 11
        else if(dealer.getHand().getCard(0).getValue() == Value.FIVE && (newAI.getHand().deckValue(newAI.getHand())>=12)){
            //stand
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
            //checks dealers hand if first card is 5 and if your hand is equal to 9,10,11
        else if((dealer.getHand().getCard(0).getValue() == Value.FIVE) && ((newAI.getHand().deckValue(newAI.getHand())==9) || (newAI.getHand().deckValue(newAI.getHand())==10) || (newAI.getHand().deckValue(newAI.getHand())>=11))) {
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI, bet, "3");
            input = "2";
        }

            //checks dealers hand if first card is 6 and if your hand is less than or equal to 8
        else if(dealer.getHand().getCard(0).getValue() == Value.SIX && (newAI.getHand().deckValue(newAI.getHand())<=8)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }
            //checks dealers hand if first card is 6 and if your hand is greater than 11
        else if(dealer.getHand().getCard(0).getValue() == Value.SIX && (newAI.getHand().deckValue(newAI.getHand())>=12)){
            //stand
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
            //checks dealers hand if first card is 6 and if your hand is equal to 9,10,11
        else if((dealer.getHand().getCard(0).getValue() == Value.SIX) && ((newAI.getHand().deckValue(newAI.getHand())==9) || (newAI.getHand().deckValue(newAI.getHand())==10) || (newAI.getHand().deckValue(newAI.getHand())>=11))){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }

        //check if dealers first card is equal to 7,8,9 nad your hand is equal to or less than 9
        else if(((dealer.getHand().getCard(0).getValue() == Value.SEVEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.EIGHT) ||
                (dealer.getHand().getCard(0).getValue() == Value.NINE)) && (newAI.getHand().deckValue(newAI.getHand())<= 9)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }

            //check if dealers first card is equal to 7,8,9 and your hand is >= 12 but <= 16
        else if(((dealer.getHand().getCard(0).getValue() == Value.SEVEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.EIGHT) ||
                (dealer.getHand().getCard(0).getValue() == Value.NINE)) && ((newAI.getHand().deckValue(newAI.getHand())>=12)&&(newAI.getHand().deckValue(newAI.getHand())<=16))){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }

            //check if dealers first card is equal to 7,8,9 and your hand is >= 17
        else if(((dealer.getHand().getCard(0).getValue() == Value.SEVEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.EIGHT) ||
                (dealer.getHand().getCard(0).getValue() == Value.NINE)) && (newAI.getHand().deckValue(newAI.getHand())>=17)){
            //stand
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }

            //check if dealers first card is equal to 10 , J , Q, K  and your hand is <=10
        else if(((dealer.getHand().getCard(0).getValue() == Value.TEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.JACK) ||
                (dealer.getHand().getCard(0).getValue() == Value.QUEEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.KING)) && (newAI.getHand().deckValue(newAI.getHand())<=10)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }

            //check if dealers first card is equal to 10 , J , Q, K and your hand is >= 12 but <= 16
        else if(((dealer.getHand().getCard(0).getValue() == Value.TEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.JACK) ||
                (dealer.getHand().getCard(0).getValue() == Value.QUEEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.KING)) && ((newAI.getHand().deckValue(newAI.getHand())>=12)&&(newAI.getHand().deckValue(newAI.getHand())<=16))){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }

            //check if dealers first card is equal to 10 , J , Q, K and your hand is 11
        else if(((dealer.getHand().getCard(0).getValue() == Value.TEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.JACK) ||
                (dealer.getHand().getCard(0).getValue() == Value.QUEEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.KING)) && (newAI.getHand().deckValue(newAI.getHand())==11)){
            //double down
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Double Down if money available ");
            checkHand(newAI,bet,"3");
            input = "2";
        }

            //check if dealers first card is equal to 10 , J , Q, K and your hand is >= 17
        else if(((dealer.getHand().getCard(0).getValue() == Value.TEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.JACK) ||
                (dealer.getHand().getCard(0).getValue() == Value.QUEEN) ||
                (dealer.getHand().getCard(0).getValue() == Value.KING)) && newAI.getHand().deckValue(newAI.getHand())>16){
            //stand
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
        //checks if dealer hand is an ACE and player hand is less than 17
        else if((dealer.getHand().getCard(0).getValue() == Value.ACE) && (newAI.getHand().deckValue(newAI.getHand())<17)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Hit");
            checkHand(newAI,bet,"1");
            input = "1";
        }

        //checks if dealer hand is an ACE and player hand is greater than
        else if((dealer.getHand().getCard(0).getValue() == Value.ACE) && (newAI.getHand().deckValue(newAI.getHand())>=17)){
            //hit
            System.out.println(" Current hand Value: "+newAI.getHand().deckValue(newAI.getHand()));
            System.out.println("AI Action : Stand");
            checkHand(newAI,bet,"2");
            input = "2";
        }
        else{
            input = "2";
            checkHand(newAI,bet,"2");
        }
    }

    //counts the wong value for every card on the table
    public void countWongValue(){
        for(int i = 0; i < table.size(); i++){
            for(int j = 0; j < table.get(i).getHand().deckSize(); j ++){
                wongHalvesCounter = wongHalvesCounter + table.get(i).getHand().getCard(j).getWongValue();
            }
        }
    }

    //method to print out action menu
    public void printActionMenu(){
        //prints our statement to check for player action
        System.out.println(" -------------What would you like to do?-------------");
        System.out.println(" Please input the number for an action");
        System.out.println("\n"+"(1)Hit | (2)Stand |(3)Double Down | (4)Split | quit");
    }

    //method to print out player wins and loses
    public void printWinsLoses(Player p){
        System.out.println("\n"+"Total Games Won: "+p.getWins());
        System.out.println("Total Games Lost: "+p.getLoses());
        System.out.print("Current Balance: " + p.getBank()+"\n");
    }
}
