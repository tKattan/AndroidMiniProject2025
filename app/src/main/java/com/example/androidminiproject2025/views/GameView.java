package com.example.androidminiproject2025.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.androidminiproject2025.CancellationToken;
import com.example.androidminiproject2025.Result;
import com.example.androidminiproject2025.SensorRepository;
import com.example.androidminiproject2025.Tasks;
import com.example.androidminiproject2025.activities.MenuActivity;
import com.example.androidminiproject2025.activities.GameActivity;
import com.example.androidminiproject2025.domain.GameThread;
import com.example.androidminiproject2025.GameState;

import timber.log.Timber;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread thread;
    private final SensorRepository sensorRepository;
    private final Handler handler;
    private final int taskCountdownTime;

    private CancellationToken cancellationToken;
    private GameActivity activity;

    private final Tasks[] tasks = new Tasks[]{Tasks.MOVEMENT, Tasks.MOVEMENT, Tasks.MOVEMENT};

    private GameState state = GameState.GAME_START;
    private int countdown = 3;
    private int currentTask = 0;

    public GameView(Context context, int taskCountdownTime) {
        super(context);
        getHolder().addCallback(this);
        sensorRepository = new SensorRepository(context);
        handler = new Handler(Looper.getMainLooper());
        thread = new GameThread(getHolder(), this);
        this.taskCountdownTime = taskCountdownTime;
        setFocusable(true);
    }
    public GameView(Context context, GameActivity activity) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        this.activity = activity;
        setFocusable(true);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
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
        state = GameState.GAME_START;
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
        state = GameState.COUNTDOWN;
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
                endCountdown();
            }
        }
    };

    private void endCountdown(){
        state = GameState.TASK_LOADING;
    }

    private void loadTask() {
        Timber.d("Starting timer for checking movement");
        cancellationToken = new CancellationToken();
        handler.postDelayed(this::endTimer, taskCountdownTime); // timer to lose

        switch(tasks[currentTask]){
            case MOVEMENT:
                Timber.d("Starting movement task");
                sensorRepository.checkIfMovementIsGood(null, this::onMovementChecked, cancellationToken);
                break;
            case TAP:
                break;
            case BLOW:
                Timber.d("Starting blow task");
                sensorRepository.checkIfMicrophoneInputIsGood(null, this::onMicCheck, cancellationToken);
                break;
        }

        state = GameState.TASK_IN_PROGRESS;
        Timber.d("Task loaded");
    }

    private void endTimer() {
        Timber.d("Timer ended, cancelling movement check");
        cancellationToken.cancel();
        state = GameState.TASK_LOST;
    }

    private void onMovementChecked(Result<Boolean> result) {
        Timber.d("Movement checked received");
        if (result instanceof Result.Success) {
            if (((Result.Success<Boolean>) result).data) {
                handler.removeCallbacksAndMessages(null);
                state = GameState.TASK_WON;
            }
        }else{
            state = GameState.GAME_ERROR;
            Timber.e(((Result.Error<?>) result).exception, "Error checking movement");
        }
    }

    private void onMicCheck(Result<Boolean> result) {
        Timber.d("Mic checked received");
        if (result instanceof Result.Success) {
            if (((Result.Success<Boolean>) result).data) {
                handler.removeCallbacksAndMessages(null);
                state = GameState.TASK_WON;
            }
        }else{
            state = GameState.GAME_ERROR;
            Timber.e(((Result.Error<?>) result).exception, "Error checking microphone");
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
                canvas.drawText(tasks[currentTask].toString(), (float) getWidth() / 2, (float) getHeight() / 2, paint);
            }
        }
    }

    public void update() {
        // Timber.d("Current state: %s", state);
        switch (state){
            case GAME_START:
                startCountdown();
                break;
            case COUNTDOWN:
            case TASK_IN_PROGRESS:
                break;
            case TASK_LOADING: // Choose the task, start the timer and check the
                loadTask();
                break;
            case TASK_WON:
                currentTask++;
                if(currentTask < tasks.length){
                    state = GameState.TASK_LOADING;
                }else{
                    state = GameState.GAME_WON;
                }
                break;
            case TASK_LOST:
                state = GameState.GAME_LOST;
                break;
            case GAME_LOST:
                getContext().startActivity(new Intent(getContext(), MenuActivity.class).putExtra("result", "lose"));
                break;
            case GAME_WON:
                getContext().startActivity(new Intent(getContext(), MenuActivity.class).putExtra("result", "win"));
                break;
            case GAME_ERROR:
                getContext().startActivity(new Intent(getContext(), MenuActivity.class).putExtra("result", "error"));
                break;
        }
    }
}