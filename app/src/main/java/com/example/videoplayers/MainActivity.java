package com.example.videoplayers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.videoplayers.jcplayer.JcPlayerActivity;
import com.example.videoplayers.mediaplayer.MediaPlayerActivity;
import com.example.videoplayers.videoview.VideoViewActivity;
import com.example.videoplayers.vitamioplayer.VitamioPlayerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void videoview(View view){
        startActivity(new Intent(this,VideoViewActivity.class));
    }

    public void mediaplay(View view){
        startActivity(new Intent(this, MediaPlayerActivity.class));
    }

    public void vitamio(View view){
        startActivity(new Intent(this, VitamioPlayerActivity.class));
    }

    public void jcPlayer(View view){
        startActivity(new Intent(this, JcPlayerActivity.class));
    }
}
