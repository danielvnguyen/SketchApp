package com.example.sketchapp.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import com.example.sketchapp.Model.DrawView;
import com.example.sketchapp.R;
import com.google.android.material.slider.RangeSlider;
import java.util.Objects;

public class SketchScreen extends AppCompatActivity {

    private DrawView paint;
    private ImageButton saveBtn;
    private ImageButton colourBtn;
    private ImageButton strokeBtn;
    private ImageButton undoBtn;
    private RangeSlider rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("You Are Sketching");

        paint = findViewById(R.id.draw_view);
        rangeSlider = findViewById(R.id.rangebar);
        undoBtn = findViewById(R.id.undoBtn);
        saveBtn = findViewById(R.id.saveBtn);
        colourBtn = findViewById(R.id.colourBtn);
        strokeBtn = findViewById(R.id.strokeBtn);

        setUpButtons();
    }

    private void setUpButtons() {
        undoBtn.setOnClickListener(view -> paint.undo());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SketchScreen.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}