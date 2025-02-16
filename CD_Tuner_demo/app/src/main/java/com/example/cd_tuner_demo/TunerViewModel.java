package com.example.cd_tuner_demo;

import android.app.Application;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TunerViewModel extends AndroidViewModel{

    public final Library library;
    //Tuner界面跳转Main
    private final MutableLiveData<Boolean> navigateToMain_Tuner = new MutableLiveData<>();
    public LiveData<Boolean> getNavigateToMain_Tuner(){
        return navigateToMain_Tuner;
    }
    public void onButtonClick_tuner_to_main(){
        navigateToMain_Tuner.setValue(true);
    }

    public LiveData<String> getFrequency(){
        return library.getFrequency();
    }
    public void setFrequency(String frequency){
        library.setFrequency(frequency);
    }
    public LiveData<Audio> getSelectedAudio() {
        return library.getSelectedAudio();
    }
    public void setSelectedAudio(Audio audio) {
        library.setSelectedAudio(audio);
    }

    public TunerViewModel(@Nullable Application application){
        super(application);
        library = Library.getInstance(application.getApplicationContext());
    }


}
