package com.example.flashcardsapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Flashcard implements Serializable {
    private String question;
    private ArrayList<String> choices;  // Store the answer options
    private String answer;
    private boolean isKnown;

    public Flashcard(String question, ArrayList<String> choices, String answer, boolean isKnown) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.isKnown = isKnown;
    }

    public String getQuestion() { return question; }
    public ArrayList<String> getChoices() { return choices; }  // Return the list of choices
    public String getAnswer() { return answer; }
    public boolean isKnown() { return isKnown; }
    public void setKnown(boolean known) { isKnown = known; }

    // Setters for choices and other fields if needed
    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
