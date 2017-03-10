package com.example.lzc.daliylife.utils;

import android.util.Log;

import com.example.lzc.daliylife.framework.Constants;

import rx.Subscriber;

/**
 * Created by lzc on 2016/11/14.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
//    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
//        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
//        Toast.makeText(, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d(Constants.NORMALTAG,"error:" + e.getMessage());
        dismissProgressDialog();
    }


    @Override
    public void onNext(Object o) {
        mSubscriberOnNextListener.onNext(o);
    }

    @Override
    public void onCancelProgress() {
        //解绑订阅
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
