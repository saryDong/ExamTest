package com.example.abu.examtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class NewActivity extends AppCompatActivity {
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置手机不休眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置隐藏标题栏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new);
        videoView=findViewById(R.id.new_videoview);

        Intent intent=getIntent();
        String path=intent.getStringExtra("videoUrl");
        String position=intent.getStringExtra("position");
        Log.i("TAG",position);
        videoView.setVideoPath(path);
        //videoView.seekTo(Integer.parseInt(position));
        MediaController controller=new MediaController(this);
        videoView.setMediaController(controller);


    }
}
