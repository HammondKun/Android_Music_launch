package com.example.cd_tuner_demo;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Library {
    private static Library instance;
    // 单例,全局唯一
    public static synchronized Library getInstance(Context context) {
        if (instance == null) {
            instance = new Library(context.getApplicationContext()); // 传入 ApplicationContext
        }
        return instance;
    }

    // 私有hua构造函数
    private Library(Context context) {
        this.context = context;
    }
    //------------------------------------一般变量区------------------------------------------------------
    private Context context;
    private final MutableLiveData<String> cd = new MutableLiveData<>("CD");
    public LiveData<String> getCd(){
        return cd;
    }
    private final MutableLiveData<String> tuner = new MutableLiveData<>("Tuner");
    public LiveData<String> getTuner(){
        return tuner;
    }
//---------------------------------Music模块变量区----------------------------------------------------
    private final  MutableLiveData<String> Song_name = new MutableLiveData<>("{Song name}}");
    private final  MutableLiveData<String> Album_name = new MutableLiveData<>("{Album name}}");
    private final  MutableLiveData<String> Artist_name = new MutableLiveData<>("Mozart");

    public LiveData<String> getSong(){
        return Song_name;
    }
    public void setSong_name(String newSong) {
        Song_name.setValue(newSong);
    }
    public LiveData<String> getAlbum(){
        return Album_name;
    }
    public void setAlbum_name(String newAlbum) {
        Album_name.setValue(newAlbum);
    }
    public LiveData<String> getArtist(){
        return Artist_name;
    }
    public void setArtist_name(String newArtist) {
        Artist_name.setValue(newArtist);
    }

    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsPlaying() {return isPlaying;}
    public MediaPlayer mediaPlayer;
    public int currentSongIndex = 0;
    private final List<Music> musicList = new ArrayList<>();
    public List<Music> getMusicList(){
        return musicList;
    }
    private final MutableLiveData<Integer> progress = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalDuration = new MutableLiveData<>(0);
    public LiveData<Integer> getProgress(){
        return progress;
    }
    public void setProgress(Integer newProgress) {
       progress.setValue(newProgress);
    }
    public LiveData<Integer> getTotalDuration(){
        return totalDuration;
    }
    private final Handler handler = new Handler();
    private Runnable progressUpdater;

    // 标志变量，确保只初始化一次
    private boolean isInitialized = false;


    // 通过 song_name 查找索引并更新 currentSongIndex
    public void setCurrentSongByName(String songName) {
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getSongName().equals(songName)) {
                currentSongIndex = i ;
                return;
            }
        }
    }

    public final MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);
    // 获取播放进度
    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    //切换播放模式

