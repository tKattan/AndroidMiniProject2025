package com.example.androidminiproject2025.fragments.main;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.activities.MenuActivity;
import com.example.androidminiproject2025.databinding.FragmentMainMenuBinding;

public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);

        initializePlayButton();

        MenuActivity menu = (MenuActivity) getActivity();
        MediaPlayer mediaPlayer = menu.getMediaPlayer();
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(menu.getApplicationContext(), R.raw.musique_accueil);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        menu.setMediaPlayer(mediaPlayer);

        return binding.getRoot();
    }

    private void initializePlayButton() {
        binding.playButton.setOnClickListener(view -> {
            MenuActivity activity = (MenuActivity) getActivity();
            activity.switchToLevelSelectionFragment();
        });
    }
}