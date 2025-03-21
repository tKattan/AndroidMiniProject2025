package com.example.androidminiproject2025.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.androidminiproject2025.CancellationToken;
import com.example.androidminiproject2025.Result;
import com.example.androidminiproject2025.SensorRepository;
import com.example.androidminiproject2025.activities.MenuActivity;
import com.example.androidminiproject2025.domain.GameThread;

import timber.log.Timber;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread thread;
    private final SensorRepository sensorRepository;
    private final Handler handler;
    private CancellationToken cancellationToken;
    private String text = "";

    private int countdown = 3;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        sensorRepository = new SensorRepository(context);
        handler = new Handler(Looper.getMainLooper());
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width,
                               int height) {
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        Timber.d("Thread started");
        startCountdown();
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                Timber.e(e, "Error destroying thread");
            }
            handler.removeCallbacksAndMessages(null);
            retry = false;
        }
    }

    private void startCountdown() {
        Timber.d("Starting countdown");
        countdown = 3;
        handler.post(countdownRunnable);
    }

    // Runnable for the 3,2,1 countdown
    private final Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (countdown > 0) {
                invalidate();
                countdown--;
                handler.postDelayed(this, 1000);
            } else {
                text = "Move device";
                handler.postDelayed(GameView.this::startTimer, 1000);
            }
        }
    };

    private void startTimer() {
        Timber.d("Starting timer for checking movement");
        cancellationToken = new CancellationToken();
        handler.postDelayed(this::endTimer, 10000); // 10 seconds timer
        sensorRepository.checkIfMovementIsGood("x", this::onMovementChecked, cancellationToken);
    }

    private void endTimer() {
        Timber.d("Timer ended, cancelling movement check");
        cancellationToken.cancel();
        getContext().startActivity(new Intent(getContext(), MenuActivity.class).putExtra("result", "lose"));
    }

    private void onMovementChecked(Result<Boolean> result) {
        Timber.d("Movement checked received");
        if (result instanceof Result.Success) {
            boolean isGoodMovement = ((Result.Success<Boolean>) result).data;
            if (isGoodMovement) {
                handler.removeCallbacksAndMessages(null);
                // Toast.makeText(getContext(), "Great job!", Toast.LENGTH_SHORT).show();
                text = "Great job!";
                getContext().startActivity(new Intent(getContext(), MenuActivity.class).putExtra("result", "win"));
            }
        }else{
            Timber.e(((Result.Error<?>) result).exception, "Error checking movement");
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setTextSize(50);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            if (countdown > 0) {
                canvas.drawText(String.valueOf(countdown), (float) getWidth() / 2, (float) getHeight() / 2, paint);
            } else {
                canvas.drawText(text, (float) getWidth() / 2, (float) getHeight() / 2, paint);
            }
        }
    }

    public void update() {
    }
}