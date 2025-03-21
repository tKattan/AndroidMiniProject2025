package com.example.androidminiproject2025.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
        return binding.getRoot();
    }

    private void initializePlayButton() {
        binding.playButton.setOnClickListener(view -> {
            MenuActivity activity = (MenuActivity) getActivity();
            activity.switchToLevelSelectionFragment();
        });
    }
}