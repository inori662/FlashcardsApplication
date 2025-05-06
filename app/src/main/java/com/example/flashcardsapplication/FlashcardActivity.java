package com.example.flashcardsapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private String flashcards_name = null;
    private TextView textView, knownTextView, unknownTextView, progressTextView, feedbackTextView;
    private ProgressBar progressBar;
    private GridLayout answersLayout;
    private Button continueButton;
    private int currentIndex = 0;
    private int initialTotalUnknown = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        textView = findViewById(R.id.questionTextView);
        knownTextView = findViewById(R.id.knownTextView);
        unknownTextView = findViewById(R.id.unknownTextView);
        progressTextView = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);
        answersLayout = findViewById(R.id.answersLayout);
        continueButton = findViewById(R.id.continueButton);
        feedbackTextView = findViewById(R.id.feedbackTextView); // Reference to the feedback view

        Intent intent = getIntent();
        if (intent.hasExtra("FLASHCARDS")) {
            flashcards = (ArrayList<Flashcard>) intent.getSerializableExtra("FLASHCARDS");
            flashcards_name = intent.getStringExtra("FLASHCARDS_NAME");
        }

        updateCard();

        continueButton.setOnClickListener(v -> {
            currentIndex++;
            initialIndex++;
            if (currentIndex >= flashcards.size()) {
                Intent i = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
                i.putExtra("FLASHCARDS", flashcards);
                i.putExtra("FLASHCARDS_NAME", flashcards_name);
                startActivity(i);
                finish();
            } else {
                updateCard();
                continueButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private int initialIndex = 1;

    private void updateCard() {
        while (currentIndex < flashcards.size() && flashcards.get(currentIndex).isKnown()) {
            currentIndex++;
        }

        if (currentIndex >= flashcards.size()) {
            Intent intent = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
            intent.putExtra("FLASHCARDS", flashcards);
            startActivity(intent);
            finish();
            return;
        }

        textView.setText(flashcards.get(currentIndex).getQuestion());
        feedbackTextView.setText(""); // Clear feedback for new question

        int totalUnknown = countUnknown();
        int shownUnknown = getShownUnknownCount();
        if (initialTotalUnknown == -1) {
            initialTotalUnknown = totalUnknown;
        }
        progressTextView.setText(flashcards_name + "\n" + initialIndex + "/" + initialTotalUnknown);

        int progress = (int) ((float) shownUnknown / totalUnknown * 100);
        progressBar.setProgress(progress);

        knownTextView.setText(String.valueOf(countKnown()));
        unknownTextView.setText(String.valueOf(countUnknown()));

        ArrayList<String> answers = flashcards.get(currentIndex).getChoices();

        for (int i = 0; i < answersLayout.getChildCount(); i++) {
            TextView answerView = (TextView) answersLayout.getChildAt(i);
            String answerText = answers.size() > i ? answers.get(i) : "";

            answerView.setText(answerText);
            answerView.setBackgroundResource(R.drawable.rounded_background);
            answerView.setClickable(true); // Re-enable
            answerView.setOnClickListener(v -> handleAnswerSelection(answerView, answerText));
        }
    }

    private void handleAnswerSelection(TextView selectedView, String selectedAnswer) {
        String correctAnswer = flashcards.get(currentIndex).getAnswer();

        for (int i = 0; i < answersLayout.getChildCount(); i++) {
            answersLayout.getChildAt(i).setClickable(false);
        }

        if (selectedAnswer.equals(correctAnswer)) {
            selectedView.setBackgroundResource(R.drawable.border_correct);
            flashcards.get(currentIndex).setKnown(true);
            feedbackTextView.setText("Good job!");
            feedbackTextView.setTextColor(Color.parseColor("#32CD32")); // Green
        } else {
            selectedView.setBackgroundResource(R.drawable.border_wrong);
            flashcards.get(currentIndex).setKnown(false);
            feedbackTextView.setText("You will get it next time!");
            feedbackTextView.setTextColor(Color.parseColor("#FF7F00")); // Orange

            for (int i = 0; i < answersLayout.getChildCount(); i++) {
                TextView answerView = (TextView) answersLayout.getChildAt(i);
                if (answerView.getText().toString().equals(correctAnswer)) {
                    answerView.setBackgroundResource(R.drawable.border_correct);
                    break;
                }
            }
        }

        knownTextView.setText(String.valueOf(countKnown()));
        unknownTextView.setText(String.valueOf(countUnknown()));
        continueButton.setVisibility(View.VISIBLE);
    }

    private int countKnown() {
        int count = 0;
        for (Flashcard card : flashcards) {
            if (card.isKnown()) count++;
        }
        return count;
    }

    private int countUnknown() {
        int count = 0;
        for (Flashcard card : flashcards) {
            if (!card.isKnown()) count++;
        }
        return count;
    }

    private int getShownUnknownCount() {
        int count = 0;
        for (int i = 0; i <= currentIndex && i < flashcards.size(); i++) {
            if (!flashcards.get(i).isKnown()) {
                count++;
            }
        }
        return count;
    }
}
