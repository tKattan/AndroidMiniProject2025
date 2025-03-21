package com.example.androidminiproject2025.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidminiproject2025.BuildConfig;
import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.fragments.level.LevelSelectionFragment;
import com.example.androidminiproject2025.fragments.main.MainMenuFragment;

import timber.log.Timber;

public class MenuActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSIONS = 10;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if(!allPermissionsAreGranted()) {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
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

    private boolean allPermissionsAreGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if(!allPermissionsAreGranted()) {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}