package com.example.flashcardsapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class FlashcardSet implements Serializable {
    private String name;
    private ArrayList<Flashcard> cards;

    public String getName() {
        return name;
    }

    public ArrayList<Flashcard> getCards() {
        return cards;
    }
}
