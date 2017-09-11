package com.lzc.daliylife.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.base.*;
import com.lzc.daliylife.main.MainActivity;
import com.lzc.daliylife.framework.ApplWork;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
            packageInfo = pm.getPackageInfo(ApplWork.ApplWorkContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);

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
