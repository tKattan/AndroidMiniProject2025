package com.example.androidminiproject2025;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.androidminiproject2025.views.GameView;

import timber.log.Timber;

public class GameThread extends Thread {

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
        while (running) {
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
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
