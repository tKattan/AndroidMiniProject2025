package com.example.androidminiproject2025.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.androidminiproject2025.views.GameView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(new GameView(this));

        SharedPreferences sharedPref =
                this.getPreferences(Context.MODE_PRIVATE);
        int y = sharedPref.getInt("y", 0);
        y = (y + 100) % 400;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("y", y);
        editor.apply();
    }
}