package com.example.sketchapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import java.util.ArrayList;

/**
 * This class handles the display in the
 * SketchScreen activity for the user
 * to draw and interact with. Includes
 * canvas functionality and handling
 * touch events from the user.
 */
public class DrawView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private float mX;
    private float mY;
    private Path mPath;
    private final Paint mPaint;

    private final ArrayList<Stroke> paths = new ArrayList<>();
    private final ArrayList<Stroke> undonePaths = new ArrayList<>();
    private int currentColour;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int backgroundColour;
    private boolean isEraser = false;
    private boolean isZoom = false;

    private float scaleFactor = 1f;
    private final ScaleGestureDetector detector;
    private float startX = 0f;
    private float startY = 0f;
    private float translateX;
    private float translateY;
    private float previousTranslateX;
    private float previousTranslateY;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attributes) {
        super(context, attributes);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float displayWidth = metrics.widthPixels;
        float displayHeight = metrics.heightPixels;

        translateX = displayWidth /4;
        translateY = displayHeight /4;
        previousTranslateX = displayWidth /4;
        previousTranslateY = displayHeight /4;

        detector = new ScaleGestureDetector(context, new ScaleListener());

        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAlpha(0xff);
        backgroundColour = Color.WHITE;
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            float MAX_ZOOM = 5.0f;
            float MIN_ZOOM = 0.1f;
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));

            invalidate();
            return true;
        }
    }

    public void setUpCanvas(int height, int width) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColour = Color.BLACK;
        strokeWidth = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate((translateX) / scaleFactor, (translateY) / scaleFactor);
        canvas.scale(scaleFactor, scaleFactor, 0, 0);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        mCanvas.drawColor(backgroundColour);
        for (Stroke currentPath: paths) {
            if (currentPath.isErasePath) {
                mPaint.setColor(backgroundColour);
            }
            else {
                mPaint.setColor(currentPath.colour);
            }
            mPaint.setStrokeWidth(currentPath.strokeWidth);
            mCanvas.drawPath(currentPath.path, mPaint);
        }

        canvas.restore();
    }

    public void changeBackground(int colour) {
        backgroundColour = colour;
        mCanvas.drawColor(backgroundColour);
        for (Stroke currentPath: paths) {
            if (currentPath.isErasePath) {
                mPaint.setColor(backgroundColour);
            }
            else {
                mPaint.setColor(currentPath.colour);
            }
            mPaint.setStrokeWidth(currentPath.strokeWidth);
            mCanvas.drawPath(currentPath.path, mPaint);
        }
        invalidate();
    }

    private void touchStart(float x, float y) {
        undonePaths.clear();
        mPath = new Path();

        Stroke currentPath;
        if (isEraser) {
            currentPath = new Stroke(backgroundColour, strokeWidth, mPath, true);
        }
        else {
            currentPath = new Stroke(currentColour, strokeWidth, mPath, false);
        }
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
        detector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isZoom) {
                startX = x - previousTranslateX;
                startY = y - previousTranslateY;
            }
            else {
                touchStart(x, y);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isZoom) {
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
            }
            else {
                touchMove(x, y);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isZoom) {
                previousTranslateX = translateX;
                previousTranslateY = translateY;
            }
            else {
                touchUp();
            }
        }

        invalidate();
        return true;
    }

    public void setColour(int colour) {
        currentColour = colour;
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void clearCanvas() {
        if (paths.size() != 0) {
            paths.clear();
        }

        invalidate();
    }

    public void undo() {
        if (paths.size() != 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
        }

        invalidate();
    }

    public void redo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size()-1));
        }

        invalidate();
    }

    public Bitmap save() {
        return mBitmap;
    }

    public void setEraser(boolean eraser) {
        isEraser = eraser;
    }

    public void setZoom(boolean zoom) {
        isZoom = zoom;
    }

    public boolean isZoom() {
        return isZoom;
    }
}
