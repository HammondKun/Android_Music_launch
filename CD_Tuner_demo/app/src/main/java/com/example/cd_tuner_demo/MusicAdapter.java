package com.example.cd_tuner_demo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList;
    private OnItemClickListener listener;
    private String currentPlayingSong = "";

    //补充构造函数
    public MusicAdapter(List<Music> musicList, OnItemClickListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String songName);
    }

    //更新当前播放的歌曲名称并刷新 UI
    public void setCurrentPlayingSong(String songName) {
        this.currentPlayingSong = songName;
        notifyDataSetChanged(); // 刷新 RecyclerView
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.songName.setText(music.getSongName());

        if (music.getSongName().equals(currentPlayingSong)) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
            holder.songName.setTextColor(Color.BLUE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); //其他项恢复原样
            holder.songName.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(music.getSongName()); // 传递 songName
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView songName;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songNameTextView);
        }
    }
}
