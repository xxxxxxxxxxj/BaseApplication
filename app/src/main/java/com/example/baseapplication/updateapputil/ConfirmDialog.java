package com.example.baseapplication.updateapputil;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.baseapplication.R;


/**
 * Created by Teprinciple on 2016/10/13.
 */
public class ConfirmDialog extends Dialog {
    private boolean isSureBtnDismiss = true;
    Callback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;
    private View vw_dialog_confirm;

    public ConfirmDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setCustomDialog();
    }

    public ConfirmDialog(Context context, Callback callback,boolean isSureBtnDismiss) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        this.isSureBtnDismiss = isSureBtnDismiss;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        sureBtn = (TextView)mView.findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView)mView.findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);
        vw_dialog_confirm = (View) mView.findViewById(R.id.vw_dialog_confirm);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(1);
                if(isSureBtnDismiss){
                    ConfirmDialog.this.cancel();
                }
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.this.cancel();
            }
        });
        super.setContentView(mView);
    }

    public ConfirmDialog setsureBtnVisible(int visibility){
        sureBtn.setVisibility(visibility);
        if(visibility == View.GONE){
            vw_dialog_confirm.setVisibility(View.GONE);
        }
        return this;
    }

    public ConfirmDialog setCancleBtnVisible(int visibility){
        cancleBtn.setVisibility(visibility);
        if(visibility == View.GONE){
            vw_dialog_confirm.setVisibility(View.GONE);
        }
        return this;
    }

    public ConfirmDialog setContent(String s){
        content.setText(s);
        return this;
    }

    public ConfirmDialog setDialogCancelable(boolean isCancelable) {
        setCancelable(isCancelable);
        return this;
    }

    public ConfirmDialog setDialogCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        return this;
    }
}
