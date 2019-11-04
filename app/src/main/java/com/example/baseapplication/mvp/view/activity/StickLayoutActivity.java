package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * 吸附悬停
 */
public class StickLayoutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_stick_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        initToolBar(toolbar, true, "吸附悬停");
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
    }
}
