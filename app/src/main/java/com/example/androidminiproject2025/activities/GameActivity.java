package com.example.androidminiproject2025.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.views.GameView;

public class GameActivity extends Activity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}