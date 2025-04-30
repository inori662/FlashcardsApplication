package com.example.flashcardsapplication;

public class Flashcard {
    private String question;
    private String answer;
    private boolean known;

    public Flashcard(String question, String answer, boolean known) {
        this.question = question;
        this.answer = answer;
        this.known = known;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }
}
