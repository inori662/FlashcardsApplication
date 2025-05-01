package com.example.flashcardsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private TextView textView, knownTextView, unknownTextView, progressTextView;
    private ProgressBar progressBar;
    private boolean isFlipped = false;
    private int currentIndex = 0, knownSum = 0, unknownSum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_flashcard);

            textView = findViewById(R.id.textView);
            knownTextView = findViewById(R.id.knownTextView);
            unknownTextView = findViewById(R.id.unknownTextView);
            progressTextView = findViewById(R.id.progress);
            progressBar = findViewById(R.id.progressBar);


            flashcards = new ArrayList<>();
            flashcards.add(new Flashcard("What is the capital of France?", "Paris", false));
            flashcards.add(new Flashcard("What is 2 + 2?", "4", false));
            flashcards.add(new Flashcard("What is the largest ocean?", "Pacific Ocean", false));
            flashcards.add(new Flashcard("Who wrote 'Romeo and Juliet'?", "William Shakespeare", false));
            flashcards.add(new Flashcard("What planet is known as the Red Planet?", "Mars", false));

            updateCard(); // Set initial card and progress

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

            // Handle onClick to reveal the answer
            textView.setOnClickListener(v -> {
                // Toggle between the question and the answer
                isFlipped = !isFlipped;
                textView.setText(isFlipped ? flashcards.get(currentIndex).getAnswer() : flashcards.get(currentIndex).getQuestion());
            });

            textView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));


        } catch (Exception e) {
            Toast.makeText(this, "Crash: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onSwipeLeft() {
        unknownSum += 1;
        flashcards.get(currentIndex).setKnown(false);
        animateSwipe(-textView.getWidth()); // swipe left
        unknownTextView.setText(String.valueOf(unknownSum));
    }

    private void onSwipeRight() {
        knownSum += 1;
        flashcards.get(currentIndex).setKnown(true);
        animateSwipe(textView.getWidth()); // swipe right
        knownTextView.setText(String.valueOf(knownSum));
    }

    private void animateSwipe(float toX) {
        textView.animate()
                .translationX(toX)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    // Reset position and load next flashcard
                    currentIndex++;
                    if (currentIndex >= flashcards.size()) {
                        Toast.makeText(this, "All flashcards reviewed!", Toast.LENGTH_SHORT).show();
                        if (unknownSum != 0) {
                            Intent i = new Intent(FlashcardActivity.this, ContinueFlashcardActivity.class);
                            startActivity(i);
                        }
                    }

                    updateCard(); // <-- Update card and progress

                    // Reset position and animate back in
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

        // Set progress in format "1/5"
        String progressText = (currentIndex + 1) + "/" + flashcards.size();
        progressTextView.setText(progressText);

        // Update the ProgressBar's progress
        int progress = (int) ((float) (currentIndex + 1) / flashcards.size() * 100);
        progressBar.setProgress(progress);
    }

}
