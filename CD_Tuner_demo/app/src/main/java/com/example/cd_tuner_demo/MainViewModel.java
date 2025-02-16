package com.example.cd_tuner_demo;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel{
    public Library library; //引用library类

    //Main跳转到CD界面
    private final MutableLiveData<Boolean> navigateToCdScreen = new MutableLiveData<>();
    public LiveData<Boolean> getNavigateToCdScreen(){
        return navigateToCdScreen;
    }
    public void onButtonClick_cd(){
        navigateToCdScreen.setValue(true);
    }

    //Main跳转到Tuner界面
    private final MutableLiveData<Boolean> navigateToTunerScreen = new MutableLiveData<>();
    public  LiveData<Boolean> getNavigateToTunerScreen(){
        return navigateToTunerScreen;
    }
    public void onButtonClick_tuner(){
        navigateToTunerScreen.setValue(true);
    }


    // 初始化音乐列表
    public MainViewModel(@NonNull Application application){
        super(application);
        library = Library.getInstance(application.getApplicationContext());
        library.initMusicList();
        library.loadMusic(library.currentSongIndex);
        Log.d("MainViewModel","initMusicList() is working");
    }

//-----------------------------------------清理资源--------------------------------------------------
//    @Override
//    protected void onCleared(){
//        super.onCleared();
//        library.releasePlayer();//释放
//    }

}
