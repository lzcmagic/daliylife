package com.lzc.daliylife.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.*;
import com.lzc.daliylife.framework.MyApp;

import butterknife.BindView;

/**
 * Created by lzc on 2017/3/21.
 */

public class AboutUs extends BaseFragment {

    @BindView(R.id.tv_about)
    TextView mText;


    @Override
    public int getResId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initUI() {
        PackageManager pm = getActivity().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(MyApp.ApplWorkContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo!=null){
            mText.setVisibility(View.VISIBLE);
            mText.setText("当前版本号："+packageInfo.versionName+"\n"+"感谢您使用休闲时光\n本App仅为业余学习Android之用\n给您带来不便还请见谅");
        }else{
            mText.setVisibility(View.INVISIBLE);
        }
    }
}
