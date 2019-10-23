package com.example.baseapplication.mvp.presenter;

import android.content.Context;

import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.BannerBean;
import com.example.baseapplication.mvp.model.entity.PostBean;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.INewsFragView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 20:09
 */
public class NewsFragPresenter extends BasePresenter<INewsFragView> {
    public NewsFragPresenter(Context mContext, INewsFragView iNewsFragView) {
        super(mContext, iNewsFragView);
    }

    /**
     * 获取顶部banner
     */
    public void getBanner(Map<String, String> params) {
        EasyHttp.post(UrlConstants.ADVERTISEMENT)
                .params(params)
                .execute(new SimpleCallBack<List<BannerBean>>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.getBannerFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<BannerBean> response) {
                        mIView.getBannerSuccess(response);
                    }
                });
    }

    /**
     * 获取帖子
     */
    public void getPost(int index, Map<String, String> params) {
        String url = "";
        if (index % 3 == 0) {
            url = UrlConstants.NEWEST_POINT;
        } else if (index % 3 == 1) {
            url = UrlConstants.HOT_POINT;
        } else if (index % 3 == 2) {
            url = UrlConstants.PROBLEM_CAR_POINT;
        }
        EasyHttp.post(url)
                .params(params)
                .execute(new SimpleCallBack<List<PostBean>>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.getPostFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<PostBean> response) {
                        mIView.getPostSuccess(response);
                    }
                });
    }
}
