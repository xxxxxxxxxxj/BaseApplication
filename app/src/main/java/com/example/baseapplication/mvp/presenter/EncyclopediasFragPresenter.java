package com.example.baseapplication.mvp.presenter;

import android.content.Context;

import com.example.baseapplication.app.UrlConstants;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.Encyclopedias;
import com.example.baseapplication.mvp.presenter.base.BasePresenter;
import com.example.baseapplication.mvp.view.iview.IEncyclopediasFragView;
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
 * @date zhoujunxia on 2019-11-02 18:48
 */
public class EncyclopediasFragPresenter extends BasePresenter<IEncyclopediasFragView> {
    public EncyclopediasFragPresenter(Context mContext, IEncyclopediasFragView iEncyclopediasFragView) {
        super(mContext, iEncyclopediasFragView);
    }

    public void getEncyclopedias(Map<String, String> params) {
        EasyHttp.post(UrlConstants.GET_ENCYCLOPEDIAS)
                .params(params)
                .baseUrl(UrlConstants.getServiceBaseUrl1())
                .execute(new SimpleCallBack<List<Encyclopedias>>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.getEncyclopediasFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Encyclopedias> response) {
                        mIView.getEncyclopediasSuccess(response);
                    }
                });
    }
}