package org.dalol.duckhunt.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.dalol.duckhunt.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Filippo-TheAppExpert on 8/25/2015.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = Game.class.getSimpleName();
    private GameLoop mThread;
    private int[] mSpeed = {2, 3, 4, 5};
    private List<Duck> mDucks;
    private int colors[] = {Color.CYAN, Color.GRAY, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.RED, Color.DKGRAY, Color.WHITE};
    private Bitmap mBitmap;
    private Paint mPaint;

    public Game(Context context) {
        super(context);
        init(null);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getHolder().addCallback(Game.this);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLACK);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.duck);

        mThread = new GameLoop(Game.this, getHolder());
        mDucks = new ArrayList<>();
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                mThread.setRunning(false);
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                broadcastEvent(event);

//                RectF rectF = new RectF(x + 50, y + 50, 50, 50);
//                detectIt(rectF);
                return true;

            case MotionEvent.ACTION_MOVE:

                return true;

            case MotionEvent.ACTION_UP:

                return true;

        }
        return super.onTouchEvent(event);
    }

    private void broadcastEvent(MotionEvent event) {

        for (int i = 0; i < mDucks.size(); i++) {
            if(mDucks.get(i).handleActionDown((int) event.getX(), (int) event.getY())) {
                mDucks.remove(i);
            }
        }
    }

    private void detectIt(RectF rectF) {
        for (int i = 0; i < mDucks.size(); i++) {

            if (new RectF(mDucks.get(i).getX() + 18, mDucks.get(i).getY(), 27, 30).intersect(rectF) ||
                    new RectF(mDucks.get(i).getX() + 30, mDucks.get(i).getY() + 30, 88, 25).intersect(rectF)) {
                System.out.println("Ball Detected!");
                mDucks.remove(i);
            }
        }

    }

    public void update() {

        if (System.currentTimeMillis() - Duck.lastTime >= Duck.timeBetweenBalls) {

            int nextDuckYPos = getNextYPos(Duck.nextDuckLines, getHeight());

            mDucks.add(new Duck(this, mBitmap, -mBitmap.getWidth(), nextDuckYPos, mSpeed[new Random().nextInt(3)], getColor()));

            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= 4)
                Duck.nextDuckLines = 0;

            Duck.lastTime = System.currentTimeMillis();

        }

        for (int i = 0; i < mDucks.size(); i++) {
            mDucks.get(i).update();
            if (mDucks.get(i).getX() > (getWidth() + 100)) {
                mDucks.remove(i);
            }
        }

    }

    private int getNextYPos(int count, int height) {

        double nextPos = 0;

        switch (count) {
            case 0:
                nextPos = height *  0.60;
                break;
            case 1:
                nextPos = height *  0.65;
                break;
            case 2:
                nextPos = height *  0.70;
                break;
            case 3:
                nextPos = height *  0.78;
                break;
        }

        return (int) nextPos;
    }

    public void render(Canvas canvas) {
        if (canvas != null) {

            canvas.drawColor(Color.CYAN);

            canvas.drawText("Runaway", 0, 0, mPaint);
            canvas.drawText("Kills", 0, 50, mPaint);
            canvas.drawText("Shoots", 0, 150, mPaint);
            canvas.drawText("Score", 0, 200, mPaint);

            for (int i = 0; i < mDucks.size(); i++) {
                mDucks.get(i).draw(canvas);
            }
        }
    }

    private int getColor() {
        return colors[new Random().nextInt(colors.length - 1)];
    }
}
