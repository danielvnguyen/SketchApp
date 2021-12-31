package com.example.sketchapp.model;

import android.graphics.Path;

/**
 * This class handles the characteristics of the user's
 * paint brush strokes. Data includes stroke width,
 * stroke colour, and if it is an eraser or not.
 */
public class Stroke {

    public int colour;
    public int strokeWidth;
    public Path path;
    public boolean isErasePath;

    public Stroke(int colour, int width, Path path, boolean erase) {
        this.colour = colour;
        this.strokeWidth = width;
        this.path = path;
        this.isErasePath = erase;
    }

}
