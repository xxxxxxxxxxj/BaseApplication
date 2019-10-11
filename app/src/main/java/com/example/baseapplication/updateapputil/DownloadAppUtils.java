package com.example.baseapplication.updateapputil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Created by Teprinciple on 2016/12/13.
 */
public class DownloadAppUtils {
    private static final String TAG = DownloadAppUtils.class.getSimpleName();
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径
    private static Context localContext;
    private static String localUrl;
    private static String localserverVersionName;
    private static int localisUpgrade;

    /**
     * 通过浏览器下载APK包
     *
     * @param context
     * @param url
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void download(Context context, String url, String serverVersionName, int isUpgrade) {
        Log.e("TAG", "url = " + url);
        localContext = context;
        localUrl = url;
        localserverVersionName = serverVersionName;
        localisUpgrade = isUpgrade;
        String packageName = localContext.getPackageName();
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            Log.i(TAG, "没有SD卡");
            return;
        }
        downloadUpdateApkFilePath = filePath + File.separator + packageName + "_" + localserverVersionName + ".apk";
        FileDownloader.setup(localContext);
        FileDownloader.getImpl().create(localUrl)
                .setPath(downloadUpdateApkFilePath)
                .setListener(new FileDownloadLargeFileListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void started(BaseDownloadTask task) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, long soFarBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                        EventBus.getDefault().post(new UpdateAppEvent(soFarBytes, totalBytes, UpdateAppEvent.DOWNLOADING,localisUpgrade));
                        /*if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(soFarBytes, totalBytes, UpdateAppEvent.DOWNLOADING));
                        } else {
                            send(localContext, (int) (soFarBytes * 100.0 / totalBytes), localserverVersionName, MyNotification.DOWNLOADING);
                        }*/
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_COMPLETE,localisUpgrade));
                        /*if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_COMPLETE));
                        } else {
                            send(localContext, 100, localserverVersionName, MyNotification.DOWNLOAD_COMPLETE);
                        }*/
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("TAG","e = "+e.toString());
                        EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_FAIL,localisUpgrade));
                       /* if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_FAIL));
                        } else {
                            send(localContext, 100, localserverVersionName, MyNotification.DOWNLOAD_FAIL);
                        }*/
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    public static void retry() {
        String packageName = localContext.getPackageName();
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            Log.i(TAG, "没有SD卡");
            return;
        }
        downloadUpdateApkFilePath = filePath + File.separator + packageName + "_" + localserverVersionName + ".apk";
        FileDownloader.setup(localContext);
        FileDownloader.getImpl().create(localUrl)
                .setPath(downloadUpdateApkFilePath)
                .setListener(new FileDownloadLargeFileListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void started(BaseDownloadTask task) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, long soFarBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                        EventBus.getDefault().post(new UpdateAppEvent(soFarBytes, totalBytes, UpdateAppEvent.DOWNLOADING,localisUpgrade));
                        /*if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(soFarBytes, totalBytes, UpdateAppEvent.DOWNLOADING));
                        } else {
                            send(localContext, (int) (soFarBytes * 100.0 / totalBytes), localserverVersionName, MyNotification.DOWNLOADING);
                        }*/
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_COMPLETE,localisUpgrade));
                        /*if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_COMPLETE));
                        } else {
                            send(localContext, 100, localserverVersionName, MyNotification.DOWNLOAD_COMPLETE);
                        }*/
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("TAG","e = "+e.toString());
                        EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_FAIL,localisUpgrade));
                       /* if (localisUpgrade == 1) {
                            EventBus.getDefault().post(new UpdateAppEvent(UpdateAppEvent.DOWNLOAD_FAIL));
                        } else {
                            send(localContext, 100, localserverVersionName, MyNotification.DOWNLOAD_FAIL);
                        }*/
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    private static void send(Context context, int progress, String serverVersionName, int state) {
        Intent intent = new Intent("teprinciple.update");
        intent.putExtra("progress", progress);
        intent.putExtra("state", state);
        intent.putExtra("title", serverVersionName);
        context.sendBroadcast(intent);
    }
}
