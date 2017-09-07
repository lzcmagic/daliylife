package com.lzc.daliylife.main;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.lzc.daliylife.R;
import com.lzc.daliylife.normalUtil.T;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * Created by lzc on 2017/9/7.
 *
 */

public class CustomerShareAction extends ShareAction {
    public CustomerShareAction(final Activity activity) {
        super(activity);
        this.setDisplayList(
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,
                SHARE_MEDIA.MORE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
                            T.toast("复制文本按钮");
                        } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            T.toast("复制链接按钮");
                        } else {
                            if (share_media == SHARE_MEDIA.QQ ||
                                    share_media == SHARE_MEDIA.QZONE) {
                                UMImage image = new UMImage(activity, R.mipmap.logo_icon);
                                UMWeb web = new UMWeb("http://fusion.qq.com/cgi-bin/qzapps/unified_jump?actionFlag=0&appid=52442571&params=pname%3Dcom.lzc.daliylife%26versioncode%3D4%26channelid%3D%26actionflag%3D0&from=mqq");
                                web.setTitle("休闲时光");//标题
                                web.setThumb(image);  //缩略图
                                web.setDescription("妹纸,干货应有尽有~~~");//描述
                                new ShareAction(activity).withMedia(web)
                                        .setPlatform(share_media)
                                        .setCallback(new CustomShareListener((FragmentActivity) activity))
                                        .share();
                            } else {
                                new ShareAction(activity).withText("体验一下休闲时光App吧")
                                        .setPlatform(share_media)
                                        .setCallback(new CustomShareListener((FragmentActivity) activity))
                                        .share();
                            }

                        }
                    }
                });
    }
}
