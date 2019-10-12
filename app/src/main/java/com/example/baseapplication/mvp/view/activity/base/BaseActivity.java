package com.example.baseapplication.mvp.view.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.widget.MProgressDialog;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.ActivityListManager;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.SharedPreferenceUtil;
import com.example.baseapplication.util.StringUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-12 15:02
 */
public abstract class BaseActivity<P extends BasePresenter> extends SwipeBackActivity {
    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = true;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = false;
    /**
     * 是否允许旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;
    /**
     * 是否允许侧滑退出
     **/
    private boolean isSwipeBackEnable = true;

    /**
     * 返回页面布局id
     *
     * @return
     */
    protected abstract int getLayoutResID();

    /**
     * 做视图相关的初始化工作
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 做视图相关的初始化工作
     */
    protected abstract void setView(Bundle savedInstanceState);

    /**
     * 做数据相关的初始化工作
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 做监听事件相关的初始化工作
     */
    protected abstract void initEvent();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 该Activity是否订阅事件总线
     *
     * @return true则自动进行注册/注销操作，false则不注册
     */
    protected abstract boolean isUseEventBus();

    /**
     * 加载提示框
     */
    private MProgressDialog mProgressDialog;
    /**
     * 业务处理类
     */
    protected P mPresenter;

    protected abstract P createPresenter();

    /**
     * activity管理类
     */
    protected ActivityListManager activityListManager;
    /**
     * SharedPreference工具类
     */
    protected SharedPreferenceUtil spUtil;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 上下文
     */
    protected Activity mActivity;
    /**
     * 侧滑退出操作类
     */
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeSize(200);//滑动删除的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        setSwipeBackEnable(isSwipeBackEnable);
        mActivity = this;
        mContext = getApplicationContext();
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (isSetStatusBar) {
            steepStatusBar();
        }
        if (isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(getLayoutResID());
        spUtil = SharedPreferenceUtil.getInstance(this);
        activityListManager = new ActivityListManager();
        mProgressDialog = new MProgressDialog(this);
        mPresenter = createPresenter();
        initView(savedInstanceState);
        setView(savedInstanceState);
        initData(savedInstanceState);
        initEvent();
        loadData();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    // 弹出Toast消息
    protected void showToast(@StringRes int resId) {
        RingToast.show(resId);
    }

    // 弹出Toast消息
    protected void showToast(String msg) {
        RingToast.show(msg);
    }

    // 显示加载提示框
    protected void showLoadDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) return;
        mProgressDialog.showDialog();
    }

    // 隐藏加载提示框
    protected void hideLoadDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //glide加载图片
    protected void displayImage(String url, ImageView imageView) {
        GlideUtil.displayImage(this, url, imageView);
    }

    //请求单个权限建议用这个
    protected void requestEach(final PermissionListener listener, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // `permission.name` is granted !
                    if (listener != null) {
                        listener.onGranted(permission.name);
                    }
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again
                    if (listener != null) {
                        listener.onDenied(permission.name);
                    }
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings
                    if (listener != null) {
                        listener.onDeniedWithNeverAsk(permission.name);
                    }
                }
            }
        });
    }

    //请求多个权限建议用这个
    protected void requestEachCombined(final PermissionListener listener, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEachCombined(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // All permissions are granted !
                    if (listener != null) {
                        listener.onGranted(permission.name);
                    }
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // At least one denied permission without ask never again
                    if (listener != null) {
                        listener.onDenied(permission.name);
                    }
                } else {
                    // At least one denied permission with ask never again
                    // Need to go to the settings
                    if (listener != null) {
                        listener.onDeniedWithNeverAsk(permission.name);
                    }
                }
            }
        });
    }

    //隐藏视图
    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    //显示视图
    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void setText(TextView tv, String str, String defaultStr,
                           int visibilt, int defaultVisibilt) {
        if (StringUtil.isNotEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(visibilt);
        } else {
            tv.setText(defaultStr);
            tv.setVisibility(defaultVisibilt);
        }
    }

    protected void setText(TextView tv, String str) {
        if (StringUtil.isNotEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [页面跳转并关闭当前界面]
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz, boolean isFinish) {
        startActivity(new Intent(BaseActivity.this, clz));
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    protected void startActivity(Class<?> clz, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * [含有Bundle通过Class打开界面并回调]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls, Bundle bundle,
                                          int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * [是否允许侧滑退出]
     *
     * @param isSwipeBackEnable
     */
    protected void setSwipeBack(boolean isSwipeBackEnable) {
        this.isSwipeBackEnable = isSwipeBackEnable;
    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    protected void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    protected void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    protected void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏(虚拟键盘不能兼容)
            //getWindow().addFlags(
            //WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
