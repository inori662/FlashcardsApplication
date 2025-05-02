package com.example.flashcardsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContinueFlashcardActivity extends AppCompatActivity {
    Button continueBtn, resetBtn;
    private ArrayList<Flashcard> unknownFlashcards;  // Store the flashcards that are unknown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continueflashcard);

        // Initialize buttons
        continueBtn = findViewById(R.id.ContinueBtn);
        resetBtn = findViewById(R.id.resetBtn);

        // Get the flashcards marked as unknown from the Intent
        unknownFlashcards = (ArrayList<Flashcard>) getIntent().getSerializableExtra("UNKNOWN_FLASHCARDS");

        // Continue button logic: Return to FlashcardActivity and continue from the unknown flashcards
        continueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ContinueFlashcardActivity.this, FlashcardActivity.class);
            intent.putExtra("UNKNOWN_FLASHCARDS", unknownFlashcards);  // Pass back the unknown flashcards
            startActivity(intent);
            finish();
        });

        // Reset button logic: Restart FlashcardActivity with all flashcards and reset progress
        resetBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ContinueFlashcardActivity.this, FlashcardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
