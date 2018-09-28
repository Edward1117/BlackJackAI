public class Player {
    //varaiable creation
    private double bank;
    private String player;
    private Deck hand;
    private Double bet;
    private Deck split;

    private int wins;
    private int loses;


    //constructor for player
    public Player(double bank,String player){
        this.bank = bank;
        this.hand = new Deck();
        this.player = player;
        this.bet = 0.0;
        this.split = new Deck();
        this.wins = 0;
        this.loses = 0;
    }

    //constructor for dealer
    public Player(){
        this.bank = 0;
        this.hand = new Deck();
        this.player = "dealer";
    }

    //overwrite toString method
    public String toString(){
        return player + " has $" + bank;
    }

    //method to get bank
    public Double getBank(){
        return this.bank;
    }

    //adds an amount to the bank
    public void addToBank(Double val){
        this.bank = this.bank + val;
    }

    //subtracts an amount to the bank
    public void subToBank(Double val){
        this.bank = this.bank - val;
    }

    //method to get player deck(hand)
    public Deck getHand(){
        return this.hand;
    }

    //method to get player name
    public String getName(){
        return this.player;
    }

    //method to set the players current bet amount
    public void setBet(Double bet){
        this.bet = bet;
    }

    //method to get current bet
    public Double getBet(){
        return this.bet;
    }

    //gets the split hand
    public Deck getSplit(){
        return this.split;
    }

    //sets the split hand
    public void setSplitDeck(Deck d){
        this.split = d;
    }

    //method to get total wins
    public int getWins(){
        return this.wins;
    }
    //method to get total loses
    public int getLoses(){
        return this.loses;
    }
    //method to increment wins
    public void incrementWins(){
        this.wins++;
    }
    //method to increment loses
    public void incrementLoses(){
        this.loses++;
    }


}
