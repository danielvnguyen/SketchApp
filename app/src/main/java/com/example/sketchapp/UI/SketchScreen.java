package com.example.sketchapp.UI;

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

import java.util.ArrayList;
import java.util.Objects;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import java.io.OutputStream;
import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * This class handles the functionality and user
 * interactivity of the main sketching screen
 * of the application. Primarily includes button
 * functionality (undo, save, etc.)
 */
public class SketchScreen extends AppCompatActivity {

    private DrawView paint;
    private ImageButton saveBtn;
    private ImageButton colourBtn;
    private ImageButton strokeBtn;
    private ImageButton undoBtn;
    private ImageButton eraserBtn;
    private ImageButton bucketBtn;
    private ImageButton clearBtn;
    private RangeSlider rangeSlider;
    private int backgroundColour;
    private int currentColour;
    private ArrayList<String> colorsHexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("You Are Sketching");

        colorsHexList = new ArrayList<>();
        colorsHexList.add("#000000");
        colorsHexList.add("#FFFFFF");
        colorsHexList.add("#cc0000");
        colorsHexList.add("#FFA500");
        colorsHexList.add("#FFFF00");
        colorsHexList.add("#00FF00");
        colorsHexList.add("#0000FF");
        colorsHexList.add("#FFC0CB");
        colorsHexList.add("#7F00FF");
        colorsHexList.add("#964B00");
        colorsHexList.add("#808080");
        colorsHexList.add("#30D5C8");
        colorsHexList.add("#B2AC88");
        colorsHexList.add("#D2B48C");
        colorsHexList.add("#a9a9a9");

        paint = findViewById(R.id.draw_view);
        rangeSlider = findViewById(R.id.rangebar);
        undoBtn = findViewById(R.id.undoBtn);
        saveBtn = findViewById(R.id.saveBtn);
        colourBtn = findViewById(R.id.colourBtn);
        strokeBtn = findViewById(R.id.strokeBtn);
        eraserBtn = findViewById(R.id.eraserBtn);
        bucketBtn = findViewById(R.id.bucketBtn);
        clearBtn = findViewById(R.id.clearBtn);
        backgroundColour = Color.WHITE;
        currentColour = Color.BLACK;

        setUpButtons();
    }

    private void setUpButtons() {
        undoBtn.setOnClickListener(view -> paint.undo());

        clearBtn.setOnClickListener(view -> paint.clearCanvas());

        eraserBtn.setOnClickListener(view -> paint.setColour(backgroundColour));

        bucketBtn.setOnClickListener(view -> {
        final ColorPicker colorPicker = new ColorPicker(SketchScreen.this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int colour) {
                paint.changeBackground(colour);
                backgroundColour = colour;
            }
            @Override
            public void onCancel() {
                colorPicker.dismissDialog();
            }
            })
                .setTitle("Choose a canvas colour")
                .setColors(colorsHexList)
                .setColumns(5)
                .show();
        });

        saveBtn.setOnClickListener(view -> {
            Bitmap bmp = paint.save();
            OutputStream imageOutStream;
            ContentValues cv = new ContentValues();

            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            }

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            try {
                imageOutStream = getContentResolver().openOutputStream(uri);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                imageOutStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        colourBtn.setOnClickListener(view -> {
            final ColorPicker colorPicker = new ColorPicker(SketchScreen.this);
            colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                @Override
                public void setOnFastChooseColorListener(int position, int colour) {
                    paint.setColour(colour);
                    currentColour = colour;
                }
                @Override
                public void onCancel() {
                    colorPicker.dismissDialog();
                }
            })
                .setTitle("Choose a colour to paint with")
                .setColors(colorsHexList)
                .setColumns(5)
                .show();
        });

        strokeBtn.setOnClickListener(view -> {
            paint.setColour(currentColour);
            if (rangeSlider.getVisibility() == View.VISIBLE)
                rangeSlider.setVisibility(View.GONE);
            else
                rangeSlider.setVisibility(View.VISIBLE);
        });

        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);
        rangeSlider.addOnChangeListener((slider, value, fromUser) ->
                paint.setStrokeWidth((int) value));

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.setUpCanvas(height, width);
            }
        });
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