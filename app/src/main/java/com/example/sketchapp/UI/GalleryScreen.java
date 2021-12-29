package com.example.sketchapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sketchapp.Model.Drawing;
import com.example.sketchapp.Model.DrawingManager;
import com.example.sketchapp.Model.GalleryLVAdapter;
import com.example.sketchapp.R;

import java.util.Objects;

/**
 * This class represents the list of
 * drawings that the user has created.
 * Data includes creation time and Bitmap of the drawing(s).
 * Supports deleting and editing the existing drawings.
 */
public class GalleryScreen extends AppCompatActivity {
    private DrawingManager drawingManager;
    private ArrayAdapter<Drawing> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Your Gallery");

        drawingManager = DrawingManager.getInstance(this);
        setUpList();
        //setUpDelBtn();
    }

    protected void onStart() {
        super.onStart();
        listAdapter.notifyDataSetChanged();
    }

    private void setUpList() {
        ListView drawingLV = findViewById(R.id.gallery_list);
        listAdapter = new GalleryLVAdapter(this, R.layout.gallery_adapter, drawingManager.getDrawingList());
        drawingLV.setAdapter(listAdapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GalleryScreen.class);
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