package com.example.baseapplication.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * author：   zp
 * date：     2015/8/19 0019 17:45
 * <p/>       公共类,主要用于一些常用的方法
 * modify by  ljy
 */
public class CommonUtil {

    /**
     * 根据输入法状态打开或隐藏输入法
     *
     * @param context 上下文
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏输入键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 移除焦点
     *
     * @param view view
     */
    public static void removeFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    /**
     * 获取随机字符串
     *
     * @param len
     * @return
     */
    public static String getRandomString(int len) {
        String returnStr;
        char[] ch = new char[len];
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            ch[i] = (char) (rd.nextInt(9) + 97);
        }
        returnStr = new String(ch);
        return returnStr;
    }

    /**
     * 对当前屏幕进行截屏操作
     */
    public static Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
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

    /**
     * description 增加view的触摸范围
     */
    public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
