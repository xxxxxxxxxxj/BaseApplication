package com.example.baseapplication.mvp.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baseapplication.R;
import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.CheckVersionBean;
import com.example.baseapplication.mvp.model.entity.TabEntity;
import com.example.baseapplication.mvp.model.event.CaptureEvent;
import com.example.baseapplication.mvp.model.event.MatisseDataEvent;
import com.example.baseapplication.mvp.presenter.MainActivityPresenter;
import com.example.baseapplication.mvp.view.activity.base.BaseActivity;
import com.example.baseapplication.mvp.view.fragment.MainFragment;
import com.example.baseapplication.mvp.view.fragment.MyFragment;
import com.example.baseapplication.mvp.view.fragment.PetCircleFragment;
import com.example.baseapplication.mvp.view.fragment.ShopFragment;
import com.example.baseapplication.mvp.view.iview.IMainActivityView;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.updateapputil.Callback;
import com.example.baseapplication.updateapputil.ConfirmDialog;
import com.example.baseapplication.updateapputil.DownloadAppUtils;
import com.example.baseapplication.updateapputil.DownloadProgressDialog;
import com.example.baseapplication.updateapputil.UpdateAppEvent;
import com.example.baseapplication.updateapputil.UpdateUtil;
import com.example.baseapplication.util.GetDeviceId;
import com.example.baseapplication.util.PathUtils;
import com.example.baseapplication.util.QMUIDeviceHelper;
import com.example.baseapplication.util.QMUIDisplayHelper;
import com.example.baseapplication.util.StringUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.meituan.android.walle.WalleChannelReader;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private List<String> pathList = new ArrayList<String>();
    private Random mRandom = new Random();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        RingLog.e("channel = " + channel);
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

        //两位数
        ctlMain.showMsg(0, 55);
        ctlMain.setMsgMargin(0, -5, 5);

        //三位数
        ctlMain.showMsg(1, 100);
        ctlMain.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        ctlMain.showDot(2);
        MsgView rtv_2_2 = ctlMain.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, QMUIDisplayHelper.dp2px(mActivity, 7));
        }

        //设置未读消息背景
        ctlMain.showMsg(3, 5);
        ctlMain.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = ctlMain.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        ctlMain.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RingLog.e("TAG", "onTabSelect position = " + position);
                if (position == 2) {
                    ctlMain.hideMsg(2);
                }
            }

            @Override
            public void onTabReselect(int position) {
                RingLog.e("TAG", "ponTabReselect position = " + position);
                if (position == 0) {//刷新
                    ctlMain.showMsg(0, mRandom.nextInt(100) + 1);
                    MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(position);
                    mainFragment.autoRefresh();
                }
            }
        });
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
                MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        QMUIDeviceHelper.goToPermissionManager(mActivity);
                        return true;
                    }
                });
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
                    RingLog.e("下载中...soFarBytes = " + soFarBytes + "---totalBytes = " + totalBytes);
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
                            RingLog.e("onDismiss");
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
                                        MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                                return false;
                                            }
                                        });
                                    }
                                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
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
                                        MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                                return false;
                                            }
                                        });
                                    }
                                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
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
        RingLog.e("requestCode = " + requestCode);
        RingLog.e("resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE://Matisse选择照片返回
                    List<Uri> uris = Matisse.obtainResult(data);
                    List<String> strings = Matisse.obtainPathResult(data);
                    EventBus.getDefault().post(new MatisseDataEvent(uris, strings));
                    break;
                case REQUEST_CODE_PREVIEW://选择相册返回码
                    //启动裁剪
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startUCrop(selectedUri, REQUEST_CODE_UCROP, 1, 1);
                    } else {
                        RingToast.show(R.string.toast_cannot_retrieve_selected_image);
                    }
                    break;
                case REQUEST_CODE_CAPTURE://拍照返回码
                    EventBus.getDefault().post(new CaptureEvent());
                    break;
                case REQUEST_CODE_UCROP://UCrop裁剪返回码
                    Uri resultUri = UCrop.getOutput(data);
                    RingLog.e("resultUri = " + resultUri);
                    if (resultUri != null) {
                        String path = PathUtils.getPath(mActivity, resultUri);
                        pathList.clear();
                        pathList.add(path);
                        RingLog.e("resultUri = " + resultUri.toString());
                        RingLog.e("path = " + path);
                        EventBus.getDefault().post(new MatisseDataEvent(null, pathList));
                    } else {
                        RingToast.show(R.string.toast_cannot_retrieve_cropped_image);
                    }
                    break;
            }
        } else if (resultCode == CameraActivity.RESULTCODE_VIDEO) {//拍摄视频返回
            int flag = data.getIntExtra("flag", 0);
            String path = data.getStringExtra("path");
            ArrayList<String> strings1 = new ArrayList<>();
            strings1.add(path);
            EventBus.getDefault().post(new MatisseDataEvent(null, strings1));
        }
    }
}
