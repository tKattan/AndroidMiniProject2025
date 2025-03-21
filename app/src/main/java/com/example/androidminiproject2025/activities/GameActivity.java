package com.example.androidminiproject2025.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidminiproject2025.R;
import com.bumptech.glide.Glide;
import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.views.GameView;

public class GameActivity extends Activity {
    private MediaPlayer mediaPlayer;

    private TextView levelText;
    private ImageView imageToShow;
    GameView gameView;
    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        levelText = findViewById(R.id.levelTextGame);
        container = findViewById(R.id.game_container);

        gameView = new GameView(this, this);
        imageToShow = new ImageView(this);

        container.addView(gameView);
        container.addView(imageToShow);
    }

    public void updateLevel(int newLevel) {
        runOnUiThread(() -> levelText.setText("Task : " + newLevel));
    }

        String levelNumber = getIntent().getStringExtra("LEVEL_NUMBER");
        assert levelNumber != null;

        mediaPlayer = MediaPlayer.create(this, R.raw.musique_jeu);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        setContentView(new GameView(this, Integer.parseInt(levelNumber)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause music when app goes into the background
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start(); // Resume music when app comes back to the foreground
        }
    }
public void updateLevel(int newLevel) {
    runOnUiThread(() -> levelText.setText("Task : " + newLevel));
}

public void showTask(Task task){
    if(task == Task.TAP){
        runOnUiThread(() -> {
            gameView.setVisibility(VISIBLE);
            imageToShow.setVisibility(INVISIBLE);
        });
        return;
    }

    int taskDrawableRessourceId = getTaskDrawable(task);

    runOnUiThread(() -> {
        Glide.with(this)
                .load(taskDrawableRessourceId)
                .into(imageToShow);

        imageToShow.setVisibility(VISIBLE);
        gameView.setVisibility(INVISIBLE);
    });
}
private int getTaskDrawable(Task task) {
    switch (task) {
        case BLOW: return R.drawable.blow;
        case MOVEMENT: return R.drawable.move;
    }
    throw new RuntimeException("Not supported type");
}
}