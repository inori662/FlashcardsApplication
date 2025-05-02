package com.example.flashcardsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private ArrayList<Flashcard> unknownFlashcards = new ArrayList<>();
    private TextView textView, knownTextView, unknownTextView, progressTextView;
    private ProgressBar progressBar;
    private boolean isFlipped = false;
    private int currentIndex = 0, knownSum = 0, unknownSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Initialize views
        textView = findViewById(R.id.textView);
        knownTextView = findViewById(R.id.knownTextView);
        unknownTextView = findViewById(R.id.unknownTextView);
        progressTextView = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);

        // Retrieve flashcards passed from MainActivity
        Intent intent = getIntent();
        if (intent.hasExtra("FLASHCARDS")) {
            flashcards = (ArrayList<Flashcard>) intent.getSerializableExtra("FLASHCARDS");
        } else {
            // If no flashcards were passed, fallback to an empty list or display an error
            flashcards = new ArrayList<>();
        }

        // Check if there are any unknown flashcards passed from ContinueFlashcardActivity
        intent = getIntent();
        if (intent.hasExtra("UNKNOWN_FLASHCARDS")) {
            unknownFlashcards = (ArrayList<Flashcard>) intent.getSerializableExtra("UNKNOWN_FLASHCARDS");
            flashcards = new ArrayList<>(unknownFlashcards);
        }

        updateCard();

        // Gesture detection
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY())) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // Card flip
        textView.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            textView.setText(isFlipped ? flashcards.get(currentIndex).getAnswer() : flashcards.get(currentIndex).getQuestion());
        });

        textView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void onSwipeLeft() {
        unknownSum += 1;
        flashcards.get(currentIndex).setKnown(false);
        unknownFlashcards.add(flashcards.get(currentIndex));
        unknownTextView.setText(String.valueOf(unknownSum));
        animateSwipe(-textView.getWidth());
    }

    private void onSwipeRight() {
        knownSum += 1;
        flashcards.get(currentIndex).setKnown(true);
        knownTextView.setText(String.valueOf(knownSum));
        animateSwipe(textView.getWidth());
    }

    private void animateSwipe(float toX) {
        textView.animate()
                .translationX(toX)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    currentIndex++;

                    if (currentIndex >= flashcards.size()) {
                        // Finished reviewing
                        ArrayList<Flashcard> remainingUnknowns = new ArrayList<>();
                        for (Flashcard card : flashcards) {
                            if (!card.isKnown()) {
                                remainingUnknowns.add(card);
                            }
                        }

                        Intent i = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
                        i.putExtra("UNKNOWN_FLASHCARDS", remainingUnknowns);
                        i.putExtra("TOTAL_FLASHCARDS", flashcards.size());
                        startActivity(i);
                        finish();
                        return; // Stop here to prevent updateCard() crash
                    }

                    updateCard();
                    textView.setTranslationX(-toX);
                    textView.animate()
                            .translationX(0)
                            .alpha(1f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    private void updateCard() {
        isFlipped = false;
        textView.setText(flashcards.get(currentIndex).getQuestion());

        String progressText = (currentIndex + 1) + "/" + flashcards.size();
        progressTextView.setText(progressText);

        int progress = (int) ((float) (currentIndex + 1) / flashcards.size() * 100);
        progressBar.setProgress(progress);
    }
}
