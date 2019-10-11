package com.example.baseapplication.updateapputil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.baseapplication.R;

import java.io.File;


/**
 * Created by Teprinciple on 2017/11/3.
 */

public class UpdateAppReceiver extends BroadcastReceiver {
    private final static String TAG = UpdateAppReceiver.class.getSimpleName();
    private MyNotification mNotification;

    @Override
    public void onReceive(Context context, Intent intent) {
        int progress = intent.getIntExtra("progress", 0);
        String title = intent.getStringExtra("title");
        int state = intent.getIntExtra("state", 0);
        Log.d(TAG, "progress = " + progress + "---title = " + title);
        if (UpdateAppUtils.showNotification) {
            mNotification = new MyNotification(context, null, 1);
            mNotification.showCustomizeNotification(R.mipmap.logo, R.string.app_name + title,
                    R.layout.download_notif, progress);
        }
        if (state == MyNotification.DOWNLOAD_COMPLETE) {
            if (mNotification != null) {
                mNotification.changeNotificationStatus(state);
                mNotification.removeNotification();
            }
            if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
                File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
                UpdateUtil.installAPK(context, apkFile);
            }
        } else if (state == MyNotification.DOWNLOAD_FAIL) {
            if (mNotification != null) {
                mNotification.changeNotificationStatus(state);
                mNotification.removeNotification();
            }
        }
    }
}
