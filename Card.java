public class Card {

    // variables to contain the suit and the value of the card
    private Suit suit;
    private Value value;

    //Constructor to create a card
    public Card(Suit suit, Value value){
        this.value = value;
        this.suit = suit;
    }

    //toString method overwrite
    public String toString(){
        return this.value.toString()+ " of " + this.suit.toString();
    }

    // getter for the value of the card
    public Value getValue(){
        return this.value;
    }
}
