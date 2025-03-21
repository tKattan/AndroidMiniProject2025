package com.example.androidminiproject2025.fragments.level;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidminiproject2025.databinding.FragmentLevelSelectionListBinding;

import java.util.ArrayList;
import java.util.List;


public class LevelSelectionFragment extends Fragment {

    private FragmentLevelSelectionListBinding binding;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private List<Integer> levels;

    public LevelSelectionFragment() {
        levels = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            levels.add(i);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLevelSelectionListBinding.inflate(inflater, container, false);

        View view = binding.list;
        MyintRecyclerViewAdapter levelAdapter = new MyintRecyclerViewAdapter(this.levels);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(levelAdapter);
        }
        return binding.getRoot();
    }
}