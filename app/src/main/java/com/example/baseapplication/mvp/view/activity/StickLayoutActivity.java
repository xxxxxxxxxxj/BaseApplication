package com.example.baseapplication.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.adapter.ShopAdapter;
import com.example.baseapplication.mvp.view.widget.DividerLinearItemDecoration;
import com.example.baseapplication.mvp.view.widget.NoScollFullLinearLayoutManager;
import com.example.baseapplication.mvp.view.widget.StickLayout;
import com.example.baseapplication.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * 吸附悬停
 */
public class StickLayoutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.sl)
    StickLayout sl;
    @BindView(R.id.rv_shopfrag_item)
    RecyclerView rvShopfragItem;
    private final String[] mTitles = {"Matisse", "zxing", "微信支付", "支付宝支付", "拍摄视频", "RichText", "普通浮层",
            "列表浮层", "加载框", "提示框", "自定义提示框", "亮色ios对话框", "暗色ios对话框", "亮色md对话框", "暗色md对话框",
            "新手引导", "倒计时", "滚轮", "瀑布流", "购物车动画", "StickLayout", "当页浮窗", "系统浮窗", "红包动画", "Flipper", "通知"};
    private ShopAdapter shopAdapter;

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
        rvShopfragItem.setHasFixedSize(true);
        rvShopfragItem.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new
                NoScollFullLinearLayoutManager(mActivity);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvShopfragItem.setLayoutManager(noScollFullLinearLayoutManager);
        shopAdapter = new ShopAdapter(mActivity, R.layout.item_shopfrag_item, new ArrayList<String>(Arrays.asList(mTitles)));
        rvShopfragItem.setAdapter(shopAdapter);
        rvShopfragItem.addItemDecoration(new DividerLinearItemDecoration(mActivity, LinearLayoutManager.VERTICAL,
                QMUIDisplayHelper.dp2px(mContext, 8),
                ContextCompat.getColor(mContext, R.color.white)));
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

    public void scrollTo2(View view) {
        //滑动到指定子控件
        sl.scrollToView(tv2);
    }

    public void scrollTo3(View view) {
        sl.scrollToView(tv3);
    }

    public void scrollTo4(View view) {
        sl.scrollToView(tv4);
    }

    public void scrollTo7(View view) {
        sl.scrollToView(tv7);
    }
}
