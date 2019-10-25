package com.example.baseapplication.mvp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.toast.RingToast;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.OnClick;

/**
 * 扫描二维码界面
 */
public class ScanCodeActivity extends BaseActivity {
    private CaptureFragment captureFragment;
    public static boolean isOpen = false;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
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
        setSwipeBack(false);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            RingToast.show("解析成功,结果为 = " + result);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanCodeActivity.this.setResult(RESULT_OK, resultIntent);
            ScanCodeActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            RingToast.show("解析错误");
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "解析错误");
            resultIntent.putExtras(bundle);
            ScanCodeActivity.this.setResult(RESULT_OK, resultIntent);
            ScanCodeActivity.this.finish();
        }
    };

    @OnClick({R.id.iv_back, R.id.toolbar_photo, R.id.vw_light})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.toolbar_photo:
                goPhotoSingle();
                break;
            case R.id.vw_light:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> strings = Matisse.obtainPathResult(data);
            if (strings != null && strings.size() > 0) {
                try {
                    CodeUtils.analyzeBitmap(strings.get(0), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            RingToast.show("解析成功,结果为 = " + result);
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                            bundle.putString(CodeUtils.RESULT_STRING, result);
                            resultIntent.putExtras(bundle);
                            ScanCodeActivity.this.setResult(RESULT_OK, resultIntent);
                            ScanCodeActivity.this.finish();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            RingToast.show("解析错误");
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                            bundle.putString(CodeUtils.RESULT_STRING, "解析错误");
                            resultIntent.putExtras(bundle);
                            ScanCodeActivity.this.setResult(RESULT_OK, resultIntent);
                            ScanCodeActivity.this.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
