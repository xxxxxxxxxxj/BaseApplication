package com.example.baseapplication.updateapputil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.baseapplication.R;

/**
 * Notification类，既可用系统默认的通知布局，也可以用自定义的布局
 *
 * @author lz
 */
public class MyNotification {
    public final static int DOWNLOAD_COMPLETE = 3;
    public final static int DOWNLOAD_FAIL = 2;
    public static final int DOWNLOADING = 1;
    Context mContext;   //Activity或Service上下文
    Notification notification;  //notification
    NotificationManager nm;
    String titleStr;   //通知标题
    PendingIntent contentIntent; //点击通知后的动作
    int notificationID = 1;   //通知的唯一标示ID
    long when = System.currentTimeMillis();
    RemoteViews remoteView = null;  //自定义的通知栏视图

    /**
     * @param context       Activity或Service上下文
     * @param contentIntent 点击通知后的动作
     * @param id            通知的唯一标示ID
     */
    public MyNotification(Context context, PendingIntent contentIntent, int id) {
        mContext = context;
        notificationID = id;
        this.contentIntent = contentIntent;
        this.nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 显示自定义通知
     *
     * @param icoId    自定义视图中的图片ID
     * @param titleStr 通知栏标题
     * @param layoutId 自定义布局文件ID
     */
    public void showCustomizeNotification(int icoId, String titleStr, int layoutId, int progress) {
        this.titleStr = titleStr;
        notification = new Notification.Builder(mContext)
                .setAutoCancel(true)
                .setContentTitle(titleStr)
                .setContentIntent(contentIntent)
                .setSmallIcon(icoId)
                .setWhen(when).build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.contentIntent = this.contentIntent;
        // 1、创建一个自定义的消息布局 view.xml
        // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段  
        if (remoteView == null) {
            remoteView = new RemoteViews(mContext.getPackageName(), layoutId);
            remoteView.setImageViewResource(R.id.ivNotification, icoId);
            remoteView.setTextViewText(R.id.tvTitle, titleStr);
            notification.contentView = remoteView;
            notification.contentView.setTextViewText(R.id.tvTip, "进度(" + progress + "%)");
            notification.contentView.setProgressBar(R.id.pbNotification, 100, progress, false);
        }
        nm.notify(notificationID, notification);
    }

    public void changeNotificationStatus(int state) {
        if (notification.contentView != null) {
            if (state == DOWNLOAD_FAIL)
                notification.contentView.setTextViewText(R.id.tvTip, "下载失败！ ");
            else if (state == DOWNLOAD_COMPLETE)
                notification.contentView.setTextViewText(R.id.tvTip, "下载完成");
        }
        nm.notify(notificationID, notification);
    }

    /**
     * 移除通知
     */
    public void removeNotification() {
        // 取消的只是当前Context的Notification  
        nm.cancel(notificationID);
    }
}
