package com.example.androidminiproject2025.domain;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.androidminiproject2025.views.GameView;

import timber.log.Timber;

public class GameThread extends Thread {
    private static final int TARGET_FPS = 60;
    private static final long FRAME_TIME = 1000000000 / TARGET_FPS; // in nanoseconds

    private boolean running;
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    @Override
    public void run() {
        long startTime;
        long timeTaken;
        long sleepTime;

        while (running) {
            startTime = System.nanoTime();
            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                Timber.e(e, "Error in GameThread");
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        Timber.e(e, "Error unlocking canvas");
                    }
                }
            }

            timeTaken = System.nanoTime() - startTime;
            sleepTime = (FRAME_TIME - timeTaken) / 1000000; // convert to milliseconds
            // Timber.d("Sleep time: %d", sleepTime);

            if (sleepTime > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Timber.e(e, "Thread sleep interrupted");
                }
            }
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
