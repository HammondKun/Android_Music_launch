package com.example.cd_tuner_demo;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cd_tuner_demo.databinding.ActivityTunerBinding;

import java.util.Arrays;
import java.util.List;

public class Tuner extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TunerAdapter adapter;
    private ActivityTunerBinding binding;
    private TunerViewModel TunerVm;
    private TextView frequencyTextView;
    private AnimationDrawable frameAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTunerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//------------------------------------初始化ViewModel------------------------------------------------
        TunerVm = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(TunerViewModel.class);
        binding.setTunerVm(TunerVm);
        binding.setLifecycleOwner(this);

//-----------------------------------Tuner界面跳转回Main----------------------------------------------
        TunerVm.getNavigateToMain_Tuner().observe(this,shouldNavigate ->{
            if(shouldNavigate != null&&shouldNavigate){
                startActivity(new Intent(Tuner.this, MainActivity.class));
            }
        });

        findViewById(R.id.back_to_main_tuner).setOnClickListener(v -> TunerVm.onButtonClick_tuner_to_main());
//----------------------------------导入TunerAdapter-------------------------------------------------

        TunerVm.library.initAudioList();;

        RecyclerView recyclerView = findViewById(R.id.tuner_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TunerAdapter(TunerVm.library.getAudioList(),TunerVm.library);
        recyclerView.setAdapter(adapter);

//-----------------------------------监听LiveData----------------------------------------------------
    frequencyTextView = findViewById(R.id.frequceny_tuner);
    TunerVm.library.getFrequency().observe(this, newFrequency -> {
        frequencyTextView.setText(newFrequency);
    });

    TunerVm.getSelectedAudio().observe(this, selectedAudio -> {
        if (selectedAudio != null) {
            frequencyTextView.setText(selectedAudio.getFrequency());
            }
        });
//-----------------------------------------帧动画播放-------------------------------------------------
        ImageView imageView = findViewById(R.id.frame_view);

        imageView.setBackgroundResource(R.drawable.frame_animation);
        frameAnimation = (AnimationDrawable) imageView.getBackground();

        frameAnimation.start();
    }

}