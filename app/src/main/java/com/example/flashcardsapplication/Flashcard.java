package com.example.flashcardsapplication;

import java.io.Serializable;

public class Flashcard implements Serializable {
    private String question;
    private String answer;
    private boolean isKnown;

    public Flashcard(String question, String answer, boolean isKnown) {
        this.question = question;
        this.answer = answer;
        this.isKnown = isKnown;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public boolean isKnown() { return isKnown; }
    public void setKnown(boolean known) { isKnown = known; }
}
