package com.example.sketchapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        int backgroundColour = Color.WHITE;
        mCanvas.drawColor(backgroundColour);

        for (Stroke currentPath: paths) {
            mPaint.setColor(currentPath.colour);
            mPaint.setStrokeWidth(currentPath.strokeWidth);
            mCanvas.drawPath(currentPath.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        Stroke currentPath = new Stroke(currentColour, strokeWidth, mPath);
        paths.add(currentPath);

        mPath.reset();
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchStart(x, y);
            invalidate();
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            touchMove(x, y);
            invalidate();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchUp();
            invalidate();
        }
        return true;
    }

    //Below is the functionality for the SketchScreen buttons

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
