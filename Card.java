public class Card {

    // variables to contain the suit and the value of the card
    private Suit suit;
    private Value value;
    private Double wongValue;

    //Constructor to create a card
    public Card(Suit suit, Value value){
        this.value = value;
        this.suit = suit;
    }

    //method to get the suit of the card
    public Suit getSuit(){
        return this.suit;
    }

    //toString method overwrite
    public String toString(){
        return this.value.toString()+ " of " + this.suit.toString();
    }

    // getter for the value of the card
    public Value getValue(){
        return this.value;
    }

    public void wongValue(){
        if(value == Value.ACE)
            wongValue = -1.0;
        else if(value == Value.TEN)
            wongValue = -1.0;
        else if(value == Value.JACK)
            wongValue = -1.0;
        else if(value == Value.QUEEN)
            wongValue = -1.0;
        else if(value == Value.KING)
            wongValue = -1.0;
        else if(value == Value.TWO)
            wongValue = 0.5;
        else if(value == Value.SEVEN)
            wongValue = 0.5;
        else if(value == Value.THREE)
            wongValue = 1.0;
        else if(value == Value.FOUR)
            wongValue = 1.0;
        else if(value == Value.SIX)
            wongValue = 1.0;
        else if(value == Value.FIVE)
            wongValue = 1.5;
        else if(value == Value.EIGHT)
            wongValue = 0.0;
        else if(value == Value.NINE)
            wongValue = -0.5;
    }

    public Double getWongValue(){
        return this.wongValue;
    }
}

