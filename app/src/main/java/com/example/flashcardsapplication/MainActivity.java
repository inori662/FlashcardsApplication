package com.example.flashcardsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> items;
    ArrayList<FlashcardSet> flashcardSets;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Init list view
            listView = findViewById(R.id.listView);

            // Load flashcard sets from JSON
            flashcardSets = loadFlashcardSetsFromJson();

            // Extract set names
            items = new ArrayList<>();
            for (FlashcardSet set : flashcardSets) {
                items.add(set.getName());
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);

            // Handle list clicks
            listView.setOnItemClickListener((parent, view, position, id) -> {
                FlashcardSet selectedSet = flashcardSets.get(position);
                Intent intent = new Intent(MainActivity.this, FlashcardActivity.class);
                intent.putExtra("FLASHCARDS", selectedSet.getCards());
                intent.putExtra("FLASHCARDS_NAME", selectedSet.getName());
                startActivity(intent);
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<FlashcardSet> loadFlashcardSetsFromJson() {
        try {
            // Access the file from the assets folder
            InputStream is = getAssets().open("flashcards.json");  // file in assets/flashcards.json
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            // Convert the byte buffer to a UTF-8 string
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON using Gson
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<FlashcardSet>>(){}.getType();

            // Parse the JSON to FlashcardSets
            ArrayList<FlashcardSet> flashcardSets = gson.fromJson(json, listType);

            // For each FlashcardSet, we need to ensure the cards are properly initialized
            for (FlashcardSet set : flashcardSets) {
                ArrayList<Flashcard> updatedCards = new ArrayList<>();
                for (Flashcard card : set.getCards()) {
                    ArrayList<String> choices = new ArrayList<>(card.getChoices());  // Choices from JSON
                    updatedCards.add(new Flashcard(card.getQuestion(), choices, card.getAnswer(), card.isKnown()));
                }
                set.setCards(updatedCards);
            }

            return flashcardSets;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}