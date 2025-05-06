package com.example.flashcardsapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class FlashcardSet implements Serializable {
    private String name;
    private ArrayList<Flashcard> cards;

    // Constructor to initialize the FlashcardSet
    public FlashcardSet(String name, ArrayList<Flashcard> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Flashcard> getCards() {
        return cards;
    }

    // Setter for cards to update the list of cards
    public void setCards(ArrayList<Flashcard> cards) {
        this.cards = cards;
    }
}
