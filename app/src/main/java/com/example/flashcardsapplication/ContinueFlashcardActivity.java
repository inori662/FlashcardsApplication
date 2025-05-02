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
    private ArrayList<Flashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continueflashcard);

        continueBtn = findViewById(R.id.ContinueBtn);
        resetBtn = findViewById(R.id.resetBtn);
        textView = findViewById(R.id.textViewContent);

        flashcards = (ArrayList<Flashcard>) getIntent().getSerializableExtra("FLASHCARDS");

        int known = 0;
        int unknown = 0;
        for (Flashcard card : flashcards) {
            if (card.isKnown()) known++;
            else unknown++;
        }

        // Show a success message if all flashcards are marked as known,
        // otherwise display progress to motivate continued learning
        if (unknown == 0) {
            continueBtn.setVisibility(View.INVISIBLE);
            textView.setText("Congratulations! You've mastered all the flashcards.");
        } else {
            textView.setText("Great job! You've learned " + known + " cards. Let's review the remaining " + unknown + ".");
        }


        continueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ContinueFlashcardActivity.this, FlashcardActivity.class);
            intent.putExtra("FLASHCARDS", flashcards);
            startActivity(intent);
            finish();
        });

        resetBtn.setOnClickListener(v -> {
            for (Flashcard card : flashcards) {
                card.setKnown(false);
            }
            Intent intent = new Intent(ContinueFlashcardActivity.this, FlashcardActivity.class);
            intent.putExtra("FLASHCARDS", flashcards);
            startActivity(intent);
            finish();
        });
    }
}
