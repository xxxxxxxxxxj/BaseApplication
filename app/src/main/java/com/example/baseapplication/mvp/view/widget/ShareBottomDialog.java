package com.example.baseapplication.mvp.view.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.example.baseapplication.R;
import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.util.SharedPreferenceUtil;
import com.example.baseapplication.util.StringUtil;
import com.example.baseapplication.util.SystemUtil;

import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by shaohui on 2016/12/10.
 */

public class ShareBottomDialog extends BottomDialog implements View.OnClickListener {
    protected final static String TAG = ShareBottomDialog.class.getSimpleName();
    private String mTitle;
    private String mSummary;
    private String mTargetUrl;
    private String mThumbUrlOrPath;
    private String uuid;
    private int type;
    private Context context;

    public ShareBottomDialog() {
        context = getActivity();
    }

    public ShareBottomDialog(Context context) {
        this.context = context;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_bottom_share;
    }

    public void setShareInfo(String title, String summary, String targetUrl, String thumbUrlOrPath) {
        this.mTitle = title;
        this.mSummary = summary;
        this.mTargetUrl = targetUrl;
        this.mThumbUrlOrPath = thumbUrlOrPath;
        if (StringUtil.isEmpty(this.mTitle)) {
            this.mTitle = context.getResources().getString(R.string.app_name);
        }
        if (StringUtil.isEmpty(this.mSummary)) {
            this.mSummary = context.getResources().getString(R.string.app_name);
        }
        if (StringUtil.isEmpty(this.mTargetUrl)) {
            this.mTargetUrl = AppConfig.URL;
        }
        if (StringUtil.isEmpty(this.mThumbUrlOrPath)) {
            this.mThumbUrlOrPath = AppConfig.SHAREIMG_URL;
        }
        RingLog.e("this.mTitle = " + this.mTitle);
        RingLog.e("this.mSummary = " + this.mSummary);
        RingLog.e("this.mTargetUrl = " + this.mTargetUrl);
        RingLog.e("this.mThumbUrlOrPath = " + this.mThumbUrlOrPath);
    }

    public void completeUrl(Activity activity) {
        if (this.mTargetUrl != null && !TextUtils.isEmpty(this.mTargetUrl)) {
            if (!this.mTargetUrl.startsWith("http:")
                    && !this.mTargetUrl.startsWith("https:") && !this.mTargetUrl.startsWith("file:///")) {
                this.mTargetUrl = UrlConstants.getServiceBaseUrl() + this.mTargetUrl;
            }
            if (this.mTargetUrl.contains("?")) {
                this.mTargetUrl = this.mTargetUrl + "&system=android_" + SystemUtil.getCurrentVersion(activity)
                        + "&imei="
                        + SystemUtil.getIMEI(activity)
                        + "&phone="
                        + SharedPreferenceUtil.getInstance(activity).getString("cellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&petTimeStamp="
                        + System.currentTimeMillis();
            } else {
                this.mTargetUrl = this.mTargetUrl + "?system=android_" + SystemUtil.getCurrentVersion(activity)
                        + "&imei="
                        + SystemUtil.getIMEI(activity)
                        + "&phone="
                        + SharedPreferenceUtil.getInstance(activity).getString("cellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&petTimeStamp="
                        + System.currentTimeMillis();
            }
        }
        if (this.mThumbUrlOrPath != null && !TextUtils.isEmpty(this.mThumbUrlOrPath)) {
            if (!this.mThumbUrlOrPath.startsWith("http:")
                    && !this.mThumbUrlOrPath.startsWith("https:") && !this.mThumbUrlOrPath.startsWith("file:///")) {
                this.mThumbUrlOrPath = UrlConstants.getServiceBaseUrl() + this.mThumbUrlOrPath;
            }
        }
    }

    @Override
    public void bindView(final View v) {
        v.findViewById(R.id.share_qq).setOnClickListener(this);
        v.findViewById(R.id.share_qzone).setOnClickListener(this);
        v.findViewById(R.id.share_weibo).setOnClickListener(this);
        v.findViewById(R.id.share_wx).setOnClickListener(this);
        v.findViewById(R.id.share_wx_timeline).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_qq:
                break;
            case R.id.share_qzone:
                break;
            case R.id.share_weibo:
                break;
            case R.id.share_wx_timeline:
                break;
            case R.id.share_wx:
                break;
        }
        dismiss();
    }
}
