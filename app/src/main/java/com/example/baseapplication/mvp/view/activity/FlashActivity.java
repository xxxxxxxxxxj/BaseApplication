package com.example.baseapplication.mvp.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.util.CountdownUtil;
import com.example.baseapplication.util.JumpToUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 */
public class FlashActivity extends BaseActivity {

    @BindView(R.id.btn_flash_skip)
    Button btnFlashSkip;

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
            JumpToUtil.jumpTo(mActivity, Integer.parseInt(point), backup);
        }
        CountdownUtil.getInstance().newTimer(3000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
                btnFlashSkip.setText("跳过  " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                startActivity(MainActivity.class, true);
            }
        }, "FLASH_TIMER");
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
        setAllowFullScreen(true);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_flash_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_flash_skip:
                startActivity(MainActivity.class, true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CountdownUtil.getInstance().cancel("FLASH_TIMER");
    }
}
