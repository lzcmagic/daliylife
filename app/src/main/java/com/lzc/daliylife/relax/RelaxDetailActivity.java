package com.lzc.daliylife.relax;

import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by lzc on 2017/9/12.
 *
 */

public class RelaxDetailActivity extends BaseActivity {
    @BindView(R.id.vv_video)
    VideoView videoView;
    private MediaController mediaController;
    String url;
    @Override
    public void initUI() {
        url = getIntent().getStringExtra("url");
        mediaController=new MediaController(this);
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
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                RelaxDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public int getResId() {
        return R.layout.activity_relax_detail;
    }
}
