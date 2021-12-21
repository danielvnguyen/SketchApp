package com.example.sketchapp.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class handles the display in the
 * SketchScreen activity for the user
 * to draw and interact with.
 */
public class DrawView extends View{

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }
}
