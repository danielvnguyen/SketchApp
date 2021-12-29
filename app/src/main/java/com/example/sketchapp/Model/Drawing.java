package com.example.sketchapp.Model;

import android.graphics.Bitmap;

/**
 * This class handles each list entry of the
 * 'Gallery' activity. Data includes
 * the bitmap of the drawing the user created,
 * as well as the date and time of the creation.
 */
public class Drawing {
    private final Bitmap drawingBitmap;
    private final String creationTime;

    public Drawing(Bitmap drawing, String time) {
        drawingBitmap = drawing;
        creationTime = time;
    }

    public Bitmap getDrawingBitmap() {
        return drawingBitmap;
    }

    public String getCreationTime() {
        return creationTime;
    }
}
