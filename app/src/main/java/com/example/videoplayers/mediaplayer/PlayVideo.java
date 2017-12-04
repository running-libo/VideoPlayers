package com.example.videoplayers.mediaplayer;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.videoplayers.R;
import java.io.IOException;
import java.util.HashMap;

public class PlayVideo {
    private Context context;
    private View videoView;
    public MediaPlayer mediaPlayer;
    private boolean isDisplay = true;
    private ProgressBroadCast progressBroadCast;
    private SurfaceView surfaceView;
    private Button btPlay;
    private SeekBar seekBar;
    private TextView tvTime;
    private ImageView ivCover;
    private LinearLayout relaVideo,llLoading;
    /** 视频播放资源地址 */
    private String mediaUrl;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(null != msg){
                ivCover.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public PlayVideo(Context context,String mediaUrl){
        this.context = context;
        this.mediaUrl = mediaUrl;
        videoView = LayoutInflater.from(context).inflate(R.layout.custom_videoplayer,null);
        initView(videoView);
        event();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView(View view){
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
        btPlay = (Button) view.findViewById(R.id.btn_play);
        seekBar = (SeekBar) view.findViewById(R.id.play_seekbar);
        tvTime = (TextView) view.findViewById(R.id.tv_video_time);
        relaVideo = (LinearLayout) view.findViewById(R.id.rela_video);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_video_loading);
        ivCover = (ImageView) view.findViewById(R.id.iv_videoplayer_cover);
        MyClickListener listener = new MyClickListener();
        surfaceView.setOnClickListener(listener);
        btPlay.setOnClickListener(listener);

        progressBroadCast = new ProgressBroadCast();
        context.registerReceiver(progressBroadCast, new IntentFilter("play"));
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new HolderCallBack());

        avaterThread();
    }

    private void event() {
        //seekbar调节进度
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {
                if(null != mediaPlayer)
                    mediaPlayer.seekTo(seekBar2.getProgress());
            }
        });
    }

    public View getVideoView(){
        return videoView;
    }

    public BroadcastReceiver getReceiver(){
        return progressBroadCast;
    }

    private void avaterThread(){
        new Thread(){
            @Override
            public void run() {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                if(Build.VERSION.SDK_INT >= 14){    //需加入api判断，不然会报IllegalArgumentException
                    mediaMetadataRetriever.setDataSource(mediaUrl,new HashMap<String, String>());
                }else{
                    mediaMetadataRetriever.setDataSource(mediaUrl);
                }
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000,MediaMetadataRetriever.OPTION_CLOSEST);
                Message message = new Message();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

        class MyClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.surfaceview:
                        if (isDisplay) {
                            relaVideo.setVisibility(View.GONE);
                        } else {
                            relaVideo.setVisibility(View.VISIBLE);
                        }
                        isDisplay = !isDisplay;
                        break;
                    case R.id.btn_play:
                        if (mediaPlayer == null) {
                            netWorkState();
                        } else {
                            //播放和暂停切换
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                btPlay.setBackgroundResource(R.mipmap.video_btn_pause);
                            } else {
                                mediaPlayer.start();
                                ivCover.setVisibility(View.GONE);
                                new ProgressThread().start();
                                btPlay.setBackgroundResource(R.mipmap.video_btn_start);
                            }
                        }
                        break;
                }
            }
        }

            /**
             * 播放视频，先判断网络，是流量就要提示用户
             */
            private void netWorkState() {
                mediaPlayer = new MediaPlayer();
                new PrepareThread().start();
                llLoading.setVisibility(View.VISIBLE);

            }

            class HolderCallBack implements SurfaceHolder.Callback {

                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            }

            class MyPrepareListener implements MediaPlayer.OnPreparedListener {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (null != mediaPlayer) {
                        llLoading.setVisibility(View.GONE);
                        mediaPlayer.start();
                        new ProgressThread().start();
                    }
                }
            }

            class PrepareThread extends Thread {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {
                    super.run();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(mediaUrl);
                        mediaPlayer.setDisplay(surfaceView.getHolder());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MyPrepareListener());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             * 时间进度广播
             */
            public class ProgressBroadCast extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                    int curPosition = intent.getIntExtra("position", 0);
                    int maxLen = intent.getIntExtra("max", 0);
                    seekBar.setProgress(curPosition);
                    seekBar.setMax(maxLen);

                    setTime(curPosition, maxLen);
                }

                /**
                 * 秒转化为00:00形式
                 * @param curPosition
                 * @param maxLen
                 */
                private void setTime(int curPosition, int maxLen) {
                    int cm = curPosition / 1000 / 60;
                    int cs = curPosition / 1000 % 60;
                    int mm = maxLen / 1000 / 60;
                    int ms = maxLen / 1000 % 60;
                    StringBuilder builder = new StringBuilder();
                    builder.append(cm / 10).append(cm % 10).append(":")
                            .append(cs / 10).append(cs % 10).append("/")
                            .append(mm / 10).append(mm % 10).append(":")
                            .append(ms / 10).append(ms % 10);
                    tvTime.setText(builder.toString());
                }

            }

            /**
             * 发送播放进度线程
             */
            class ProgressThread extends Thread {
                @Override
                public void run() {
                    while (null != mediaPlayer && mediaPlayer.isPlaying()) {
                        int currentProgress = mediaPlayer.getCurrentPosition();
                        int maxLen = mediaPlayer.getDuration();
                        //每隔一秒发送一次播放进度
                        Intent progressIntent = new Intent("play");
                        progressIntent.putExtra("position", currentProgress);
                        progressIntent.putExtra("max", maxLen);
                        context.sendBroadcast(progressIntent);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

}
