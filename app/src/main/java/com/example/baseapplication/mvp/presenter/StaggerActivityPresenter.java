package com.example.baseapplication.mvp.presenter;

import android.content.Context;

import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.EncyclopediasTitleBean;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IStaggerActivityView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:34
 */
public class StaggerActivityPresenter extends BasePresenter<IStaggerActivityView> {
    public StaggerActivityPresenter(Context mContext, IStaggerActivityView iStaggerActivityView) {
        super(mContext, iStaggerActivityView);
    }

    /**
     * 获取最新版本
     */
    public void getTab() {
        EasyHttp
                .get(UrlConstants.GET_ENCYCLOPEDIAS_TITLE)
                .baseUrl(UrlConstants.getServiceBaseUrl1())
                .execute(new SimpleCallBack<EncyclopediasTitleBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.getTabFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(EncyclopediasTitleBean response) {
                        mIView.getTabSuccess(response);
                    }
                });
    }
}
