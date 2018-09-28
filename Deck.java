import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    //variable to contain arraylist of cards
    private ArrayList<Card> cards;

    //constructor
    public Deck(){
        this.cards = new ArrayList<>();
    }

    //method to populate the deck with cards
    public void createDeck(){
        //generate all cards
        for(Suit cardSuit : Suit.values()){
            for(Value cardValue : Value.values()){
                //adds card to deck
                Card card = new Card(cardSuit, cardValue);
                card.wongValue();
                this.cards.add(card);
            }
        }
    }

    //overwrite toString method
    public String toString(){
        String cardListOutput = "";
        for(Card aCard : this.cards){
            cardListOutput += "\n" + "-" + aCard.toString();
        }
        return cardListOutput;
    }

    //Shuffle Deck
    public void shuffle(){
        Collections.shuffle(cards);
    }

    //removes card at index i
    public void removeCard(int i){
        this.cards.remove(i);
    }

    //returns the card at the index i
    public Card getCard(int i){
        return this.cards.get(i);
    }

    //adds a card to the list
    public void addCard(Card add){
        this.cards.add(add);
    }

    //draws card from deck
    public void draw(Deck comingFrom){
        this.cards.add(comingFrom.getCard(0));
        comingFrom.removeCard(0);
    }

    //method to get total size of deck
    public int deckSize(){
        return this.cards.size();
    }

    //prints out total value of deck
    //for players it prints out hand totals
    public int deckValue(Deck deck){

        int totalValue = 0;
        int aces = 0;

        //switch statement to determine hand values by checking each card
        for(int i = 0; i < deck.deckSize(); i++){
            switch (deck.getCard(i).getValue()){
                case TWO : totalValue += 2;
                    break;
                case THREE : totalValue += 3;
                    break;
                case FOUR : totalValue += 4;
                    break;
                case FIVE : totalValue += 5;
                    break;
                case SIX : totalValue += 6;
                    break;
                case SEVEN : totalValue += 7;
                    break;
                case EIGHT : totalValue += 8;
                    break;
                case NINE : totalValue += 9;
                    break;
                case TEN : totalValue += 10;
                    break;
                case JACK : totalValue += 10;
                    break;
                case QUEEN : totalValue += 10;
                    break;
                case KING : totalValue += 10;
                    break;
                case ACE : aces += 1;
                    break;
            }
        }
        //for loop to determine the amount an Ace is worth
        for (int i = 0; i< aces; i++ ){
            if (totalValue > 10)
                totalValue += 1;
            else
                totalValue += 11;
        }
        return totalValue;
    }

    //removes all cards from the deck
    public void clearDeck(){
        cards.clear();
    }

    //method to swap the second card to the split hand
    public void splitHand(Deck comingFrom, Deck GoingTo){
        GoingTo.addCard(comingFrom.getCard(1));
        comingFrom.removeCard(1);
    }
}
