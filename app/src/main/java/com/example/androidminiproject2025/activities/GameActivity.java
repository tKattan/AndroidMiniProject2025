package com.example.androidminiproject2025.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.Tasks;
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

        String levelNumber = getIntent().getStringExtra("LEVEL_NUMBER");
        assert levelNumber != null;
        gameView = new GameView(this, this, Integer.parseInt(levelNumber));
        imageToShow = new ImageView(this);

        int heightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, imageToShow.getResources().getDisplayMetrics());

        imageToShow.setLayoutParams(new ViewGroup.LayoutParams(
                // or ViewGroup.LayoutParams.WRAP_CONTENT
                ViewGroup.LayoutParams.MATCH_PARENT,
                // or ViewGroup.LayoutParams.WRAP_CONTENT,
                heightInPx));
        container.addView(gameView);
        container.addView(imageToShow);

        mediaPlayer = MediaPlayer.create(this, R.raw.musique_jeu);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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
public void updateTaskNumber(int taskNumber) {
    runOnUiThread(() -> levelText.setText("Task : " + taskNumber));
}

public void showTask(Tasks task){
    if(task == Tasks.TAP){
        runOnUiThread(() -> {
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
    });
}
private int getTaskDrawable(Tasks task) {
    switch (task) {
        case BLOW: return R.drawable.blow;
        case MOVEMENT: return R.drawable.move;
    }
    throw new RuntimeException("Not supported type");
}
}

