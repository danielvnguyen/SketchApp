package com.example.sketchapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.sketchapp.R;

/**
 * This class handles a simple welcome screen
 * for SketchApp. Includes animations and skip button.
 */
public class WelcomeScreen extends AppCompatActivity {

    private Boolean screenEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setUpSkipBtn();
        setUpAnimation();

        fadeIn(findViewById(R.id.welcome_screen_title));
        fadeIn(findViewById(R.id.welcome_screen_author));
        Handler loadingHandler = new Handler();
        loadingHandler.postDelayed(this::startMainMenu, 4000);
    }

    private void setUpAnimation() {

    }

    private void setUpSkipBtn() {
        Button skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener((v)-> startMainMenu());
    }

    private void startMainMenu() {
        if (!screenEnded) {
            screenEnded = true;
            startActivity(MainMenu.makeIntent(this));
            finish();
        }
    }

    private void fadeIn(View view) {
        view.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        view.animate();
    }
}