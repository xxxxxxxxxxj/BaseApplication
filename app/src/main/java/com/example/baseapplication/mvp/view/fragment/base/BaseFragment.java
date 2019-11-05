package com.example.baseapplication.mvp.view.fragment.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.baseapplication.R;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.widget.GifSizeFilter;
import com.example.baseapplication.mvp.view.widget.dialog.QMUITipDialog;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.CommonUtil;
import com.example.baseapplication.util.GlideUtil;
import com.example.baseapplication.util.QMUIDeviceHelper;
import com.example.baseapplication.util.SharedPreferenceUtil;
import com.example.baseapplication.util.StringUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:Fragment的基类</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-12 15:02
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
    protected QMUITipDialog tipDialog;
    private QMUITipDialog.Builder tipDialogBuilder;
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
    private CompositeDisposable mDisposable;
    /**
     * Luban压缩回调
     */
    public OnLuBanSuccessListener onLuBanSuccessListener = null;

    public interface OnLuBanSuccessListener {
        public void OnLuBanSuccess(List<File> list);
    }

    public void setOnLuBanSuccessListener(
            OnLuBanSuccessListener onLuBanSuccessListener) {
        this.onLuBanSuccessListener = onLuBanSuccessListener;
    }

    /**
     * Matisse返回码
     */
    public static final int REQUEST_CODE_CHOOSE = 23;
    /**
     * 选择相册返回码
     */
    public static final int REQUEST_CODE_PREVIEW = 24;
    /**
     * 拍照返回码
     */
    public static final int REQUEST_CODE_CAPTURE = 25;
    /**
     * UCrop裁剪返回码
     */
    public static final int REQUEST_CODE_UCROP = 26;

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
            mDisposable = new CompositeDisposable();
            spUtil = SharedPreferenceUtil.getInstance(mActivity);
            tipDialogBuilder = new QMUITipDialog.Builder(mActivity)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在加载...");
            tipDialog = tipDialogBuilder.create();
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
        hideLoadDialog();
        tipDialogBuilder.setTipWord("正在加载...");
        tipDialog = tipDialogBuilder.create();
        tipDialog.show();
    }

    // 显示加载提示框
    protected void showLoadDialog(CharSequence tipWord) {
        hideLoadDialog();
        tipDialogBuilder.setTipWord(tipWord);
        tipDialog = tipDialogBuilder.create();
        tipDialog.show();
    }

    // 隐藏加载提示框
    protected void hideLoadDialog() {
        tipDialog.dismiss();
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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title, View.OnClickListener listener) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        toolbar.setNavigationOnClickListener(listener);
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

    protected void getPhoto() {
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        DialogSettings.tipTheme = DialogSettings.THEME.DARK;
        BottomMenu.show(mActivity, new String[]{"拍照", "从手机相册选择"}, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                if (index == 0) {
                    capture();
                } else if (index == 1) {
                    pickFromGallery();
                }
            }
        });
    }

    protected void pickFromGallery() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                        .setType("image/*")
                        .addCategory(Intent.CATEGORY_OPENABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_CODE_PREVIEW);
            }

            @Override
            public void onDenied(String permissionName) {
                showToast("请打开存储权限");
            }

            @Override
            public void onDeniedWithNeverAsk(String permissionName) {
                MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        QMUIDeviceHelper.goToPermissionManager(mActivity);
                        return false;
                    }
                });
            }
        }, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    protected void capture() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                // 步骤一：创建存储照片的文件
                String path = mActivity.getFilesDir() + File.separator + "images" + File.separator;
                File file = new File(path, System.currentTimeMillis()
                        + "capture.jpg");
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                Uri mUri = CommonUtil.getUri(mActivity,file);
                //步骤四：调取系统拍照
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE);
            }

            @Override
            public void onDenied(String permissionName) {
                showToast("请打开存储权限");
            }

            @Override
            public void onDeniedWithNeverAsk(String permissionName) {
                MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        QMUIDeviceHelper.goToPermissionManager(mActivity);
                        return false;
                    }
                });
            }
        }, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    /**
     * 启动裁剪
     *
     * @param activity       上下文
     * @param sourceFilePath 需要裁剪图片的绝对路径
     * @param requestCode    比如：UCrop.REQUEST_CROP
     * @param aspectRatioX   裁剪图片宽高比
     * @param aspectRatioY   裁剪图片宽高比
     * @return
     */
    protected String startUCrop(Uri sourceUri,
                                int requestCode, float aspectRatioX, float aspectRatioY) {
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimaryDark));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();//使用图片的宽高
        //跳转裁剪页面
        uCrop.start(mActivity, requestCode);
        return cameraScalePath;
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
                                new CaptureStrategy(true, mActivity.getPackageName() + ".fileProvider", "base"))
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

    protected <T> void withRx(final List<T> photos) {
        showLoadDialog("正在压缩...");
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
    public void onStart() {
        super.onStart();
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        RingLog.e("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        RingLog.e("onDestroy");
        super.onDestroy();
        mDisposable.clear();
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
        //ButterKnife解绑
        if (unbinder != null) unbinder.unbind();
        isViewReady = false;
        isLoaded = false;
    }
}
