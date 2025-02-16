package com.example.cd_tuner_demo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class CdViewModel extends AndroidViewModel {

    public Library library;
    public LiveData<Integer> getProgress(){
        return library.getProgress();
    }

    public LiveData<Integer> getTotalDuration() {
        return library.getTotalDuration();
    }

    public void seekTo(int progress) {
        library.seekTo(progress);
    }

    //cd界面跳转Main
    private final MutableLiveData<Boolean> navigateToMain_CD = new MutableLiveData<>();
    public LiveData<Boolean> getNavigateToMain_CD(){
        return navigateToMain_CD;
    }
    public void onButtonClick_cd_to_main(){
        navigateToMain_CD.setValue(true);
    }


//------------------------------初始化---------------------------
    public CdViewModel(@NonNull Application application){
        super(application);
        library = Library.getInstance(application.getApplicationContext());
    }

    public void setCurrentSongByName(String songName) {
        library.setCurrentSongByName(songName);
    }


    public void saveProgress(int progress) {
        library.currentProgress.setValue(progress);
    }

}
