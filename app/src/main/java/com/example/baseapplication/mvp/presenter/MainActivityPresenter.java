package com.example.baseapplication.mvp.presenter;

import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.mvp.model.entity.CheckVersionBean;
import com.example.baseapplication.mvp.model.entity.base.HttpResult;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IMainActivityView;
import com.example.baseapplication.util.StringUtil;
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
    public MainActivityPresenter(IMainActivityView iMainActivityView) {
        super(iMainActivityView);
    }

    /**
     * 获取最新版本
     */
    public void checkVersion() {
        EasyHttp.post(UrlConstants.CHECK_VERSION)
                .execute(new SimpleCallBack<HttpResult<CheckVersionBean>>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mIView != null) {
                            mIView.checkVersionFail(e.getCode(), e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(HttpResult<CheckVersionBean> response) {
                        if (mIView != null && response != null) {
                            if (response.getCode() == 0) {
                                mIView.checkVersionSuccess(response.getData());
                            } else {
                                if (StringUtil.isNotEmpty(response.getMsg())) {
                                    mIView.checkVersionFail
                                            (response.getCode(), response.getMsg());
                                } else {
                                    mIView.checkVersionFail(AppConfig.SERVER_ERROR, AppConfig.SERVER_ERROR_MSG + "-code=" + response.getCode());
                                }
                            }
                        }
                    }
                });
    }
}
