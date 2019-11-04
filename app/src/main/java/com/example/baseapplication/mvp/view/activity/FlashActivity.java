package com.example.baseapplication.mvp.view.activity;

import android.net.Uri;
import android.os.Bundle;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.util.JumpToUtil;

import java.util.List;

/**
 * 启动页
 */
public class FlashActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_flash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            RingLog.e("url:" + uri);

            // scheme部分
            String scheme = uri.getScheme();
            RingLog.e("scheme:" + scheme);

            // host部分
            String host = uri.getHost();
            RingLog.e("host:" + host);

            // port部分
            int port = uri.getPort();
            RingLog.e("port:" + port);

            // 访问路劲
            String path = uri.getPath();
            RingLog.e("path:" + path);

            List<String> pathSegments = uri.getPathSegments();

            // Query部分
            String query = uri.getQuery();
            RingLog.e("query:" + query);

            //获取指定参数值
            String point = uri.getQueryParameter("point");
            RingLog.e("point:" + point);
            String backup = uri.getQueryParameter("backup");
            RingLog.e("backup:" + backup);
            JumpToUtil.jumpTo(mActivity, Integer.parseInt(point),backup);
        }
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
