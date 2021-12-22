package com.example.sketchapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.sketchapp.R;

/**
 * This class handles the main menu
 * of the application, mainly the buttons.
 */
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Welcome to SketchApp!");

        setUpButtons();
    }

    private void setUpButtons() {
        Button sketchBtn = findViewById(R.id.sketchBtn);
        Button galleryBtn = findViewById(R.id.galleryBtn);
        Button infoBtn = findViewById(R.id.infoBtn);

        sketchBtn.setOnClickListener((v) -> startActivity(SketchScreen.makeIntent(this)));
        galleryBtn.setOnClickListener((v) -> startActivity(GalleryScreen.makeIntent(this)));
        infoBtn.setOnClickListener((v) -> startActivity(InfoScreen.makeIntent(this)));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenu.class);
    }
}