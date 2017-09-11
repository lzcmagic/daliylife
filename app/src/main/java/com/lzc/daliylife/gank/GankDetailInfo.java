package com.lzc.daliylife.gank;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.lzc.daliylife.R;
import com.lzc.daliylife.normalUtil.T;
import com.umeng.analytics.MobclickAgent;

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
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initWebView();
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

    public void onClickCopy() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gank, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.gank_copy: {
                    onClickCopy();
                    T.showShort("复制成功");
                    break;
                }
                case R.id.gank_share: {
//                    UMImage image = new UMImage(GankDetailInfo.this, R.mipmap.logo_icon);
//                    UMWeb umWeb = new UMWeb(url, title, "来自休闲时光", image);
//                    new ShareAction(GankDetailInfo.this)
//                            .withMedia(umWeb)
//                            .setPlatform(SHARE_MEDIA.QQ)
//                            .setCallback(new CustomShareListener(GankDetailInfo.this))
//                            .share();
                    break;
                }
            }

            return true;
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            if (mWebView != null) {
                mWebView.stopLoading();
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mWebView.onResume();
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();

        }
    }


    /**
     * 初始化WebView
     */
    private void initWebView() {
        mWebView = new WebView(getApplicationContext());
        WebViewContainer.addView(mWebView);
        // 启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

//        // //设置加载进来的页面自适应手机屏幕
//        settings.setUseWideViewPort(true);
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebClient());
        // 覆盖webview默认使用第三方或系统默认浏览器打开网页得行为，是网页用webview打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true得时候控制去webview打开，为false调用系统浏览器或第三方浏览器
                mProgressBar.setVisibility(View.VISIBLE);
                if (url != null)
                    view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
            }
        });
    }

    private class WebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView web, int progress) {
            super.onProgressChanged(web, progress);
            mProgressBar.setProgress(progress);
            if (progress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}