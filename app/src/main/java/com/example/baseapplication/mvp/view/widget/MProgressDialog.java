package com.example.baseapplication.mvp.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.util.DensityUtil;

/**
 * 加载框
 */
public class MProgressDialog extends Dialog {
    private Context context;
    private TextView text_status;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                try {
                    if (isShowing()) {
                        dismiss();
                    } else {
                        dismiss();
                    }
                } catch (Exception e) {
                    RingLog.e("==-->Exception " + e.getMessage());
                }
            }
            super.handleMessage(msg);
        }

    };

    public MProgressDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        setDialog();
    }

    public MProgressDialog(Context context, int theme) {
        super(context, R.style.loadingdialog);
        this.context = context;
        setDialog();
    }

    public MProgressDialog(Context context) {
        super(context, R.style.loadingdialog);
        this.context = context;
        setDialog();
    }

    public void setDialog() {
        setContentView(R.layout.myprogressdialogcenter);
        text_status = (TextView) findViewById(R.id.text_status);
        LayoutParams lay = getWindow().getAttributes();
        setParams(lay);
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    private void setParams(LayoutParams lay) {
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = DensityUtil.dp2px(context, 80);
        lay.width = DensityUtil.dp2px(context, 80);
    }

    public void showDialog() {
        if (!isShowing()) {
            try {
                show();
                text_status.setVisibility(View.GONE);
            } catch (Exception e) {
                RingLog.e("==-->Exception " + e.getMessage());
            }
        }
    }

    public void showDialog(String title) {
        if (!isShowing()) {
            try {
                show();
                text_status.setText(title);
                text_status.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                RingLog.e("==-->Exception " + e.getMessage());
            }
        }
    }

    public void closeDialog() {
        text_status.setVisibility(View.GONE);
        mHandler.sendEmptyMessageDelayed(100, 800);
    }
}
