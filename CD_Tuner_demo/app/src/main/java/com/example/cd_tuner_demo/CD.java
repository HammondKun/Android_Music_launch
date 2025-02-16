package com.example.cd_tuner_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cd_tuner_demo.databinding.ActivityCdBinding;

import java.util.List;


public class CD extends AppCompatActivity {

    private ActivityCdBinding binding;
    private CdViewModel CdVm;
    private MusicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//----------------------------绑定ViewModel---------------------------
        binding = ActivityCdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CdVm = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(CdViewModel.class);
        //CdVm = new ViewModelProvider(this).get(KunViewModel.class);
        binding.setCdVm(CdVm);
        binding.setLifecycleOwner(this);

        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//---------------------------监听事件----------------------------------
        //点击事件
        CdVm.getNavigateToMain_CD().observe(this, shouldNavigate -> {
            if (shouldNavigate != null && shouldNavigate) {
                startActivity(new Intent(CD.this, MainActivity.class));
            }
        });

        //监听LiveData，更新song,album,artist
        CdVm.library.getSong().observe(this, songName -> {
            TextView songTextView = findViewById(R.id.song_name);
            songTextView.setText(songName);
        });

        CdVm.library.getAlbum().observe(this, albumName -> {
            TextView albumTextView = findViewById(R.id.album_name);
            albumTextView.setText(albumName);
        });

        CdVm.library.getArtist().observe(this, artistName -> {
            TextView artistTextView = findViewById(R.id.artist_name);
            artistTextView.setText(artistName);
        });

        //按钮返回点击事件
        findViewById(R.id.back_to_main_cd).setOnClickListener(v -> CdVm.onButtonClick_cd_to_main());

//--------------------------------------设置进度条---------------------------

        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView seekBarCurrentTime = findViewById(R.id.seekBarCurrentTime);
        TextView seekBarTotalTime = findViewById(R.id.seekBarTotalTime);

        // 监听总时长
        CdVm.getTotalDuration().observe(this, duration -> {
            seekBar.setMax(duration);
            seekBarTotalTime.setText(formatTime(duration));
        });

// 监听进度变化
        CdVm.getProgress().observe(this, progress -> {
            seekBar.setProgress(progress);
            seekBarCurrentTime.setText(formatTime(progress));
            CdVm.saveProgress(progress);
        });


// 监听用户拖动进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    CdVm.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

//-------------------------载入MusicAdapter-----------------------------

// 假设 Library 类中有获取音乐列表的方法
        List<Music> musicList = CdVm.library.getMusicList();

// 创建适配器并设置点击事件
        adapter= new MusicAdapter(musicList, songName -> {
            CdVm.setCurrentSongByName(songName); // 通过 songName 修改 currentSongIndex
            CdVm.library.loadMusic(CdVm.library.currentSongIndex); // 加载选中的歌曲
            CdVm.library.playOrPauseMusic();  // 播放选中的歌曲
        });

//设置切换图片
        ImageView imageWhichShouldSeen = findViewById(R.id.cd_music);
        CdVm.library.getArtist().observe(this, name->{
            imageWhichShouldSeen.setImageResource(CdVm.library.getMusicPicture());
        });

// 设置适配器
        RecyclerView recyclerView = findViewById(R.id.cd_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // 设置布局管理器
        recyclerView.setAdapter(adapter);

        CdVm.library.getSong().observe(this, songName -> {
            adapter.setCurrentPlayingSong(songName); // 传递正在播放的歌曲名
        });




//点击时间（图片可视化）
        ImageView autoPlayMode = findViewById(R.id.auto_opened);
        ImageView unAutoPlayMode = findViewById(R.id.auto_closed);

        ImageView playSingleMusic = findViewById(R.id.boardstyle_single);
        ImageView playListMusic = findViewById(R.id.boardstyle_list);

        //自动播放状态
        CdVm.library.getIsAutoPlayEnabled().observe(this, isEnable ->{
            autoPlayMode.setVisibility(isEnable? View.VISIBLE : View.INVISIBLE);
            unAutoPlayMode.setVisibility(isEnable? View.INVISIBLE : View.VISIBLE);
        });

        autoPlayMode.setOnClickListener( v -> CdVm.library.toggleAutoPlay());
        unAutoPlayMode.setOnClickListener( v -> CdVm.library.toggleAutoPlay());

        //播放模式
            CdVm.library.getPlayMode().observe(this, isListMode ->{
            playSingleMusic.setVisibility(isListMode ? View.INVISIBLE : View.VISIBLE);
            playListMusic.setVisibility(isListMode ? View.VISIBLE : View.INVISIBLE);
        });

        playListMusic.setOnClickListener( v -> CdVm.library.togglePlayMode());
        playSingleMusic.setOnClickListener( v -> CdVm.library.togglePlayMode());



    }
//切换界面时候保存
    @Override
    protected void onPause() {
        super.onPause();
        int currentProgress = CdVm.getProgress().getValue() != null ? CdVm.getProgress().getValue() : 0;
        CdVm.saveProgress(currentProgress); // 存储当前播放进度
    }


        private String formatTime(int millis) {
            int minutes = (millis / 1000) / 60;
            int seconds = (millis / 1000) % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }