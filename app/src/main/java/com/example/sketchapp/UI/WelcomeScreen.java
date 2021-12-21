package com.example.sketchapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.sketchapp.R;

public class WelcomeScreen extends AppCompatActivity {

    private Boolean screenEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        fadeIn(findViewById(R.id.welcome_screen_title));
        fadeIn(findViewById(R.id.welcome_screen_author));
        Handler loadingHandler = new Handler();
        loadingHandler.postDelayed(this::startMainMenu, 4000);
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