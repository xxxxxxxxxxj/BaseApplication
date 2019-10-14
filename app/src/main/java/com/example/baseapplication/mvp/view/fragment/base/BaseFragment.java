package com.example.baseapplication.mvp.view.fragment.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baseapplication.R;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.widget.MProgressDialog;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.SharedPreferenceUtil;
import com.example.baseapplication.util.StringUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;


/**
 * author:    ljy
 * date：     2018/3/19
 * description： Fragment的基类
 * <p>
 * <a>https://www.jianshu.com/p/3d9ee98a9570</a>
 * 此基类的作用：
 * 1.提供延迟加载（懒加载）
 * 2.提供getContentLayout()、initView()等方法子类实现初始化操作
 * 3.销毁Presenter层对View层的引用。
 * 4.实现IBaseFragment接口，以便通过FragmentManager.FragmentLifecycleCallbacks完成部分"基类操作"
 * <p>
 * <p>
 * 由于Java的单继承的限制，DevRing库就不提供基类了，所以把一些基类操作通过FragmentManager.FragmentLifecycleCallbacks来完成
 * 只需你的Fragment需实现IBaseFragment接口，另外如果你的Activity实现了IBaseActivity，那请确保isUseFragment()返回true。
 * 即可完成以下"基类操作"：（具体请查看 {@link })
 * 1.操作PublishSubject以便控制网络请求的生命周期
 * 2.根据isUseEventBus()来决定EventBus的注册/注销
 * 3.数据的保存与恢复 <a>https://blog.csdn.net/donglynn/article/details/47065999</a>
 * <p>
 * <p>
 * 这种基类实现方式，参考自JessYan
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected BaseActivity mActivity;
    //根布局视图
    private View mContentView;
    //视图是否已经初始化完毕
    protected boolean isViewReady;
    //fragment是否处于可见状态
    protected boolean isFragmentVisible;
    //是否已经初始化加载过
    protected boolean isLoaded;
    //用于butterknife解绑
    private Unbinder unbinder;
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
     * 该Activity是否订阅事件总线
     *
     * @return true则自动进行注册/注销操作，false则不注册
     */
    protected abstract boolean isUseEventBus();

    protected Bundle savedInstanceState;

    public View getmContentView() {
        return mContentView;
    }

    protected abstract boolean isLazyLoad();//是否使用懒加载 (Fragment可见时才进行初始化操作(以下四个方法))

    protected abstract int getContentLayout();//返回页面布局id

    protected abstract void initView();//做视图相关的初始化工作

    protected abstract void initData();//做数据相关的初始化工作

    protected abstract void initEvent();//做监听事件相关的初始化工作

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * SharedPreference工具类
     */
    protected SharedPreferenceUtil spUtil;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        if (mContentView == null) {
            try {
                mContentView = inflater.inflate(getContentLayout(), container, false);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            unbinder = ButterKnife.bind(this, mContentView);
        }
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //视图准备完毕
        isViewReady = true;
        if (!isLazyLoad() || isFragmentVisible) {
            init();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        //如果视图准备完毕且Fragment处于可见状态，则开始初始化操作
        if (isLazyLoad() && isViewReady && isFragmentVisible) {
            init();
        }
    }

    public void init() {
        if (!isLoaded) {
            spUtil = SharedPreferenceUtil.getInstance(mActivity);
            mProgressDialog = new MProgressDialog(mActivity);
            mPresenter = createPresenter();
            isLoaded = true;
            initView();
            initData();
            initEvent();
            loadData();
        }
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
        GlideUtil.displayImage(mActivity, url, imageView);
    }

    //请求单个权限建议用这个
    protected void requestEach(final PermissionListener listener, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
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
        RxPermissions rxPermissions = new RxPermissions(mActivity);
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
        startActivity(new Intent(mActivity, clz));
    }

    /**
     * [页面跳转并关闭当前界面]
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz, boolean isFinish) {
        startActivity(new Intent(mActivity, clz));
        if (isFinish) {
            mActivity.finish();
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
        intent.setClass(mActivity, clz);
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
        intent.setClass(mActivity, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isFinish) {
            mActivity.finish();
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
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 获取主题色
     */
    protected int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        mActivity.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    protected int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        mActivity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife解绑
        if (unbinder != null) unbinder.unbind();
        isViewReady = false;
        isLoaded = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }
}