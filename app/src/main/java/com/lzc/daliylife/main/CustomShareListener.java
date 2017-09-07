package com.lzc.daliylife.main;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.lzc.daliylife.R;
import com.lzc.daliylife.normalUtil.T;
import com.tapadoo.alerter.Alerter;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.lang.ref.WeakReference;

/**
 * Created by lzc on 2017/9/7.
 */

public class CustomShareListener  implements UMShareListener {
    private WeakReference mActivity;

    public CustomShareListener(FragmentActivity activity) {
        mActivity = new WeakReference(activity);
    }

    @Override
    public void onStart(SHARE_MEDIA platform) {

    }

    @Override
    public void onResult(SHARE_MEDIA platform) {

        if (platform.name().equals("WEIXIN_FAVORITE")) {
            T.showShort(platform + " 收藏成功");
        } else {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Alerter.create((Activity) mActivity.get())
                        .setTitle("提示")
                        .setText("分享成功")
                        .setBackgroundColor(R.color.colorAccent)
                        .setIcon(R.mipmap.xiaolian)
                        .setDuration(500)
                        .show();
            }

        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
        if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                && platform != SHARE_MEDIA.EMAIL
                && platform != SHARE_MEDIA.FLICKR
                && platform != SHARE_MEDIA.FOURSQUARE
                && platform != SHARE_MEDIA.TUMBLR
                && platform != SHARE_MEDIA.POCKET
                && platform != SHARE_MEDIA.PINTEREST

                && platform != SHARE_MEDIA.INSTAGRAM
                && platform != SHARE_MEDIA.GOOGLEPLUS
                && platform != SHARE_MEDIA.YNOTE
                && platform != SHARE_MEDIA.EVERNOTE) {
            Alerter.create((Activity) mActivity.get())
                    .setTitle("提示")
                    .setText("分享失败")
                    .setBackgroundColor(android.R.color.holo_red_dark)
                    .setIcon(R.mipmap.kulian)
                    .setDuration(500)
                    .show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
        T.showShort(platform + " 取消分享");
    }
}
