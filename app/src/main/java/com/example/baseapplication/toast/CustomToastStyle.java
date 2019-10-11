package com.example.baseapplication.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.baseapplication.R;
import com.example.baseapplication.app.MApplication;

/**
 * author:  ljy
 * date:    2018/10/16
 * description:
 */

public class CustomToastStyle implements IToastStyle {
    private Context context;

    public CustomToastStyle(Context context) {
        this.context = context;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getCornerRadius() {
        return 0;
    }

    @Override
    public int getBackgroundColor() {
        return 0X00000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return 15;
    }

    @Override
    public int getMaxLines() {
        return 5;
    }

    @Override
    public int getPaddingLeft() {
        return 0;
    }

    @Override
    public int getPaddingTop() {
        return 0;
    }

    @Override
    public int getPaddingRight() {
        return 0;
    }

    @Override
    public int getPaddingBottom() {
        return 0;
    }

    @Override
    public View getCustomToastView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null);
        return view;
    }
}
