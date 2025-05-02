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
    private TextView textView, knownTextView, unknownTextView, progressTextView;
    private ProgressBar progressBar;
    private boolean isFlipped = false;
    private int currentIndex = 0;

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

        // Retrieve flashcards passed from MainActivity or ContinueFlashcardActivity
        Intent intent = getIntent();
        if (intent.hasExtra("FLASHCARDS")) {
            flashcards = (ArrayList<Flashcard>) intent.getSerializableExtra("FLASHCARDS");
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

        textView.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            textView.setText(isFlipped ? flashcards.get(currentIndex).getAnswer() : flashcards.get(currentIndex).getQuestion());
        });

        textView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private int shownIndex = 0;
    private void onSwipeLeft() {
        shownIndex += 1;
        flashcards.get(currentIndex).setKnown(false);
        unknownTextView.setText(String.valueOf(countUnknown()));
        animateSwipe(-textView.getWidth());
    }

    private void onSwipeRight() {
        shownIndex += 1;
        flashcards.get(currentIndex).setKnown(true);
        knownTextView.setText(String.valueOf(countKnown()));
        animateSwipe(textView.getWidth());
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

    private void animateSwipe(float toX) {
        textView.animate()
                .translationX(toX)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    currentIndex++;

                    if (currentIndex >= flashcards.size()) {
                        Intent i = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
                        i.putExtra("FLASHCARDS", flashcards);
                        startActivity(i);
                        finish();
                        return;
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

    private int initialTotalUnknown = -1;
    private void updateCard() {
        isFlipped = false;

        // Skip known cards
        while (currentIndex < flashcards.size() && flashcards.get(currentIndex).isKnown()) {
            currentIndex++;
        }

        // If there are no more unknown cards left
        if (currentIndex >= flashcards.size()) {
            Intent intent = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
            intent.putExtra("FLASHCARDS", flashcards); // pass the full list
            startActivity(intent);
            finish();
            return;
        }

        // Show question of the current (unknown) card
        textView.setText(flashcards.get(currentIndex).getQuestion());

        // Progress display based on total unknown cards
        int totalUnknown = countUnknown();
        int shownUnknown = getShownUnknownCount();
        if (initialTotalUnknown == -1) {
            initialTotalUnknown = totalUnknown;
        }
        progressTextView.setText((shownIndex+1) + "/" + initialTotalUnknown);

        int progress = (int) ((float) shownUnknown / totalUnknown * 100);
        progressBar.setProgress(progress);

        knownTextView.setText(String.valueOf(countKnown()));
        unknownTextView.setText(String.valueOf(countUnknown()));
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
