package com.example.baseapplication.mvp.view.activity.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.widget.GifSizeFilter;
import com.example.baseapplication.mvp.view.widget.MProgressDialog;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.ActivityListManager;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.SharedPreferenceUtil;
import com.example.baseapplication.util.StringUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import top.zibin.luban.Luban;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:Activity的基类</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-12 15:02
 */
public abstract class BaseActivity<P extends BasePresenter> extends SwipeBackActivity {
    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = false;
    /**
     * 是否允许旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;

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
    protected MProgressDialog mProgressDialog;
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

    private CompositeDisposable mDisposable;
    /**
     * Luban压缩回调
     */
    public BaseFragment.OnLuBanSuccessListener onLuBanSuccessListener = null;

    public interface OnLuBanSuccessListener {
        public void OnLuBanSuccess(List<File> list);
    }

    public void setOnLuBanSuccessListener(
            BaseFragment.OnLuBanSuccessListener onLuBanSuccessListener) {
        this.onLuBanSuccessListener = onLuBanSuccessListener;
    }

    /**
     * Luban压缩返回码
     */
    public static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mAllowFullScreen) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        }
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeSize(200);//滑动删除的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        mDisposable = new CompositeDisposable();
        mActivity = this;
        mContext = this;
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
        initData(getIntent().getExtras());
        initView(getIntent().getExtras());
        setView(getIntent().getExtras());
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
        startActivity(new Intent(this, clz));
    }

    /**
     * [页面跳转并关闭当前界面]
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz, boolean isFinish) {
        startActivity(new Intent(this, clz));
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
        setSwipeBackEnable(isSwipeBackEnable);
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

    /**
     * 获取主题色
     */
    protected int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    protected int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    protected View setEmptyViewBase(int flag, String msg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    protected View setEmptyViewBase(int flag, String msg, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(mActivity, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        return emptyView;
    }

    protected void goPhoto(int maxSelectable) {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                Matisse.from(mActivity)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(true, mActivity.getPackageName() + ".fileProvider", "test"))
                        .maxSelectable(maxSelectable)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            Log.e("onSelected", "onSelected: pathList=" + pathList);
                        })
                        .showSingleMediaType(true)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(isChecked -> {
                            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                        })
                        .forResult(REQUEST_CODE_CHOOSE);
            }

            @Override
            public void onDenied(String permissionName) {
                showToast("请打开存储和相机权限");
            }

            @Override
            public void onDeniedWithNeverAsk(String permissionName) {
                showToast("请打开存储和相机权限");
            }
        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
    }

    protected void goPhotoSingle() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                Matisse.from(mActivity)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .maxSelectable(1)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            Log.e("onSelected", "onSelected: pathList=" + pathList);
                        })
                        .showSingleMediaType(true)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(isChecked -> {
                            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                        })
                        .forResult(REQUEST_CODE_CHOOSE);
            }

            @Override
            public void onDenied(String permissionName) {
                showToast("请打开存储和相机权限");
            }

            @Override
            public void onDeniedWithNeverAsk(String permissionName) {
                showToast("请打开存储和相机权限");
            }
        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
    }

    protected <T> void withRx(final List<T> photos) {
        showLoadDialog();
        mDisposable.add(Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(new Function<List<T>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<T> list) throws Exception {
                        return Luban.with(mActivity)
                                .setTargetDir(getPath())
                                .load(list)
                                .get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        RingLog.e(throwable.getMessage());
                    }
                })
                .onErrorResumeNext(Flowable.<List<File>>empty())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(@NonNull List<File> list) {
                        hideLoadDialog();
                        onLuBanSuccessListener.OnLuBanSuccess(list);
                    }
                }));
    }

    private String getPath() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
            path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        } else {
            path = mActivity.getFilesDir() + "/Luban/image/";
        }
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
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
        mDisposable.clear();
        ButterKnife.bind(this).unbind();
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
