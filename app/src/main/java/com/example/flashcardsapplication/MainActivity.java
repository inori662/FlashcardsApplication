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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> items;
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

            // Init elements for the list view
            listView = findViewById(R.id.listView);

            // Create a list of items
            items = new ArrayList<>();
            items.add("Item 1");
            items.add("Item 2");

            // Create an adapter to display the list
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

            // Set the adapter to the ListView
            listView.setAdapter(adapter);

            // To add a new item later:
            items.add("New Item");
            adapter.notifyDataSetChanged(); // Refresh the ListView

            // Handle item clicks
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String clickedItem = items.get(position);
                Intent intent = new Intent(MainActivity.this, FlashcardActivity.class);
                startActivity(intent);
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}