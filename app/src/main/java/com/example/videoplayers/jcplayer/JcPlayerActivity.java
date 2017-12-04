package com.example.videoplayers.jcplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.videoplayers.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JcPlayerActivity extends AppCompatActivity {
    private JCVideoPlayerStandard playerStandard;
    private String videoUrl = "https://key002.ku6.com/xy/d7b3278e106341908664638ac5e92802.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_player);

        init();
    }

    private void init() {
        playerStandard = (JCVideoPlayerStandard) findViewById(R.id.playerstandard);
        playerStandard.setUp(videoUrl,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"追龙");
        playerStandard.startVideo();
    }
}
