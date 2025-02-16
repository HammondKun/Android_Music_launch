package com.example.cd_tuner_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class TunerAdapter extends RecyclerView.Adapter<TunerAdapter.TunerViewHolder> {

    private final List<Audio> audioList;
    private final Library library;

    public TunerAdapter(List<Audio> audioList, Library library) {
        this.audioList = audioList;
        this.library = library;
    }

    @NonNull
    @Override
    public TunerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tuner, parent, false);
        return new TunerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TunerViewHolder holder, int position) {
        Audio audio = audioList.get(position);
        holder.tunertextView.setText(audio.getBroadcast() + " - " + audio.getFrequency());

        // 监听选中状态变化，更新 UI
        library.getSelectedAudio().observeForever(selectedAudio -> {
            if (selectedAudio != null && selectedAudio.getFrequency().equals(audio.getFrequency())) {
                holder.itemView.setBackgroundColor(Color.YELLOW);
                holder.tunertextView.setTextColor(Color.BLUE);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT); //其他项恢复原样
                holder.tunertextView.setTextColor(Color.WHITE);
            }
        });

        // 点击 item，设置为选中项
        holder.itemView.setOnClickListener(v -> {
            library.setSelectedAudio(audio);
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public static class TunerViewHolder extends RecyclerView.ViewHolder {
        TextView tunertextView;
        public TunerViewHolder(View itemView) {
            super(itemView);
            tunertextView = itemView.findViewById(R.id.TunerTextView);
        }
    }
}
