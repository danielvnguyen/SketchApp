package com.example.sketchapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sketchapp.R;

/**
 * This class handles the main menu
 * of the application, mainly the buttons.
 */
public class MainMenu extends AppCompatActivity {

    private MediaPlayer buttonSound;
    private TextView widthTV;
    private TextView heightTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Welcome to SketchApp!");

        widthTV = findViewById(R.id.widthTV);
        heightTV = findViewById(R.id.heightTV);
        buttonSound = MediaPlayer.create(getApplicationContext(), R.raw.button_click);

        setUpButtons();
    }

    //Constraints: Not too big or small, not 1 empty. Need both empty or both filled.
    private boolean validateInput(TextView width, TextView height) {
        String widthString = width.getText().toString();
        String heightString = height.getText().toString();

        if (widthString.matches("") && heightString.matches("")) {
            return true;
        }

        if (widthString.matches("") && !heightString.matches("")) {
            Toast.makeText(getApplicationContext(),
                    "You left the width blank!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (heightString.matches("") && !widthString.matches("")) {
            Toast.makeText(getApplicationContext(),
                    "You left the height blank!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int widthVal = Integer.parseInt(widthString);
        int heightVal = Integer.parseInt(heightString);

        if ((widthVal > 4000 || heightVal > 4000) || (widthVal < 50 || heightVal < 50)) {
            Toast.makeText(getApplicationContext(),
                    "Your dimension value(s) are too big/small!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setUpButtons() {
        Button sketchBtn = findViewById(R.id.sketchBtn);
        Button infoBtn = findViewById(R.id.infoBtn);

        sketchBtn.setOnClickListener((v) -> {
            buttonSound.start();
            Integer widthValue;
            Integer heightValue;
            if (validateInput(widthTV, heightTV)) {
                if (widthTV.getText().toString().equals("") && heightTV.getText().toString().equals("")) {
                    startActivity(SketchScreen.makeIntent(this));
                }
                else {
                    widthValue = Integer.parseInt(widthTV.getText().toString());
                    heightValue = Integer.parseInt(heightTV.getText().toString());

                    Intent i = new Intent(MainMenu.this, SketchScreen.class);
                    i.putExtra("width", widthValue);
                    i.putExtra("height", heightValue);
                    startActivity(i);
                }
            }
        });

        infoBtn.setOnClickListener((v) -> {
            buttonSound.start();
            startActivity(InfoScreen.makeIntent(this));
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenu.class);
    }
}