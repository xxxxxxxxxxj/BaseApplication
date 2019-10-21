package com.example.baseapplication.mvp.presenter;

import android.content.Context;

import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.CheckVersionBean;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IMainActivityView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 10:15
 */
public class MainActivityPresenter extends BasePresenter<IMainActivityView> {
    public MainActivityPresenter(Context mContext, IMainActivityView iMainActivityView) {
        super(mContext, iMainActivityView);
    }

    /**
     * 获取最新版本
     */
    public void checkVersion() {
        EasyHttp.post(UrlConstants.CHECK_VERSION)
                .execute(new SimpleCallBack<CheckVersionBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.checkVersionFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(CheckVersionBean response) {
                        mIView.checkVersionSuccess(response);
                    }
                });
    }
}
