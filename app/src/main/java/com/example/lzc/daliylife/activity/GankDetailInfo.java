package com.example.lzc.daliylife.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.lzc.daliylife.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzc on 2017/3/15.
 */

public class GankDetailInfo extends AppCompatActivity {

    @BindView(R.id.tb_gank_detail)
    Toolbar mToolbar;
    @BindView(R.id.ll_webview_container)
    FrameLayout WebViewContainer;
    WebView mWebView;
    @BindView(R.id.pb_web_load)
    ProgressBar mProgressBar;
    private String title;
    private String date;
    private String user;
    private String url;
    private boolean IsFirstFinish = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_gank_detail_info);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.toolbar_news);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        user = getIntent().getStringExtra("user");
        url = getIntent().getStringExtra("url");
        initWebView();
        Log.d("click", url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
//
                if (IsFirstFinish) {
                    if (mWebView != null) {
                   mWebView.stopLoading();
                }
                    finish();
                    IsFirstFinish = false;
                }
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            if (mWebView!=null){
                mWebView.stopLoading();
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {

            mWebView.removeAllViews();
            mWebView.destroy();

        }
    }

    /**
     * 启动GankDetailActivity
     *
     * @param context
     * @param title
     * @param date
     * @param user
     * @param url
     */
    public static void actionIntentStart(Context context, String title, String date, String user, String url) {
        Intent intent = new Intent();
        intent.setClass(context, GankDetailInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", title);
        intent.putExtra("date", date);
        intent.putExtra("user", user);
        intent.putExtra("url", url);

        context.startActivity(intent);
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        mWebView=new WebView(getApplicationContext());
        WebViewContainer.addView(mWebView);
        // 启用支持javascript
        WebSettings settings = mWebView.getSettings();
        // //设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebClient());
        // 覆盖webview默认使用第三方或系统默认浏览器打开网页得行为，是网页用webview打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true得时候控制去webview打开，为false调用系统浏览器或第三方浏览器
                mProgressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.i("click", "网页加载失败  " + i + "    " + s + "    " + s1);
            }
        });
    }

    private class WebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView web, int progress) {
            mProgressBar.setProgress(progress);
            if (progress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(web, progress);
        }
    }
}
