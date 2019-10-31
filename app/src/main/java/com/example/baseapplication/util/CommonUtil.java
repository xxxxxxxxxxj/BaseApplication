package com.example.baseapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.baseapplication.mvp.model.entity.ImageInfo;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * author：   zp
 * date：     2015/8/19 0019 17:45
 * <p/>       公共类,主要用于一些常用的方法
 * modify by  ljy
 */
public class CommonUtil {
    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    //查看大图
    public static void photoView(Activity context, int position, List<ImageInfo> imgList) {
        /*Intent intent = new Intent();
        intent.setClass(context, PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PhotoViewActivity.KEY_PHOTOVIEW_POSITION, position);
        bundle.putSerializable(PhotoViewActivity.KEY_PHOTOVIEW_IMGLIST, (Serializable) imgList);
        intent.putExtras(bundle);
        context.startActivity(intent);*/
    }

    public static List<File> pathToFile(List<String> pathList) {
        List<File> fileList = new ArrayList<File>();
        for (int i = 0; i < pathList.size(); i++) {
            fileList.add(new File(pathList.get(i)));
        }
        return fileList;
    }

    public static List<String> fileToPath(List<File> fileList) {
        List<String> pathList = new ArrayList<String>();
        for (int i = 0; i < fileList.size(); i++) {
            pathList.add(fileList.get(i).getAbsolutePath());
        }
        return pathList;
    }

    /**
     * 判断当前日期是否在给定的两个日期之间
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static boolean compareDateState(String beginDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c.setTimeInMillis(System.currentTimeMillis());
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endDate));
        } catch (java.text.ParseException e) {
            return false;
        }
        int resultBegin = c.compareTo(c1);
        int resultEnd = c.compareTo(c2);
        return resultBegin > 0 && resultEnd < 0;
    }
}
