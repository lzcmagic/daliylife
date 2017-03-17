package com.example.lzc.daliylife.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.framework.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

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
    PhotoViewAttacher mAttacher;
    AlertDialog mChooseDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_detail);
        ButterKnife.bind(this);
        pareseIntent();
        initChooseDialog();
        Picasso.with(this).load(url).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(Constants.NORMALTAG, "onSingleTapConfirmed: " + e.getAction());
                FuLiDetailActivity.this.finish();
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(Constants.NORMALTAG, "onDoubleTap: " + e.getAction());
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
                Log.d(Constants.NORMALTAG, "onDoubleTapEvent: " + e.getAction());
                return false;
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(Constants.NORMALTAG, "setOnLongClickListener: ");
                //保存图片
                mChooseDialog.show();
                return false;
            }
        });

    }

    private void askWritePermssion(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission_group.STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            saveImageToGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
        {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                saveImageToGallery();
            } else
            {
                // Permission Denied
                Alerter.create(FuLiDetailActivity.this)
                        .setTitle("提示")
                        .setText("未授权权限，保存图片失败")
                        .setBackgroundColor(android.R.color.holo_red_dark)
                        .setIcon(R.mipmap.loading)
                        .setDuration(500)
                        .show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
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
                            .setIcon(R.mipmap.loading)
                            .setDuration(500)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alerter.create(FuLiDetailActivity.this)
                            .setTitle("提示")
                            .setText("操作失败")
                            .setBackgroundColor(android.R.color.holo_orange_dark)
                            .setIcon(R.mipmap.loading)
                            .setDuration(500)
                            .show();
                }
            }
        }).start();

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

    private void pareseIntent() {
        url = getIntent().getStringExtra("url");
        date = getIntent().getStringExtra("date");
        Log.d(Constants.NORMALTAG, url);
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
