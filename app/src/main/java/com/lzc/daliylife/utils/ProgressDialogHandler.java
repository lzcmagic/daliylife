package com.lzc.daliylife.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.lzc.daliylife.R;
import com.lzc.daliylife.framework.ApplWork;

/**
 * 保持在主线程中的对话框
 * Created by lzc on 2016/11/14.
 */
public class ProgressDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

//    private ProgressDialog pd;
    private MyProgressDialog mpd;
//    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    private class MyProgressDialog extends ProgressDialog{

        public MyProgressDialog(Context context) {
            super(context);
            this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    public ProgressDialogHandler( ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
//        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if (mpd == null) {
            mpd = new MyProgressDialog(ApplWork.getApplWorkContext());
            mpd.setTitle(R.string.dialog_progress_tip);
            mpd.setIcon(R.mipmap.ic_launcher);
            mpd.setMessage(ApplWork.ApplWorkContext.getString(R.string.dialog_progress_message));

            mpd.setCancelable(cancelable);

            if (cancelable) {
                mpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!mpd.isShowing()) {
                mpd.show();
            }
        }
    }

    private void dismissProgressDialog(){
        if (mpd != null) {
            mpd.dismiss();
            mpd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
