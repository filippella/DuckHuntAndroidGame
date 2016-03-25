package org.dalol.duckhunt.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Filippo-TheAppExpert on 8/25/2015.
 */
public class GameLoop extends Thread {

    private Game mGame;
    private SurfaceHolder mHolder;
    private boolean mRunning;

    public GameLoop(Game game, SurfaceHolder holder) {
        mGame = game;
        mHolder = holder;
    }

    @Override
    public void run() {

        Canvas canvas = null;

        while (mRunning) {
            try {
                canvas = mHolder.lockCanvas();
                synchronized (mHolder) {
                    long startTime = System.currentTimeMillis();

                    mGame.update();

                    mGame.render(canvas);

                    long elapsed = System.currentTimeMillis() - startTime;

                    long sleepTime = Constants.TARGET_TIME - elapsed / Constants.MILLISECONDS;

                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }

                }
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

}
