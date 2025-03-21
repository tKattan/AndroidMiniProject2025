package com.example.androidminiproject2025.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidminiproject2025.R;
import com.example.androidminiproject2025.activities.MenuActivity;
import com.example.androidminiproject2025.databinding.FragmentMainMenuBinding;
import com.example.androidminiproject2025.databinding.FragmentResultBinding;


public class ResultFragment extends Fragment {

    private boolean hasWon;
    private FragmentResultBinding binding;
    public ResultFragment(boolean hasWon) {
        this.hasWon = hasWon;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = com.example.androidminiproject2025.databinding.FragmentResultBinding.inflate(inflater, container, false);

        if(hasWon) {
            binding.resultText.setText("You have won !");
        } else {
            binding.resultText.setText("You have lost !");
        }
        intitializeBackToMainMenuButton();
        return binding.getRoot();
    }

    private void intitializeBackToMainMenuButton() {
        binding.backToMainMenu.setOnClickListener(view -> {
            MenuActivity activity = (MenuActivity) getActivity();
            activity.switchToMainMenuFragment();
        });
    }
}