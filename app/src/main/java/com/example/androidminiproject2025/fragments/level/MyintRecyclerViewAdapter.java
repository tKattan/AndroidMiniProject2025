package com.example.androidminiproject2025.fragments.level;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidminiproject2025.activities.GameActivity;
import com.example.androidminiproject2025.databinding.FragmentLevelSelectionBinding;

import java.util.List;


public class MyintRecyclerViewAdapter extends RecyclerView.Adapter<MyintRecyclerViewAdapter.ViewHolder> {

    private final List<Integer> mValues;

    public MyintRecyclerViewAdapter(List<Integer> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentLevelSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Integer levelNumber = mValues.get(position);
        holder.levelNumber.setText(String.valueOf(levelNumber));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final TextView levelNumber;

        public ViewHolder(FragmentLevelSelectionBinding binding) {
            super(binding.getRoot());
            levelNumber = binding.levelNumber;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, GameActivity.class);
            intent.putExtra("LEVEL_NUMBER", "7000");
            context.startActivity(intent);
        }
    }
}