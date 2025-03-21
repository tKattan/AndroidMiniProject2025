package com.example.androidminiproject2025.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.androidminiproject2025.views.GameView;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(new GameView(this));
    }
}