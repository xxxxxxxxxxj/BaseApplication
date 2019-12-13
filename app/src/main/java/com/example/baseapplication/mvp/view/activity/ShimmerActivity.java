package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.widget.Toolbar;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * 微光加载效果
 */
public class ShimmerActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.shimmer_layout)
    ShimmerLayout shimmerLayout;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shimmer;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        initToolBar(toolbar, true, "微光加载效果");
        refreshLayout.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        shimmerLayout.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerLayout.stopShimmerAnimation();
            }
        }, 3000);
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
