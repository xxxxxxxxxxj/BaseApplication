package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Flipper
 */
public class FlipperActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_flipper;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        initToolBar(toolbar, true, "Flipper");
        refreshLayout.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        initViewFlipper(R.id.vf_1);
        initViewFlipper(R.id.vf_2);
        initViewFlipper(R.id.vf_3);
        initViewFlipper(R.id.vf_4);
        initViewFlipper(R.id.vf_5);
        initViewFlipper(R.id.vf_6);
        initViewFlipper(R.id.vf_7);
    }

    private void initViewFlipper(int id) {
        ViewFlipper vf = (ViewFlipper) findViewById(id);
        View item1 = View.inflate(this, R.layout.item_flipper, null);
        View item2 = View.inflate(this, R.layout.item_flipper, null);
        View item3 = View.inflate(this, R.layout.item_flipper, null);
        vf.addView(item1);
        vf.addView(item2);
        vf.addView(item3);
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
