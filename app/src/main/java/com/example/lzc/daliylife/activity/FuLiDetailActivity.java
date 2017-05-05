package com.example.lzc.daliylife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.framework.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by lzc on 2017/3/17.
 */

public class FuLiDetailActivity extends AppCompatActivity {
    public static final String TRANSIT_PIC = "fuli_picture_detail";
    private String url;
    private String date;
    @BindView(R.id.aiv_fuli_detail)
    ImageView mImageView;
    @BindView(R.id.fuliContainer)
    CoordinatorLayout mLayout;
    PhotoViewAttacher mAttacher;
    AlertDialog mChooseDialog;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_detail);
        ButterKnife.bind(this);

        parseIntent();
        initChooseDialog();
        initProgressDialog();
        mProgressDialog.show();
        Picasso.with(this).load(url).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mImageView.getLayoutParams();
                params.topMargin = 20;
                mImageView.setLayoutParams(params);
                mProgressDialog.dismiss();
                if (Constants.IsFirstInDetail) {
                    Constants.IsFirstInDetail = false;
                    Snackbar.make(mLayout, "双击放大图片，长按可保存图片.", 4000).setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
                }

            }

            @Override
            public void onError() {
                mProgressDialog.dismiss();
            }
        });
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                FuLiDetailActivity.this.finish();
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                float scale = mAttacher.getScale();
                //PhotoView预定义了3种缩放大小，大，中，小，
                // 可以实际使用感受下，双击后先缩放到中，再双击缩放到大，再双击缩放到小。
                if (scale < mAttacher.getMediumScale()) {//当前缩放为小，变换为中
                    mAttacher.setScale(mAttacher.getMediumScale(), x, y, true);
                } else {//当前缩放为大，变换为小
                    mAttacher.setScale(mAttacher.getMinimumScale(), x, y, true);
                }
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //保存图片
                mChooseDialog.show();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 初始化等待框
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.load_more));
    }

    /**
     * 保存图片
     */
    private void saveImageToGallery() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Picasso.with(FuLiDetailActivity.this).load(url).get();
                    File appDir = new File(Environment.getExternalStorageDirectory(), "JiXin");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    String fileName = System.currentTimeMillis() + ".jpg";
                    File file = new File(appDir, fileName);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Uri uri = Uri.fromFile(file);
                    // 通知图库更新
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    sendBroadcast(scannerIntent);
                    Alerter.create(FuLiDetailActivity.this)
                            .setTitle("提示")
                            .setText("保存成功")
                            .setBackgroundColor(R.color.colorAccent)
                            .setIcon(R.mipmap.xiaolian)
                            .setDuration(500)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alerter.create(FuLiDetailActivity.this)
                            .setTitle("提示")
                            .setText("操作失败")
                            .setBackgroundColor(android.R.color.holo_red_dark)
                            .setIcon(R.mipmap.kulian)
                            .setDuration(500)
                            .show();
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }

    /**
     * 初始化选择框
     */
    private void initChooseDialog() {
        AlertDialog.Builder buld = new AlertDialog.Builder(this);
        buld.setTitle("提示");
        buld.setMessage("保存图片吗");
        buld.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImageToGallery();
                dialog.dismiss();
            }
        });
        buld.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mChooseDialog = buld.create();
    }

    private void parseIntent() {
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("date");
    }

    /**
     * intent
     *
     * @param context
     */
    public static Intent newIntent(Context context, String url, String date) {
        Intent intent = new Intent();
        intent.setClass(context, FuLiDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        intent.putExtra("date", date);
        return intent;
    }
}
