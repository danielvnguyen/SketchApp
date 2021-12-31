package com.example.sketchapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.sketchapp.model.DrawView;
import com.example.sketchapp.R;
import com.google.android.material.slider.RangeSlider;
import java.io.OutputStream;
import java.util.Objects;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

/**
 * This class handles the functionality and user
 * interactivity of the main sketching screen
 * of the application. Primarily includes button
 * functionality (undo, save, etc.)
 */
public class SketchScreen extends AppCompatActivity implements ColorPickerDialogListener {

    private DrawView paint;
    private ImageButton saveBtn;
    private ImageButton colourBtn;
    private ImageButton strokeBtn;
    private ImageButton undoBtn;
    private ImageButton eraserBtn;
    private ImageButton bucketBtn;
    private ImageButton clearBtn;
    private ImageButton redoBtn;
    private RangeSlider rangeSlider;
    private int backgroundColour;
    private int currentColour;
    private MediaPlayer buttonSound;
    private Button hideBtn;
    private Button zoomBtn;
    private boolean toolsHidden = false;
    private int widthValue;
    private int heightValue;
    private boolean isExtras;
    private static final int DIALOG_ID = 0;
    private boolean isBackground;
    public static boolean isZoom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("You Are Sketching");

        paint = findViewById(R.id.draw_view);
        hideBtn = findViewById(R.id.hide_btn);
        rangeSlider = findViewById(R.id.rangebar);
        undoBtn = findViewById(R.id.undoBtn);
        saveBtn = findViewById(R.id.saveBtn);
        colourBtn = findViewById(R.id.colourBtn);
        strokeBtn = findViewById(R.id.strokeBtn);
        eraserBtn = findViewById(R.id.eraserBtn);
        bucketBtn = findViewById(R.id.bucketBtn);
        clearBtn = findViewById(R.id.clearBtn);
        redoBtn = findViewById(R.id.redoBtn);
        zoomBtn = findViewById(R.id.zoom_btn);

        backgroundColour = Color.WHITE;
        currentColour = Color.BLACK;

        getDimensionValues();
        setUpSounds();
        setUpButtons();
    }

    private void getDimensionValues() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isExtras = true;
            widthValue = extras.getInt("width");
            heightValue = extras.getInt("height");
        }
    }

    private void setUpSounds() {
        buttonSound = MediaPlayer.create(getApplicationContext(), R.raw.click_sound);
    }

    private void setUpButtons() {
        zoomBtn.setOnClickListener(view -> {
            buttonSound.start();
            if (isZoom) {
                isZoom = false;
                zoomBtn.setText(R.string.zoom);
            }
            else {
                isZoom = true;
                zoomBtn.setText(R.string.done);
            }
        });

        redoBtn.setOnClickListener(view -> {
            buttonSound.start();
            paint.redo();
        });

        undoBtn.setOnClickListener(view -> {
            buttonSound.start();
            paint.undo();
        });

        clearBtn.setOnClickListener(view -> {
            buttonSound.start();
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Clear Drawing");
            newDialog.setMessage("Clear your canvas? This will erase all of your progress.");
            newDialog.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(getApplicationContext(),"Cleared the canvas!", Toast.LENGTH_SHORT).show();
                paint.clearCanvas();
                dialog.dismiss();
            });
            newDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            newDialog.show();
        });

        eraserBtn.setOnClickListener(view -> {
            buttonSound.start();
            paint.setEraser(true);
            paint.setColour(backgroundColour);
        });

        hideBtn.setOnClickListener(view -> {
            buttonSound.start();
            if (!toolsHidden) {
                hideBtn.setText(R.string.show);
                toolsHidden = true;
                redoBtn.setVisibility(View.GONE);
                undoBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);
                colourBtn.setVisibility(View.GONE);
                strokeBtn.setVisibility(View.GONE);
                eraserBtn.setVisibility(View.GONE);
                bucketBtn.setVisibility(View.GONE);
                clearBtn.setVisibility(View.GONE);
                rangeSlider.setVisibility(View.GONE);
            }
            else {
                hideBtn.setText(R.string.hide);
                toolsHidden = false;
                redoBtn.setVisibility(View.VISIBLE);
                undoBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                colourBtn.setVisibility(View.VISIBLE);
                strokeBtn.setVisibility(View.VISIBLE);
                eraserBtn.setVisibility(View.VISIBLE);
                bucketBtn.setVisibility(View.VISIBLE);
                clearBtn.setVisibility(View.VISIBLE);
                rangeSlider.setVisibility(View.VISIBLE);
            }
        });

        saveBtn.setOnClickListener(view -> {
            buttonSound.start();
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Drawing");
            saveDialog.setMessage("Save drawing to your device gallery?");
            saveDialog.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(getApplicationContext(),"Drawing saved to gallery!", Toast.LENGTH_SHORT).show();
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
            saveDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            saveDialog.show();
        });

        bucketBtn.setOnClickListener(view -> {
            isBackground = true;
            buttonSound.start();
            ColorPickerDialog.newBuilder()
                    .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                    .setAllowPresets(true)
                    .setDialogId(DIALOG_ID)
                    .setColor(currentColour)
                    .setShowColorShades(true)
                    .show(this);
        });

        colourBtn.setOnClickListener(view -> {
            isBackground = false;
            buttonSound.start();
            ColorPickerDialog.newBuilder()
                    .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                    .setAllowPresets(true)
                    .setDialogId(DIALOG_ID)
                    .setColor(currentColour)
                    .setShowColorShades(true)
                    .show(this);
        });

        strokeBtn.setOnClickListener(view -> {
            buttonSound.start();
            paint.setEraser(false);
            paint.setColour(currentColour);
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
                int width = paint.getWidth();
                int height = paint.getHeight();
                if (isExtras) {
                    paint.setUpCanvas(heightValue, widthValue);
                }
                else {
                    heightValue = height;
                    widthValue = width;
                    paint.setUpCanvas(height, width);
                }
            }
        });
    }

    @Override public void onColorSelected(int dialogId, int colour) {
        if (dialogId == DIALOG_ID) {
            if (isBackground) {
                Toast.makeText(getApplicationContext(),"Background colour changed!", Toast.LENGTH_SHORT).show();
                backgroundColour = colour;
                paint.changeBackground(colour);
            }
            else {
                Toast.makeText(getApplicationContext(),"Drawing colour changed!", Toast.LENGTH_SHORT).show();
                currentColour = colour;
                paint.setColour(colour);
            }
        }
    }
    @Override public void onDialogDismissed(int dialogId) {}

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