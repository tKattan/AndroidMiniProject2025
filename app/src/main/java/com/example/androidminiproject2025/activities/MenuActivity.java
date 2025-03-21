package com.example.androidminiproject2025.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidminiproject2025.BuildConfig;
import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.fragments.level.LevelSelectionFragment;
import com.example.androidminiproject2025.fragments.main.MainMenuFragment;

import timber.log.Timber;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        switchToMainMenuFragment();
    }

    public void switchToMainMenuFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_fragment_container, new MainMenuFragment())
                .addToBackStack(null)
                .commit();
    }

    public void switchToLevelSelectionFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_fragment_container, new LevelSelectionFragment())
                .addToBackStack(null)
                .commit();
    }
}