package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;

/**
 * 扫描二维码界面
 */
public class ScanCodeActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
    }
}