//----------------------------------Tuner功能变量区---------------------------------------------------
    private final MutableLiveData<String> broadcast_name = new MutableLiveData<>("Broadcast Model");
    public LiveData<String> getBroadcast(){
        return broadcast_name;
    }
    private final MutableLiveData<String> frequency = new MutableLiveData<>("Please select channel");
    private final MutableLiveData<String> band = new MutableLiveData<>("AM");
    public LiveData<String> getBand(){
        return band;
    }
    public LiveData<String> getFrequency(){
        return frequency;
    }
    public void setFrequency(String newFrequency){
        frequency.setValue(newFrequency);
    }
    private final List<Audio> audioList = new ArrayList<>();
    public List<Audio> getAudioList(){
        return audioList;
    }
    private final MutableLiveData<Audio> selectedAudio = new MutableLiveData<>();
    public LiveData<Audio> getSelectedAudio() {
        return selectedAudio;
    }
    public void setSelectedAudio(Audio audio) {
        selectedAudio.setValue(audio);
        setFrequency(audio.getFrequency()); // 同时更新频率
    }
    private boolean isInitialized_audio = false;
    //-----------------------------------CD播放功能------------------------------------------------------
    // Initialize the music list
    public void initMusicList() {
        if (isInitialized) {
            return; // 如果已经初始化，直接返回
        }
        musicList.add(new Music("g_nightmusic_5","night music","Chopin",R.raw.g_nightmusic_5));
        musicList.add(new Music("fate_symphony_5", "Symphony", "Beethoven", R.raw.fate_symphony_5));
        musicList.add(new Music("zupit_symphony_41", "Symphony", "Mozart", R.raw.zupit_symphony_41));
        musicList.add(new Music("downd_nightmusic_16","night music","Chopin",R.raw.downd_night_16));
        musicList.add(new Music("f_violin-5","violin","Beethoven",R.raw.f_violin_5));
        musicList.add(new Music("fegaro_wedding_6","wedding","Mozart",R.raw.fegaro_wedding_6));

        isInitialized = true; // 设置初始化标志为 true
        Log.d("Library", "Music list size: " + musicList.size()); // 打印 musicList 的大小
    }

    // Play or Pause music
    public void playOrPauseMusic(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                isPlaying.setValue(false);
            }else{
                mediaPlayer.start();
                isPlaying.setValue(true);
                startProgressUpdater();
            }
        }
    }
    //播放下一首
    public void nextSong(){
        currentSongIndex = (currentSongIndex + 1 ) % musicList.size();
        loadMusic(currentSongIndex);
        playOrPauseMusic();
    }

    // bofang 上一首
    public void previousSong() {
        currentSongIndex = (currentSongIndex - 1 + musicList.size()) % musicList.size();
        loadMusic(currentSongIndex);
        playOrPauseMusic();
    }
    //重复播放本首歌（单曲循环）
    public void replaySong(){
        loadMusic(currentSongIndex);
        playOrPauseMusic();
    }

    // Load a song and start the progress of seekbar
    public void loadMusic(int index){
        if (index >=0 && index < musicList.size()){
            Music music = musicList.get(index);
            Song_name.setValue(music.getSongName());
            Album_name.setValue(music.getAlbumName());
            Artist_name.setValue(music.getArtistName());

            if (mediaPlayer != null){
                mediaPlayer.release();
            }
            //使用getApplication()作为context
            mediaPlayer = MediaPlayer.create(context,music.getSongResId());
            isPlaying.setValue(false);

            //获取歌曲总时长和现在事件初始化
            mediaPlayer.setOnPreparedListener(mp -> {
                totalDuration.setValue(mp.getDuration());
                progress.setValue(currentProgress.getValue());
                startProgressUpdater();
            });

            // **绑定歌曲播放完成的监听器，自动播放下一首**
            mediaPlayer.setOnCompletionListener(mp -> {
                if (Boolean.TRUE.equals(getIsAutoPlayEnabled().getValue())) {
                    if (Boolean.TRUE.equals(getPlayMode().getValue())) {
                        nextSong(); // **列表循环**
                    } else {
                        replaySong(); // **单曲循环**
                    }
                }
            });

        }
    }

    private void startProgressUpdater() {
        if (progressUpdater != null) {
            handler.removeCallbacks(progressUpdater); // 确保不会有多个任务在运行
        }

        progressUpdater = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    progress.postValue(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 200); // 每 200ms 更新一次
                }
            }
        };
        handler.post(progressUpdater);
    }

    //设置进度
    public void seekTo(int progress){
        if (mediaPlayer != null){
            mediaPlayer.seekTo(progress);
        }
    }

    // 更新音乐家名字，并自动更新对应的图片
    public int getMusicPicture(){
        String name = Artist_name.getValue();
        if (name == null) return R.drawable.music_launch;

        switch (name){
            case "Mozart":
                return R.drawable.mozart;
            case "Beethoven":
                return R.drawable.beethoven;
            case "Chopin":
                return R.drawable.chopin;
            default:
                return R.drawable.music_launch;
        }
    }
//--------------------------------------Tuner功能区--------------------------------------------------
    public void initAudioList(){
    if(isInitialized_audio){
        return;
    }
    audioList.add(new Audio("Audio 1","88.5MHz"));
    audioList.add(new Audio("Audio 2","99.2MHz"));
    audioList.add(new Audio("Audio 3","103.3MHz"));
    audioList.add(new Audio("Audio 4","105.5MHz"));
    audioList.add(new Audio("Audio 5","112.5MHz"));
    audioList.add(new Audio("Audio 6","118.2MHz"));


    isInitialized_audio = true;
    }

    public void changeBand(){
        if("AM".equals(band.getValue())) {
            band.setValue("FM");
        }else {
            band.setValue("AM");
        }
    }




//歌曲播放模式
    private final MutableLiveData<Boolean> isAutoPlayEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> playMode = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsAutoPlayEnabled() {
        return isAutoPlayEnabled;
    }
    public LiveData<Boolean> getPlayMode() {
        return playMode;
    }
//切换开关
    public void toggleAutoPlay(){
        isAutoPlayEnabled.setValue(!Boolean.TRUE.equals(isAutoPlayEnabled.getValue()));
    }
    public void togglePlayMode(){
        if (!Boolean.TRUE.equals(isAutoPlayEnabled.getValue())) {
            Toast.makeText(context, "Please open auto play!", Toast.LENGTH_SHORT).show();
        } else {
            playMode.setValue(!Boolean.TRUE.equals(playMode.getValue()));
        }
    }

}
