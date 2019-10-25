package com.example.baseapplication.mvp.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baseapplication.R;
import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.CheckVersionBean;
import com.example.baseapplication.mvp.model.entity.TabEntity;
import com.example.baseapplication.mvp.model.event.MatisseDataEvent;
import com.example.baseapplication.mvp.presenter.MainActivityPresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.fragment.MainFragment;
import com.example.baseapplication.mvp.view.fragment.MyFragment;
import com.example.baseapplication.mvp.view.fragment.PetCircleFragment;
import com.example.baseapplication.mvp.view.fragment.ShopFragment;
import com.example.baseapplication.mvp.view.iview.IMainActivityView;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.updateapputil.Callback;
import com.example.baseapplication.updateapputil.ConfirmDialog;
import com.example.baseapplication.updateapputil.DownloadAppUtils;
import com.example.baseapplication.updateapputil.DownloadProgressDialog;
import com.example.baseapplication.updateapputil.UpdateAppEvent;
import com.example.baseapplication.updateapputil.UpdateUtil;
import com.example.baseapplication.util.GetDeviceId;
import com.example.baseapplication.util.StringUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.zhihu.matisse.Matisse;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity<MainActivityPresenter> implements IMainActivityView {
    @BindView(R.id.ctl_main)
    CommonTabLayout ctlMain;
    private DownloadProgressDialog progressDialog;
    private boolean isShow;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private String[] mTitles = {"首页", "商城", "宠圈", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_normal, R.mipmap.tab_shop_normal,
            R.mipmap.tab_petcircle_normal, R.mipmap.tab_my_normal};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_passed, R.mipmap.tab_shop_passed,
            R.mipmap.tab_petcircle_passed, R.mipmap.tab_my_passed};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int currentIndex;
    private long exitTime;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        mFragments.add(new MainFragment());
        mFragments.add(new ShopFragment());
        mFragments.add(new PetCircleFragment());
        mFragments.add(new MyFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctlMain.setTabData(mTabEntities, this, R.id.fl_main, mFragments);
        ctlMain.setCurrentTab(currentIndex);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                if (StringUtil.isEmpty(GetDeviceId.readDeviceID(mContext))) {
                    GetDeviceId.saveDeviceID(mContext);
                    EasyHttp.getInstance().addCommonHeaders(UrlConstants.getHeaders(mActivity));//设置全局公共头
                }
            }

            @Override
            public void onDenied(String permissionName) {
                showToast("请打开存储权限");
            }

            @Override
            public void onDeniedWithNeverAsk(String permissionName) {
                showToast("请打开存储权限");
            }
        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.checkVersion();
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBack(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(UpdateAppEvent event) {
        if (event != null) {
            if (event.getState() == UpdateAppEvent.DOWNLOADING) {
                long soFarBytes = event.getSoFarBytes();
                long totalBytes = event.getTotalBytes();
                if (event.getIsUpgrade() == 0 && isShow) {

                } else {
                    Log.e("TAG", "下载中...soFarBytes = " + soFarBytes + "---totalBytes = " + totalBytes);
                    if (progressDialog != null && progressDialog.isShowing()) {

                    } else {
                        progressDialog = new DownloadProgressDialog(this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("下载提示");
                        progressDialog.setMessage("当前下载进度:");
                        progressDialog.setIndeterminate(false);
                        if (event.getIsUpgrade() == 1) {
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        } else {
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(true);
                        }
                        progressDialog.show();
                    }
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e("TAG", "onDismiss");
                            isShow = true;
                        }
                    });
                }
                if (progressDialog != null) {
                    progressDialog.setMax((int) totalBytes);
                    progressDialog.setProgress((int) soFarBytes);
                }
                isShow = true;
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_COMPLETE) {
                UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_FAIL) {
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            }
        }
    }

    @Override
    public void checkVersionSuccess(CheckVersionBean checkVersionBean) {
        RingLog.e("checkVersionSuccess() checkVersionBean = " + checkVersionBean);
        hideLoadDialog();
        if (checkVersionBean != null) {
            checkVersionBean.setUrl("http://3g.163.com/links/4636");
            checkVersionBean.setCompulsory(0);
            if (checkVersionBean.getCompulsory() == 1) {
                // 强制升级
                UpdateUtil.showForceUpgradeDialog(mContext, checkVersionBean.getContent(),
                        checkVersionBean.getUrl(), checkVersionBean.getVersion(), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                requestEachCombined(new PermissionListener() {
                                    @Override
                                    public void onGranted(String permissionName) {
                                        UpdateUtil.updateApk(mContext,
                                                checkVersionBean.getUrl(), checkVersionBean.getVersion(), UpdateUtil.UPDATEFORNOTIFICATION, checkVersionBean.getCompulsory());
                                    }

                                    @Override
                                    public void onDenied(String permissionName) {
                                        showToast("请打开存储权限");
                                    }

                                    @Override
                                    public void onDeniedWithNeverAsk(String permissionName) {
                                        showToast("请打开存储权限");
                                    }
                                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES});
                            }
                        });
            } else if (checkVersionBean.getCompulsory() == 0) {
                // 非强制升级
                UpdateUtil.showUpgradeDialog(mContext, checkVersionBean.getContent(),
                        checkVersionBean.getUrl(), checkVersionBean.getVersion(), new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                requestEachCombined(new PermissionListener() {
                                    @Override
                                    public void onGranted(String permissionName) {
                                        UpdateUtil.updateApk(mContext,
                                                checkVersionBean.getUrl(), checkVersionBean.getVersion(), UpdateUtil.UPDATEFORNOTIFICATION, checkVersionBean.getCompulsory());
                                    }

                                    @Override
                                    public void onDenied(String permissionName) {
                                        showToast("请打开存储权限");
                                    }

                                    @Override
                                    public void onDeniedWithNeverAsk(String permissionName) {
                                        showToast("请打开存储权限");
                                    }
                                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES});
                            }
                        });
            }
        }
    }

    @Override
    public void checkVersionFail(int status, String desc) {
        hideLoadDialog();
        RingLog.e("checkVersionFail() status = " + status + "---desc = " + desc);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            onDestroy();
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShopFragment.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> strings = Matisse.obtainPathResult(data);
            EventBus.getDefault().post(new MatisseDataEvent(uris, strings));
        }
    }
}
