package com.example.videoplayers.videoview;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.videoplayers.R;

public class VideoViewActivity extends AppCompatActivity {
    private VideoView videoView;
    public final String videoUrl = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        init();
    }

    private void init(){
        videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setVideoPath(videoUrl);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

    }
}
