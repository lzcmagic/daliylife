package com.lzc.daliylife.relax;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseActivity;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by lzc on 2017/9/12.
 *
 */

public class RelaxDetailActivity extends BaseActivity {
    @BindView(R.id.vv_video)
    VideoView videoView;
    private  MediaPlayer mediaPlayer;
    private MediaController mediaController;
    String url;
    @Override
    public void initUI() {
        url = getIntent().getStringExtra("url");
        mediaController=new MediaController(this);
        new ThumbTask().execute(url);

    }

    private class ThumbTask extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //获取网络视频
            retriever.setDataSource(url, new HashMap<String, String>());
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
            return Float.parseFloat(width) > Float.parseFloat(height);
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            if (flag){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer=mp;
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                RelaxDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("lala", "onConfigurationChanged: ");
        if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            //竖屏
        }else if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            //横屏
        }else{

        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public int getResId() {
        return R.layout.activity_relax_detail;
    }
}
