package com.example.sketchapp.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

/**
 * This class handles the display in the
 * SketchScreen activity for the user
 * to draw and interact with.
 */
public class DrawView extends View{

    private static final float TOUCH_TOLERANCE = 4;
    private float mX;
    private float mY;
    private Path mPath;
    private Paint mPaint;

    private ArrayList<Stroke> paths = new ArrayList<>();
    private int currentColour;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attributes) {
        super(context, attributes);

        //Initializing settings for the user
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAlpha(0xff);
    }

    public void setUpCanvas(int height, int width) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        //Default values
        currentColour = Color.GREEN;
        strokeWidth = 20;
    }

    public void setColour(int colour) {
        currentColour = colour;
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void undo() {
        if (paths.size() != 0) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }

    public Bitmap save() {
        return mBitmap;
    }
}
