package com.example.flashcardsapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private TextView textView;
    private boolean isFlipped = false;
    private int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_flashcard);

            textView = findViewById(R.id.textView);

            flashcards = new ArrayList<>();
            flashcards.add(new Flashcard("What is the capital of France?", "Paris", false));
            flashcards.add(new Flashcard("What is 2 + 2?", "4", false));
            flashcards.add(new Flashcard("What is the largest ocean?", "Pacific Ocean", false));
            flashcards.add(new Flashcard("Who wrote 'Romeo and Juliet'?", "William Shakespeare", false));
            flashcards.add(new Flashcard("What planet is known as the Red Planet?", "Mars", false));

            textView.setText(flashcards.get(currentIndex).getQuestion());

            textView.setOnClickListener(v -> {
                isFlipped = !isFlipped;
                textView.setText(isFlipped
                        ? flashcards.get(currentIndex).getAnswer()
                        : flashcards.get(currentIndex).getQuestion());
            });

        } catch (Exception e) {
            Toast.makeText(this, "Crash: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
