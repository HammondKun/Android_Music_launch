package com.example.cd_tuner_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.cd_tuner_demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; //View Binding用于访问xml组件
    private MainViewModel mVm;   //用于管理数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //绑定布局文件
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //绑定activity.xml,b
        setContentView(binding.getRoot());

        //初始化ViewModel
        mVm = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(MainViewModel.class);

        //创建kunViewModel,将其生命周期和Activity绑定

        //把xml中的‘vm’变量绑定到‘mVm’对象
        binding.setMainVm(mVm);

        //让LiveData自动更新UI，让XML知道Activity的生命周期
        binding.setLifecycleOwner(this);

        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //点击事件
        mVm.getNavigateToCdScreen().observe(this,shouldNavigate ->{
            if(shouldNavigate != null && shouldNavigate){
                startActivity(new Intent(MainActivity.this,CD.class));
            }
        });

        mVm.getNavigateToTunerScreen().observe(this,shouldNavigate ->{
            if (shouldNavigate != null && shouldNavigate){
                startActivity(new Intent(MainActivity.this,Tuner.class));
            }
        });


        findViewById(R.id.cd_view).setOnClickListener( v -> mVm.onButtonClick_cd());
        findViewById(R.id.tuner_view).setOnClickListener( v -> mVm.onButtonClick_tuner());

        //监听song,album,artist并更新UI
        mVm.library.getSong().observe(this,songName -> {
            TextView songTextView = findViewById(R.id.song_name);
            songTextView.setText(songName);
        });

        mVm.library.getAlbum().observe(this,albumName -> {
            TextView albumTextView = findViewById(R.id.album_name);
            albumTextView.setText(albumName);
        });

        mVm.library.getArtist().observe(this,artistName -> {
            TextView artistTextView = findViewById(R.id.artist_name);
            artistTextView.setText(artistName);
        });

        mVm.library.getFrequency().observe(this,newFrequency ->{
            TextView frequencyTextView = findViewById(R.id.frequceny);
            frequencyTextView.setText(newFrequency);
        });

        //设置切换图片
        ImageView imageWhichShouldSeen = findViewById(R.id.main_music);
        mVm.library.getArtist().observe(this, name->{
            imageWhichShouldSeen.setImageResource(mVm.library.getMusicPicture());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 获取上次播放进度
        mVm.library.getCurrentProgress().observe(this, progress -> {
            if (progress > 0) {
                mVm.library.seekTo(progress); // 直接跳转到上次播放位置
            }
        });
    }

}