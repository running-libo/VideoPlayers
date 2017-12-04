package com.example.videoplayers.vitamioplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.videoplayers.R;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView tvBuffer,tvNet;
    private String videoUrl = "https://key003.ku6.com/movie/1af61f05352547bc8468a40ba2d29a1d.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamio_player);

        init();

    }

    private void init() {
        videoView = (VideoView) findViewById(R.id.videoview);
        tvBuffer = (TextView) findViewById(R.id.tv_buffer);
        tvNet = (TextView) findViewById(R.id.tv_net);

        if (Vitamio.isInitialized(this)) {
            videoView.setVideoPath(videoUrl);
            videoView.setMediaController(new MediaController(this));
            videoView.start();
        }

        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                tvBuffer.setText("已缓冲：" + percent + "%");
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        tvBuffer.setVisibility(View.VISIBLE);
                        tvNet.setVisibility(View.VISIBLE);
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        tvBuffer.setVisibility(View.GONE);
                        tvNet.setVisibility(View.GONE);
                        mp.start();
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        tvNet.setText("网速:" + extra + "kb/s");
                        break;
                }
                return true;
            }
        });

    }
}
