package com.example.videoplayers.mediaplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.example.videoplayers.R;

public class MediaPlayerActivity extends AppCompatActivity {
    private RelativeLayout rlPlayer;
    private final String url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
    private PlayVideo playVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        init();
    }

    private void init() {
        rlPlayer = (RelativeLayout) findViewById(R.id.rl_player);
        playVideo = new PlayVideo(this,url);
        rlPlayer.addView(playVideo.getVideoView());
    }

    @Override
    public void onDestroy() {    //退出停止视频播放
        super.onDestroy();
        try {
            if (null != playVideo) {
                playVideo.mediaPlayer.release();
                playVideo.mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(playVideo.getReceiver());
    }
}
