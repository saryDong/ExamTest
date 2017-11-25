package com.example.abu.examtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    VideoView videoView;
    Button player,stopplayer,replayer,getVideo;
    TextView textView,newView;
    Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initView();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }

        mUri = Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.aa );
        videoView.setVideoURI(mUri);
    }
    private void initView() {
        videoView=findViewById(R.id.vidioView);
        player=findViewById(R.id.player);
        stopplayer=findViewById(R.id.stop);
        replayer=findViewById(R.id.replay);
        textView=findViewById(R.id.videoLegth);
        newView=findViewById(R.id.newView);
        getVideo=findViewById(R.id.getVideo);

        videoView.setOnClickListener(this);
        player.setOnClickListener(this);
        stopplayer.setOnClickListener(this);
        replayer.setOnClickListener(this);
        newView.setOnClickListener(this);
        getVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player:
                // videoView.setMediaController(mediaController);
                videoView.start();
                break;
            case R.id.stop:
                if (videoView.isPlaying())
                videoView.pause();
                break;
            case R.id.replay:
                videoView.resume();
                break;
            case R.id.newView:
                if (videoView.isPlaying()){
                    videoView.stopPlayback();
                }
                Intent intent=new Intent(getApplicationContext(),NewActivity.class);
                int position=videoView.getCurrentPosition();
                intent.putExtra("videoUrl",mUri+"");
                intent.putExtra("position",position+"");
                startActivity(intent);
                break;
            case R.id.getVideo:
                Intent itVideo = new Intent(Intent.ACTION_GET_CONTENT);
                itVideo.setType("video/*");
                startActivityForResult(itVideo,12);
            default:
                    break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()){
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView.isPlaying()){
            videoView.pause();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case 12:
                    //获取用户选取的Uri
                    Log.i("TAG", data.getData().toString());
                    mUri = convertUri(data.getData());
                    videoView.setVideoURI(mUri);
                    //返回mUri的最后一段文字，即文件名部分
                    break;

            }
        }
    }
    Uri convertUri(Uri uri){
        //判断如果是以content开头
        if(uri.toString().substring(0,7).equals("content")){
            //声明要查询的字段
            String[] colName ={MediaStore.MediaColumns.DATA};
            //对uri进行查询；对共享库的查询都采用getContentResolver()方法
            Cursor cusor = getContentResolver().query(uri,colName,null,null,null);
            //移动查询结果的第一个记录
            cusor.moveToFirst();
            //将路径转换为"file://"开头的Uri
            mUri = Uri.parse("file://" + cusor.getString(0));
            //关闭查询结构
            cusor.close();
        }
        return uri;//返回Uri对象
    }

}
