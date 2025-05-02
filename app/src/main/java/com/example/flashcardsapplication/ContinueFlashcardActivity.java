package com.example.flashcardsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContinueFlashcardActivity extends AppCompatActivity {
    Button continueBtn, resetBtn;
    TextView textView;
    private ArrayList<Flashcard> unknownFlashcards;  // Store the flashcards that are unknown
    private int totalFlashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continueflashcard);

        // Initialize buttons
        continueBtn = findViewById(R.id.ContinueBtn);
        resetBtn = findViewById(R.id.resetBtn);
        textView = findViewById(R.id.textViewContent);

        // Get the flashcards marked as unknown from the Intent
        unknownFlashcards = (ArrayList<Flashcard>) getIntent().getSerializableExtra("UNKNOWN_FLASHCARDS");
        totalFlashcards = getIntent().getIntExtra("TOTAL_FLASHCARDS", 0);

        if (unknownFlashcards == null || unknownFlashcards.isEmpty()) {
            continueBtn.setVisibility(View.INVISIBLE);
            textView.setText("Congratulations!");
        }
        else {
            String message = String.valueOf(textView.getText())
                    .replace("{x}", String.valueOf(totalFlashcards - unknownFlashcards.size()))
                    .replace("{y}", String.valueOf(unknownFlashcards.size()));
            textView.setText(message);
        }



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
