package com.example.androidminiproject2025.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.androidminiproject2025.views.GameView;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String levelNumber = getIntent().getStringExtra("LEVEL_NUMBER");
        assert levelNumber != null;
        setContentView(new GameView(this, Integer.parseInt(levelNumber)));
    }
}