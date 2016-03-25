package org.dalol.duckhunt.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Filippo-TheAppExpert on 8/25/2015.
 */
public class Duck {

    private static final String TAG = Duck.class.getSimpleName();
    public static int nextDuckLines = 0;
    private int x, y, speed;
    private Game game;
    private int color;
    public static long timeBetweenBalls = 1000L / 2;
    public static long lastTime = 0;
    private Paint mPaint;
    private Bitmap mDuck;

    public Duck(Game game, Bitmap bitmap, int x, int y, int speed, int color) {
        this.game = game;
        mDuck = bitmap;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = color;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        x += speed;
    }

    public void draw(Canvas canvas) {
        mPaint.setColor(color);
        canvas.drawBitmap(mDuck, x, y, mPaint);
        //canvas.drawCircle(x, y, 100, mPaint);
    }

    public boolean handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - mDuck.getWidth() / 2) && (eventX <= (x + mDuck.getWidth()/2))) {
            if (eventY >= (y - mDuck.getHeight() / 2) && (y <= (y + mDuck.getHeight() / 2))) {
                Log.d(TAG, "Duck Touched");
                return true;
            } else {
                Log.d(TAG, "Duck Not Touched");
                return false;
            }
        } else {
            Log.d(TAG, "Duck Not Touched");
            return false;
        }

    }
}
